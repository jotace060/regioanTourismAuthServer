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

        Map<String, Object> licenseMap = new HashMap<String, Object>();
        licenseMap.put("cdkey", licRequest.getCdkey());
        //licenseMap.put("mac", licRequest);
        
        // Primero validar si existe una empresa con el nombre companyName. Si no existe, crear una.
        // De existir, buscar por empresa hasta encontrar una que tenga licencia activa.
        LicenseCompany dbLicense = this.sqlSession.selectOne("getLicenseInfo", licenseMap);
        if (dbLicense != null){
            log.debug("found "+dbLicense.getInstanceName());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expireDate = dbLicense.getExprDate();
            log.debug("checking if license is expired "+expireDate+" > "+now+" ?");
            if ( now.isBefore(expireDate)) {
                //revisar si cdkey esta fijo a una MAC
                boolean isValid = true;
                List<ProductOption> pOptions = this.sqlSession.selectList("getProductOptionsOfLicense", licenseMap);
                if (pOptions != null && !pOptions.isEmpty()){
                    //existe opcion de MAC Required?
                    boolean macNeeded = false;
                    ProductOption macOption = null;
                    for (ProductOption pOption : pOptions){
                        if (pOption.getName().equalsIgnoreCase("mac required")){
                            macOption = pOption;
                            macNeeded = true;
                            break;
                        }
                    }
                    if (macNeeded){
                        Map<String, Object> optionMap = new HashMap<String, Object>();
                        optionMap.put("license_company_id", dbLicense.getLicenseCompanyId());
                        optionMap.put("product_option_id", macOption.getProductOptionId());
                        LicenseOption lOption = this.sqlSession.selectOne("getLicenseOption", optionMap);
                        if (lOption == null || !lOption.getOptionValue().equalsIgnoreCase(licRequest.getMac())){
                            isValid = false;
                            response.addProperty("status", "error");
                            response.addProperty("message", "License already in use");
                            createDbLog("ERROR_LICENSE",
                                    "License already in use. ["+licRequest.getCdkey()+" / "+licRequest.getMac()+"]",
                                    licRequest.getCdkey(), "", "");
                        }
                    }
                } else
                    log.debug("no product options found");
                if (isValid) {
                    response.addProperty("status", "success");
                    response.addProperty("message", "License found");
                }
            } else {
                response.addProperty("status", "error");
                response.addProperty("message", "License found but expired");
                createDbLog("ERROR_LICENSE",
                        "License found but expired. ["+licRequest.getCdkey()+" / "+licRequest.getMac()+"]",
                        licRequest.getCdkey(), "", "");
            }
        } else {
            response.addProperty("status", "error");
            response.addProperty("message", "License does not exists");
            createDbLog("ERROR_LICENSE",
                    "License does not exists. ["+licRequest.getCdkey()+" / "+licRequest.getMac()+"]",
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
