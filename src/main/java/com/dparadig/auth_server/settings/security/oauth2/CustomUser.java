package com.dparadig.auth_server.settings.security.oauth2;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dparadig authentication server User implementation
 * authorities = privileges
 * username = email
 * name = full user name
 * roles used as extra info on TokenEnhancer
 * @author JLabarca
 */
public class CustomUser extends User {

    private CustomerUser customerUser;


    public CustomUser(CustomerUser customerUser, Collection<GrantedAuthority> authorities) {
        super(customerUser.getEmail(), customerUser.getPassCurr(), authorities);
        this.customerUser = customerUser;
    }

    /*
    public Set<String> getUserRolesNames() {
        return getUserRoles().stream()
                .map(p -> p.getName())
                .collect(Collectors.toSet());
    }
    */

    public CustomerUser getCustomerUser() {
        return customerUser;
    }

    public void setCustomerUser(CustomerUser user) {
        this.customerUser = user;
    }

    /*public int getId() {
        return user.getCustomerUserID();
    }*/
}
