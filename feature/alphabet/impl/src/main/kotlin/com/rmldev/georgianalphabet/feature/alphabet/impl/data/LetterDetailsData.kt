package com.rmldev.georgianalphabet.feature.alphabet.impl.data

import com.rmldev.georgianalphabet.feature.alphabet.api.model.ExampleWord
import com.rmldev.georgianalphabet.feature.alphabet.api.model.LocalizedText

private fun word(georgian: String, cyrillic: String, latin: String, ru: String, en: String) =
    ExampleWord(georgian, cyrillic, latin, LocalizedText(ru, en))

/**
 * Группы похожих по начертанию букв (id из [LETTERS]): каждая буква группы похожа на остальные.
 * Сверено по шрифтам каталога: пары, которые путаются хотя бы в нескольких начертаниях.
 */
internal val SIMILAR_BY_SHAPE: List<Set<Int>> = listOf(
    setOf(0, 17), // ა ს
    setOf(17, 30), // ს ხ
    setOf(3, 10, 22), // დ ლ ღ
    setOf(27, 28, 29), // ძ წ ჭ
    setOf(1, 26), // ბ ც
    setOf(4, 5), // ე ვ
    setOf(5, 32), // ვ ჰ
    setOf(7, 13), // თ ო
    setOf(8, 13), // ი ო
    setOf(11, 24), // მ შ
    setOf(16, 25), // რ ჩ
    setOf(9, 14), // კ პ
    setOf(15, 21), // ჟ ქ
    setOf(19, 20), // უ ფ
)

/**
 * Группы похожих по звучанию букв: придыхательный/абруптивный (concept.md 2.2), ღ–გ, ხ–ყ
 * и ხ–ჰ (в кириллице обе передаются как «х»).
 */
internal val SIMILAR_BY_SOUND: List<Set<Int>> = listOf(
    setOf(21, 9), // ქ კ
    setOf(7, 18), // თ ტ
    setOf(20, 14), // ფ პ
    setOf(26, 28), // ც წ
    setOf(25, 29), // ჩ ჭ
    setOf(22, 2), // ღ გ
    setOf(30, 23), // ხ ყ
    setOf(30, 32), // ხ ჰ
)

/**
 * Контент карточек по id буквы. Архаичные буквы записей не имеют и показываются без ассоциации и слов.
 *
 * Транслитерация слов побуквенная, как в названиях букв ([LETTERS]): абруптивы отмечены апострофом
 * (кʼ пʼ тʼ цʼ чʼ / k' p' t' ts' ch'), ყ — «къ» / «q'», придыхательные без отметки; ე в начале слова
 * кириллицей «э», в остальных позициях «е». Правило проверяет LetterDetailsDataTest.
 */
internal val LETTER_CONTENT: Map<Int, LetterContent> = mapOf(
    0 to LetterContent( // ა
        association = LocalizedText(
            ru = "Круглый бочок и хвостик сверху, как у арбуза с веточкой.",
            en = "A round belly with a little tail on top, like a watermelon with a stem.",
        ),
        words = listOf(
            word("არა", "ара", "ara", "нет", "no"),
            word("მადლობა", "мадлоба", "madloba", "спасибо", "thank you"),
            word("აფთიაქი", "аптиаки", "aptiaki", "аптека", "pharmacy"),
            word("ხაჭაპური", "хачʼапʼури", "khach'ap'uri", "хачапури", "khachapuri"),
            word("ბანკი", "банкʼи", "bank'i", "банк", "bank"),
        ),
    ),
    1 to LetterContent( // ბ
        association = LocalizedText(
            ru = "Петелька сверху и круглый низ, как у бублика на палочке.",
            en = "A loop on top and a round bottom, like a bagel on a stick.",
        ),
        words = listOf(
            word("ბანკი", "банкʼи", "bank'i", "банк", "bank"),
            word("ბაზარი", "базари", "bazari", "рынок", "market"),
            word("ბავშვი", "бавшви", "bavshvi", "ребёнок", "child"),
            word("ბინა", "бина", "bina", "квартира", "apartment"),
        ),
    ),
    2 to LetterContent( // გ
        association = LocalizedText(
            ru = "Похожа на латинскую g и звучит так же: г.",
            en = "Looks like a Latin g and sounds the same.",
        ),
        words = listOf(
            word("გამარჯობა", "гамарджоба", "gamarjoba", "здравствуйте", "hello"),
            word("გზა", "гза", "gza", "дорога", "road"),
            word("გოგო", "гого", "gogo", "девочка", "girl"),
            word("გემრიელი", "гемриели", "gemrieli", "вкусный", "tasty"),
        ),
    ),
    3 to LetterContent( // დ
        association = LocalizedText(
            ru = "Волна с завитком, как дельфин в прыжке: д.",
            en = "A wave with a curl, like a dolphin leaping: d.",
        ),
        words = listOf(
            word("დედა", "деда", "deda", "мама", "mother"),
            word("დიდი", "диди", "didi", "большой", "big"),
            word("დილა", "дила", "dila", "утро", "morning"),
            word("დღე", "дгхе", "dghe", "день", "day"),
        ),
    ),
    4 to LetterContent( // ე
        association = LocalizedText(
            ru = "Похожа на букву Э и звучит как э.",
            en = "Looks like a backwards c and sounds like e in \"pet\".",
        ),
        words = listOf(
            word("ერთი", "эрти", "erti", "один", "one"),
            word("ეკლესია", "экʼлесиа", "ek'lesia", "церковь", "church"),
            word("მეტრო", "метʼро", "met'ro", "метро", "metro"),
            word("სახელი", "сахели", "sakheli", "имя", "name"),
        ),
    ),
    5 to LetterContent( // ვ
        association = LocalizedText(
            ru = "Похожа на цифру 3, а читается как в.",
            en = "Looks like the number 3 but reads as v.",
        ),
        words = listOf(
            word("ვაშლი", "вашли", "vashli", "яблоко", "apple"),
            word("ღვინო", "гхвино", "ghvino", "вино", "wine"),
            word("ავტობუსი", "автʼобуси", "avt'obusi", "автобус", "bus"),
            word("ვარდი", "варди", "vardi", "роза", "rose"),
        ),
    ),
    6 to LetterContent( // ზ
        association = LocalizedText(
            ru = "Извивается, как змея: з-з-з.",
            en = "Wiggles like a snake: z-z-z.",
        ),
        words = listOf(
            word("ზღვა", "згхва", "zghva", "море", "sea"),
            word("ზამთარი", "замтари", "zamtari", "зима", "winter"),
            word("ზაფხული", "запхули", "zapkhuli", "лето", "summer"),
            word("ბაზარი", "базари", "bazari", "рынок", "market"),
        ),
    ),
    7 to LetterContent( // თ
        association = LocalizedText(
            ru = "Две пухлые горки, как тучка: т.",
            en = "Two puffy humps, like a thundercloud: t.",
        ),
        words = listOf(
            word("თბილისი", "тбилиси", "tbilisi", "Тбилиси", "Tbilisi"),
            word("თევზი", "тевзи", "tevzi", "рыба", "fish"),
            word("ოთახი", "отахи", "otakhi", "комната", "room"),
            word("ათი", "ати", "ati", "десять", "ten"),
        ),
    ),
    8 to LetterContent( // ი
        association = LocalizedText(
            ru = "Одна арка, как вход в иглу: и.",
            en = "A single arch, like the entrance to an igloo: i.",
        ),
        words = listOf(
            word("ხილი", "хили", "khili", "фрукты", "fruit"),
            word("იაფი", "иапи", "iapi", "дешёвый", "cheap"),
            word("პური", "пʼури", "p'uri", "хлеб", "bread"),
            word("ღვინო", "гхвино", "ghvino", "вино", "wine"),
        ),
    ),
    9 to LetterContent( // კ
        association = LocalizedText(
            ru = "Похожа на крючок вешалки: к.",
            en = "Shaped like a coat hook: k.",
        ),
        words = listOf(
            word("კატა", "кʼатʼа", "k'at'a", "кошка", "cat"),
            word("კარი", "кʼари", "k'ari", "дверь", "door"),
            word("კაცი", "кʼаци", "k'atsi", "мужчина", "man"),
            word("ბანკი", "банкʼи", "bank'i", "банк", "bank"),
        ),
    ),
    10 to LetterContent( // ლ
        association = LocalizedText(
            ru = "Три волны подряд, как лапша: л.",
            en = "Three loops in a row, like a lasso: l.",
        ),
        words = listOf(
            word("ლარი", "лари", "lari", "лари", "lari"),
            word("ლუდი", "луди", "ludi", "пиво", "beer"),
            word("ლობიო", "лобио", "lobio", "лобио", "lobio (beans)"),
            word("წყალი", "цʼкъали", "ts'q'ali", "вода", "water"),
            word("ქალაქი", "калаки", "kalaki", "город", "city"),
        ),
    ),
    11 to LetterContent( // მ
        association = LocalizedText(
            ru = "Похожа на мешочек с завязкой: м.",
            en = "Looks like a money bag with a tie: m.",
        ),
        words = listOf(
            word("მადლობა", "мадлоба", "madloba", "спасибо", "thank you"),
            word("მთა", "мта", "mta", "гора", "mountain"),
            word("მზე", "мзе", "mze", "солнце", "sun"),
            word("მწვადი", "мцʼвади", "mts'vadi", "мцвади (шашлык)", "mtsvadi (barbecue)"),
        ),
    ),
    12 to LetterContent( // ნ
        association = LocalizedText(
            ru = "Похожа на шестёрку с носиком: н.",
            en = "Looks like a 6 with a nose: n.",
        ),
        words = listOf(
            word("ნახვამდის", "нахвамдис", "nakhvamdis", "до свидания", "goodbye"),
            word("ნაყინი", "накъини", "naq'ini", "мороженое", "ice cream"),
            word("ღვინო", "гхвино", "ghvino", "вино", "wine"),
            word("ბინა", "бина", "bina", "квартира", "apartment"),
        ),
    ),
    13 to LetterContent( // ო
        association = LocalizedText(
            ru = "Горка с колечком слева, как обруч: о.",
            en = "An arch with a ring on the left, like a hoop: o.",
        ),
        words = listOf(
            word("ორი", "ори", "ori", "два", "two"),
            word("ოთახი", "отахи", "otakhi", "комната", "room"),
            word("ოქრო", "окро", "okro", "золото", "gold"),
            word("ლობიო", "лобио", "lobio", "лобио", "lobio (beans)"),
        ),
    ),
    14 to LetterContent( // პ
        association = LocalizedText(
            ru = "Как კ с горбиком наверху: пʼ.",
            en = "Like კ with an extra bump on top: p'.",
        ),
        words = listOf(
            word("პური", "пʼури", "p'uri", "хлеб", "bread"),
            word("პარკი", "пʼаркʼи", "p'ark'i", "парк", "park"),
            word("პოლიცია", "пʼолициа", "p'olitsia", "полиция", "police"),
            word("ხაჭაპური", "хачʼапʼури", "khach'ap'uri", "хачапури", "khachapuri"),
        ),
    ),
    15 to LetterContent( // ჟ
        association = LocalizedText(
            ru = "Крючок с хвостом, как жук на травинке: ж.",
            en = "A hook with a tail, like a beetle on a stalk: zh, as in \"pleasure\".",
        ),
        words = listOf(
            word("ჟოლო", "жоло", "zholo", "малина", "raspberry"),
            word("ჟურნალი", "журнали", "zhurnali", "журнал", "magazine"),
            word("ბაჟე", "баже", "bazhe", "баже (ореховый соус)", "bazhe (walnut sauce)"),
            word("ჟანრი", "жанри", "zhanri", "жанр", "genre"),
        ),
    ),
    16 to LetterContent( // რ
        association = LocalizedText(
            ru = "Похожа на латинскую r с горкой: р.",
            en = "Looks like a Latin r with a hump: r.",
        ),
        words = listOf(
            word("რესტორანი", "рестʼорани", "rest'orani", "ресторан", "restaurant"),
            word("რძე", "рдзе", "rdze", "молоко", "milk"),
            word("ქართული", "картули", "kartuli", "грузинский", "Georgian"),
            word("ორი", "ори", "ori", "два", "two"),
        ),
    ),
    17 to LetterContent( // ს
        association = LocalizedText(
            ru = "Арка с хвостиком, как полозья санок: с.",
            en = "An arch with a tail, like a sled runner: s.",
        ),
        words = listOf(
            word("სასტუმრო", "састʼумро", "sast'umro", "гостиница", "hotel"),
            word("სახლი", "сахли", "sakhli", "дом", "house"),
            word("სადგური", "садгури", "sadguri", "вокзал", "station"),
            word("თბილისი", "тбилиси", "tbilisi", "Тбилиси", "Tbilisi"),
        ),
    ),
    18 to LetterContent( // ტ
        association = LocalizedText(
            ru = "Колечко на палочке, как тюльпан: тʼ.",
            en = "A ring on a stick, like a tulip: t'.",
        ),
        words = listOf(
            word("ტაქსი", "тʼакси", "t'aksi", "такси", "taxi"),
            word("მეტრო", "метʼро", "met'ro", "метро", "metro"),
            word("ტელეფონი", "тʼелепони", "t'eleponi", "телефон", "phone"),
            word("ავტობუსი", "автʼобуси", "avt'obusi", "автобус", "bus"),
        ),
    ),
    19 to LetterContent( // უ
        association = LocalizedText(
            ru = "Изгибается, как ухо: у.",
            en = "Curves like a U-turn: u.",
        ),
        words = listOf(
            word("უფასო", "упасо", "upaso", "бесплатно", "free"),
            word("ქუჩა", "куча", "kucha", "улица", "street"),
            word("ლუდი", "луди", "ludi", "пиво", "beer"),
            word("ჩურჩხელა", "чурчхела", "churchkhela", "чурчхела", "churchkhela"),
        ),
    ),
    20 to LetterContent( // ფ
        association = LocalizedText(
            ru = "Как უ с пузырём посередине: п с выдохом.",
            en = "Like უ with a puff in the middle: p with a puff of air.",
        ),
        words = listOf(
            word("ფული", "пули", "puli", "деньги", "money"),
            word("ფასი", "паси", "pasi", "цена", "price"),
            word("აფთიაქი", "аптиаки", "aptiaki", "аптека", "pharmacy"),
            word("ფანჯარა", "панджара", "panjara", "окно", "window"),
        ),
    ),
    21 to LetterContent( // ქ
        association = LocalizedText(
            ru = "Крюк с перекладиной: к с выдохом.",
            en = "A hook with a crossbar: k with a puff of air.",
        ),
        words = listOf(
            word("ქუჩა", "куча", "kucha", "улица", "street"),
            word("ქალაქი", "калаки", "kalaki", "город", "city"),
            word("ქართული", "картули", "kartuli", "грузинский", "Georgian"),
            word("ქალი", "кали", "kali", "женщина", "woman"),
        ),
    ),
    22 to LetterContent( // ღ
        association = LocalizedText(
            ru = "Как დ с лишним горбом: гортанное гх, как французское r.",
            en = "Like დ with an extra hump: a throaty gh, like a French r.",
        ),
        words = listOf(
            word("ღია", "гхиа", "ghia", "открыто", "open"),
            word("ღვინო", "гхвино", "ghvino", "вино", "wine"),
            word("ღამე", "гхаме", "ghame", "ночь", "night"),
            word("ზღვა", "згхва", "zghva", "море", "sea"),
        ),
    ),
    23 to LetterContent( // ყ
        association = LocalizedText(
            ru = "Похожа на латинскую y, а звучит как глубокое къ из горла.",
            en = "Looks like a Latin y but sounds like a deep q' from the throat.",
        ),
        words = listOf(
            word("ყავა", "къава", "q'ava", "кофе", "coffee"),
            word("ყველი", "къвели", "q'veli", "сыр", "cheese"),
            word("წყალი", "цʼкъали", "ts'q'ali", "вода", "water"),
            word("ნაყინი", "накъини", "naq'ini", "мороженое", "ice cream"),
        ),
    ),
    24 to LetterContent( // შ
        association = LocalizedText(
            ru = "Как მ, только с тремя зубцами, как у ш.",
            en = "Like მ with three teeth, like a shark: sh.",
        ),
        words = listOf(
            word("შესასვლელი", "шесасвлели", "shesasvleli", "вход", "entrance"),
            word("შაქარი", "шакари", "shakari", "сахар", "sugar"),
            word("ვაშლი", "вашли", "vashli", "яблоко", "apple"),
            word("შაურმა", "шаурма", "shaurma", "шаурма", "shawarma"),
        ),
    ),
    25 to LetterContent( // ჩ
        association = LocalizedText(
            ru = "Похожа на латинскую h с завитком: ч.",
            en = "Looks like a Latin h with a curl: ch.",
        ),
        words = listOf(
            word("ჩაი", "чаи", "chai", "чай", "tea"),
            word("ჩანთა", "чанта", "chanta", "сумка", "bag"),
            word("ჩურჩხელა", "чурчхела", "churchkhela", "чурчхела", "churchkhela"),
            word("ჩემი", "чеми", "chemi", "мой", "my"),
        ),
    ),
    26 to LetterContent( // ც
        association = LocalizedText(
            ru = "Завиток с хвостиком, как звено цепочки: ц.",
            en = "A curl with a tail, like a hanging chain link: ts.",
        ),
        words = listOf(
            word("ცივი", "циви", "tsivi", "холодный", "cold"),
            word("ცხელი", "цхели", "tskheli", "горячий", "hot"),
            word("პოლიცია", "пʼолициа", "p'olitsia", "полиция", "police"),
            word("ცა", "ца", "tsa", "небо", "sky"),
        ),
    ),
    27 to LetterContent( // ძ
        association = LocalizedText(
            ru = "Похожа на латинскую d: дз.",
            en = "Looks like a Latin d: say dz.",
        ),
        words = listOf(
            word("ძაღლი", "дзагхли", "dzaghli", "собака", "dog"),
            word("ძმა", "дзма", "dzma", "брат", "brother"),
            word("რძე", "рдзе", "rdze", "молоко", "milk"),
            word("ძველი", "дзвели", "dzveli", "старый", "old"),
        ),
    ),
    28 to LetterContent( // წ
        association = LocalizedText(
            ru = "Как ც с острым уголком сверху: резкое цʼ.",
            en = "Like ც with a sharp tip on top: a crisp ts'.",
        ),
        words = listOf(
            word("წყალი", "цʼкъали", "ts'q'ali", "вода", "water"),
            word("წიგნი", "цʼигни", "ts'igni", "книга", "book"),
            word("მწვადი", "мцʼвади", "mts'vadi", "мцвади (шашлык)", "mtsvadi (barbecue)"),
            word("წელი", "цʼели", "ts'eli", "год", "year"),
        ),
    ),
    29 to LetterContent( // ჭ
        association = LocalizedText(
            ru = "Крючок с перекладиной и завитком: резкое чʼ.",
            en = "A hook with a crossbar and a curl: a crisp ch'.",
        ),
        words = listOf(
            word("ჭიქა", "чʼика", "ch'ika", "стакан", "glass"),
            word("ჭადი", "чʼади", "ch'adi", "мчади (кукурузная лепёшка)", "mchadi (corn bread)"),
            word("ხაჭაპური", "хачʼапʼури", "khach'ap'uri", "хачапури", "khachapuri"),
            word("ჭკვიანი", "чʼкʼвиани", "ch'k'viani", "умный", "smart"),
        ),
    ),
    30 to LetterContent( // ხ
        association = LocalizedText(
            ru = "Как ს с высокой палочкой: х.",
            en = "Like ს with a tall stem: kh, as in Scottish \"loch\".",
        ),
        words = listOf(
            word("ხინკალი", "хинкʼали", "khink'ali", "хинкали", "khinkali"),
            word("ხილი", "хили", "khili", "фрукты", "fruit"),
            word("ხიდი", "хиди", "khidi", "мост", "bridge"),
            word("სახლი", "сахли", "sakhli", "дом", "house"),
        ),
    ),
    31 to LetterContent( // ჯ
        association = LocalizedText(
            ru = "Похожа на джойстик с ручкой: дж.",
            en = "Looks like a joystick: j.",
        ),
        words = listOf(
            word("გამარჯობა", "гамарджоба", "gamarjoba", "здравствуйте", "hello"),
            word("აჯიკა", "аджикʼа", "ajik'a", "аджика", "adjika"),
            word("ჯვარი", "джвари", "jvari", "крест", "cross"),
            word("ფანჯარა", "панджара", "panjara", "окно", "window"),
        ),
    ),
    32 to LetterContent( // ჰ
        association = LocalizedText(
            ru = "Как ვ, но с хвостом вниз: лёгкое х.",
            en = "Like ვ with a tail hanging down: h.",
        ),
        words = listOf(
            word("ჰო", "хо", "ho", "да", "yes"),
            word("ჰაერი", "хаери", "haeri", "воздух", "air"),
            word("ჰავა", "хава", "hava", "климат", "climate"),
            word("ჰოსტელი", "хостʼели", "host'eli", "хостел", "hostel"),
        ),
    ),
)
