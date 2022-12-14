import io.kotest.core.extensions.SpecExtension
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.spec.Spec
import io.kotest.core.test.Enabled
import io.kotest.core.test.EnabledOrReasonIf
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object SomethingExtensions : TestCaseExtension {

    private lateinit var enabledInitialized: Enabled
    private lateinit var disabledInitialized: Enabled
    val enabledOrReasonIf: EnabledOrReasonIf = {
        //On JS this is called once before the intercept on the testCase is called. Skip it in that case
        if (!this::enabledInitialized.isInitialized)
            Enabled.enabled
        else
            enabledInitialized
    }

    val oppositeEnabledOrReasonIf: EnabledOrReasonIf = {
        //On JS this is called once before the intercept on the testCase is called. Skip it in that case
        if (!this::disabledInitialized.isInitialized)
            Enabled.enabled
        else
            disabledInitialized
    }

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        //Ensure our condition is initialized. EnabledCheckInterceptor will run later to confirm via EnabledOrReasonIf
        if (!this::enabledInitialized.isInitialized) {
            runPromise()
            enabledInitialized = Enabled.enabled
            disabledInitialized = Enabled.disabled("Disabled")
        }
        return execute(testCase)
    }

}

expect suspend fun runPromise()