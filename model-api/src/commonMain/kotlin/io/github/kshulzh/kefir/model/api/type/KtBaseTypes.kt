/*
 * Copyright (c) 2025-2026. Kirill Shulzhenko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.kshulzh.kefir.model.api.type

/**
 * Represents a predefined collection of base types in Kotlin, such as primitives and common standard types.
 * This enum implements the `KtTypeElement` interface, indicating its role in type-related functionalities.
 */
enum class KtBaseTypes : KtTypeElement {
    /**
     * Represents the STRING type in the KtBaseTypes enumeration.
     *
     * This type is primarily used to define and handle properties or functions
     * that involve String data type in the Kotlin type system.
     */
    STRING,

    /**
     * Represents the `Int` type in the Kotlin type system.
     *
     * This type corresponds to a 32-bit signed integer and is a part of the base type definitions
     * within the `KtBaseTypes` enumeration. It is used to define and represent integer types in
     * transformed contexts, such as Intermediate Representation (IR) and Frontend Intermediate Representation (FIR).
     */
    INT,

    /**
     * Represents the Kotlin native `Long` type.
     *
     * This type is used to denote 64-bit signed integer values in Kotlin source code.
     * It can be utilized in various transformations and mappings during intermediate representation (IR)
     * or frontend type processing.
     *
     * Common scenarios for its usage include handling constants, resolving type references,
     * and transforming type elements to their corresponding backend representations.
     */
    LONG,

    /**
     * Represents the `DOUBLE` type in the `KtBaseTypes` enumeration.
     *
     * This type is used to define and handle double-precision 64-bit IEEE 754 floating point numbers
     * in Kotlin transformations or type mappings. It corresponds to the `Double` type in Kotlin.
     */
    DOUBLE,

    /**
     * Represents the `FLOAT` type within the `KtBaseTypes` enumeration.
     * This type corresponds to the floating-point data type in Kotlin and is used to
     * denote values that support decimal numbers with single precision.
     *
     * Commonly used in transformations or classifications where the `FLOAT`
     * type needs to be identified among other base types.
     */
    FLOAT,

    /**
     * Represents the `SHORT` type in the Kotlin type system.
     *
     * It is part of the `KtBaseTypes` enumeration, which defines basic Kotlin primitive types
     * and the `Unit` type. This type corresponds to the Kotlin `Short` type and is used in
     * contexts where type transformation or analysis of base types is required.
     *
     * Commonly used in type transformation processes such as IR (Intermediate Representation)
     * or FIR (Frontend Intermediate Representation) transformations.
     */
    SHORT,

    /**
     * Represents the Kotlin `Byte` type within the `KtBaseTypes` enumeration.
     * It is used to define and transform the `byte` type in intermediate representations (IR)
     * or front-end representations (FIR) during various stages of code processing.
     */
    BYTE,

    /**
     * Represents the `Unit` type in Kotlin.
     *
     * This is a special type used to denote the absence of a meaningful value. It is equivalent to `void` in other languages.
     */
    UNIT,
}