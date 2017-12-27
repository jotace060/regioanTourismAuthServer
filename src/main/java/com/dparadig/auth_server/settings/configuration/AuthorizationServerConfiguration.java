package com.dparadig.auth_server.settings.configuration;

import com.dparadig.auth_server.settings.security.oauth2.CustomUserDetailsService;
import com.dparadig.auth_server.settings.security.jwt.JWTConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${jwt.expiration-seconds}")
    private int expirationSeconds;

    @Value("${jwt.refresh-seconds}")
    private int refreshSeconds;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JWTConfiguration jwtConfiguration;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("dparadig").secret("ddp4r4d1g")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .scopes("read", "write")
                .accessTokenValiditySeconds(expirationSeconds)
                .refreshTokenValiditySeconds(refreshSeconds);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenServices(jwtConfiguration.defaultTokenServices())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }
}
