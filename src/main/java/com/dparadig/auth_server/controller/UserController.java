package com.dparadig.auth_server.controller;

import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.common.Constants;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author jlabarca
 */
@Controller
@CommonsLog
@CrossOrigin(origins = Constants.ORIGIN)
public class UserController {

    private final SqlSession sqlSession;

    public UserController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public String getAllUser() {
        return Constants.GSON.toJson(this.sqlSession.selectList("getAllUser"));
    }



    @RequestMapping("/getUserByEmail")
    @ResponseBody
    public String getUserByEmail(String email) {
        return Constants.GSON.toJson((CustomerUser)this.sqlSession.selectOne("getUserByEmail",email));
    }





}
