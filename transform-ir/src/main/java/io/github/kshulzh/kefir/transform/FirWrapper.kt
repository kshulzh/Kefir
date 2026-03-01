/*
 * Copyright (c) 2026. Kirill Shulzhenko
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

package io.github.kshulzh.kefir.transform

/**
 * Represents a wrapper interface for encapsulating a specific type of FIR (Frontend Intermediate Representation) element.
 *
 * @param I The type of the FIR element that the implementing class encapsulates.
 */
interface FirWrapper<I> {
    /**
     * Represents the FIR (Frontend Intermediate Representation) element associated with the current wrapper.
     *
     * This property provides access to the underlying FIR structure related to the associated element,
     * enabling transformations or analyses at the FIR level. The type of the FIR element is generic and
     * is determined by the type parameter of the implementing class or interface.
     */
    val firElement: I
}