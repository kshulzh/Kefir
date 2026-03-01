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

package io.github.kshulzh.kefir.model.utils

import io.github.kshulzh.kefir.model.api.KtPath
import io.github.kshulzh.kefir.model.api.type.KtTypeElement
import io.github.kshulzh.kefir.model.type.KtClassTypeElementImpl

/**
 * Parses a type declaration from a given string and returns the corresponding type element.
 *
 * @param type the string representation of the type to parse
 * @return the parsed type element
 */
fun typeOf(type: String) = typeOf(type, 0).first

/**
 * Parses a type declaration from a given string, starting from a specified position,
 * and returns the corresponding type element along with the new parsing position.
 *
 * @param type the string containing the type declaration to be parsed
 * @param start the starting position in the string from which parsing begins
 * @return a pair consisting of the parsed type element and the position in the string after parsing
 */
private fun typeOf(type: String, start: Int): Pair<KtTypeElement, Int> {
    val packagePath = mutableListOf<String>()
    val classPath = mutableListOf<String>()
    val typeArguments: MutableList<KtTypeElement> = mutableListOf()
    var position = start
    do {
        readName(type, position)?.also {
            packagePath.add(it)
            position += it.length
        }
        readDot(type, position)?.also {
            position++
        } ?: break
    } while (position < type.length)

    if (readSemicolon(type, position) != null) {
        position++
        do {
            readName(type, position)?.also {
                classPath.add(it)
                position += it.length
            }
            readDot(type, position)?.also {
                position++
            } ?: break
        } while (position < type.length)
    } else {
        classPath.add(packagePath.removeLast())
    }

    if (readOpenBracket(type, position) != null) {
        do {
            position++
            val (typeElement, end) = typeOf(type, position)
            typeArguments.add(typeElement)
            position = end
        } while (readComma(type, position) != null)
        readCloseBracket(type, position)?.also {
            position++
        } ?: error("Unclosed type argument list")
    }
    val isNullable = readQuestionMark(type, position)?.also { position++ } != null

    return KtClassTypeElementImpl(KtPath(packagePath), KtPath(classPath), isNullable, typeArguments) to position
}

/**
 * Extracts a substring starting from the specified position in the given string if the
 * characters at and after the position are alphanumeric.
 *
 * @param type the input string to extract the name from
 * @param start the starting index from which to begin extracting the name
 * @return the extracted substring if it consists of alphanumeric characters, or null if no valid
 *         name can be extracted starting from the specified position
 */
private fun readName(type: String, start: Int): String? {
    if (start >= type.length || !type[start].isLetterOrDigit()) return null
    var end = start
    while (end < type.length && type[end].isLetterOrDigit()) {
        end++
    }
    return type.substring(start, end)
}

/**
 * Reads a dot (.) from the specified position in the given string if present.
 *
 * @param type The string to read from.
 * @param start The starting position within the string to check for a dot.
 * @return Returns "." if a dot is present at the specified position; otherwise, null.
 */
private fun readDot(type: String, start: Int): String? {
    if (start >= type.length || type[start] != '.') return null
    return "."
}

/**
 * Reads a semicolon (represented by `:`) from the specified position in the input string.
 *
 * @param type the input string to be read from
 * @param start the position in the string to read from
 * @return the semicolon `:` as a string if present at the specified position, or null if not found
 */
private fun readSemicolon(type: String, start: Int): String? {
    if (start >= type.length || type[start] != ':') return null
    return ":"
}

/**
 * Reads a comma at the specified position in the given string.
 *
 * @param type The string to read from.
 * @param start The position in the string to check for a comma.
 * @return A comma as a string if found at the specified position, otherwise null.
 */
private fun readComma(type: String, start: Int): String? {
    if (start >= type.length || type[start] != ',') return null
    return ","
}

/**
 * Reads an opening angle bracket ('<') from the specified position in the given string.
 *
 * @param type the string to read from
 * @param start the starting position in the string
 * @return the opening bracket ("<") if found at the specified position, or null otherwise
 */
private fun readOpenBracket(type: String, start: Int): String? {
    if (start >= type.length || type[start] != '<') return null
    return "<"
}

/**
 * Reads the closing bracket character '>' from the specified position in the given string.
 *
 * @param type the string to read the character from
 * @param start the position in the string to check for the closing bracket
 * @return the closing bracket '>' if present at the specified position, or null otherwise
 */
private fun readCloseBracket(type: String, start: Int): String? {
    if (start >= type.length || type[start] != '>') return null
    return ">"
}

/**
 * Reads a question mark character from the given string starting at the specified position.
 *
 * @param type the string to read from
 * @param start the starting position in the string
 * @return a string containing the question mark if found; otherwise, null
 */
private fun readQuestionMark(type: String, start: Int): String? {
    if (start >= type.length || type[start] != '?') return null
    return "?"
}