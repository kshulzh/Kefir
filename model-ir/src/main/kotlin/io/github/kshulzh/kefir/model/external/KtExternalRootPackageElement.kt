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

package io.github.kshulzh.kefir.model.external

import io.github.kshulzh.kefir.model.api.KtExternalElement
import io.github.kshulzh.kefir.model.api.KtName
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.transform.context.KtTransformContext
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class KtExternalRootPackageElement(
    var transformContext: KtTransformContext,
    val symbolProvider: FirSymbolProvider,
    override val packageElements: MutableSet<KtPackageScopeElement> = mutableSetOf()
) : KtPackageScope, KtExternalElement {
    var fqName: FqName = FqName.ROOT

    override fun createPackage(name: KtName): KtPackageScope {
        return KtExternalPackageElement(name, this, fqName.child(Name.identifier(name)), this).also {
            packageElements.add(it)
        }
    }

    override fun toString() = "<ROOT>"
}