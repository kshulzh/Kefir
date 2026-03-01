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
import io.github.kshulzh.kefir.model.api.io.KtFileElement
import io.github.kshulzh.kefir.model.api.io.KtPackageElement
import io.github.kshulzh.kefir.model.api.io.KtPackageScope
import io.github.kshulzh.kefir.model.api.io.KtPackageScopeElement
import io.github.kshulzh.kefir.model.external.declaration.KtExternalClassElement
import io.github.kshulzh.kefir.model.external.declaration.KtExternalFileElement
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

class KtExternalPackageElement(
    override var name: KtName,
    val rootPackage: KtExternalRootPackageElement,
    val fqName: FqName,
    val root: KtExternalRootPackageElement,
    override var parent: KtPackageScope? = null,
    override val packageElements: MutableSet<KtPackageScopeElement> = mutableSetOf()
) : KtPackageElement, KtExternalElement {
    companion object {
        const val CALLABLES_FILE = "<callables>.kt"
    }

    override fun createPackage(name: KtName): KtPackageScope {
        return KtExternalPackageElement(name, rootPackage, fqName.child(Name.identifier(name)), root, this).also {
            packageElements.add(it)
        }
    }

    @OptIn(SymbolInternals::class)
    override fun getFile(name: KtName): KtFileElement? {
        return super.getFile(name) ?: run {
            val className = name.removeSuffix(".kt")
            val klass =
                root.symbolProvider.getClassLikeSymbolByClassId(ClassId(fqName, Name.identifier(className)))?.fir
            if (klass != null) {
                return KtExternalFileElement(
                    mutableSetOf(),
                    name,
                    this,
                    root
                ).also {
                    it.declarations.add(KtExternalClassElement(klass as FirClass, root, it))
                }
            } else {
                super.getFile(CALLABLES_FILE) ?: KtExternalFileElement(
                    mutableSetOf(),
                    CALLABLES_FILE,
                    this,
                    root
                ).also {
                    packageElements.add(it)
                }
            }
        }
    }

    override fun toString() = name
}