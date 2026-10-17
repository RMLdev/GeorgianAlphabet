#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store.state

import javax.inject.Inject

class ${NAME}StateToUiMapper @Inject constructor() : (${NAME}State) -> ${NAME}UiState {

    override fun invoke(state: ${NAME}State): ${NAME}UiState {
        return ${NAME}UiState()
    }
}
