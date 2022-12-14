import io.kotest.mpp.syspropOrEnv
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import org.w3c.dom.url.URL
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.js.Promise

enum class Platform(val value: Boolean) {
    JVM(false),
    JS(true),
    WEB(
        js(
            "typeof window !== 'undefined' && typeof window.document !== 'undefined' || typeof self !== 'undefined' && typeof self.location !== 'undefined'" // ktlint-disable max-line-length
        ) as Boolean
    ),
    NODE(
        js(
            "typeof process !== 'undefined' && process.versions != null && process.versions.node != null"
        ) as Boolean
    ),
    WINDOWS(NODE.value && js("process.platform == \"win32\"") as Boolean),
    LINUX(NODE.value && js("process.platform == \"linux\"") as Boolean);
}

actual suspend fun runPromise() {
    val strategies = listOf(
        ConfigStrategy,
        UnixSocketStrategy,
        NpipeSocketStrategy
    )
    val availableStrategies = strategies.filter { it.isAvailable() }
    println("Trying strategies ${availableStrategies.map { it.name }}, platform ${js("process.platform")}")
    val ok = availableStrategies
            .map { strategy ->
                try {
                    val resp = createDockerInstance(strategy.createOptions()).ping().await()
                    println(JSON.stringify(resp))
                    true
                } catch (e: Throwable) {
                    false
                }
            }.any { it }
    println("Ran promise $ok")
}

interface DockerStrategy {
    fun isAvailable(): Boolean
    fun createOptions(): DockerOptions
    val name: String
}

object ConfigStrategy : DockerStrategy {
    override fun isAvailable() = Platform.NODE.value && syspropOrEnv("DOCKER_HOST") != null

    override fun createOptions(): DockerOptions {
        val url = URL(syspropOrEnv("DOCKER_HOST")!!)
        return object : DockerOptions {
            init {
                if (url.host.isNotBlank()) {
                    host = url.host
                    port = url.port
                } else {
                    socketPath = url.pathname
                }
            }
        }
    }

    override val name: String = "ConfigStrategy"

}

object UnixSocketStrategy : DockerStrategy {
    override fun isAvailable(): Boolean = Platform.LINUX.value

    override fun createOptions(): DockerOptions = object : DockerOptions {
        override var socketPath: String? = "/var/run/docker.sock"
    }

    override val name: String
        get() = "UnixSocketStrategy"

}

object NpipeSocketStrategy : DockerStrategy {
    override fun isAvailable(): Boolean = Platform.WINDOWS.value

    override fun createOptions(): DockerOptions = object : DockerOptions {
        override var socketPath: String? = "//./pipe/docker_engine"
    }

    override val name: String = "NpipeSocketStrategy"

}