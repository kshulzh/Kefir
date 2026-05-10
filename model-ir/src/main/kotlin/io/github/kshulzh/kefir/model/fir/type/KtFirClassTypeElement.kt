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

package io.github.kshulzh.kefir.model.fir.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.types.ConeClassLikeType

/**
 * Represents a Kotlin class type element within the FIR (Frontend Intermediate Representation) context.
 *
 * This class wraps a [ConeClassLikeType] instance, providing access to various properties
 * of a class type, such as its package, class name, nullability, and type arguments. It acts
 * as a bridge between Kotlin's type model and the FIR element abstraction, enabling type-related
 * transformations and analyses.
 *
 * @property firElement The FIR element [ConeClassLikeType] associated with this class type element.
 */
class KtFirClassTypeElement(
    override val firElement: ConeClassLikeType,
) : KtClassTypeElement, FirWrapper<ConeClassLikeType> {
    /**
     * Represents the identifier of the class associated with the given FIR element.
     *
     * This value is derived from the `lookupTag` property of the underlying `firElement` and
     * provides access to the class's unique identifier within the Kotlin type system.
     * The identifier encapsulates the fully qualified package name and the relative class name.
     *
     * Commonly used in scenarios involving type resolution, transformations, or analyses
     * in intermediate representations such as FIR (Frontend Intermediate Representation).
     */
    val classId = firElement.lookupTag.classId
    /**
     * Represents the package path of a `KtClassTypeElement` in the Kotlin type model.
     *
     * This property is calculated using the `classId` of the associated `firElement`
     * and is represented as a [KtPath], which encapsulates the fully qualified name
     * of the package in a hierarchical structure.
     *
     * The package path is derived from the `packageFqName` portion of the `classId`,
     * and is commonly used in type transformations, lookups, and analyses related to
     * resolving package-level information for the associated class type.
     */
    override val ktPackage: KtPath = KtPath(classId.packageFqName.asString())
    /**
     * Represents the class path within the Kotlin type system for a specific class-like type.
     *
     * This property provides an instance of [KtPath], which contains the relative name of the class
     * based on its declaration hierarchy, excluding the package path. The class path is expressed as
     * a dot-separated string, offering a structured representation of the class's name within its
     * containing context.
     *
     * It is utilized in intermediate representations (IR/FIR) for deriving type information,
     * resolving the class name, and navigating the model structure of Kotlin's type system.
     */
    override val ktClass: KtPath = KtPath(classId.relativeClassName.asString())
    /**
     * Determines whether the type represented by this element is nullable.
     *
     * If `true`, the type allows null values, signifying it is nullable within the
     * Kotlin type system. If `false`, the type is strictly non-nullable and does not
     * permit `null` as a valid value.
     *
     * This property is derived from the `isMarkedNullable` attribute of the underlying
     * `ConeClassLikeType` instance. It plays a significant role in type-checking processes,
     * ensuring type constraints align with Kotlin's enforced nullability rules.
     */
    override val isNullable: Boolean = firElement.isMarkedNullable
    /**
     * Represents the list of type arguments associated with the current type element.
     *
     * Each type argument is an instance of [KtTypeElement], which models a specific
     * type parameter used in generic type constructs. This property is mutable, allowing
     * modifications such as adding, removing, or replacing type arguments, typically during
     * type transformation or analysis processes.
     *
     * The type arguments play a significant role in representing parameterized types
     * in Kotlin, such as class or function types with generics. They facilitate operations
     * like type resolution, validation, and mapping in intermediate type representations
     * (e.g., IR or FIR).
     */
    override var typeArguments: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")
        set(value) {}
    /**
     * Retrieves the corresponding class element associated with this type element.
     *
     * This property provides access to the underlying [KtClassElement] that represents
     * the structure and characteristics of the class for this type. It may return `null`
     * if the class element is not resolved or applicable in the current context.
     *
     * Commonly used for analyzing or transforming class-specific details during
     * intermediate representation (IR) or frontend intermediate representation (FIR) processing.
     */
    override val klass: KtClassElement?
        get() = TODO("Not yet implemented")
    /**
     * Represents a mutable list of annotations associated with this type element.
     *
     * Each element in the list is an instance of [KtAnnotationElement], which provides details about
     * the annotation, such as its type and nested annotation scope. This property allows you to
     * manage the annotations applied to the type, such as adding or removing specific annotations.
     *
     * It is commonly used in scenarios involving transformation, resolution, and analysis of annotations
     * within the Kotlin type system, particularly when working with intermediate representations (e.g., FIR or IR).
     */
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf()
}