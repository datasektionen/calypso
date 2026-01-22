package se.datasektionen.calypso.auth;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OidcDebugBean {

    private final Environment env;

    public OidcDebugBean(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void logOidcProperties() {
        System.out.println("===== OIDC DEBUG START =====");
        System.out.println("Environment variables and resolved properties:");

        String[] keys = new String[] {
            "OIDC_PROVIDER",
            "OIDC_ID",
            "OIDC_SECRET",
            "REDIRECT_URL"
        };

        for (String key : keys) {
            String value = env.getProperty(key);
            System.out.printf("%s = %s%n", key, value);
        }

        System.out.println("===== OIDC DEBUG END =====");
    }
}
