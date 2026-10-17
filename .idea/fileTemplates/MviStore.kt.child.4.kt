#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store.message

internal sealed interface ${NAME}Action {
    object Stub : ${NAME}Action
}
