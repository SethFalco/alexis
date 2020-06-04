package org.elypia.alexis.services;

import com.github.philippheuer.credentialmanager.*;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.credentialmanager.identityprovider.OAuth2IdentityProvider;
import com.github.twitch4j.*;
import com.github.twitch4j.helix.domain.*;
import org.elypia.alexis.configuration.TwitchConfig;
import org.slf4j.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class TwitchService {

    private static final Logger logger = LoggerFactory.getLogger(TwitchService.class);

    /** The identify provider of the OAuth client. */
    private static final String IDENTIFY_PROVIDER = "twitch";

    /** Client to interact with the Twitch API. */
    private final TwitchClient client;

    /** Manage authentication to the Twitch API. */
    private final CredentialManager credentialManager;

    /** Retain instance of access token for as long as it's valid. */
    private OAuth2Credential appAccessToken;

    @Inject
    public TwitchService(final TwitchConfig config) {
        credentialManager = CredentialManagerBuilder.builder().build();

        this.client = TwitchClientBuilder.builder()
            .withEnableHelix(true)
            .withClientId(config.getClientId())
            .withClientSecret(config.getClientSecret())
            .withCredentialManager(credentialManager)
            .build();

        logger.info("Succesfully authenticated to the Twitch API.");
    }

    public String getAccessToken() {
        if (appAccessToken == null) {
            Optional<OAuth2IdentityProvider> optIdentityProvider = credentialManager.getOAuth2IdentityProviderByName(IDENTIFY_PROVIDER);

            if (optIdentityProvider.isEmpty())
                throw new IllegalStateException("Identity Provider should never return null.");

            OAuth2IdentityProvider identityProvider = optIdentityProvider.get();
            appAccessToken = identityProvider.getAppAccessToken();
        }

        return appAccessToken.getAccessToken();
    }

    /**
     * Query the Twitch API by username.
     *
     * @param username The username of the Twitch user to query.
     * @return An optional which will contain the user if found, else be {@link Optional#empty()}.
     */
    public Optional<User> getUser(String username) {
        UserList userList = client.getHelix().getUsers(getAccessToken(), null, List.of(username)).execute();
        List<User> users = userList.getUsers();
        return (users.isEmpty()) ? Optional.empty() : Optional.of(users.get(0));
    }
}
