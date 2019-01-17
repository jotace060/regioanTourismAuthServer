package com.dparadig.auth_server.controller;

import com.dparadig.auth_server.alias.*;
import com.dparadig.auth_server.common.*;
import com.dparadig.auth_server.service.EmailService;
import com.google.gson.JsonObject;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author RMujica
 */
@Controller
@CommonsLog
@CrossOrigin(origins = Constants.ORIGIN)
@RequestMapping(produces = "application/json")
public class LicenseController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private final SqlSession sqlSession;
    @Value("${frontend.url}")
    private String frontendURL;
    @Value("${frontend.pnotificaciones_url}")
    private String pNotificacionesFeURL;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public LicenseController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RequestMapping(value = "/liblic/checkLicense", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String checkLicense(@RequestBody LibLicenseRequest licRequest) {
        JsonObject response = new JsonObject();
        String cdkey = licRequest.getCdkey();
        String pkey = licRequest.getPkey();

        Map<String, Object> licenseMap = new HashMap<String, Object>();
        licenseMap.put("cdkey", licRequest.getCdkey());
        LicenseCompany dbLicense = this.sqlSession.selectOne("getLicenseInfo", licenseMap);
        if (dbLicense != null){
            log.debug("found "+dbLicense.getInstanceName());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expireDate = dbLicense.getExprDate();
            log.debug("checking if license is expired "+expireDate+" > "+now+" ?");
            if ( now.isBefore(expireDate)) {
                if (pkey.equalsIgnoreCase(dbLicense.getPkey())){
                    if (dbLicense.getStatus() == 2) {
                        if (dbLicense.getRetries() > 0) {
                            Map<String, Object> mapUpdate = new HashMap<>();
                            mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                            int updateRows = this.sqlSession.update("discountRetries", mapUpdate);
                            if (updateRows > 0) {
                                response.addProperty("status", "success");
                                response.addProperty("message", "License found");
                            } else {
                                //ERROR Actualizar retries
                                response.addProperty("status", "error");
                                response.addProperty("message", "License found but an error occurred while updating.");
                                createDbLog("ERROR_LICENSE",
                                        "License found but has exceeded the usage limit. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                                        licRequest.getCdkey(), "", "");
                            }
                        } else {
                            response.addProperty("status", "error");
                            response.addProperty("message", "License found but has exceeded the usage limit");
                            createDbLog("ERROR_LICENSE",
                                    "License found but has exceeded the usage limit. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                                    licRequest.getCdkey(), "", "");
                        }
                    } else if (dbLicense.getStatus() == 1) {
                        response.addProperty("status", "success");
                        response.addProperty("message", "License found");
                    }
                } else if(dbLicense.getPkey() == null){
                    //No tiene pkey registrado
                    Map<String, Object> mapUpdate = new HashMap<>();
                    mapUpdate.put("pkey", pkey);
                    mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                    int updateRows = this.sqlSession.update("registerPkey", mapUpdate);
                    if (updateRows > 0) {
                        response.addProperty("status", "success");
                        response.addProperty("message", "License found");
                        createDbLog("LICENSE",
                                "License found and a pkey has been registered. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                                licRequest.getCdkey(), "", "");
                    } else {
                        //ERROR Actualizar pkey
                        response.addProperty("status", "error");
                        response.addProperty("message", "License found but an error occurred while updating.");
                        createDbLog("ERROR_LICENSE",
                                "License found but has exceeded the usage limit. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                                licRequest.getCdkey(), "", "");
                    }
                } else {
                    //Tiene pkey registrado pero no es igual al enviado
                    response.addProperty("status", "error");
                    response.addProperty("message", "License does not exists");
                    createDbLog("ERROR_LICENSE",
                            "License found but used by an unrecognized machine. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                            licRequest.getCdkey(), "", "");
                }
            } else {
                response.addProperty("status", "error");
                response.addProperty("message", "License found but expired");
                createDbLog("ERROR_LICENSE",
                        "License found but expired. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                        licRequest.getCdkey(), "", "");
            }
        } else {
            response.addProperty("status", "error");
            response.addProperty("message", "License does not exists");
            createDbLog("ERROR_LICENSE",
                    "License does not exists. ["+licRequest.getCdkey()+" / "+licRequest.getPkey()+"]",
                    licRequest.getCdkey(), "", "");
        }

        log.info("RMR - checkLicense: response = "+response.toString());
        return response.toString();
    }

    private void createDbLog(String log_type, String message, String license, String source_name, String source_ip){
        Map<String, Object> logMap = new HashMap<String, Object>();
        logMap.put("log_type", log_type);
        logMap.put("message", message);
        logMap.put("license", license);
        logMap.put("source_name", source_name);
        logMap.put("source_ip", source_ip);
        log.debug("Inserting DB log: "+message);
        this.sqlSession.insert("insertLog", logMap);
    }
    //endregion
}
