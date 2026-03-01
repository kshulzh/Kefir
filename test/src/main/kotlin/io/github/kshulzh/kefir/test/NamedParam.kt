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

package io.github.kshulzh.kefir.test

/**
 * Represents a named parameter, encapsulating a name and an associated value.
 *
 * This class is utilized in contexts where parameter identification and storage
 * of related data, potentially nullable, are required.
 *
 * @property name The identifier for the parameter.
 * @property value The data associated with the parameter, which can be null.
 */
class NamedParam(
    val name: String,
    val value: Any?
)