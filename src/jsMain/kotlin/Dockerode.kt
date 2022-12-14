import kotlin.js.Promise

external interface DockerOptions {
    var socketPath: String?
        get() = definedExternally
        set(value) = definedExternally
    var host: String?
        get() = definedExternally
        set(value) = definedExternally
    var port: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var username: String?
        get() = definedExternally
        set(value) = definedExternally
    var ca: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var cert: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var key: dynamic
        get() = definedExternally
        set(value) = definedExternally
    var protocol: String?
        get() = definedExternally
        set(value) = definedExternally
    var timeout: Number?
        get() = definedExternally
        set(value) = definedExternally
    var version: String?
        get() = definedExternally
        set(value) = definedExternally
    var sshAuthAgent: String?
        get() = definedExternally
        set(value) = definedExternally
    var Promise: dynamic
        get() = definedExternally
        set(value) = definedExternally
}

external interface DockerResponse

@JsModule("dockerode")
@JsNonModule
@JsName("Dockerode")
external fun createDockerInstance(opts: DockerOptions?) : DockerInstance

external interface DockerInstance {
    fun ping(options: dynamic = definedExternally): Promise<DockerResponse>
    fun ping(options: dynamic = definedExternally, callback: (err:dynamic,data:dynamic) -> Unit): Unit
}