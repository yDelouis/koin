/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.koin.core.registry

import org.koin.core.error.NoScopeDefinitionFoundException
import org.koin.core.error.ScopeNotAlreadyExistsException
import org.koin.core.error.ScopeNotCreatedException
import org.koin.core.scope.ScopeDefinition
import org.koin.core.scope.ScopeInstance

/**
 * Scope Registry
 * create/find scopes for Koin
 *
 * @author Arnaud Giuliani
 */
class ScopeRegistry {

    private val definitions = hashMapOf<String, ScopeDefinition>()
    private val instances = hashMapOf<String, ScopeInstance>()

    /**
     * Create a scope instance for given scope
     * @param id - scope instance id
     * @param scopeName - scope name
     */
    fun createScopeInstance(id: String, scopeName: String? = null): ScopeInstance {
        val definition: ScopeDefinition? = scopeName?.let {
            definitions[scopeName] ?: throw NoScopeDefinitionFoundException("No scope found for '$scopeName'")
        }
        val instance = ScopeInstance(id, definition)
        registerScopeInstance(instance)
        return instance
    }

    private fun registerScopeInstance(instance: ScopeInstance) {
        if (instances[instance.id] != null) {
            throw ScopeNotAlreadyExistsException("A scope with id '${instance.id}' already exists. Reuse or close it.")
        }
        instances[instance.id] = instance
    }

    fun getScopeInstance(id: String): ScopeInstance {
        return instances[id] ?: throw ScopeNotCreatedException("ScopeInstance with id '$id' not found")
    }

    fun deleteScopeInstance(id: String) {
        instances.remove(id)
    }

    fun close() {
        definitions.clear()
        instances.clear()
    }
}