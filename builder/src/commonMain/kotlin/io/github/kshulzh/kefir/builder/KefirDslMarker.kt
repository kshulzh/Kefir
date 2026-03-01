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

package io.github.kshulzh.kefir.builder

/**
 * A DSL marker annotation used to restrict the scope of certain DSL functions and builders
 * within the `Kefir` library. Applying this annotation ensures that nested DSL receivers
 * cannot unintentionally access methods or properties from outer scopes, promoting cleaner and safer DSL usage.
 *
 * This annotation is typically applied to DSL initialization lambdas to provide context-specific
 * isolation, reducing the likelihood of accidental misuse of APIs that belong to a different scope.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class KefirDslMarker()
