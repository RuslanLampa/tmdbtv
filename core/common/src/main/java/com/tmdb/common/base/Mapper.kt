package com.tmdb.common.base

// Abstract Base Mapper for bi-directional mapping
abstract class BaseMapper<Input, Output> {

    // Forward mapping: Input -> Output
    abstract fun mapFrom(input: Input): Output

    // Reverse mapping: Output -> Input
    open fun mapTo(output: Output): Input? = null
}
