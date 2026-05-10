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

package io.github.kshulzh.kefir.transform.type

import io.github.kshulzh.kefir.model.api.type.KtBaseTypes
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.transform
import org.jetbrains.kotlin.fir.types.ConeKotlinType
import org.jetbrains.kotlin.fir.types.impl.ConeClassLikeTypeImpl
import org.jetbrains.kotlin.fir.types.toLookupTag
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

/**
 * Transforms a given `KtTypeElement` into its corresponding `IrType` representation based on its specific type.
 * This function supports transformation for certain concrete implementations of `KtTypeElement`.
 *
 * @param input The `KtTypeElement` instance to be transformed. It can represent different types in the Kotlin type system,
 *              such as `KtClassTypeElement` or `KtBaseTypes`.
 * @return The transformed `IrType` representation if the input type is supported and valid; otherwise, returns `null`.
 */
fun KtIrLocalTransformContext.transformIrType(input: KtTypeElement): IrType? {
    return when (input) {
        is KtClassTypeElement -> irTransform(input)
        is KtBaseTypes -> irTransform(input)
        else -> null
    }
}

/**
 * Transforms a given [KtTypeElement] into its corresponding [ConeKotlinType].
 * This method operates within the context of [KtFirLocalTransformContext] and maps
 * specific type elements to their FIR (Frontend Intermediate Representation) counterparts.
 *
 * @param input The [KtTypeElement] to be transformed. Supported types include [KtClassTypeElement]
 * and [KtBaseTypes], which represent class type elements and base types, respectively.
 * For unsupported types, the method will return null.
 *
 * @return A [ConeKotlinType] representing the FIR transformation of the given [KtTypeElement],
 * or null if the transformation is not applicable for the provided input.
 */
fun KtFirLocalTransformContext.transformFirType(input: KtTypeElement): ConeKotlinType? {
    return when (input) {
        is KtClassTypeElement -> firTransform(input)
        is KtBaseTypes -> firTransform(input)
        else -> null
    }
}

/**
 * Transforms a Kotlin class type element (`KtClassTypeElement`) into its corresponding intermediate representation type (`IrType`).
 * Utilizes the transformation context to resolve the class reference and associated type arguments.
 *
 * @param input The `KtClassTypeElement` representing the Kotlin class type to be transformed.
 *              Includes details about the package, class name, nullability, and type arguments.
 * @return The transformed `IrType` for the given class type element, or `null` if the transformation cannot be performed.
 */
fun KtIrLocalTransformContext.transformClassIrType(input: KtClassTypeElement): IrType? {
    val typeArguments = input.typeArguments.map { irTransform(it)!! }
    return transformContext.pluginContext.referenceClass(
        ClassId(
            input.ktPackage.transform() ?: FqName.ROOT,
            input.ktClass.transform()!!,
            false
        )
    )?.typeWith(typeArguments)
}

/**
 * Transforms a `KtClassTypeElement` into a corresponding `ConeKotlinType` representation.
 *
 * This method processes the type arguments of the provided `KtClassTypeElement` and constructs
 * a `ConeClassLikeTypeImpl` with transformed type arguments, fully qualified class ID, and nullability.
 *
 * @param input the `KtClassTypeElement` to be transformed. It contains information
 * about the type's package, class name, type arguments, and nullability.
 * @return the transformed `ConeKotlinType`, or null if the transformation is not applicable.
 */
fun KtFirLocalTransformContext.transformClassFirType(input: KtClassTypeElement): ConeKotlinType? {
    val typeArguments = input.typeArguments.map { firTransform(it)!! }.toTypedArray()

    return ConeClassLikeTypeImpl(
        ClassId(
            input.ktPackage.transform() ?: FqName.ROOT,
            input.ktClass.transform()!!, false
        ).toLookupTag(),
        typeArguments,
        input.isNullable
    )
}

/**
 * Transforms a given `KtBaseTypes` to its corresponding `IrType`.
 *
 * @param input The input base type represented as a `KtBaseTypes` enumeration value.
 * @return The corresponding `IrType` for the given `KtBaseTypes`, or `null` if no match is found.
 */
fun KtIrLocalTransformContext.transformBaseIrType(input: KtBaseTypes): IrType? {
    return when (input) {
        KtBaseTypes.STRING -> transformContext.pluginContext.irBuiltIns.stringType
        KtBaseTypes.INT -> transformContext.pluginContext.irBuiltIns.intType
        KtBaseTypes.LONG -> transformContext.pluginContext.irBuiltIns.longType
        KtBaseTypes.DOUBLE -> transformContext.pluginContext.irBuiltIns.doubleType
        KtBaseTypes.FLOAT -> transformContext.pluginContext.irBuiltIns.floatType
        KtBaseTypes.SHORT -> transformContext.pluginContext.irBuiltIns.shortType
        KtBaseTypes.BYTE -> transformContext.pluginContext.irBuiltIns.byteType
        KtBaseTypes.UNIT -> transformContext.pluginContext.irBuiltIns.unitType
    }
}

/**
 * Transforms a given base type from the `KtBaseTypes` enumeration into its corresponding `ConeKotlinType`.
 *
 * @param input The base type from the `KtBaseTypes` enumeration to be transformed.
 * @return The corresponding `ConeKotlinType` for the given base type, or `null` if the type cannot be transformed.
 */
fun KtFirLocalTransformContext.transformBaseFirType(input: KtBaseTypes): ConeKotlinType? {
    return when (input) {
        KtBaseTypes.STRING -> transformContext.firSession.builtinTypes.stringType.coneType
        KtBaseTypes.INT -> transformContext.firSession.builtinTypes.intType.coneType
        KtBaseTypes.LONG -> transformContext.firSession.builtinTypes.longType.coneType
        KtBaseTypes.DOUBLE -> transformContext.firSession.builtinTypes.doubleType.coneType
        KtBaseTypes.FLOAT -> transformContext.firSession.builtinTypes.floatType.coneType
        KtBaseTypes.SHORT -> transformContext.firSession.builtinTypes.shortType.coneType
        KtBaseTypes.BYTE -> transformContext.firSession.builtinTypes.byteType.coneType
        KtBaseTypes.UNIT -> transformContext.firSession.builtinTypes.unitType.coneType
    }
}