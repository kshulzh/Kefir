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

package io.github.kshulzh.kefir.model.ir.type

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.transform.IrWrapper
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.classOrNull
import org.jetbrains.kotlin.ir.util.isNullable
import org.jetbrains.kotlin.ir.util.packageFqName

class KtIrClassTypeElement(override val irElement: IrType) : KtClassTypeElement, IrWrapper<IrType> {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override val ktPackage: KtPath by lazy {
        KtPath(irElement.classOrNull?.owner?.packageFqName?.asString() ?: "")
    }
    override val ktClass: KtPath by lazy {
        KtPath(irElement.classFqName!!.asString())
    }
    override val isNullable: Boolean by lazy {
        irElement.isNullable()
    }
    override val typeArguments: MutableList<KtTypeElement>
        get() = TODO("Not yet implemented")

    override fun toString() =
        ktPackage.parts.joinToString(".") + "." +
                ktClass.parts.joinToString(".") +
                //todo add typeArgs
                if (isNullable) "?" else ""
}