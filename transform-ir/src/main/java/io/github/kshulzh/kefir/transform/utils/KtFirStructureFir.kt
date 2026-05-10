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
import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.FirFile
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolNamesProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProviderInternals
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.scopes.impl.AbstractFirUseSiteMemberScope
import org.jetbrains.kotlin.fir.scopes.impl.FirClassUseSiteMemberScope
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

/**
 * Represents a custom implementation of the [FirSymbolProvider] and [KtFirStructure] interfaces.
 * This class is responsible for managing FIR (Frontend Intermediate Representation) entities,
 * including files, classes, and callables, within a specific session.
 *
 * @property files A list of FIR files.
 * @property classes A mutable map that caches classes by their [ClassId].
 * @property callables A mutable map that caches callable declarations by their [CallableId].
 * @property symbolNameProvider The provider for managing symbol names.
 */
class KtFirStructureFir(
    val files: ArrayList<FirFile>,
    session: FirSession,
    val classes: MutableMap<ClassId, FirClass> = mutableMapOf(),
    val callables: MutableMap<CallableId, FirCallableDeclaration> = mutableMapOf(),
    val symbolNameProvider: LocalFirSymbolNamesProvider = LocalFirSymbolNamesProvider(
        hasSpecificClassifierPackageNamesComputation = false,
        hasSpecificCallablePackageNamesComputation = false
    )
) : FirSymbolProvider(session), KtFirStructure {
    /**
     * Adds a FIR (Frontend Intermediate Representation) file to the collection of files.
     *
     * @param firFile the FIR file to be added
     */
    override fun addFile(firFile: FirFile) {
        files.add(firFile)
    }

    /**
     * Adds a class to the internal structure for managing FIR (Frontend Intermediate Representation) classes.
     *
     * @param firClass the class to be added, represented by its FIR structure.
     */
    @OptIn(SymbolInternals::class)
    override fun addClass(firClass: FirClass) {

        val classId = firClass.symbol.classId
        val packageId = classId.packageFqName
        classes[classId] = firClass
        if (classId.isNestedClass) {
            session.symbolProvider.getClassLikeSymbolByClassId(classId.parentClassId!!)?.fir
        } else {
            symbolNameProvider.classes.getOrPut(packageId) { mutableSetOf() }.add(classId.shortClassName)
        }

    }

    /**
     * Adds a callable declaration to the internal structure.
     *
     * @param firCallable The callable declaration to be added.
     * @param isProperty Indicates whether the callable is a property.
     */
    @OptIn(SymbolInternals::class)
    override fun addCallable(firCallable: FirCallableDeclaration, isProperty: Boolean) {
        val callableId = firCallable.symbol.callableId!!
        val classId = callableId.classId
        val packageId = callableId.packageName
        callables[callableId] = firCallable
        if (classId != null) {
            //refresh
            session.symbolProvider.getClassLikeSymbolByClassId(classId)!!.fir as FirClass

        } else {
            symbolNameProvider.callables.getOrPut(packageId) { mutableSetOf() }.add(callableId.callableName)
        }
    }

    override val symbolNamesProvider: FirSymbolNamesProvider get() = symbolNameProvider

    /**
     * Retrieves a class-like symbol associated with the given class identifier.
     *
     * @param classId the identifier of the class whose symbol is to be retrieved.
     * @return the corresponding [FirClassLikeSymbol] for the class identifier if it exists, or null if no such symbol is found.
     */
    override fun getClassLikeSymbolByClassId(classId: ClassId): FirClassLikeSymbol<*>? {
        return classes[classId]?.symbol
    }

    /**
     * Retrieves top-level callable symbols (functions or properties) from a specified package with a given name
     * and adds them to the provided destination list.
     *
     * @param destination the list where the callable symbols will be added
     * @param packageFqName the fully qualified name of the package containing the top-level callable symbols
     * @param name the name of the callable symbols to look for
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
     * Populates the given destination list with top-level function symbols that match the given package and name.
     *
     * @param destination The list to which the matching top-level function symbols will be added.
     * @param packageFqName The fully qualified name of the package to search in.
     * @param name The name of the top-level functions to retrieve.
     */
    @FirSymbolProviderInternals
    override fun getTopLevelFunctionSymbolsTo(
        destination: MutableList<FirNamedFunctionSymbol>,
        packageFqName: FqName,
        name: Name
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Retrieves the top-level property symbols from the specified package and name, and adds them to the given destination list.
     *
     * @param destination The mutable list where the retrieved `FirPropertySymbol` instances are added.
     * @param packageFqName The fully qualified name of the package to search for the top-level property symbols.
     * @param name The name of the properties to retrieve.
     */
    @FirSymbolProviderInternals
    override fun getTopLevelPropertySymbolsTo(
        destination: MutableList<FirPropertySymbol>,
        packageFqName: FqName,
        name: Name
    ) {
        TODO("Not yet implemented")
    }

    /**
     * Checks whether the specified fully qualified package name exists.
     *
     * @param fqName The fully qualified name of the package to be checked.
     * @return True if the package exists, false otherwise.
     */
    override fun hasPackage(fqName: FqName): Boolean {
        TODO("Not yet implemented")
    }


    /**
     * Adds a property declaration to the given class use-site member scope.
     *
     * @param declarations The `FirClassUseSiteMemberScope` where the property declaration should be added.
     * @param firProperty The `FirCallableDeclaration` representing the property to be added.
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
     * Adds a given function declaration to the specified use site member scope.
     *
     * @param declarations The scope to which the function declaration should be added. This is an instance of
     *                     FirClassUseSiteMemberScope that holds the functions and declarations within the scope.
     * @param firFunction  The callable function declaration to be added to the scope. This object must be a
     *                     descendant of FirCallableDeclaration and represents the new function to register.
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
     * Provides a local implementation of [FirSymbolNamesProvider] that maintains mappings of classifier and callable
     * names to their respective package FQNs in a mutable structure. This implementation allows for querying top-level
     * classes and callables and their associated package names.
     *
     * @property hasSpecificClassifierPackageNamesComputation A flag indicating if the provider has computation for
     * specific package names containing top-level classifiers.
     * @property hasSpecificCallablePackageNamesComputation A flag indicating if the provider has computation for
     * specific package names containing top-level callables.
     * @property classes A mutable map storing mappings of package FQNs to sets of classifier names.
     * @property callables A mutable map storing mappings of package FQNs to sets of callable names.
     */
    class LocalFirSymbolNamesProvider(
        override val hasSpecificClassifierPackageNamesComputation: Boolean,
        override val hasSpecificCallablePackageNamesComputation: Boolean,
        val classes: MutableMap<FqName, MutableSet<Name>> = mutableMapOf(),
        val callables: MutableMap<FqName, MutableSet<Name>> = mutableMapOf()
    ) : FirSymbolNamesProvider() {
        /**
         * Retrieves the set of top-level classifier names in the given package.
         *
         * @param packageFqName the fully qualified name of the package for which top-level classifier names are requested.
         * @return a set of top-level classifier names within the specified package, or an empty set if none are found.
         */
        override fun getTopLevelClassifierNamesInPackage(packageFqName: FqName): Set<Name>? {
            return classes[packageFqName] ?: setOf()
        }

        /**
         * Retrieves the names of top-level callable symbols (such as functions or properties) declared in a specified package.
         *
         * @param packageFqName the fully qualified name of the package for which the top-level callable names are to be fetched.
         * @return a set of names representing the top-level callables within the package, or an empty set if no callables are present in the package.
         */
        override fun getTopLevelCallableNamesInPackage(packageFqName: FqName): Set<Name>? {
            return callables[packageFqName] ?: setOf()
        }

        /**
         * Retrieves a set of fully qualified package names derived from the class and callable symbol tables.
         *
         * @return a set of strings representing the package names, or null if no packages are available.
         */
        override fun getPackageNames(): Set<String>? {
            return classes.keys.map { it.asString() }.toSet() + callables.keys.map { it.asString() }.toSet()
        }

        /**
         * Retrieves a set of package names that contain top-level callable symbols.
         *
         * The method processes the keys from the `callables` map, converts each key to
         * its string representation, and collects them into a set. The resulting set
         * contains the package names where top-level callable symbols are present.
         *
         * @return A set of strings representing package names with top-level callables,
         *         or null if none exist.
         */
        override fun getPackageNamesWithTopLevelCallables(): Set<String>? {
            return callables.keys.map { it.asString() }.toSet()
        }

        /**
         * Retrieves a set of package names that contain top-level classifiers.
         *
         * @return A set of package names represented as strings, or null if no classifiers are available.
         */
        override fun getPackageNamesWithTopLevelClassifiers(): Set<String>? {
            return classes.keys.map { it.asString() }.toSet()
        }
    }
}