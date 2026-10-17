#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store.state

import androidx.compose.runtime.Stable

@Stable
class ${NAME}UiState {
}
