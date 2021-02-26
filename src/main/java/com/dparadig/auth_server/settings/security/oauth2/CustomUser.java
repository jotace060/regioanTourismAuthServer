package com.dparadig.auth_server.settings.security.oauth2;

import com.dparadig.auth_server.alias.CustomerUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

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

    public CustomerUser getCustomerUser() {
        return customerUser;
    }

    public void setCustomerUser(CustomerUser user) {
        this.customerUser = user;
    }
}
