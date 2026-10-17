export const meta = {
  name: 'feature',
  description: 'Сделать фичу целиком: карточка Trello → план → data/ui параллельно → тесты → ревью с циклом исправлений',
  whenToUse: 'Пользователь описывает фичу или изменение, которое надо реализовать в приложении. args: { request: string, cardUrl?: string }',
  phases: [
    { title: 'Card', detail: 'найти или создать карточку в Trello' },
    { title: 'Plan', detail: 'feature-architect: контракт :api и задачи по агентам' },
    { title: 'Implement', detail: 'data-layer-dev и ui-dev параллельно' },
    { title: 'Test', detail: 'test-writer' },
    { title: 'Review', detail: 'architecture-reviewer + исправления, до 3 раундов' },
  ],
}

const MAX_REVIEW_ROUNDS = 3

const input = typeof args === 'string' ? { request: args } : (args || {})
if (!input.request && !input.cardUrl) {
  throw new Error('Нужен args.request (описание фичи) или args.cardUrl')
}

const CARD = {
  type: 'object',
  properties: {
    cardId: { type: 'string', description: 'ARI карточки' },
    cardUrl: { type: 'string' },
    title: { type: 'string' },
  },
  required: ['cardId', 'cardUrl', 'title'],
}

const PLAN = {
  type: 'object',
  properties: {
    blockingQuestions: {
      type: 'array', items: { type: 'string' },
      description: 'Вопросы, без ответа на которые нельзя начинать реализацию. Пусто, если можно начинать.',
    },
    contract: { type: 'string', description: 'Окончательный контракт :api (Kotlin-код) или "не меняется"' },
    dataTasks: { type: 'array', items: { type: 'string' } },
    uiTasks: { type: 'array', items: { type: 'string' } },
    testTasks: { type: 'array', items: { type: 'string' } },
    fileOwnership: { type: 'string', description: 'Кто какие файлы/модули правит' },
  },
  required: ['blockingQuestions', 'contract', 'dataTasks', 'uiTasks', 'testTasks', 'fileOwnership'],
}

const WORK = {
  type: 'object',
  properties: {
    status: { type: 'string', enum: ['done', 'partial', 'blocked'] },
    build: { type: 'string', enum: ['passed', 'failed', 'not_run'] },
    filesChanged: { type: 'array', items: { type: 'string' } },
    notes: { type: 'string', description: 'Что сделано, проблемы, вопросы к контракту' },
  },
  required: ['status', 'build', 'filesChanged', 'notes'],
}

const REVIEW = {
  type: 'object',
  properties: {
    verdict: { type: 'string', enum: ['approved', 'changes_requested'] },
    build: { type: 'string', enum: ['passed', 'failed'] },
    fixes: {
      type: 'array',
      items: {
        type: 'object',
        properties: {
          owner: { type: 'string', enum: ['data', 'ui', 'test'] },
          item: { type: 'string', description: 'путь:строка — что исправить' },
        },
        required: ['owner', 'item'],
      },
    },
    summary: { type: 'string' },
  },
  required: ['verdict', 'build', 'fixes', 'summary'],
}

const AGENT_BY_OWNER = { data: 'data-layer-dev', ui: 'ui-dev', test: 'test-writer' }

const GRADLE_NOTE =
  'Другие агенты могут параллельно запускать Gradle в этом же проекте: если сборка упала на блокировке ' +
  '(lock/timeout), а не на ошибке компиляции — подожди и повтори. Чужие файлы, которые сейчас меняются, не трогай.'

// ── Card ────────────────────────────────────────────────────────────────
phase('Card')
const card = await agent(
  input.cardUrl
    ? `Прочитай карточку Trello ${input.cardUrl} (trelloReadCard get) и верни её ARI, ссылку и название. Ничего не меняй.`
    : `Создай карточку в списке «Нужно сделать» на доске Georgian Alphabet (ARI списка — в разделе «Trello» в CLAUDE.md).
Название — короткое, по-русски, суть фичи. Описание — постановка задачи от пользователя дословно, затем пустая строка и «Создано workflow feature».
Постановка:
"""
${input.request}
"""
Верни ARI, ссылку и название карточки.`,
  { label: 'trello:card', phase: 'Card', schema: CARD, effort: 'low' },
)
if (!card) throw new Error('Не удалось получить карточку Trello')
log(`Карточка: ${card.title} — ${card.cardUrl}`)

const ctx = `Карточка Trello: ${card.cardId} (${card.cardUrl}).` +
  (input.request ? `\nПостановка пользователя:\n"""\n${input.request}\n"""` : '')

// ── Plan ────────────────────────────────────────────────────────────────
phase('Plan')
const plan = await agent(
  `${ctx}

Спланируй задачу и выполни свою часть процесса Trello (комментарий с планом, чеклист «План», перенос в «В процессе»).
Если без ответа пользователя начинать нельзя — заполни blockingQuestions, опубликуй их комментарием на карточке и НЕ создавай чеклист и НЕ двигай карточку.
Задачи в dataTasks/uiTasks/testTasks должны совпадать с пунктами чеклиста (без префиксов). Если слой не затрагивается — пустой массив.`,
  { label: 'feature-architect', phase: 'Plan', agentType: 'feature-architect', schema: PLAN },
)
if (!plan) throw new Error('feature-architect не вернул план')
if (plan.blockingQuestions.length) {
  log('План остановлен: нужны ответы пользователя')
  return { stage: 'plan', card, blockingQuestions: plan.blockingQuestions }
}
log(`План: data ${plan.dataTasks.length}, ui ${plan.uiTasks.length}, test ${plan.testTasks.length}`)

const planCtx = `${ctx}

Контракт :api (окончательный, менять нельзя — если чего-то не хватает, напиши в notes):
${plan.contract}

Владение файлами:
${plan.fileOwnership}`

const taskList = tasks => tasks.map(t => `- ${t}`).join('\n')

// ── Implement ───────────────────────────────────────────────────────────
phase('Implement')
const implJobs = []
if (plan.dataTasks.length) implJobs.push(['data', plan.dataTasks])
if (plan.uiTasks.length) implJobs.push(['ui', plan.uiTasks])

// Барьер: тестам нужны оба слоя.
const implResults = await parallel(implJobs.map(([owner, tasks]) => () =>
  agent(
    `${planCtx}

Твои пункты чеклиста «План» ([${owner}]):
${taskList(tasks)}

${owner === 'ui' ? 'data-layer-dev реализует :impl параллельно — пиши против интерфейсов из контракта. ' : ''}${GRADLE_NOTE}
Выполни пункты, отметь их в Trello и оставь комментарий-отчёт.`,
    { label: AGENT_BY_OWNER[owner], phase: 'Implement', agentType: AGENT_BY_OWNER[owner], schema: WORK },
  ).then(r => r && { owner, ...r })))

const impl = implResults.filter(Boolean)
const blocked = impl.filter(r => r.status === 'blocked')
if (impl.length < implJobs.length || blocked.length) {
  log('Реализация заблокирована — workflow остановлен')
  return { stage: 'implement', card, plan, impl }
}

// ── Test ────────────────────────────────────────────────────────────────
phase('Test')
let tests = null
if (plan.testTasks.length) {
  tests = await agent(
    `${planCtx}

Твои пункты чеклиста «План» ([test]):
${taskList(plan.testTasks)}

Изменённые файлы:
${impl.flatMap(r => r.filesChanged).map(f => `- ${f}`).join('\n')}

Напиши и запусти тесты, отметь пункты в Trello и оставь комментарий-отчёт. В notes перечисли упавшие тесты и найденные баги.`,
    { label: 'test-writer', phase: 'Test', agentType: 'test-writer', schema: WORK },
  )
}

// ── Review ──────────────────────────────────────────────────────────────
phase('Review')
let review = null
let round = 0
const fixHistory = []
while (round < MAX_REVIEW_ROUNDS) {
  round++
  review = await agent(
    `${ctx}

Раунд ревью ${round}/${MAX_REVIEW_ROUNDS}.${round > 1 ? ' Проверь, что пункты [fix] из прошлого раунда исправлены, и отметь их.' : ''}
Отчёты агентов — в комментариях на карточке.${tests ? `\nРезультат тестов: ${tests.notes}` : ''}

Проведи ревью и выполни свою часть процесса Trello. В fixes верни все блокеры и нарушения архитектуры (не замечания), owner — чья это зона.`,
    { label: `architecture-reviewer #${round}`, phase: 'Review', agentType: 'architecture-reviewer', schema: REVIEW },
  )
  if (!review) throw new Error('architecture-reviewer не вернул результат')
  if (review.verdict === 'approved') break
  if (round === MAX_REVIEW_ROUNDS) break

  log(`Раунд ${round}: ${review.fixes.length} исправлений`)
  const byOwner = {}
  review.fixes.forEach(f => { (byOwner[f.owner] = byOwner[f.owner] || []).push(f.item) })

  const fixes = await parallel(Object.entries(byOwner).map(([owner, items]) => () =>
    agent(
      `${planCtx}

Ревьюер вернул пункты [fix] в чеклисте «Ревью» для твоей зоны:
${taskList(items)}

${GRADLE_NOTE}
Исправь, отметь пункты в Trello и оставь комментарий-отчёт.`,
      { label: `fix:${AGENT_BY_OWNER[owner]} #${round}`, phase: 'Review', agentType: AGENT_BY_OWNER[owner], schema: WORK },
    ).then(r => r && { owner, ...r })))
  fixHistory.push(fixes.filter(Boolean))
}

if (review.verdict !== 'approved') {
  log(`Ревью не пройдено за ${MAX_REVIEW_ROUNDS} раунда — карточка осталась в «В процессе»`)
}

return {
  stage: review.verdict === 'approved' ? 'done' : 'review',
  card,
  reviewRounds: round,
  review,
  filesChanged: [...new Set([
    ...impl.flatMap(r => r.filesChanged),
    ...(tests ? tests.filesChanged : []),
    ...fixHistory.flat().flatMap(r => r.filesChanged),
  ])],
  notes: {
    impl: impl.map(r => ({ owner: r.owner, status: r.status, build: r.build, notes: r.notes })),
    tests: tests && tests.notes,
  },
}
