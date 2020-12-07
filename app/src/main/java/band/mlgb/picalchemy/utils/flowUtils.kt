package band.mlgb.picalchemy.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

// similar to java stream()
val flowToy: Flow<Int> = flow {
    while (true) {
        emit(1)
        kotlinx.coroutines.delay(500)
    }
}

// similar to map
val flowMapped = flowToy.map { i ->
    i * 23
}.flowOn(Dispatchers.Default)

// sequence can't be updated on the fly
val seq: Sequence<Int> = sequence {
    repeat(23) {
        yield(3)
    }
}

suspend fun receive() {
    flowToy.collect {
        print(it)
    }
}

suspend fun main() {
//    coroutineScope {
//        flowMapped.collect {
//            print(it)
//        }
//    }

//    listOf(1, 2, 3).forEach {
//        print(it)
//    }
//    seq.forEach {
//        print(it)
//    }
    val f1 = listOf(1, 2, 3).asFlow()
    val f2 = flowOf(4, 5, 6)
//    withTimeoutOrNull(10000) {
//        receive()
//    }
//
//    val f3 = f1.zip(f2) { f1V, f2V->
//        "${f1V} ${f2V} "
//    }


    f1.onEach { delay(100) }.flatMapConcat { f2 }.collect {
        print(it)
    }

}