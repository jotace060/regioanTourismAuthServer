package com.dparadig.auth_server.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dparadig.auth_server.alias.*;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dparadig.auth_server.common.Constants;
import com.dparadig.auth_server.common.TokenType;
import com.dparadig.auth_server.service.EmailService;
import com.google.gson.JsonObject;

import lombok.extern.apachecommons.CommonsLog;

/**
 * @author Jlabarca
 */
@Controller
@CommonsLog
@CrossOrigin(origins = Constants.ORIGIN)
@RequestMapping(produces = "application/json")
public class LicenseController {

    @Autowired
    private EmailService emailService;
    @Value("${frontend.url}")
    private String frontendURL;
    private final SqlSession sqlSession;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LicenseController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RequestMapping("/getAllLicences")
    @ResponseBody
    public String getAllLicences() {
        return Constants.GSON.toJson(this.sqlSession.selectList("getAllLicences"));
    }

    @RequestMapping("/getLicenseById")
    @ResponseBody
    public String getLicenseById(int licenseCompanyId) {
        return Constants.GSON.toJson((LicenseCompany)this.sqlSession.selectOne("getLicenseById", licenseCompanyId));
    }

    @RequestMapping("/insertLicense")
    @ResponseBody
    public String insertLicense(String companyRut,String customerUserId, String instanceName, int autoRenew) {
       /* (`customer_company_id`,
        `license_type_id`,
        `pay_element_id`,
        `customer_user_id`,
        `instance_name`,
        `expr_date`,
        `auto_renew`,
        `cdkey`)
        */
        LicenseCompany licenseCompany = new LicenseCompany();
        licenseCompany.setAutoRenew(autoRenew);
        //generate cdkey if otrs
        return Constants.GSON.toJson(this.sqlSession.insert("insertLicense"));
    }

    //LicenseType

    @RequestMapping("/getAllLicenceTypes")
    @ResponseBody
    public String getAllLicenceTypes() {
        return Constants.GSON.toJson(this.sqlSession.selectList("getAllLicenceTypes"));
    }



}
