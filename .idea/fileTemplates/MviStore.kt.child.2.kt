#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store

import com.rmldev.georgianalphabet.core.ui.ActorScope
import com.rmldev.georgianalphabet.core.ui.MviActor
import ${BASE_PACKAGE}.screen.store.message.${NAME}Action
import ${BASE_PACKAGE}.screen.store.message.${NAME}Event
import ${BASE_PACKAGE}.screen.store.message.${NAME}SideEffect
import ${BASE_PACKAGE}.screen.store.state.${NAME}State
import javax.inject.Inject

private typealias ${NAME}ActorScope = ActorScope<${NAME}State, ${NAME}Event, ${NAME}SideEffect>

internal class ${NAME}Actor @Inject constructor() :
    MviActor<${NAME}Action, ${NAME}State, ${NAME}Event, ${NAME}SideEffect> {

    override suspend fun ${NAME}ActorScope.executeAction(
        action: ${NAME}Action
    ) {

    }
}
