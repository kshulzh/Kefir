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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationElement
import io.github.kshulzh.kefir.model.api.annotation.KtAnnotationsScope
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement


/**
 * Finds and returns a list of annotation elements that match the specified type within the current annotation scope.
 *
 * This method filters the annotations in the current scope, returning those whose type
 * matches the provided `type` parameter.
 *
 * @param type The type element to be matched against the types of annotations in the scope.
 * @return A list of annotation elements whose types match the provided type.
 */
fun KtAnnotationsScope.findAnnotations(type: KtTypeElement) : List<KtAnnotationElement> {
    return annotations.filter { it.type.equal(type) }
}

/**
 * Searches for an annotation matching the specified type within the current annotation scope.
 *
 * This method iterates over the annotations present in the `KtAnnotationsScope` and
 * returns the first annotation whose type matches the provided `KtTypeElement`.
 *
 * @param type The type of the annotation to search for.
 * @return The first `KtAnnotationElement` whose type matches the provided `type`,
 *         or `null` if no matching annotation is found.
 */
fun KtAnnotationsScope.findAnnotation(type: KtTypeElement) : KtAnnotationElement? {
    return annotations.firstOrNull { it.type.equal(type) }
}

/**
 * Compares the current [KtTypeElement] to another [KtTypeElement] to determine if they are equal.
 *
 * The equality check is specific to instances of [KtClassTypeElement]. For two such elements to be
 * considered equal, their `ktPackage` and `ktClass` properties must match. If either of the elements
 * is not a [KtClassTypeElement], the method returns `false`.
 *
 * @param type The [KtTypeElement] to compare against the current element.
 * @return `true` if the current element and the provided [KtTypeElement] are equal
 *         according to the specified conditions; otherwise, `false`.
 */
private infix fun KtTypeElement.equal(type: KtTypeElement) : Boolean {
    if (this is KtClassTypeElement && type is KtClassTypeElement) {
        if (this.ktPackage != type.ktPackage) return false
        if (this.ktClass != type.ktClass) return false
        return true
    }
    return false
}

/**
 * Checks if a specified type element is annotated within the current scope.
 *
 * This method determines whether an annotation matching the given type
 * element exists in the annotation scope by attempting to locate such an
 * annotation.
 *
 * @param type The [KtTypeElement] to check for an associated annotation.
 * @return `true` if an annotation matching the provided type element exists,
 *         otherwise `false`.
 */
fun KtAnnotationsScope.isAnnotated(type: KtTypeElement) : Boolean {
    return findAnnotation(type) != null
}

/**
 * Finds and retrieves all annotated scopes within the given package that match the specified
 * annotation targets and are declared on the provided type element.
 *
 * This method searches through all files in the current package scope, identifying annotations
 * applied to the specified type element that match any of the given annotation targets.
 *
 * @param typeElement The type element to search for annotations on.
 * @param targets The annotation targets to filter the search by. Multiple targets can be specified.
 * @return A list of [KtAnnotationsScope] instances representing the annotated scopes that match
 *         the criteria within the package scope.
 */
fun KtPackageScope.findAnnotated(typeElement: KtTypeElement, vararg targets: AnnotationTarget) : List<KtAnnotationsScope> {
    return getFiles().flatMap { it.findAnnotated(typeElement, *targets) }
}

/**
 * Finds all annotated elements within a Kotlin file element that match the specified type and annotation targets.
 *
 * @param typeElement The type element that is used to check for specific annotations within the Kotlin file.
 * @param targets A variable number of annotation targets used to filter the elements to be included in the result.
 * @return A list of annotated scopes within the file element that match the specified type and targets.
 */
fun KtFileElement.findAnnotated(typeElement: KtTypeElement, vararg targets: AnnotationTarget) : List<KtAnnotationsScope> {
    val visitor = KtAnnotationFinderVisitor(typeElement, targets.toList())
    visitor.visitFile(this, Unit)

    return visitor.result
}

