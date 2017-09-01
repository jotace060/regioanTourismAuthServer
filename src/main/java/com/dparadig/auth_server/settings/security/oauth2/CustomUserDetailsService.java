package com.dparadig.auth_server.settings.security.oauth2;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Role;
import com.dparadig.auth_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author JLabarca
 */

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        CustomerUser user = userService.getUserByEmail(email);
        if (null == user) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        List<Role> userRoles = userService.getUserRoles(user.getCustomerUserID());

        return new CustomUserDetails(user, userRoles);
    }
}
