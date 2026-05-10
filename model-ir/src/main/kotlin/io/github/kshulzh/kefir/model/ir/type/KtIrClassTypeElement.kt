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

package io.github.kshulzh.kefir.model.ir.type

import io.github.kshulzh.kefir.builder.io.FindClass
import io.github.kshulzh.kefir.builder.io.Package
import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.utils.createLazyIrList2
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.isNullable

/**
 * Represents an implementation of the `KtClassTypeElement` interface, which models
 * Kotlin class types within the Intermediate Representation (IR) transformation process.
 *
 * This class wraps an [IrSimpleType] element and provides additional Kotlin-specific
 * type information such as its package, class name, type arguments, nullability,
 * and annotations. It is designed to integrate with IR transformation workflows
 * and enables interaction between Kotlin IR structures and the `KtTransformContext`.
 *
 * @constructor Creates an instance of `KtIrClassTypeElement` with the given [irElement]
 * and associated [transformContext] for transformation and analysis during compilation.
 *
 * @property irElement The underlying [IrSimpleType] that represents the IR-level type information.
 * This is used as the foundation for extracting and transforming the Kotlin-specific type model.
 *
 * @property transformContext The context used for transforming and interacting with Kotlin IR elements.
 */
class KtIrClassTypeElement(
    override val irElement: IrSimpleType,
    var transformContext: KtTransformContext) : KtClassTypeElement, IrWrapper<IrSimpleType> {
    /**
     * Represents a reference to the `classId` of the class associated with the `irElement`.
     * The value is derived by resolving the class symbol from the `irElement` and then
     * obtaining the corresponding `classId`.
     *
     * This property is guaranteed to hold a non-null value due to the enforced resolution
     * of the `classOrNull` symbol and subsequent dereferencing.
     *
     * Usage context: Typically used within the type system transformations for accessing
     * or processing the unique identifier of a class in the Intermediate Representation (IR).
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    val irClass = irElement.classOrNull?.owner?.classId!!
    /**
     * Represents the package path of the associated Kotlin IR (Intermediate Representation) class.
     *
     * The `ktPackage` property uses the fully qualified package name of the underlying IR class
     * to create a [KtPath], which provides a structured, hierarchical representation of the package path.
     *
     * This lazy property is constructed by mapping and converting the segments of the fully qualified
     * package name into their string representation.
     *
     * It is marked with [@OptIn(UnsafeDuringIrConstructionAPI::class)] to indicate that it accesses
     * elements which may not be stable during the early stages of IR construction.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val ktPackage: KtPath by lazy {
        KtPath(irClass.packageFqName.pathSegments().map { it.asString() })
    }
    /**
     * Represents the corresponding class name in a Kotlin type system for the IR (Intermediate Representation) class.
     * The value is computed based on the relative class name of the `IrClass` element and stored as a `KtPath`.
     *
     * This property is lazily initialized to ensure it is computed only when first accessed.
     * The computation involves extracting the relative class name of the `IrClass` associated with the type and
     * transforming it into a hierarchical path structure (`KtPath`) for further usage or lookups.
     */
    override val ktClass: KtPath by lazy {
        KtPath(irClass.relativeClassName.pathSegments().map { it.asString() })
    }
    /**
     * Indicates whether the type represented by this element is nullable.
     * This value is derived lazily from the associated IR element using
     * its `isNullable` method. If `true`, the type can represent a null value.
     */
    override val isNullable: Boolean by lazy {
        irElement.isNullable()
    }
    override var typeArguments: MutableList<KtTypeElement> by createLazyIrList2(
        transformContext,
        irElement::arguments,
        {wrapType(it, transformContext)!!},
        onAdd = { e, i ->
            val element = irTransform(e)!!
            val arguments = irElement.arguments
            if (arguments is MutableList) {
                if (i > -1) {
                    arguments.add(i, element)
                } else {
                    arguments.add(element)
                }
            }
            null
        }
    )
    /**
     * The `klass` property represents the transformed `KtClassElement` corresponding
     * to the Kotlin class structure. It is lazily initialized and attempts to resolve
     * the class element from the transformation context using the package and class paths.
     *
     * The resolution process first searches for the class within the current root
     * transformation context. If it is not found, it then searches the external root.
     *
     * This property is useful for obtaining and interacting with the underlying
     * class representation in cases where transformations of Kotlin elements are
     * performed, ensuring a seamless linkage between Kotlin IR (Intermediate
     * Representation) and the model layer.
     */
    override val klass: KtClassElement? by lazy {
        transformContext.root.Package(ktPackage).FindClass(ktClass)
            ?: transformContext.externalRoot.Package(ktPackage).FindClass(ktClass)
    }

    /**
     * Converts the `KtIrClassTypeElement` instance into a string representation.
     *
     * The resulting string consists of the fully qualified package name, the
     * relative class name, and an optional nullable marker ("?") depending on
     * whether the type is nullable. Components are concatenated with dots.
     *
     * Note: Type arguments are currently not included in the generated string.
     *
     * @return A string representation of the `KtIrClassTypeElement` in the format:
     *         `<package>.<class>[?]`.
     */
    override fun toString() =
        ktPackage.parts.joinToString(".") + "." +
                ktClass.parts.joinToString(".") +
                //todo add typeArgs
                if (isNullable) "?" else ""

    /**
     * Represents a lazily initialized mutable list of `KtAnnotationElement` annotations
     * for the corresponding `irElement`.
     *
     * This property is a wrapper around IR annotations, transforming them into
     * Kotlin-specific model annotations using the `wrapIrAnnotations` function.
     * The transformation relies on the `transformContext` and assigns the current
     * instance (`this`) as the scope for annotation transformation.
     *
     * Usage of `lazy` ensures that the annotation list is initialized only once,
     * and the transformed annotations are stored in a mutable list for future modifications.
     */
    override val annotations: MutableList<KtAnnotationElement> by lazy {
        wrapIrAnnotations(irElement.annotations, transformContext, this).toMutableList()
    }
}