package one.realme

import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.system.measureTimeMillis


class IdeaSet {
    @ParameterizedTest
    @CsvSource(
            "mainnet.conf, mainnet, 50505",
            "testnet.conf, testnet, 50505"
    )
    fun testTypeSafeConfig(file: String, eName: String, ePort: Int) {
        val env = ConfigFactory.load(file)
        assertEquals(eName, env.getString("name"))
        assertEquals(ePort, env.getInt("net.port"))
    }


    private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
        for (x in 1..5) send(x * x)
    }

    private suspend fun oneHeavyWork(no: Int): String {
        delay(1000)
        return "$no done"
    }

    @Test
    fun testCoroutinesConcurrent() = runBlocking {
        val time = measureTimeMillis {
            repeat(4) {
                println(oneHeavyWork(it))
            }
        }
        println("sync used: ${time / 1000.0} seconds.") // about 4 seconds

        val asyncTime = measureTimeMillis {
            List(4) { it ->
                async {
                    println(oneHeavyWork(it))
                }
            }.forEach { it.join() }
        }
        println("async Time used: ${asyncTime / 1000.0} seconds.") // about  1 seconds
    }

    @Test
    fun testCoroutinesChannel() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close()
        }
        for (y in channel) { // auto receive?
            println(y)
        }
//        repeat(5) {
//            println(channel.receive())
//        }

        val squares = produceSquares()
        squares.consumeEach {
            println(it)
        }

        println("Done")
    }

    @Test
    fun testCoroutinesBasic() = runBlocking {
        val job = GlobalScope.launch {
            // launch new coroutine in background and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
        println("Hello,") // main thread continues while coroutine is delayed

        job.join() // suspend func must in coroutines scope
//        runBlocking {
//            delay(1000L) // = Thread.sleep(2000L)
//        }

        val job2 = launch {
            delay(1000)
            println("world 2!")
        }
        println("hello, ")
        job2.join()

        coroutineScope {
            // Creates a new coroutine scope
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // This line will be printed before nested launch
        }

        val job3 = launch {
            printWorld(3)
        }
        println("hello, ")
        job3.join()

        repeat(100) {
            launch {
                printWorld(it)
            }
            print(".")
        }

        val result = withTimeoutOrNull(1000, {
            repeat(300) {
                println("I'm sleeping $it...")
                delay(100)
            }
            "ok"
        })
        println("Ok with $result") // it will print:  ok with null   (it's timeout return null)
    }

    private suspend fun printWorld(no: Int) {
        delay(1000)
        println("world from no $no!!")
    }
}