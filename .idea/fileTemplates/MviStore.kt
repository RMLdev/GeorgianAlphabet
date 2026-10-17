#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rmldev.georgianalphabet.core.ui.CollectSideEffects
import ${BASE_PACKAGE}.screen.${NAME}ViewModel
import ${BASE_PACKAGE}.screen.store.message.${NAME}SideEffect
import ${BASE_PACKAGE}.screen.ui.${NAME}Screen

@Composable
internal fun ${NAME}Route(
    viewModel: ${NAME}ViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    CollectSideEffects(viewModel.sideEffects) { sideEffect ->
        when (sideEffect) {
            ${NAME}SideEffect.Stub -> Unit
        }
    }

    ${NAME}Screen(
        state = state,
        onIntent = viewModel::dispatch
    )
}
