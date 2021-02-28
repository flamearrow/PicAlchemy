package band.mlgb.picalchemy.utils

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


suspend fun asyncTestDelay() = coroutineScope {
    val result = async {
        println("dela1y")
        delay(2000)
        3
    }
    println("${result.await()}")
}


suspend fun asyncTestNoDelay() = coroutineScope {
    val result = async {
        println("no dela1y")
        2
    }
    println("${result.await()}")
}


/**
 * Toy network API that takes time to run and return a value
 */
suspend fun netWorkApi(i: Int): Int = coroutineScope {
    delay(1000)
    println("mlgb: $i ")
    return@coroutineScope i + 1
}

suspend fun toy1() = coroutineScope {
    delay(1000)
    println("mlgb")
}


suspend fun toy2() = coroutineScope {
    delay(1000)
    println("mlgb2")
}

suspend fun toy3() = coroutineScope {
    delay(1000)
    println("mlgb3")
}

suspend fun netWorkApiSequentialCallTests() {
    val resultList = mutableListOf<Int>()

    // sequential cal
    for (i in 1..5) {
        resultList.add(netWorkApi(i))
    }

    println("done with all results, resultListSize == ${resultList.size}")

}

suspend fun netWorkApiSequentialCallTests2() = coroutineScope {
    val resultList = mutableListOf<Int>()
    for (i in 1..5) {
        withContext(Dispatchers.Default) {
            resultList.add(netWorkApi(i))
        }
    }
    println("done with all results, resultListSize == ${resultList.size}")
}


suspend fun netWorkApiAsynchronousCallTests() = coroutineScope {
    val resultList = mutableListOf<Int>()

    val deferredList = mutableListOf<Deferred<Int>>()
    // async call cal
    for (i in 1..5) {
        deferredList.add(
            async {
                netWorkApi(i)
            })
    }
    // wait all deferred to return
    deferredList.forEach {
        resultList.add(it.await())
    }

    println("done with all results, resultListSize == ${resultList.size}")

}


suspend fun netWorkApiAsynchronousCallTests2() = coroutineScope {
    val resultList = mutableListOf<Int>()

    val jobList = mutableListOf<Job>()
    for (i in 1..5) {
        launch {
            resultList.add(netWorkApi(i))
        }.also { jobList.add(it) }
    }

    jobList.forEach {
        it.join()
    }

    println("done with all results, resultListSize == ${resultList.size}")

}


suspend fun main(): Unit = runBlocking {// root coroutine
//    launch { // sub coroutine1
//        asyncTestDelay() // will suspend coroutine1 and let root execute coroutine2
//    }
//    launch {// sub coroutine2
//        asyncTestNoDelay() // will suspend coroutine2 and let root execute other coroutines if existed
//    }
//
//
//    asyncTestDelay() // will suspend root and let root's scope to execute other root sibling coroutine if any, essentially asyncTestNoDelay() will need to wait until this gets finished
//    asyncTestNoDelay()

//    withContext(Dispatchers.Default) {
//
//        toy1()
//    }
//
//    withContext(Dispatchers.Default) {
//
//        toy2()
//    }
//    netWorkApiSequentialCallTests()
    netWorkApiSequentialCallTests2()
//    netWorkApiAsynchronousCallTests()
//    netWorkApiAsynchronousCallTests2()


//    val deferred1 = async {
//        toy(1)
//    }
//
//    val deferred2 = async {
//        toy(2)
//    }
//
//    val job1 = launch {
//        toy(1)
//    }
//
//
//    val job2 = launch {
//        toy(2)
//    }
//
////    job1.join()
////    job2.join()
//    deferred1.await()
//    deferred2.await()
//
//    toy(3)
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


//
//    val longJob2 = with(CoroutineScope(EmptyCoroutineContext)) {
//        launch {
//            repeat(1000) {
//                delay(100)
//                println("longlonglong2")
//            }
//        }
//    }
//
//    val job2 = GlobalScope.launch(Dispatchers.IO) {
//
//        val job3 = launch {
//            repeat(4) {
//                println("mlgb")
//                delay(100)
//            }
//        }
//        val job4 = coroutineScope {
//            launch {
//                repeat(1000) {
//                    println("bglm")
//                    delay(100)
//                }
//            }
//        }
//
////        longJob.join()
//
//    }
//    longJob2.join()
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