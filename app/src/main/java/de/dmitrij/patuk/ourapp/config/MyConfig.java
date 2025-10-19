package de.dmitrij.patuk.ourapp.config;

public class MyConfig {
    public static final String SSO_AUTH = "https://10.0.2.2:8443/realms/my-enterprise-realm/protocol/openid-connect/auth";
    public static final String SSO_LOGOUT = "https://10.0.2.2:8443/realms/my-enterprise-realm/protocol/openid-connect/logout";
    public static final String SSO_TOKEN = "https://10.0.2.2:8443/realms/my-enterprise-realm/protocol/openid-connect/token";
    public static final String CLIENT_ID = "my-client";
    public static final String SSO_SCOPE_OPENID = "openid";
    public static final String OAUTH_CALLBACK = "de.dmitrij.patuk.myapp:/oauth2callback";
}
