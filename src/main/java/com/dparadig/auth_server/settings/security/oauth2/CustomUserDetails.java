package com.dparadig.auth_server.settings.security.oauth2;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Role;
import com.dparadig.auth_server.common.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JLabarca
 */

public class CustomUserDetails implements UserDetails {

    private CustomerUser user;
    private List<Role> userRoles;

    public CustomUserDetails(CustomerUser user, List<Role> userRoles) {
        this.user = user;
        this.userRoles = userRoles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> rolesList = userRoles.stream()
                .map(p -> p.getName())
                .collect(Collectors.toList());
        //String roles = Constants.GSON.toJson(userRoles);
        String roles = StringUtils.collectionToCommaDelimitedString(rolesList);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
    }

    @Override
    public String getPassword() {
        return user.getPassCurr();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (user.getStatus() == 1) return true;
        else return false;
    }

    public int getId() {
        return user.getCustomerUserID();
    }
}
