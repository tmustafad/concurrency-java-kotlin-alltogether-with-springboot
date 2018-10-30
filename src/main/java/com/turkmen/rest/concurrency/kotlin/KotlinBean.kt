package com.turkmen.rest.concurrency.kotlin

import org.springframework.stereotype.Component
import java.util.concurrent.Callable


@Component
class KotlinBean {

    fun helloKotlin(param: String): String {
        println("A call from java to kotlin is being executed with param " + param)

        return "Hi From Kotlin"
    }


    fun <T> getCallable(task: Callable<T>): Callable<T> {
        return Callable {
            task.call()

        }
    }
}