/*
 * Copyright 2002-2012 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.springframework.integration.dsl.groovy.builder

import groovy.util.AbstractFactory
import groovy.util.FactoryBuilderSupport

import java.util.Map

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author David Turanski
 *
 */
abstract class IntegrationComponentFactory extends AbstractFactory {
	protected Log logger = LogFactory.getLog(this.class)

	protected defaultAttributes(name, value, attributes) {
		assert !(attributes.containsKey('name') && value), "$name cannot accept both a default value and a 'name' attribute"

		attributes = attributes ?: [:]
		attributes.builderName = name

		if (!attributes.containsKey('name') && value){
			attributes.name = value
		}

		attributes
	}

	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
		if (logger.isDebugEnabled()){
			logger.debug("newInstance name: $name value:$value attr:$attributes")
		}

		attributes = defaultAttributes(name, value, attributes)
		def instance = doNewInstance(builder, name, value, attributes)

		def validationContext = instance.validateAttributes(attributes)
		assert !validationContext.hasErrors, validationContext.errorMessage

		instance
	}

	protected abstract doNewInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
}
