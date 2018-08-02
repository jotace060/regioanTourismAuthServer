package com.dparadig.auth_server.settings.security.oauth2;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Privilege;
import com.dparadig.auth_server.alias.Role;
import com.dparadig.auth_server.service.UserService;
import com.dparadig.auth_server.settings.security.oauth2.CustomUser;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author JLabarca
 */

@CommonsLog
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("RMR - loadUserByUsername starting...");
        CustomerUser customerUser = userService.getUserByEmail(email);
        if (null == customerUser)
            throw new UsernameNotFoundException("Bad credentials");
        log.info("Founded "+customerUser.getCustomerUserId());
        List<Role> userRoles = userService.getUserRoles(customerUser.getCustomerUserId());
        List<Privilege> userPrivileges = userService.getRolePrivilegesOfUser(customerUser.getCustomerUserId());
        log.info("userRoles "+userRoles.size());
        log.info("userPrivileges "+userPrivileges.size());

        //Only name
        Set<String> rolesList = userRoles
                .stream()
                .map(p -> {
                    String role = p.getName().toUpperCase();
                    String[] s = role.split("_");
                    if(s[0].equals("ROLE"))
                        return role;
                    else
                        return "ROLE_" + role;
                })
                .collect(Collectors.toSet());

        //Only name
        Set<String> privilegesList = userPrivileges
                .stream()
                .map(p -> {
                    String role = p.getName().toUpperCase();
                    String[] s = role.split("_");
                    if(s[0].equals("PRIV"))
                        return role;
                    else
                        return "PRIV_" + role;
                })
                .collect(Collectors.toSet());


        //Join
        rolesList.addAll(privilegesList);

        log.info(rolesList);

        String rolesAndPrivileges = StringUtils.collectionToCommaDelimitedString(rolesList);

        return new CustomUser(customerUser, AuthorityUtils.commaSeparatedStringToAuthorityList(rolesAndPrivileges));
    }
}
