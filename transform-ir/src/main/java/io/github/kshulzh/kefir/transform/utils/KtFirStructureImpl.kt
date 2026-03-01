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

package io.github.kshulzh.kefir.transform.utils

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.backend.Fir2IrComponentsStorage
import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.packageFqName
import org.jetbrains.kotlin.fir.resolve.providers.*
import org.jetbrains.kotlin.fir.scopes.impl.*
import org.jetbrains.kotlin.fir.scopes.kotlinScopeProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

/**
 * Implementation of the `KtFirStructure` interface that provides various operations for managing
 * FIR (Frontend Intermediate Representation) entities such as files, classes, and callables within
 * a given compiler `FirSession`.
 *
 * @constructor Creates an instance of `KtFirStructureImpl`.
 * @property files A list of FIR files managed by this structure.
 * @property nestedClasses A map associating FIR classes with their corresponding nested classifier scopes.
 * @property classes A map associating class IDs with their respective FIR classes.
 * @property callables A map associating callable IDs with a list of FIR callable declarations.
 * @property firCompositeCachedSymbolNamesProvider A symbol provider for managing cached symbol names.
 * @property firClassDeclaredMemberScopes A map associating FIR classes with their declared member scopes.
 * @property fir2IrComponents A storage for FIR-to-IR conversion components.
 * @property symbolNameProvider A provider for managing and retrieving symbol names.
 */
class KtFirStructureImpl(
    val files: ArrayList<FirFile>,
    session: FirSession,
    val nestedClasses: MutableMap<FirClass, Any?>,
    val classes: MutableMap<ClassId, FirClass> = mutableMapOf(),
    val callables: MutableMap<CallableId, MutableList<FirCallableDeclaration>> = mutableMapOf(),
    val firCompositeCachedSymbolNamesProvider: FirCompositeCachedSymbolNamesProvider,
    val firClassDeclaredMemberScopes: MutableMap<FirClass, FirClassDeclaredMemberScopeImpl>,
    val fir2IrComponents: Fir2IrComponentsStorage,
    val symbolNameProvider: LocalFirSymbolNamesProvider = LocalFirSymbolNamesProvider(
        hasSpecificClassifierPackageNamesComputation = false,
        hasSpecificCallablePackageNamesComputation = false
    )
) : FirSymbolProvider(session), KtFirStructure {
    /**
     * Adds a given [FirFile] to the internal collection of files, ensuring the files
     * remain ordered by their package fully qualified name in ascending order.
     *
     * @param firFile The FIR (Front-End Intermediate Representation) file to be added.
     */
    override fun addFile(firFile: FirFile) {
        for ((index, file) in files.withIndex()) {
            if (file.packageFqName.asString() >= firFile.packageFqName.asString()) {
                files.add(index, firFile)
                return
            }
        }
        files.add(firFile)
    }

    /**
     * Adds a given FIR (Frontend Intermediate Representation) class to the current representation structure.
     * Handles registration of top-level classes and nested classes.
     *
     * @param firClass The FIR class to add. It contains symbol and class details used to update the structure.
     */
    @OptIn(SymbolInternals::class)
    override fun addClass(firClass: FirClass) {

        val classId = firClass.symbol.classId
        val packageId = classId.packageFqName
        classes[classId] = firClass
        if (classId.isNestedClass) {
            val parent = session.symbolProvider.getClassLikeSymbolByClassId(classId.parentClassId!!)?.fir
            parent?.let { fir ->
                nestedClasses[fir] as? FirNestedClassifierScope
            }

            parent?.also { fir ->
                nestedClasses[fir as FirClass] = FirNestedClassifierScopeImpl(
                    fir,
                    session
                )
            }

        } else {
            symbolNameProvider.classes.getOrPut(packageId) { mutableSetOf() }.add(classId.shortClassName)
            (firCompositeCachedSymbolNamesProvider.getTopLevelClassifierNamesInPackage(packageId) as? LinkedHashSet<Name>)
                ?.addAll(firCompositeCachedSymbolNamesProvider.computeTopLevelClassifierNames(packageId) as LinkedHashSet<Name>)
        }

    }

    /**
     * Adds a callable declaration to the FIR (Frontend Intermediate Representation).
     * Depending on the context of the callable declaration, it updates various scopes
     * and caches to ensure the callable is properly integrated.
     *
     * @param firCallable The FIR callable declaration to be added. This could represent
     *                     a property or a function.
     * @param isProperty   Indicates whether the callable is a property (true) or a function (false).
     */
    @OptIn(SymbolInternals::class)
    override fun addCallable(firCallable: FirCallableDeclaration, isProperty: Boolean) {
        val callableId = firCallable.symbol.callableId!!
        val classId = callableId.classId
        val packageId = callableId.packageName
        callables.getOrPut(callableId) { mutableListOf() }.add(firCallable)
        if (classId != null) {
            //refresh
            val firClass = session.symbolProvider.getClassLikeSymbolByClassId(classId)!!.fir as FirClass
            firClassDeclaredMemberScopes[firClass] = FirClassDeclaredMemberScopeImpl(session, firClass, null)

            val declarations = session.kotlinScopeProvider.getUseSiteMemberScope(
                firClass,
                session,
                fir2IrComponents.scopeSession,
                null
            ) as FirClassUseSiteMemberScope
            addFunctionName(declarations, callableId.callableName)
            if (isProperty) {
                addPropertyDeclaration(declarations, firCallable)
            } else {
                addFunctionDeclaration(declarations, firCallable)
            }
        } else {
            symbolNameProvider.callables.getOrPut(packageId) { mutableSetOf() }.add(callableId.callableName)
        }
    }

    /**
     * Retrieves a class-like symbol corresponding to the provided class ID.
     *
     * @param classId The unique identifier representing the class whose symbol is to be fetched.
     * @return The class-like symbol associated with the given class ID, or null if no such symbol exists.
     */
    override fun getClassLikeSymbolByClassId(classId: ClassId): FirClassLikeSymbol<*>? {
        return classes[classId]?.symbol
    }

    /**
     * Populates the provided destination list with top-level callable symbols that match the specified package and name.
     *
     * @param destination A mutable list where the matching callable symbols will be added.
     * @param packageFqName The fully qualified name of the package containing the callable symbols.
     * @param name The name of the callable symbols to retrieve.
     */
    @FirSymbolProviderInternals
    override fun getTopLevelCallableSymbolsTo(
        destination: MutableList<FirCallableSymbol<*>>,
        packageFqName: FqName,
        name: Name
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Collects top-level function symbols matching the given package and name into the provided destination list.
     *
     * @param destination The list to which matching top-level function symbols will be added.
     * @param packageFqName The fully qualified name of the package containing the functions.
     * @param name The name of the functions to retrieve.
     */
    @FirSymbolProviderInternals
    override fun getTopLevelFunctionSymbolsTo(
        destination: MutableList<FirNamedFunctionSymbol>,
        packageFqName: FqName,
        name: Name
    ) {
        callables[CallableId(packageFqName, name)]?.forEach {
            if (it.symbol is FirNamedFunctionSymbol) {
                destination.add(it.symbol as FirNamedFunctionSymbol)
            }
        }
    }

    /**
     * Populates the given list with top-level property symbols matching the specified package and name.
     *
     * @param destination Mutable list where the matching `FirPropertySymbol` instances will be added.
     * @param packageFqName Fully qualified name of the package to search within.
     * @param name Name of the property to look for.
     */
    @FirSymbolProviderInternals
    override fun getTopLevelPropertySymbolsTo(
        destination: MutableList<FirPropertySymbol>,
        packageFqName: FqName,
        name: Name
    ) {
        callables[CallableId(packageFqName, name)]?.forEach {
            if (it.symbol is FirPropertySymbol) {
                destination.add(it.symbol as FirPropertySymbol)
            }
        }
    }

    /**
     * Checks if there is a package defined with the specified fully qualified name.
     *
     * @param fqName The fully qualified name of the package to check.
     * @return `true` if the package exists, `false` otherwise.
     */
    override fun hasPackage(fqName: FqName): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Provides access to the names of FIR symbols managed within the implementation of the FIR structure.
     *
     * This property retrieves the `FirSymbolNamesProvider` associated with `KtFirStructureImpl`, allowing
     * access to symbol names and facilitating operations related to FIR symbol management.
     */
    override val symbolNamesProvider: FirSymbolNamesProvider get() = symbolNameProvider

    /**
     * Adds a function name to the internal cache of callable names within the specified declarations.
     *
     * @param declarations The FirClassUseSiteMemberScope that holds the declarations in which the name should be added.
     * @param name The name of the function to be added to the cache.
     */
    private fun addFunctionName(declarations: FirClassUseSiteMemberScope, name: Name) {
        //reflection is bad, but why not?
        AbstractFirUseSiteMemberScope::class.java.getDeclaredField($$"callableNamesCached$delegate").also { field ->
            field.isAccessible = true
            val f = field.get(declarations)
            val nf = f::class.java.getDeclaredField("_value")
            nf.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val current = nf.get(f) as? Set<Name>
            if (current != null) {
                val res = mutableSetOf(name)
                res.addAll(current)
                nf.set(f, res)
            }
        }
    }

    /**
     * Adds a property declaration to the given FirClassUseSiteMemberScope.
     * This method updates the internal map of properties by adding the specified
     * FIR (Front-End Intermediate Representation) property.
     *
     * @param declarations The FirClassUseSiteMemberScope that contains the member declarations.
     * @param firProperty The FIR callable declaration representing the property to be added.
     */
    private fun addPropertyDeclaration(declarations: FirClassUseSiteMemberScope, firProperty: FirCallableDeclaration) {
        AbstractFirUseSiteMemberScope::class.java.getDeclaredField("properties").also { field ->
            field.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val f = field.get(declarations) as MutableMap<Name, Collection<FirVariableSymbol<*>>>
            val callableId = firProperty.symbol.callableId!!
            val names = f.getOrDefault(callableId.callableName, listOf())
            val newFunctions = names.toMutableList()
            newFunctions.add(firProperty.symbol as FirVariableSymbol<*>)
            f.put(callableId.callableName, newFunctions)
        }
    }

    /**
     * Adds a function declaration to the given member scope by updating its internal function collection.
     *
     * @param declarations the `FirClassUseSiteMemberScope` where the function declaration should be added
     * @param firFunction the `FirCallableDeclaration` representing the function to be added
     */
    private fun addFunctionDeclaration(declarations: FirClassUseSiteMemberScope, firFunction: FirCallableDeclaration) {
        AbstractFirUseSiteMemberScope::class.java.getDeclaredField("functions").also { field ->
            field.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val f = field.get(declarations) as MutableMap<Name, Collection<FirNamedFunctionSymbol>>
            val callableId = firFunction.symbol.callableId!!
            val names = f.getOrDefault(callableId.callableName, listOf())
            val newFunctions = names.toMutableList()
            newFunctions.add(firFunction.symbol as FirNamedFunctionSymbol)
            f.put(callableId.callableName, newFunctions)
        }
    }

    /**
     * A symbol names provider that maintains mappings of top-level class and callable names
     * grouped by their fully qualified package names. This implementation utilizes in-memory
     * data structures to store and retrieve symbol names efficiently.
     *
     * @property hasSpecificClassifierPackageNamesComputation Indicates whether computation for classifier package names is specific.
     * @property hasSpecificCallablePackageNamesComputation Indicates whether computation for callable package names is specific.
     * @property classes A mutable map that stores sets of class names grouped by their fully qualified package names.
     * @property callables A mutable map that stores sets of callable names grouped by their fully qualified package names.
     */
    class LocalFirSymbolNamesProvider(
        override val hasSpecificClassifierPackageNamesComputation: Boolean,
        override val hasSpecificCallablePackageNamesComputation: Boolean,
        val classes: MutableMap<FqName, MutableSet<Name>> = mutableMapOf(),
        val callables: MutableMap<FqName, MutableSet<Name>> = mutableMapOf()
    ) : FirSymbolNamesProvider() {
        /**
         * Retrieves the set of classifier names available at the top level within the specified package.
         *
         * @param packageFqName the fully qualified name of the package for which top-level classifier names are to be retrieved
         * @return a set of top-level classifier names in the given package, or an empty set if no classifiers are found
         */
        override fun getTopLevelClassifierNamesInPackage(packageFqName: FqName): Set<Name>? {
            return classes[packageFqName] ?: setOf()
        }

        /**
         * Retrieves the set of top-level callable names available in the given package.
         *
         * @param packageFqName The fully qualified name of the package to query for top-level callables.
         * @return A set of callable names in the specified package, or an empty set if no callables are found.
         */
        override fun getTopLevelCallableNamesInPackage(packageFqName: FqName): Set<Name>? {
            return callables[packageFqName] ?: setOf()
        }

        /**
         * Retrieves a set of package names derived from the keys of `classes` and `callables`.
         *
         * @return A set of package names as strings, or null if no package names are present.
         */
        override fun getPackageNames(): Set<String>? {
            return classes.keys.map { it.asString() }.toSet() + callables.keys.map { it.asString() }.toSet()
        }

        /**
         * Retrieves the set of package names that contain top-level callable entities.
         *
         * @return a set of package names as strings, or null if no top-level callables are available.
         */
        override fun getPackageNamesWithTopLevelCallables(): Set<String>? {
            return callables.keys.map { it.asString() }.toSet()
        }

        /**
         * Retrieves the package names containing top-level classifiers.
         *
         * @return a set of package names as strings where top-level classifiers are present,
         *         or null if no such packages exist.
         */
        override fun getPackageNamesWithTopLevelClassifiers(): Set<String>? {
            return classes.keys.map { it.asString() }.toSet()
        }
    }
}