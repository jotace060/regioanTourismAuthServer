package com.dparadig.auth_server.controller;

import com.dparadig.auth_server.alias.*;
import com.dparadig.auth_server.common.*;
import com.dparadig.auth_server.service.EmailService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
        //decrypt incomming payload
        dpaEncrypter encrypter = new dpaEncrypter("dparadiglJc9SPQWT3n23kytdrx4internet");
        log.debug("RMR - checkLicense: received = "+licRequest.getPayload());
        String decryptedPayload = encrypter.decryptString(licRequest.getPayload());
        log.debug("RMR - checkLicense: decryptedPayload = "+decryptedPayload);
        Gson gson = new Gson();
        JsonObject payloadObj = gson.fromJson(decryptedPayload, JsonObject.class);
        log.debug("RMR - checkLicense: payloadObj = "+payloadObj);

        if(payloadObj==null || payloadObj.isJsonNull()) {
            response.addProperty("status", "error");
            response.addProperty("message", "Received payload is null");
            /*createDbLog("ERROR_LICENSE",
                    "License does not exists. ["+licRequest.getCdkey()+" / "+licRequest.getMac()+"]",
                    licRequest.getCdkey(), "", "");*/
        }
        else {
            String cdkey = "";
            String pkey = "";
            if (payloadObj.get("cdkey")!=null && !payloadObj.get("cdkey").isJsonNull()) cdkey = payloadObj.get("cdkey").getAsString();
            if (payloadObj.get("pkey")!=null && !payloadObj.get("pkey").isJsonNull()) pkey = payloadObj.get("pkey").getAsString();
            Map<String, Object> licenseMap = new HashMap<String, Object>();
            licenseMap.put("cdkey", cdkey);
            LicenseCompany dbLicense = this.sqlSession.selectOne("getLicenseInfo", licenseMap);
            if (dbLicense != null) {
                log.debug("found " + dbLicense.getInstanceName());
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expireDate = dbLicense.getExprDate();
                log.debug("checking if license is expired " + expireDate + " > " + now + " ?");
                if (now.isBefore(expireDate)) {
                    if (pkey.equalsIgnoreCase(dbLicense.getPkey())) {
                        if (dbLicense.getStatus() == 2) {
                            if (dbLicense.getRetries() > 0) {
                                Map<String, Object> mapUpdate = new HashMap<>();
                                mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                                int updateRows = this.sqlSession.update("discountRetries", mapUpdate);
                                if (updateRows > 0) {
                                    response.addProperty("status", "success");
                                    response.addProperty("message", "License found");
                                    createDbLog("LICENSE_INIT_CHECK",
                                            "Test License use. Retries left = "+(dbLicense.getRetries()-1)+". [" + cdkey + " / " + pkey + "]",
                                            cdkey, "", "");
                                } else {
                                    //ERROR Actualizar retries
                                    response.addProperty("status", "error");
                                    response.addProperty("message", "License found but an error occurred while updating.");
                                    createDbLog("ERROR_LICENSE",
                                            "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                            cdkey, "", "");
                                }
                            } else {
                                response.addProperty("status", "error");
                                response.addProperty("message", "License found but has exceeded the usage limit");
                                createDbLog("ERROR_LICENSE",
                                        "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                        cdkey, "", "");
                            }
                        } else if (dbLicense.getStatus() == 1) {
                            response.addProperty("status", "success");
                            response.addProperty("message", "License found");
                        }
                    } else if (dbLicense.getPkey() == null) {
                        //No tiene pkey registrado
                        Map<String, Object> mapUpdate = new HashMap<>();
                        mapUpdate.put("pkey", pkey);
                        mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                        int updateRows = this.sqlSession.update("registerPkey", mapUpdate);
                        if (updateRows > 0) {
                            response.addProperty("status", "success");
                            response.addProperty("message", "License found");
                            createDbLog("LICENSE",
                                    "License found and a pkey has been registered. [" + cdkey + " / " + pkey + "]",
                                    cdkey, "", "");
                        } else {
                            //ERROR Actualizar pkey
                            response.addProperty("status", "error");
                            response.addProperty("message", "License found but an error occurred while updating.");
                            createDbLog("ERROR_LICENSE",
                                    "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                    cdkey, "", "");
                        }
                    } else {
                        //Tiene pkey registrado pero no es igual al enviado
                        response.addProperty("status", "error");
                        response.addProperty("message", "License does not exists");
                        createDbLog("ERROR_LICENSE",
                                "License found but used by an unrecognized machine. [" + cdkey + " / " + pkey + "]",
                                cdkey, "", "");
                    }
                } else {
                    response.addProperty("status", "error");
                    response.addProperty("message", "License does not exists");
                    createDbLog("ERROR_LICENSE",
                            "License found but expired. [" + cdkey + " / " + licRequest.getPkey() + "]",
                            cdkey, "", "");
                }
            } else {
                response.addProperty("status", "error");
                response.addProperty("message", "License does not exists");
                createDbLog("ERROR_LICENSE",
                        "License does not exists. [" + cdkey + " / " + licRequest.getPkey() + "]",
                        cdkey, "", "");
            }
        }

        log.info("RMR - checkLicense: response = "+response.toString());
        Instant instant = Instant.now();
        response.addProperty("timestamp", instant.toEpochMilli());
        String encryptedText = encrypter.encryptString(response.toString());
        JsonObject encryptedResponse = new JsonObject();
        encryptedResponse.addProperty("payload",encryptedText);
        log.info("RMR - checkLicense: encryptedResponse = "+encryptedResponse.toString());
        return encryptedResponse.toString();
    }

    //Solo para pruebas de nodos, limite de usos inicial = 5
    @RequestMapping(value = "/liblic/checkLicenseInit", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public String checkLicenseInit(@RequestBody LibLicenseRequest licRequest) {
        JsonObject response = new JsonObject();
        dpaEncrypter encrypter = new dpaEncrypter("dparadiglJc9SPQWT3n23kytdrx4internet");
        String decryptedPayload = encrypter.decryptString(licRequest.getPayload());
        Gson gson = new Gson();
        JsonObject payloadObj = gson.fromJson(decryptedPayload, JsonObject.class);

        if(payloadObj==null || payloadObj.isJsonNull()) {
            response.addProperty("status", "error");
            response.addProperty("message", "Received payload is null");
            /*createDbLog("ERROR_LICENSE",
                    "License does not exists. ["+licRequest.getCdkey()+" / "+licRequest.getMac()+"]",
                    licRequest.getCdkey(), "", "");*/
        }
        else {
            String cdkey = "";
            String pkey = "";
            if (payloadObj.get("cdkey")!=null && !payloadObj.get("cdkey").isJsonNull()) cdkey = payloadObj.get("cdkey").getAsString();
            if (payloadObj.get("pkey")!=null && !payloadObj.get("pkey").isJsonNull()) pkey = payloadObj.get("pkey").getAsString();
            Map<String, Object> licenseMap = new HashMap<String, Object>();
            licenseMap.put("cdkey", cdkey);
            LicenseCompany dbLicense = this.sqlSession.selectOne("getLicenseInfo", licenseMap);
            if (dbLicense != null) {
                log.debug("found " + dbLicense.getInstanceName());
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expireDate = dbLicense.getExprDate();
                log.debug("checking if license is expired " + expireDate + " > " + now + " ?");
                if (now.isBefore(expireDate)) {
                    if (pkey.equalsIgnoreCase(dbLicense.getPkey())) {
                        if (dbLicense.getStatus() == 2) {
                            if (dbLicense.getRetries() > 0) {
                                Map<String, Object> mapUpdate = new HashMap<>();
                                mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                                int updateRows = this.sqlSession.update("discountRetries", mapUpdate);
                                if (updateRows > 0) {
                                    response.addProperty("status", "success");
                                    response.addProperty("message", "License found");
                                    createDbLog("LICENSE_INIT_CHECK",
                                            "Test License use. Retries left = "+(dbLicense.getRetries()-1)+". [" + cdkey + " / " + pkey + "]",
                                            cdkey, "", "");
                                } else {
                                    //ERROR Actualizar retries
                                    response.addProperty("status", "error");
                                    response.addProperty("message", "License found but an error occurred while updating.");
                                    createDbLog("ERROR_LICENSE",
                                            "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                            cdkey, "", "");
                                }
                            } else {
                                response.addProperty("status", "error");
                                response.addProperty("message", "License found but has exceeded the usage limit");
                                createDbLog("ERROR_LICENSE",
                                        "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                        cdkey, "", "");
                            }
                        } else if (dbLicense.getStatus() == 1) {
                            response.addProperty("status", "success");
                            response.addProperty("message", "License found");
                        }
                    } else if (dbLicense.getPkey() == null) {
                        //No tiene pkey registrado
                        Map<String, Object> mapUpdate = new HashMap<>();
                        mapUpdate.put("pkey", pkey);
                        mapUpdate.put("licenseCompanyId", dbLicense.getLicenseCompanyId());
                        int updateRows = this.sqlSession.update("registerPkey", mapUpdate);
                        if (updateRows > 0) {
                            response.addProperty("status", "success");
                            response.addProperty("message", "License found");
                            createDbLog("LICENSE",
                                    "License found and a pkey has been registered. [" + cdkey + " / " + pkey + "]",
                                    cdkey, "", "");
                        } else {
                            //ERROR Actualizar pkey
                            response.addProperty("status", "error");
                            response.addProperty("message", "License found but an error occurred while updating.");
                            createDbLog("ERROR_LICENSE",
                                    "License found but has exceeded the usage limit. [" + cdkey + " / " + pkey + "]",
                                    cdkey, "", "");
                        }
                    } else {
                        //Tiene pkey registrado pero no es igual al enviado
                        response.addProperty("status", "error");
                        response.addProperty("message", "License does not exists");
                        createDbLog("ERROR_LICENSE",
                                "License found but used by an unrecognized machine. [" + cdkey + " / " + pkey + "]",
                                cdkey, "", "");
                    }
                } else {
                    response.addProperty("status", "error");
                    response.addProperty("message", "License does not exists");
                    createDbLog("ERROR_LICENSE",
                            "License found but expired. [" + cdkey + " / " + pkey + "]",
                            cdkey, "", "");
                }
            } else {
                //Revisar si existen mas licencias en modo INIT/TEST en este dispositivo
                licenseMap.put("pkey", pkey);
                LicenseCompany dbLicenseInit = this.sqlSession.selectOne("getExistingLicenseInit", licenseMap);
                if (dbLicenseInit == null) {
                    //Crear Nueva cdkey en modo INIT/TEST (2)
                    Map<String, Object> mapInsert = new HashMap<>();
                    mapInsert.put("cdkey", cdkey);
                    mapInsert.put("pkey", pkey);
                    int insertedRows = this.sqlSession.insert("registerLicenseInit", mapInsert);
                    if (insertedRows > 0) {
                        response.addProperty("status", "success");
                        response.addProperty("message", "License found");
                        createDbLog("LICENSE",
                                "New Init License registered. [" + cdkey + " / " + pkey + "/5retries]",
                                cdkey, "", "");
                    } else {
                        //ERROR registrar key
                        response.addProperty("status", "error");
                        response.addProperty("message", "License does not exists");
                        createDbLog("ERROR_LICENSE",
                                "Error registering New Init License. [" + cdkey + " / " + pkey + "]",
                                cdkey, "", "");
                    }
                } else {
                    response.addProperty("status", "error");
                    response.addProperty("message", "Can't have more than one testing license");
                    createDbLog("ERROR_LICENSE",
                            "Can't have more than one testing license. [" + cdkey + " / " + pkey + "]",
                            cdkey, "", "");
                }
            }
        }

        Instant instant = Instant.now();
        response.addProperty("timestamp", instant.toEpochMilli());
        String encryptedText = encrypter.encryptString(response.toString());
        JsonObject encryptedResponse = new JsonObject();
        encryptedResponse.addProperty("payload",encryptedText);
        return encryptedResponse.toString();
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
