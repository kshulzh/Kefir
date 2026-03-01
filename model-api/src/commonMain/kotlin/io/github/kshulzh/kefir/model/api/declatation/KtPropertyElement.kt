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

package io.github.kshulzh.kefir.model.api.declatation

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.modifiers.KtModifierScope
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

/**
 * Represents a property element in the Kotlin model structure.
 *
 * A `KtPropertyElement` is a specific type of declaration element that models properties in Kotlin.
 * It supports key components such as a type, field, getter, setter, and annotations. This interface
 * provides functionalities for defining and managing the properties of a class, interface, or object
 * in the model.
 *
 * The properties defined here may include optional getters and setters to provide custom behavior
 * or access control. It integrates seamlessly with the modifier and annotation scopes, allowing
 * modifications and additional metadata to be applied to the property.
 */
interface KtPropertyElement : KtDeclarationElement,
    KtModifierScope,
    KtAnnotationsScope {
    /**
     * Represents the type associated with a property element in the Kotlin model structure.
     *
     * This property defines the declared type of the property as a [KtTypeElement].
     * It can be used to determine or modify the type of the property within the associated
     * `KtPropertyElement`. The type represents the data type or structure that the property is bound to.
     */
    var type: KtTypeElement

    /**
     * Represents the backing field of a property in the Kotlin model structure.
     *
     * This variable models the backing field associated with a property, allowing
     * direct access to its definition within the Kotlin model. The field is represented
     * as a [KtFieldElement], which encapsulates details such as the field's type,
     * value, modifiers, and annotations. The field may be null, indicating that the
     * property may not have an explicitly defined backing field.
     */
    var field: KtFieldElement?

    /**
     * Represents the getter function associated with a property in the Kotlin model structure.
     *
     * This property holds an instance of [KtFunctionElement], defining the getter logic
     * for the associated [KtPropertyElement]. If set, the getter provides functionality
     * for retrieving the property's value, potentially accompanied by custom logic.
     * A null value indicates the absence of a custom getter, and the default getter behavior is used.
     */
    var getter: KtFunctionElement?

    /**
     * Represents the setter function of a property in the Kotlin model structure.
     *
     * The setter function is a [KtFunctionElement] that defines the behavior for
     * assigning a value to the associated property. It encapsulates the logic
     * for validating or processing the value before storing it in the backing field.
     *
     * This property is nullable, meaning that a property may not have an explicit setter
     * defined. In such cases, the default setter logic is assumed.
     */
    var setter: KtFunctionElement?
}