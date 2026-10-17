#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import ${BASE_PACKAGE}.screen.${NAME}ViewModel
import ${BASE_PACKAGE}.screen.ui.${NAME}Screen

@Composable
internal fun ${NAME}Route(
    viewModel: ${NAME}ViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    ${NAME}Screen(
        state = state,
        onIntent = viewModel::dispatch
    )
}
