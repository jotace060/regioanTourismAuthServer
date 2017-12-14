package com.dparadig.auth_server.settings.security.jwt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dparadig.auth_server.settings.security.oauth2.CustomUser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

@CommonsLog
@Configuration
public class JWTTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        CustomUser customUser = (CustomUser) authentication.getPrincipal();
        Map<String, Object> additionalInfo = new HashMap<>();
        String auths = "";
        Set<GrantedAuthority> grantedAuthorities =
                (Set<GrantedAuthority>) customUser.getAuthorities();
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            auths = auths.concat(" " + grantedAuthority.getAuthority());
        }

        auths = auths.trim();

        //additionalInfo.put("uuid", customUser.getId());
        additionalInfo.put("auths", auths);
        additionalInfo.put("fullname", customUser.getCustomerUser().getName());
        additionalInfo.put("company_id", customUser.getCustomerUser().getCustomerCompanyId());
        additionalInfo.put("user_id", customUser.getCustomerUser().getCustomerUserId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
