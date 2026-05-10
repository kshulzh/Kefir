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

package io.github.kshulzh.kefir.model.arg

import io.github.kshulzh.kefir.model.api.KtAttributes
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.arg.KtParameterElement
import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.utils.createNullableDelegate

/**
 * Implementation of the `KtParameterElement` interface, representing a parameter element
 * in the Kotlin language model. This class defines the structure and attributes of a parameter,
 * including its name, type, default value, and scope.
 *
 * This class serves as a concrete implementation capable of representing parameters in various
 * contexts such as function parameters, constructor parameters, or lambda parameters.
 * It also supports attributes and customization through a mutable attributes map.
 *
 * @property name The name of the parameter, identifying it within its respective scope.
 * @property type The type of the parameter, represented by a `KtTypeElement`. This may be null if no type is specified.
 * @property value The default value or initializer for the parameter, represented by a `KtExpressionElement`.
 * @property parametersScope The scope associated with the parameter, encapsulated in a `KtParametersScope`.
 * @property kind Specifies the kind of parameter, such as regular or dispatch receiver, defined by the `KtParameterElement.Kind` enum.
 * @property attributes A mutable map holding key-value pairs for custom attributes attached to the parameter.
 */
class KtParameterElementImpl(
    override var name: KtName,
    override var type: KtTypeElement? = null,
    value: KtExpressionElement? = null,
    override var parametersScope: KtParametersScope? = null,
    override val annotations: MutableList<KtAnnotationElement> = mutableListOf(),
    override var kind: KtParameterElement.Kind? = null,
    override var attributes: MutableMap<String, Any> = mutableMapOf(),
) : KtParameterElement, KtAttributes {
    /**
     * Represents the default value or initializer of a parameter in the Kotlin model.
     *
     * This property holds a reference to a `KtExpressionElement` that defines the initial value
     * associated with a parameter. It may be null to indicate that the parameter does not
     * have a default value or initializer.
     *
     * The `value` can be used for type resolution, where the parameter's type can be inferred
     * based on the type of the initializer expression. Additionally, it is useful in scenarios
     * where a parameter's default value contributes to semantic analysis or transformation
     * of the syntax structure.
     *
     * This overridden property delegates its implementation using the `createDelegate` function,
     * which allows for enhanced behavior by delegating access and modification operations.
     * The associated `parent` property from `KtExpressionElement` is utilized for maintaining
     * hierarchical relationships within the abstract syntax tree structure.
     */
    override var value: KtExpressionElement? by createNullableDelegate(value, KtExpressionElement::parent)
}