/*
 * Alexis - A general purpose chatbot for Discord.
 * Copyright (C) 2019-2019  Elypia CIC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.elypia.alexis.config;

import org.elypia.commandler.config.ConfigService;

import javax.inject.*;
import java.util.Objects;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@Singleton
public class DatabaseConfig {

    /** Should database functionaly be enabled. This must be specified. */
    private final boolean enabled;

    /** The host server this database server is running on. */
    private String host;

    /** You must connect to the database with SSL, specify the certificate URL. */
    private String trustCertificateKeyStoreUrl;

    /** The password to the certificate for {@link #trustCertificateKeyStoreUrl}. */
    private String trustCertificateKeyStorePassword;

    /** You must connect to the database with SSL, specify the client certificate URL. */
    private String clientCertificateKeyStoreUrl;

    /** The password to the certificate for {@link #clientCertificateKeyStoreUrl}. */
    private String clientCertificateKeyStorePassword;

    /** The username for this database user. */
    private String username;

    /** Password to the Alexis user on the database. */
    private String password;

    @Inject
    public DatabaseConfig(final ConfigService configService) {
        this.enabled = configService.getBoolean("database.enabled");

        if (!enabled)
            return;

        this.host = Objects.requireNonNull(configService.getString("database.host"));
        this.trustCertificateKeyStoreUrl = Objects.requireNonNull(configService.getString("database.trust-certificate-key-store-url"));
        this.trustCertificateKeyStorePassword = Objects.requireNonNull(configService.getString("database.trust-certificate-key-store-password"));
        this.clientCertificateKeyStoreUrl = Objects.requireNonNull(configService.getString("database.client-certificate-key-store-url"));
        this.clientCertificateKeyStorePassword = Objects.requireNonNull(configService.getString("database.client-certificate-key-store-password"));
        this.username = Objects.requireNonNull(configService.getString("database.username"));
        this.password = Objects.requireNonNull(configService.getString("database.password"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getHost() {
        return host;
    }

    public String getTrustCertificateKeyStoreUrl() {
        return trustCertificateKeyStoreUrl;
    }

    public String getTrustCertificateKeyStorePassword() {
        return trustCertificateKeyStorePassword;
    }

    public String getClientCertificateKeyStoreUrl() {
        return clientCertificateKeyStoreUrl;
    }

    public String getClientCertificateKeyStorePassword() {
        return clientCertificateKeyStorePassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
