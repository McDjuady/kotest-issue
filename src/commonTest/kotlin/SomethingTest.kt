import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import kotlinx.coroutines.delay

class SomethingTest: FunSpec() {

    init {
        extension(SomethingExtensions)

        suspend fun somethingThatTakesAWhile() {
            delay(2000)
        }

        test("should run").config(enabledOrReasonIf = SomethingExtensions.enabledOrReasonIf) {
            somethingThatTakesAWhile()
            true.shouldBeTrue()
        }

        test("should not run").config(enabledOrReasonIf = SomethingExtensions.oppositeEnabledOrReasonIf) {
            somethingThatTakesAWhile()
            fail("This should not run")
        }
    }

}