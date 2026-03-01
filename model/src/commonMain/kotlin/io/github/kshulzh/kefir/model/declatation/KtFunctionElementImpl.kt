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

package io.github.kshulzh.kefir.model.declatation

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.declatation.KtFunctionElement
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.modifiers.KtModifier
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createListDelegate
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Represents the implementation of a Kotlin function element in the abstract syntax tree (AST).
 *
 * This class provides functionality for handling Kotlin functions, including their body,
 * parameters, type, annotations, and modifiers. It supports managing attributes
 * and scopes associated with the function while allowing for modifications and dynamic updates
 * to its structure.
 *
 * @property name The name of the function. It is represented as a [KtName] and identifies the function.
 * @property body The body of the function, represented as a [KtExpressionElement]. It may be null
 *                if the function has no defined body (e.g., abstract functions).
 * @property type The return type of the function, represented as a [KtTypeElement]. It may be null
 *                if the function does not have an explicitly defined return type.
 * @property modifiers A mutable collection of modifiers associated with the function. These modifiers
 *                     are instances of [KtModifier] and define characteristics like visibility, modality, etc.
 * @property declarationsScope The declarations scope for the function, represented as a [KtDeclarationsScope].
 *                             It provides context and containment for the declarations within the function.
 * @property annotations A mutable list of annotations applied to the function. Each annotation is
 *                       represented as a [KtAnnotationElement].
 * @property parameters The parameter list of the function, represented as a mutable list of [KtParameterElement].
 *                      Parameters define the input values accepted by the function.
 * @property attributes A collection of additional attributes associated with the function. Attributes are
 *                      stored as a map of key-value pairs and can represent supplementary metadata.
 */
class KtFunctionElementImpl(
    override var name: KtName,
    body: KtExpressionElement? = null,
    override var type: KtTypeElement? = null,
    override var modifiers: MutableSet<KtModifier> = mutableSetOf(),
    override var declarationsScope: KtDeclarationsScope? = null,
    override var annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    parameters: MutableList<KtParameterElement> = mutableListOf(),
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtFunctionElement, KtAttributes {
    init {
        for (element in parameters) {
            element.parametersScope = this
        }
    }

    /**
     * Represents the body of a Kotlin function element within the abstract syntax tree (AST).
     *
     * This property holds an instance of [KtExpressionElement] or null. It provides the structural
     * representation of the function body and supports both simple and complex expressions.
     *
     * If the function does not have a body, the property is null. The delegate mechanism ensures
     * synchronization with the corresponding `parent` reference in [KtExpressionElement], enabling
     * consistent hierarchical relationships within the AST.
     */
    override var body: KtExpressionElement? by createNullableDelegate(body, KtExpressionElement::parent)

    /**
     * Represents the collection of parameters defined for this function element.
     *
     * This property uses a delegated mutable list to manage `KtParameterElement` instances
     * associated with the function. The delegation ensures that updates to the list are
     * consistently propagated to maintain their relationship with the parent function element's scope.
     *
     * Each parameter in the list has its `parametersScope` property associated with the containing
     * function element, enabling access to the scope context. This linkage is crucial for
     * resolving parameter-level references and for maintaining structural integrity within the
     * Kotlin model.
     *
     * Modifications to the parameter collection, such as adding or removing elements, will trigger
     * updates to associate the parameters with the correct parent function scope.
     */
    override var parameters: MutableList<KtParameterElement> by createListDelegate(
        parameters,
        KtParameterElement::parametersScope
    )

    override fun toString() = "<FUNCTION> $name"
}