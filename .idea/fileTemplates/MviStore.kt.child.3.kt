#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store

import com.rmldev.georgianalphabet.core.ui.MviReducer
import ${BASE_PACKAGE}.screen.store.message.${NAME}Event
import ${BASE_PACKAGE}.screen.store.state.${NAME}State
import javax.inject.Inject

internal class ${NAME}Reducer @Inject constructor() : MviReducer<${NAME}State, ${NAME}Event> {

    override fun reduce(
        oldState: ${NAME}State,
        event: ${NAME}Event
    ): ${NAME}State {
        return oldState
    }
}
