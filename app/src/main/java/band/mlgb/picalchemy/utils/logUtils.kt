package band.mlgb.picalchemy.utils

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.measureTimeMillis

fun debugBGLM(msg: String) {
    Log.d("BGLM", msg)
}

fun errBGLM(msg: String) {
    Log.e("BGLM", msg)
}

fun main() = runBlocking {
//    testTwoJobs(this)
//    testAsync()
//    val job1 = launch {
//        repeat(4) {
//            println("mlgb")
//            delay(100)
//        }
//    }


//    val job = coroutineScope {
//        launch {
//            repeat(4) {
//                println("mlgb")
//                delay(100)
//            }
//        }
//
//        GlobalScope.launch {
//            repeat(100) {
//                println("bglm")
//                delay(100)
//            }
//        }
//    }
//    return@runBlocking
//    val longJob = coroutineScope {
//        launch {
//            repeat(1000) {
//                delay(100)
//                println("longlonglong")
//            }
//        }
//    }

    MainScope()

    val longJob2 = with(CoroutineScope(EmptyCoroutineContext)) {
        launch {
            repeat(1000) {
                delay(100)
                println("longlonglong2")
            }
        }
    }
    val job2 = GlobalScope.launch(Dispatchers.IO) {

        val job3 = launch {
            repeat(4) {
                println("mlgb")
                delay(100)
            }
        }
        val job4 = coroutineScope {
            launch {
                repeat(1000) {
                    println("bglm")
                    delay(100)
                }
            }
        }

//        longJob.join()

    }
    longJob2.join()
/* job2.join() */
}


suspend fun testTwoJobs(scope: CoroutineScope) {
    with(scope) {
        val job1 = launch {
            try {
                repeat(1000) {
                    println("mlgb")
                    delay(100)
                }
            } catch (e: CancellationException) {
                println("i'm cancelled")
            }
        }
        // launch returns a Job
        val job2 = launch {
            repeat(1000) {
                println("bglm")
                delay(100)
            }
        }
        withTimeout(1300) {

        }

        measureTimeMillis {

        }

        delay(3000)
        job1.cancel() // when canceled, an exception is thrown
        println("job1 cancleed")
        // need to wait job2 finish before printing quite
        job2.join()
        println("quit")
    }
}

suspend fun testAsync() {
    // async returns a Deferred, which is a subclass of Job
    val one =
        withContext(Dispatchers.Default) { doSomethingUsefulOne() }
    val two = GlobalScope.async { doSomethingUsefulTwo() }.await()


    println("The answer is ${one + two}")
}


suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}