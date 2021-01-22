package com.alphemsoft.education.regression.helper

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

inline fun <reified T> mock(): T =
    Mockito.mock(T::class.java)

fun <T> whenever(methodCall: T): OngoingStubbing<T> =
    Mockito.`when`(methodCall)