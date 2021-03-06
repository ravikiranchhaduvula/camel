/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.servicenow;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ComponentVerifier;
import org.apache.camel.Endpoint;
import org.apache.camel.VerifiableComponent;
import org.apache.camel.impl.UriEndpointComponent;
import org.apache.camel.spi.Metadata;
import org.apache.camel.util.EndpointHelper;
import org.apache.camel.util.IntrospectionSupport;

/**
 * Represents the component that manages {@link ServiceNowEndpoint}.
 */
@Metadata(label = "verifiers", enums = "parameters,connectivity")
public class ServiceNowComponent extends UriEndpointComponent implements VerifiableComponent {

    @Metadata(label = "advanced")
    private ServiceNowConfiguration configuration;

    public ServiceNowComponent() {
        super(ServiceNowEndpoint.class);

        this.configuration = new ServiceNowConfiguration();
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        final CamelContext context = getCamelContext();
        final ServiceNowConfiguration configuration = this.configuration.copy();

        Map<String, Object> models = IntrospectionSupport.extractProperties(parameters, "model.");
        for (Map.Entry<String, Object> entry : models.entrySet()) {
            configuration.addModel(
                entry.getKey(),
                EndpointHelper.resolveParameter(context, (String)entry.getValue(), Class.class));
        }

        Map<String, Object> requestModels = IntrospectionSupport.extractProperties(parameters, "requestModel.");
        for (Map.Entry<String, Object> entry : requestModels.entrySet()) {
            configuration.addRequestModel(
                entry.getKey(),
                EndpointHelper.resolveParameter(context, (String)entry.getValue(), Class.class));
        }

        Map<String, Object> responseModels = IntrospectionSupport.extractProperties(parameters, "responseModel.");
        for (Map.Entry<String, Object> entry : responseModels.entrySet()) {
            configuration.addResponseModel(
                entry.getKey(),
                EndpointHelper.resolveParameter(context, (String)entry.getValue(), Class.class));
        }

        setProperties(configuration, parameters);

        String instanceName = getCamelContext().resolvePropertyPlaceholders(remaining);
        if (!configuration.hasApiUrl()) {
            configuration.setApiUrl(String.format("https://%s.service-now.com/api", instanceName));
        }
        if (!configuration.hasOauthTokenUrl()) {
            configuration.setOauthTokenUrl(String.format("https://%s.service-now.com/oauth_token.do", instanceName));
        }

        return new ServiceNowEndpoint(uri, this, configuration, instanceName);
    }

    public ServiceNowConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * The ServiceNow default configuration
     */
    public void setConfiguration(ServiceNowConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getApiUrl() {
        return configuration.getApiUrl();
    }

    /**
     * The ServiceNow REST API url
     */
    public void setApiUrl(String apiUrl) {
        configuration.setApiUrl(apiUrl);
    }

    public String getUserName() {
        return configuration.getUserName();
    }

    /**
     * ServiceNow user account name
     */
    @Metadata(label = "security", secret = true)
    public void setUserName(String userName) {
        configuration.setUserName(userName);
    }

    public String getPassword() {
        return configuration.getPassword();
    }

    /**
     * ServiceNow account password
     */
    @Metadata(label = "security", secret = true)
    public void setPassword(String password) {
        configuration.setPassword(password);
    }

    public String getOauthClientId() {
        return configuration.getOauthClientId();
    }

    /**
     * OAuth2 ClientID
     */
    @Metadata(label = "security", secret = true)
    public void setOauthClientId(String oauthClientId) {
        configuration.setOauthClientId(oauthClientId);
    }

    public String getOauthClientSecret() {
        return configuration.getOauthClientSecret();
    }

    /**
     * OAuth2 ClientSecret
     */
    @Metadata(label = "security", secret = true)
    public void setOauthClientSecret(String oauthClientSecret) {
        configuration.setOauthClientSecret(oauthClientSecret);
    }

    public String getOauthTokenUrl() {
        return configuration.getOauthTokenUrl();
    }

    /**
     * OAuth token Url
     */
    @Metadata(label = "security", secret = true)
    public void setOauthTokenUrl(String oauthTokenUrl) {
        configuration.setOauthTokenUrl(oauthTokenUrl);
    }

    /**
     * TODO: document
     */
    public ComponentVerifier getVerifier() {
        return new ServiceNowComponentVerifier(this);
    }
}
