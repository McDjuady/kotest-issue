import io.kotest.core.extensions.SpecExtension
import io.kotest.core.spec.Spec
import io.kotest.core.test.Enabled
import io.kotest.core.test.EnabledOrReasonIf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object SomethingExtensions : SpecExtension {

    private var initialized = false
    val enabledOrReasonIf: EnabledOrReasonIf = {
        if (initialized)
            Enabled.enabled
        else
            Enabled.disabled("Not initialized")
    }

    val oppositeEnabledOrReasonIf: EnabledOrReasonIf = {
        if (!initialized)
            Enabled.enabled
        else
            Enabled.disabled("Not initialized")
    }

    override suspend fun intercept(spec: Spec, execute: suspend (Spec) -> Unit) {
        initialized = true
        runPromise()
        execute(spec)
    }

}

expect suspend fun runPromise()