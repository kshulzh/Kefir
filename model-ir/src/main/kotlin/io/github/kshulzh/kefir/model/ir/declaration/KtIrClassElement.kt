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

package io.github.kshulzh.kefir.model.ir.declaration

import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declaration.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeParameterElement
import io.github.kshulzh.kefir.model.ir.annotation.wrapIrAnnotations
import io.github.kshulzh.kefir.model.ir.type.KtIrTypeParameterElement
import io.github.kshulzh.kefir.model.ir.type.wrapType
import io.github.kshulzh.kefir.model.utils.createLazyIrList2
import io.github.kshulzh.kefir.model.utils.createLazyIrSet2
import io.github.kshulzh.kefir.transform.FirWrapper
import io.github.kshulzh.kefir.transform.IrWrapper
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import io.github.kshulzh.kefir.transform.model.addDeclaration
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.utils.addToStdlib.UnsafeCastFunction
import org.jetbrains.kotlin.utils.addToStdlib.cast

/**
 * Represents an Intermediate Representation (IR) class element in the Kotlin model.
 *
 * This class provides functionality for working with an IR-based representation
 * of a class and encapsulates its various attributes and behaviors. It acts as
 * a bridge between FIR (Frontend Intermediate Representation) and IR while
 * maintaining a transformation context for Kotlin IR manipulation.
 *
 * @property irElement The underlying IR class element associated with this instance.
 * @property transformContext The transformation context used for handling IR transformations.
 * @property declarationsScope The declarations scope of this class element, if available.
 */
class KtIrClassElement(
    override var irElement: IrClass,
    var transformContext: KtTransformContext,
    override var declarationsScope: KtDeclarationsScope? = null
) : KtClassElement, IrWrapper<IrClass>, FirWrapper<FirClass> {
    /**
     * Represents the name of the current `KtIrClassElement` instance.
     *
     * The value is derived from the underlying IR element's identifier and provides a way
     * to access or modify the `KtName` associated with the element.
     */
    override var name: KtName
        get() = irElement.name.identifier
        set(value) {}

    /**
     * Represents a lazily initialized mutable set of Kotlin declaration elements that belong to this `KtIrClassElement`.
     *
     * This property is implemented using the `createLazyIrSet2` utility function, which enables deferred initialization
     * and efficient transformation of Intermediate Representation (IR) declarations into their corresponding Kotlin model
     * declarations (`KtDeclarationElement`). The set is initialized and modified with the following configuration:
     *
     * - **Initialization**: The set is initialized lazily by invoking a block that gathers the current IR element's declarations
     *   and transforms them into mutable `KtDeclarationElement` instances.
     * - **Transformation**: Each IR declaration in the set can be processed and wrapped into its corresponding Kotlin declaration model
     *   using the `wrapIrDeclaration` function.
     * - **Addition**: When new declaration elements are added to the set, they are processed and added to the associated
     *   IR element using the `addDeclaration` method.
     * - **Deletion**: Any custom behavior for element deletion can be implemented by completing the provided `onDelete` block.
     *
     * This property utilizes the `@OptIn(UnsafeDuringIrConstructionAPI::class)` annotation, indicating that it may depend
     * on APIs or behaviors that are marked as unsafe during the construction of IR elements.
     */
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val declarations: MutableSet<KtDeclarationElement> by createLazyIrSet2(
        transformContext = transformContext,
        initializer = { typeParameters; irElement.declarations.toMutableSet() },
        transformer = { wrapIrDeclaration(it, transformContext, this@KtIrClassElement) },
        property = KtDeclarationElement::declarationsScope,
        onAdd = {
            irElement.addDeclaration(it, this)
        },
        onDelete = {
            //TODO()
        }
    )
    /**
     * Represents the collection of annotations associated with this class element in the Kotlin Intermediate Representation (IR) model.
     *
     * This property is a mutable list of `KtAnnotationElement` instances, which encapsulate the annotations
     * applied to the IR class element. The annotations are wrapped and transformed from their IR counterparts
     * (`IrConstructorCall`) into a Kotlin-based model by leveraging the `wrapIrAnnotations` utility function.
     *
     * Modifications to this property allow updating the annotations associated with this IR class element.
     */
    override var annotations: MutableList<KtAnnotationElement> =
        wrapIrAnnotations(irElement.annotations, transformContext, this)
        set(value) {}


    /**
     * Returns a string representation of the object.
     *
     * This implementation formats the string in the form "<CLASS> <name>",
     * where `<name>` corresponds to the `name` property of the class instance.
     *
     * @return the string representation of the object.
     */
    override fun toString() = "<CLASS> $name"

    /**
     * Represents the FIR (Frontend Intermediate Representation) element associated with this IR (Intermediate Representation) class element.
     *
     * This property is overridden to retrieve the FIR representation of the class using metadata and cast operations.
     * It leverages Kotlin's unsafe cast function to ensure the retrieved metadata corresponds to a `FirClass` type.
     *
     * Useful for interconnecting IR elements with their corresponding FIR elements in compiler transformations
     * or analysis processes where metadata linkage is required.
     *
     * @property firElement The FIR class element derived from the IR metadata.
     */
    @OptIn(UnsafeCastFunction::class)
    override val firElement: FirClass = irElement.metadata?.cast<FirMetadataSource>()?.fir?.cast<FirClass>()!!
    /**
     * Represents the collection of supertypes associated with a Kotlin class element.
     *
     * This property provides a lazily-initialized mutable list of supertypes for the current instance
     * of `KtIrClassElement`. The supertypes are represented as instances of [KtTypeElement], which
     * encapsulate type-related information in Kotlin's intermediate and frontend representations.
     *
     * The initialization and transformation of supertypes are managed through the `createLazyIrList2` utility,
     * using the following behavior:
     *
     * - Supertypes are derived from the underlying IR element's `superTypes` property.
     * - Each IR type is transformed into a [KtTypeElement] instance through the `wrapType` function.
     * - Custom handling operations are provided for additions (`onAdd`).
     *
     * This property is modifiable, allowing for programmatically updating the list of supertypes
     * in transformation or analysis workflows while ensuring consistency in the representation and lifecycle
     * of associated elements.
     */
    override val supertypes: MutableList<KtTypeElement> by createLazyIrList2(
        transformContext,
        {typeParameters; irElement.superTypes},
        { wrapType(it, transformContext)!! },
        onAdd = {e,i->
            //todo
            null
        }
    )
    /**
     * Represents a mutable list of type parameters associated with the class `KtIrClassElement`.
     *
     * This property enables lazy initialization and transformation of type parameters in IR (Intermediate Representation),
     * leveraging the `createLazyIrList2` function. Each type parameter is wrapped as a `KtIrTypeParameterElement` and linked
     * to its scope within the transformation context.
     *
     * The transformation is performed lazily, ensuring efficient handling of type parameter data. Additionally,
     * support for customized actions on adding or removing elements is incorporated.
     *
     * This property is overridden in the `KtIrClassElement` class and works seamlessly with the IR representation framework.
     */
    override var typeParameters: MutableList<KtTypeParameterElement> by createLazyIrList2(
        transformContext,
        irElement::typeParameters,
        { KtIrTypeParameterElement(it, transformContext, this) },
        property = KtTypeParameterElement::typeParameterScope,
        onAdd = { p,i ->
            //todo
            null
        }

    )
}