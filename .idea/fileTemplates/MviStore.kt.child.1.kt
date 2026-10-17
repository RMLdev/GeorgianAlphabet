#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.ui

import androidx.compose.runtime.Composable
import ${BASE_PACKAGE}.screen.store.message.${NAME}Intent
import ${BASE_PACKAGE}.screen.store.state.${NAME}UiState

@Composable
internal fun ${NAME}Screen(
    state: ${NAME}UiState,
    onIntent: (${NAME}Intent) -> Unit
) {

}
