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
package org.springframework.integration.dsl.groovy.builder;

import groovy.util.FactoryBuilderSupport;

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.Map;

import org.apache.commons.logging.LogFactory
import org.apache.commons.logging.Log
import org.springframework.integration.dsl.groovy.SimpleEndpoint;
 

/**
 * @author David Turanski
 *
 */

class EndpointFactory extends IntegrationComponentFactory {
 
	private Class requiredType

	EndpointFactory(Class requiredType){
		this.requiredType = requiredType
	}

	/* (non-Javadoc)
	 * @see groovy.util.Factory#newInstance(groovy.util.FactoryBuilderSupport, java.lang.Object, java.lang.Object, java.util.Map)
	 */
	@Override
	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
	throws InstantiationException, IllegalAccessException {
		logger.debug("newInstance name: $name value:$value attr:$attributes")

		attributes = attributes ?: [:]
		
		if (!attributes.containsKey('name') && value){
			attributes.name = value
		}
		if (!attributes.containsKey('inputChannel') && value){
			attributes.inputChannel ="${value}#inputChannel"
		}
		
		if (attributes.evaluate){
			attributes.action = attributes.evaluate
			attributes.remove('evaluate')
		}
		
		logger.debug("newInstance name: $name value:$value attr:$attributes")

		return endpointInstance(attributes);
	}
	
	@Override
	public boolean isLeaf() {
		true
	}

	@Override
	public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
		
		
		if (parent.respondsTo("add")){
			parent.add(child)
			if (logger.isDebugEnabled()){
				logger.debug("setParent parent ${parent} child $child")
			}
		} else {
			logger.warn("attempted to invoke 'add' method on parent")
		}
	}

	@Override
	public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
		logger.debug("setChild parent ${parent} child $child")
	}
	


	protected SimpleEndpoint endpointInstance(Map attributes) {
		SimpleEndpoint endpoint =  requiredType.newInstance(attributes)
		if (logger.isDebugEnabled()){
			logger.debug("created new ${endpoint.class.name} $endpoint")
		}
		endpoint
	}
	
	
}
