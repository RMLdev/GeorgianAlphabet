#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen

import com.rmldev.georgianalphabet.core.ui.StoreViewModel
import ${BASE_PACKAGE}.screen.store.${NAME}Actor
import ${BASE_PACKAGE}.screen.store.${NAME}Reducer
import ${BASE_PACKAGE}.screen.store.message.${NAME}Action
import ${BASE_PACKAGE}.screen.store.message.${NAME}Event
import ${BASE_PACKAGE}.screen.store.message.${NAME}Intent
import ${BASE_PACKAGE}.screen.store.message.${NAME}SideEffect
import ${BASE_PACKAGE}.screen.store.state.${NAME}State
import ${BASE_PACKAGE}.screen.store.state.${NAME}StateToUiMapper
import ${BASE_PACKAGE}.screen.store.state.${NAME}UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ${NAME}ViewModel @Inject constructor(
    actor: ${NAME}Actor,
    reducer: ${NAME}Reducer,
    uiStateMapper: ${NAME}StateToUiMapper,
) : StoreViewModel<${NAME}Intent, ${NAME}Action, ${NAME}State, ${NAME}UiState, ${NAME}SideEffect, ${NAME}Event>(
    actor = actor,
    reducer = reducer,
    initialState = ${NAME}State(),
    uiStateMapper = uiStateMapper,
    intentToAction = { intent ->
        when (intent) {
            ${NAME}Intent.Stub -> ${NAME}Action.Stub
        }
    },
)
