package org.elypia.alexis.persistence;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.deltaspike.jpa.spi.entitymanager.PersistenceConfigurationProvider;
import org.elypia.alexis.configuration.PersistenceConfig;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import java.util.*;

/**
 * Programatically set the url, username, and password configuration
 * for the {@link EntityManagerFactory}.
 *
 * It was already possible to alter the properties with the default implementation
 * provided by DeltaSpike however we wanted to set these through our CDI
 * aware configuration, namely environment variables,
 * using our own custom properties.
 *
 * @author seth@elypia.org (Seth Falco)
 * @since 3.0.0
 */
@Priority(1)
@Alternative
@ApplicationScoped
public class PersistenceConfigProducer implements PersistenceConfigurationProvider {

    private final PersistenceConfig persistenceConfig;

    @Inject
    public PersistenceConfigProducer(PersistenceConfig persistenceConfig) {
        this.persistenceConfig = Objects.requireNonNull(persistenceConfig);
    }

    @Override
    public Properties getEntityManagerFactoryConfiguration(String persistenceUnitName) {
        Properties properties = new Properties(6);

        String dialect = persistenceConfig.dialect();
        String url = persistenceConfig.getUrl();
        String username = persistenceConfig.getUsername();
        String password = persistenceConfig.getPassword();
        String driver = persistenceConfig.driver();

        if (dialect != null)
            properties.put("hibernate.dialect", dialect);

        if (url != null)
            properties.put("hibernate.connection.url", url);

        if (username != null)
            properties.put("hibernate.connection.username", username);

        if (password != null)
            properties.put("hibernate.connection.password", password);

        if (driver != null)
            properties.put("hibernate.connection.driver_class", driver);

        properties.put("javax.persistence.bean.manager", BeanManagerProvider.getInstance().getBeanManager());
        return properties;
    }
}
