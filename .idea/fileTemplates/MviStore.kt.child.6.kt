#set($BASE_PACKAGE = $PACKAGE_NAME.replaceAll("(\.screen(\.store(\.message|\.state)?|\.ui)?)$", ""))
package ${BASE_PACKAGE}.screen.store.message

internal sealed interface ${NAME}Intent {
    object Stub : ${NAME}Intent
}
