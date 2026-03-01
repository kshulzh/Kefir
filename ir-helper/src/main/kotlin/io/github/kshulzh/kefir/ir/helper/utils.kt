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

package io.github.kshulzh.kefir.ir.helper


import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationElement
import io.github.kshulzh.kefir.model.api.declatation.KtDeclarationsScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementElement
import io.github.kshulzh.kefir.model.api.statement.KtStatementsScope
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrDeclaration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.expressions.IrExpression

/**
 * Creates a new IR (Intermediate Representation) declaration element within the current declarations scope.
 * The provided initializer function is used to define the initialization logic for the IR declaration.
 *
 * @param initializer A lambda function that initializes an [IrDeclaration] within the given IR context.
 *                    It accepts an [IrModuleFragment] and [IrPluginContext] as parameters.
 * @return A [KtDeclarationElement] representing the initialized IR declaration.
 */
fun KtDeclarationsScope.irDeclaration(initializer: (IrModuleFragment, IrPluginContext) -> IrDeclaration): KtDeclarationElement {
    return KtIrInitDeclarationElement(initializer, this)
}

/**
 * Creates an instance of `KtIrInitExpressionElement` using the provided initializer lambda.
 * The initializer defines the logic for constructing an IR expression within the given module
 * and plugin context. This function bridges the Kotlin compiler's intermediate representation (IR)
 * model with the Kotlin syntax tree.
 *
 * @param initializer A lambda function that initializes an `IrExpression` within the context of the
 *                    provided `IrModuleFragment` and `IrPluginContext`. The function is used to dynamically
 *                    generate the IR representation of the desired expression.
 * @return An instance of `KtExpressionElement` specifically implemented as `KtIrInitExpressionElement`,
 *         or `null` if the creation logic does not proceed as expected.
 */
fun irExpression(initializer: (IrModuleFragment, IrPluginContext) -> IrExpression): KtExpressionElement? {
    return KtIrInitExpressionElement(initializer)
}

/**
 * Creates and returns a `KtStatementElement` that encapsulates an IR-backed initializer
 * for integration with a Kotlin statements scope.
 *
 * This method facilitates the creation of a `KtIrInitStatementElement` which serves as
 * an intermediate representation (IR) statement element associated with a given initializer.
 *
 * @param initializer A lambda function that initializes the IR representation of the statement.
 *                    It takes an `IrModuleFragment` and an `IrPluginContext` as parameters
 *                    and returns an `IrStatement` to serve as the representation.
 * @return A `KtStatementElement` initialized with the given IR context and statement initializer,
 *         or `null` if the construction fails or is deemed invalid within the current context.
 */
fun KtStatementsScope.irStatement(initializer: (IrModuleFragment, IrPluginContext) -> IrStatement): KtStatementElement? {
    return KtIrInitStatementElement(initializer, this)
}


