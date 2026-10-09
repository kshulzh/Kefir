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

package io.github.kshulzh.kefir.model.external.type

import io.github.kshulzh.kefir.builder.io.FindClass
import io.github.kshulzh.kefir.builder.io.Package
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.external.KtExternalRootPackageElement
import io.github.kshulzh.kefir.model.external.annotation.wrapExternalAnnotations
import io.github.kshulzh.kefir.transform.FirWrapper
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.fir.types.ConeClassLikeType
import org.jetbrains.kotlin.fir.types.classId
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI

/**
 * Represents a class type element in an external Kotlin type model.
 *
 * This class extends the [KtClassTypeElement] interface and implements [FirWrapper]
 * for `ConeClassLikeType`, enabling interactions with both Kotlin's class type model
 * and FIR (Frontend Intermediate Representation). This class encapsulates information
 * about the associated package, class name, nullability, type arguments, and annotations,
 * and provides utilities for handling external type elements within a transformation context.
 *
 * @constructor Initializes the [KtExternalClassTypeElement] with a specified FIR element
 * and root package element.
 * @param firElement The FIR representation of the class-like type used in the external type model.
 * @param root The root package element in which this type element is contained.
 */
class KtExternalClassTypeElement(
    override val firElement: ConeClassLikeType,
    val root: KtExternalRootPackageElement,) : KtClassTypeElement, FirWrapper<ConeClassLikeType> {
    /**
     * The `firClass` property retrieves the class ID of the corresponding FIR (Frontend Intermediate Representation) element.
     *
     * This value identifies the class representation tied to the `firElement` in the Kotlin compiler's FIR model.
     */
    val firClass = firElement.classId
    /**
     * Represents the fully qualified package path of the associated Kotlin FIR class.
     *
     * The value is computed lazily by extracting the package segments from the `packageFqName`
     * property of the FIR class ID (`firClass`) and mapping them into a `KtPath` structure.
     *
     * This property uses the `@OptIn` annotation for experimental or unsafe APIs during IR construction.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val ktPackage: KtPath by lazy {
        KtPath(firClass.packageFqName.pathSegments().map { it.asString() })
    }
    /**
     * Represents the relative class path within the Kotlin model.
     *
     * This value is lazily initialized and provides access to a `KtPath` object
     * that is derived from the `relativeClassName` of the associated `firClass`.
     *
     * The path is constructed based on the sequence of segments contained in
     * the `relativeClassName`, each one converted to its string representation.
     *
     * This is intended to allow navigation or referencing of the relative class path
     * corresponding to the current type element within the Kotlin model structure.
     *
     * @property ktClass A lazily-initialized instance of `KtPath` representing the relative class path.
     */
    override val ktClass: KtPath by lazy {
        KtPath(firClass.relativeClassName.pathSegments().map { it.asString() })
    }
    /**
     * Indicates whether the type is nullable.
     *
     * This property reflects if the type can represent a null value at runtime.
     * The value `true` means the type is nullable, while `false` indicates it is non-nullable.
     *
     * The value is determined lazily based on the `isMarkedNullable` property of `firElement`.
     */
    override val isNullable: Boolean by lazy {
        firElement.isMarkedNullable
    }
    /**
     * Represents the mutable list of type arguments associated with a class or type element.
     *
     * This property contains a collection of `KtTypeElement` instances, which are derived
     * from the `firElement`'s type arguments. Each element in the list corresponds to a type
     * argument specified in the Kotlin class or type, either as a concrete type or a type parameter.
     *
     * The type arguments are mapped and wrapped from their FIR-based `ConeType` representation
     * into `KtTypeElement` objects using the `wrapExternalType` function. The resulting mutable list
     * allows modification of the type argument collection for further processing or transformations.
     */
    override var typeArguments: MutableList<KtTypeElement> = firElement.typeArguments.map { wrapExternalType(it, root)!! }.toMutableList()
    /**
     * Lazily evaluated property that represents the Kotlin class element (`KtClassElement`)
     * associated with the specified package and class path.
     *
     * This property uses the `ktPackage` and `ktClass` paths derived from the `firElement`
     * to locate the corresponding class element within the provided root package scope.
     * It utilizes the `Package` and `FindClass` functions to traverse and resolve the class
     * path in the external model. If the class cannot be found, this property will return `null`.
     *
     * The resolution logic ensures proper connection between the Kotlin FIR model's
     * package-class structure and the external Kotlin model representation.
     */
    override val klass: KtClassElement? by lazy {
        root.Package(ktPackage).FindClass(ktClass)
    }

    /**
     * A lazily-initialized list of annotation elements associated with the current type element.
     *
     * This property provides access to the annotations present on the associated FIR type reference
     * (`firElement.toFirResolvedTypeRef().annotations`), converting them into a list of `KtAnnotationElement`
     * instances suitable for external usage. The wrapping of annotations is facilitated by the
     * `wrapExternalAnnotations` function, with `this` as the annotation scope.
     *
     * The implementation ensures that annotations are resolved and stored in a mutable list format,
     * enabling modification and retrieval as needed.
     */
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapExternalAnnotations(firElement.toFirResolvedTypeRef().annotations, this).toMutableList()
    }

    /**
     * Returns a string representation of the external class type element.
     *
     * The resulting string is constructed by concatenating:
     * - The fully qualified package path, derived from `ktPackage`.
     * - The fully qualified class name, derived from `ktClass`.
     * - A question mark (`?`) if the type is nullable, based on the `isNullable` property.
     *
     * This method provides a human-readable representation of the type element,
     * suitable for debugging or logging purposes.
     *
     * Note: The representation currently does not include type arguments. Their
     * inclusion is marked as a TODO in the implementation.
     *
     * @return A string that represents the external class type element.
     */
    override fun toString() =
        ktPackage.parts.joinToString(".") + "." +
                ktClass.parts.joinToString(".") +
                //todo add typeArgs
                if (isNullable) "?" else ""
}