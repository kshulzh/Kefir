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

package io.github.kshulzh.kefir.model.api.arg

import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.api.utils.KtVisitor

/**
 * Represents a parameter element in the Kotlin language model.
 * Typically describes parameters of functions, constructors, lambdas, or other callable entities.
 */
interface KtParameterElement : KtElement, KtAnnotationsScope {
    /**
     * Represents the name of the parameter in a Kotlin model.
     *
     * The `name` property is used to identify the parameter within its respective scope, which could
     * include function definitions, lambda parameters, or other elements that support parameterization.
     * It is essential for mapping parameters to their corresponding values in various constructs.
     */
    var name: KtName

    /**
     * Represents the type of the parameter element in the Kotlin abstract model.
     *
     * This property holds a reference to a `KtTypeElement` that defines the type
     * of the parameter. It may be null if the type is not explicitly specified.
     *
     * The `type` value can be resolved or inferred, depending on the usage context.
     * It is used in various transformations or analyses of the Kotlin model to
     * associate type information with parameters.
     */
    var type: KtTypeElement?

    /**
     * Represents the default value or initializer of a parameter in the Kotlin model.
     *
     * This property holds an expression that defines the initial value for a parameter
     * when no value is explicitly provided. It can also be null to indicate that the parameter
     * does not have a default value.
     *
     * The expression assigned to this property can be used for type resolution, where the
     * type of the parameter may be inferred from the type of the expression.
     */
    var value: KtExpressionElement?

    /**
     * Represents the scope of parameters associated with a specific parameter element.
     *
     * This property holds a reference to a `KtParametersScope` instance, which encapsulates a collection
     * of `KtParameterElement` instances. It is used to manage the relationship between a parameter
     * element and its containing scope and to facilitate structured access to the parameters within
     * the corresponding context.
     *
     * The `parametersScope` property is typically set when a parameter is added to a scope, associating
     * the parameter with the parent container that defines the scope.
     */
    var parametersScope: KtParametersScope?

    /**
     * Represents the kind of the parameter, providing additional context or categorization
     * regarding its role.
     *
     * This property can be used to identify if the parameter is a regular parameter
     * or has a specific purpose, such as serving as a dispatch receiver.
     * The value can be null to indicate default behavior.
     *
     * Possible values are defined in the nested `Kind` enum.
     */
    var kind: Kind?

    override fun <R, D> accept(visitor: KtVisitor<R, D>, data: D): R = visitor.visitParameter(this, data)

        /**
     * Represents the kind of a parameter in the Kotlin model.
     * Defines whether the parameter is a regular parameter or a dispatch receiver.
     */
    enum class Kind {
        /**
         * Represents a regular kind of parameter in the Kotlin model.
         *
         * This is one of the possible kinds defined in the `Kind` enum for parameter elements.
         * The `Regular` kind is commonly used to denote parameters with standard behavior
         * that do not fulfill specialized roles like being a dispatch receiver.
         */
        Regular,

        /**
         * Represents a special kind of receiver parameter called a dispatch receiver in Kotlin's abstract representation model.
         *
         * In the context of Kotlin functions, a dispatch receiver is implicitly bound to the instance on which the
         * function is invoked. This is commonly used in member functions and extensions that operate within the scope
         * of a specific class or object instance.
         *
         * Dispatch receiver parameters are generally not explicitly declared in the source code but are an integral part
         * of the underlying function structure. They are critical to resolving `this` references within the body of the
         * function, enabling access to the member properties and functions of the enclosing class or object.
         *
         * This class or enum value can be used within abstract models to signify the nature of a parameter as being of
         * dispatch receiver kind, enabling enhanced support for modeling and analyzing the implicit scope of functions
         * and their relationships to the enclosing structures in Kotlin programs.
         */
        DispatchReceiver,
    }
}