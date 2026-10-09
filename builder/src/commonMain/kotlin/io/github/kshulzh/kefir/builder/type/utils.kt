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

package io.github.kshulzh.kefir.builder.type

import io.github.kshulzh.kefir.model.api.arg.KtParametersScope
import io.github.kshulzh.kefir.model.api.expression.KtExpressionElement
import io.github.kshulzh.kefir.model.api.type.KtClassTypeElement
import io.github.kshulzh.kefir.model.api.type.KtParameterTypeElement
import io.github.kshulzh.kefir.model.api.type.KtTypeElement

fun <T : KtParametersScope> List<T>.match(args: List<Any?>): T? {
    val acceptable = this.filter { scope ->
        scope.matchParams(args)
    }
    return acceptable.getOrNull(0)
}

@Suppress("UNCHECKED_CAST")
fun <T : KtParametersScope> T.matchArgs(args: List<Any?>, acceptNullArg: Boolean = true, acceptNullParam: Boolean = true): List<KtExpressionElement?>? {
    val args1 = ArrayList<KtExpressionElement?>(parameters.size)
    var i = 0;
    for (arg in args) {
        if (arg is KtExpressionElement) {
            val type = arg.type
            val expected = parameters[i].type
            if (type != null && expected != null) {
                if (expected.isAssignableFrom(type)) {
                    args1[i] = arg
                } else {
                    return null
                }
            } else {
                if (acceptNullArg && expected == null) {
                    args1[i] = arg
                } else if (acceptNullParam && type == null) {
                    args1[i] = arg
                } else {
                    return null
                }

            }
            i++
        } else if (arg is Pair<*, *>) {
            val a = arg as Pair<String, KtExpressionElement>
            val type = arg.second as? KtTypeElement
            val param = parameters.firstOrNull() { it.name == a.first } ?: return null
            val expected = param.type
            val indexOf = parameters.indexOf(param)
            if (type != null && expected != null) {
                if (expected.isAssignableFrom(type)) {
                    args1[indexOf] = arg.second
                } else {
                    return null
                }
            }else {
                if (acceptNullArg && expected == null) {
                    args1[indexOf] = arg.second
                } else if (acceptNullParam && type == null) {
                    args1[indexOf] = arg.second
                } else {
                    return null
                }

            }

        } else {
            return null
        }

    }

    return args1
}

@Suppress("UNCHECKED_CAST")
fun <T : KtParametersScope> T.matchParams(params: List<Any?>, acceptNullArg: Boolean = true, acceptNullParam: Boolean = true): Boolean {
    if (params.size > parameters.size) return false
    val args1 = Array(parameters.size) { false }
    var i = 0;
    for (arg in params) {
        if (arg is KtTypeElement) {
            val type = arg
            val expected = parameters[i].type
            if (expected != null) {
                if (expected.isAssignableFrom(type)) {
                    args1[i] = true
                } else {
                    return false
                }
            } else {
                if (acceptNullArg) {
                    args1[i] = true
                } else {
                    return false
                }

            }
            i++
        } else if (arg is Pair<*, *>) {
            val a = arg as Pair<String, KtExpressionElement>
            val type = arg.second as? KtTypeElement
            val param = parameters.firstOrNull() { it.name == a.first } ?: return false
            val expected = param.type
            val indexOf = parameters.indexOf(param)
            if (type != null && expected != null) {
                if (expected.isAssignableFrom(type)) {
                    args1[indexOf] = true
                } else {
                    return false
                }
            }else {
                if (acceptNullArg && expected == null) {
                    args1[indexOf] = true
                } else if (acceptNullParam && type == null) {
                    args1[indexOf] = true
                } else {
                    return false
                }

            }

        } else {
            args1[i] = acceptNullArg
            i++
        }

    }
    parameters.forEachIndexed { i, e ->
        if (!args1[i] && e.value == null) return false
    }

    return true
}



fun KtTypeElement.isAssignableFrom(arg: KtTypeElement) : Boolean {
    if (this is KtParameterTypeElement) {
        return this.parameterType?.supperTypes?.any { it.isAssignableFrom(arg) } ?: false
    }
    if (arg is KtClassTypeElement) {
        if (this is KtClassTypeElement) {
            if (this.ktPackage == arg.ktPackage && this.ktClass == arg.ktClass) {
                return this.typeArguments.zip(arg.typeArguments).all { it.first.isAssignableFrom(it.second) }
            }
        }
        val klass = arg.klass ?: return false

        return klass.supertypes.map {
            if(it is KtClassTypeElement) {
               val tklass = it.klass?: return@map it
               return@map tklass.Type(it.isNullable, it.typeArguments.map { a ->
                    if (a is KtParameterTypeElement) {
                        val index = a.parameterType?.let { pt->klass.typeParameters.indexOf(pt) } ?: -1
                        if (index == -1) {
                            a
                        } else {
                            arg.typeArguments[index]
                        }
                    } else {
                        a
                    }
                }.toMutableList())
            }
            it
        }.any {
            this.isAssignableFrom(it)
        }
    } else if (arg is KtParameterTypeElement) {
        return arg.parameterType?.supperTypes?.any { this.isAssignableFrom(it) } ?: false
    } else {
        return false
    }
}