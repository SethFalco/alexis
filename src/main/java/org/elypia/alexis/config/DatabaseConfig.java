/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.config;

import org.apache.deltaspike.core.api.config.*;

import javax.inject.Singleton;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
@Configuration(prefix = "alexis.database.")
public interface DatabaseConfig {

    /** Should database functionaly be enabled. This must be specified. */
    @ConfigProperty(name = "enabled")
    boolean isEnabled();

    /** The host server this database server is running on. */
    @ConfigProperty(name = "host")
    String getHost();

    /** You must connect to the database with SSL, specify the certificate URL. */
    @ConfigProperty(name = "trust-certificate-key-store-url")
    String getTrustCertificateKeyStoreUrl();

    /** The password to the certificate for {@link #getTrustCertificateKeyStoreUrl}. */
    @ConfigProperty(name = "trust-certificate-key-store-password")
    String getTrustCertificateKeyStorePassword();

    /** You must connect to the database with SSL, specify the client certificate URL. */
    @ConfigProperty(name = "client-certificate-key-store-url")
    String getClientCertificateKeyStoreUrl();

    /** The password to the certificate for {@link #getClientCertificateKeyStoreUrl}. */
    @ConfigProperty(name = "client-certificate-key-store-password")
    String getClientCertificateKeyStorePassword();

    /** The username for this database user. */
    @ConfigProperty(name = "username")
    String getUsername();

    /** Password to the Alexis user on the database. */
    @ConfigProperty(name = "password")
    String getPassword();
}
