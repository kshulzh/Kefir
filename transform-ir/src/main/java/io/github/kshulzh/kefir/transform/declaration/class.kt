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

package io.github.kshulzh.kefir.transform.declaration

import io.github.kshulzh.kefir.model.api.declaration.KtClassElement
import io.github.kshulzh.kefir.transform.context.KtFirLocalTransformContext
import io.github.kshulzh.kefir.transform.context.KtIrLocalTransformContext
import io.github.kshulzh.kefir.transform.utils.*
import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.Visibilities
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.declarations.*
import org.jetbrains.kotlin.fir.declarations.builder.buildRegularClass
import org.jetbrains.kotlin.fir.declarations.impl.FirDeclarationStatusImpl
import org.jetbrains.kotlin.fir.moduleData
import org.jetbrains.kotlin.fir.scopes.kotlinScopeProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.util.createImplicitParameterDeclarationWithWrappedDescriptor

/**
 * Transforms a `KtClassElement` into a corresponding `IrClass` representation using the
 * specified local IR transformation context.
 *
 * This method creates an `IrClass` using the attributes and declarations of the given
 * `KtClassElement`, links it with the related intermediate representation context,
 * and performs additional setup, including metadata and declarations initialization.
 *
 * @param input The `KtClassElement` instance representing the class to be transformed
 *              into its IR equivalent. It contains the relevant properties, annotations, declarations,
 *              and structural information necessary for the transformation.
 * @return An `IrClass` object representing the transformed IR class, or `null` if the transformation could not complete.
 */
@OptIn(DeprecatedForRemovalCompilerApi::class, UnsafeDuringIrConstructionAPI::class)
fun KtIrLocalTransformContext.transformIrClass(input: KtClassElement): IrClass? {
    return transformContext.pluginContext.irFactory.buildClass {
        name = input.name.transform()
        kind = ClassKind.CLASS
    }.linkIr(input).also {
        it.createImplicitParameterDeclarationWithWrappedDescriptor()
        fork {
            it.parent = input.declarationsScope.getIrOrExternal() ?: throw RuntimeException("Parent is null")
        }
        fork {
            it.metadata = FirMetadataSource.Class(with(this@transformIrClass.fir()) { firTransform(input)!! })

            transformContext.irStructure.addClass(it)
        }
        fork {
            input.declarations.forEach { d ->
                it.declarations.add(irTransform(d)!!)
            }
        }

    }
}

/**
 * Transforms a Kotlin class element into its corresponding FIR (Frontend Intermediate Representation) class structure.
 *
 * This method converts a given [KtClassElement] into a [FirClass] by constructing a new regular FIR class using the class's
 * metadata, such as its name, visibility, modality, and other properties. The method links the FIR class with the original
 * Kotlin class element and processes its declarations into the resulting FIR structure.
 *
 * @param input The Kotlin class element ([KtClassElement]) to be transformed into a FIR class.
 * @return The transformed [FirClass] representation of the given [KtClassElement], or `null` if the transformation fails.
 */
@OptIn(DirectDeclarationsAccess::class)
fun KtFirLocalTransformContext.transformFirClass(input: KtClassElement): FirClass? {
    val classId = input.classId()
    return buildRegularClass {
        source = createSource()
        resolvePhase = FirResolvePhase.BODY_RESOLVE
        moduleData = firSession.moduleData
        origin = FirDeclarationOrigin.Source
        attributes = FirDeclarationAttributes()
        //typeParameters = mutableListOf()
        status = FirDeclarationStatusImpl(
            visibility = Visibilities.Public,
            modality = Modality.OPEN
        )
        deprecationsProvider = UnresolvedDeprecationProvider
        scopeProvider = firSession.kotlinScopeProvider
        //todo fix it later
        classKind = ClassKind.CLASS
        //annotations = mutableListOf()
        name = input.name.transform()
        symbol = FirRegularClassSymbol(classId)
        //companionObjectSymbol: FirRegularClassSymbol? = null
        //superTypeRefs = mutableListOf()
        //contextParameters= mutableListOf()
    }.linkFir(input).also { klass ->
        firStructure.addClass(klass)

        val declarations = klass.declarations as MutableList<FirDeclaration>

        fork {
            input.declarations.forEach {
                declarations.add(firTransform(it)!!)
            }
        }
    }
}