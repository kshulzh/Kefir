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


import io.github.kshulzh.kefir.model.api.KtElement
import io.github.kshulzh.kefir.model.utils.list.LazyDelayTransformObserverCrossListDelegate
import io.github.kshulzh.kefir.model.utils.list.LazyDelayTransformObserverCrossSetDelegate
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext1
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext2
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KMutableProperty1

fun <O : KtElement, P : KtElement, I> createLazyIr2(
    transformContext: KtTransformContext,
    initializer: () -> I,
    transformer: (I) -> P?,
    initializer1: () -> P? = { initializer().let(transformer) },
    property: KMutableProperty1<P, O?>? = null,
    getter: ((P) -> O?)? = property?.getter,
    setter: ((P, O?) -> Unit)? = property?.setter,
    copy: P.() -> P = { this },
    onSet: KtIrLocalTransformContext.(P?) -> I?,
): ReadWriteProperty<O, P?> {
    val problemContext = transformContext.problemContext
    return if (getter != null && setter != null) {
        if (problemContext != null) {
            LazyDelayTransformObserverCrossDelegate<O, P, KtIrLocalTransformContext, I>(
                KtIrLocalTransformContext1(transformContext),
                problemContext,
                getter, setter, copy, initializer1, onSet
            )
        } else {
            LazyTransformObserverCrossDelegate<O, P, KtIrLocalTransformContext, I>(
                KtIrLocalTransformContext2(transformContext),
                transformer, initializer1, copy, getter, setter, onSet
            )
        }
    } else {
        if (problemContext != null) {
            DelayLazyObserverDelegate<O, P, KtIrLocalTransformContext, I>(
                problemContext,
                KtIrLocalTransformContext1(transformContext), initializer1, onSet
            )
        } else {
            LazyObserverDelegate<O, P, KtIrLocalTransformContext, I>(
                KtIrLocalTransformContext2(transformContext),
                initializer1,
                transformer,
                onSet
            )
        }
    }
}

fun <O : KtElement, P : KtElement, I> createLazyIrList2(
    transformContext: KtTransformContext,
    initializer: () -> List<I>,
    transformer: (I) -> P?,
    property: KMutableProperty1<P, O?>? = null,
    getter: ((P) -> O?)? = property?.getter,
    setter: ((P, O?) -> Unit)? = property?.setter,
    copy: P.() -> P = { this },
    onDelete: KtIrLocalTransformContext.(P, Int) -> Unit = { p, _ -> },
    onAdd: KtIrLocalTransformContext.(P, Int) -> I?,
): ReadWriteProperty<O, MutableList<P>> {
    val problemContext = transformContext.problemContext
    val initializer1 = { initializer().map { transformer(it)!! } }
    return if (getter != null && setter != null) {
        if (problemContext != null) {
            LazyDelayTransformObserverCrossListDelegate<O, P, KtIrLocalTransformContext, I>(
                KtIrLocalTransformContext1(transformContext),
                initializer1,
                problemContext,
                getter, copy, setter, onDelete, onAdd
            )
        } else {
            TODO()
        }
    } else {
        TODO()
    }
}

fun <O : KtElement, P : KtElement, I> createLazyIrSet2(
    transformContext: KtTransformContext,
    initializer: (() -> Set<I>)? = null,
    initializer1: (() -> Set<P>)? = null,
    transformer: (I) -> P?,
    property: KMutableProperty1<P, O?>? = null,
    getter: ((P) -> O?)? = property?.getter,
    setter: ((P, O?) -> Unit)? = property?.setter,
    copy: P.() -> P = { this },
    onDelete: KtIrLocalTransformContext.(P) -> Unit = { p -> },
    onAdd: KtIrLocalTransformContext.(P) -> I?,
): ReadWriteProperty<O, MutableSet<P>> {
    val problemContext = transformContext.problemContext
    val initializer2 = initializer1 ?: { initializer!!().map { transformer(it)!! }.toMutableSet() }
    return if (getter != null && setter != null) {
        if (problemContext != null) {
            LazyDelayTransformObserverCrossSetDelegate<O, P, KtIrLocalTransformContext, I>(
                KtIrLocalTransformContext1(transformContext),
                initializer2,
                problemContext,
                getter, copy, setter, onDelete, onAdd
            )
        } else {
            TODO()
        }
    } else {
        TODO()
    }
}

