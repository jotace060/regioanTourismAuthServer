package com.dparadig.auth_server.service;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Privilege;
import com.dparadig.auth_server.alias.Role;
import com.dparadig.auth_server.settings.exceptions.ExceptionAttributes;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jlabarca
 */
@CommonsLog
@Service
public class UserService {

    @Autowired
    SqlSession sqlSession;



    @Autowired
    public UserService() {
    }



    public List<Role> getUserRoles(int customerUserID) {
        return this.sqlSession.selectList("getUserRoles",customerUserID);
    }

    public List<Privilege> getRolePrivileges(int roleID) {
        log.info("RMR - getRolePrivileges: of role "+roleID);
        return this.sqlSession.selectList("getRolePrivileges",roleID);
    }

    public List<Privilege> getRolePrivilegesOfUser(long customerUserID) {
        log.info("RMR - getRolePrivilegesOfUser: of role "+customerUserID);
        return this.sqlSession.selectList("getRolePrivilegesOfUser",customerUserID);
    }


}
