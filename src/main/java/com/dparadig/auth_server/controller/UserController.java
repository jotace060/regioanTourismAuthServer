package com.dparadig.auth_server.controller;

import com.dparadig.auth_server.alias.CustomerCompany;
import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.LicenseCompany;
import com.dparadig.auth_server.alias.Token;
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
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Jlabarca
 */
@Controller
@CommonsLog
@CrossOrigin(origins = Constants.ORIGIN)
@RequestMapping(produces = "application/json")
public class UserController{

    @Autowired
    private EmailService emailService;
    @Autowired
    private final SqlSession sqlSession;
    
    //URLs de redirect para cuando se solicita cambio de contrasena. Al solicitar cambio de contrasena se envia un
    //correo con un link al sitio donde se debe hacer el cambio de contrasena, dependiendo del producto (DVU, SNI, etc...)
    @Value("${frontend.url}")
    private String frontendURL;
    
    @Value("${frontend.pnotificaciones_url}")
    private String pNotificacionesFeURL;
    
    @Value("${frontend.dvu_url}")
    private String pDvuURL;
    
    //Token de seguridad que usan algunas APIs para aumentar la seguridad. Se usa en aquellas APIs cuyos argumentos
    //puede ser facilmente detectados por terceras personas.
    @Value("${privToken}")
    private String privToken;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @GetMapping("/getAllUser")
    @ResponseBody
    public String getAllUser() {

           return Constants.GSON.toJson(this.sqlSession.selectList("getAllUser"));
    }

    //POST: PBM DVU SNI DISCOVERY ENTEL OTRS
    @ApiIgnore
    @Deprecated
    @PostMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(Long customerUserId) {
    	JsonObject response = new JsonObject();
    	System.out.println("deleteUser: " + customerUserId);
    	HashMap<String, Long> options = new HashMap<String, Long>();
    	options.put("customerUserId", customerUserId);
    	
    	this.sqlSession.delete("deleteUserRoles", options);
    	this.sqlSession.delete("deleteUserTokens", options);
    	this.sqlSession.delete("deleteUserData", options);
    	
    	return response.toString();
    }
    //POST: PBM DVU SNI DISCOVERY ENTEL
    @PostMapping("/insertUser")
    @ResponseBody
    public String insertUser(String name, String email, String companyName, String passCurr, String portalType) {
        JsonObject response = new JsonObject();

        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName(companyName);
        
        Map<String, Object> companyMap = new HashMap<String, Object>();
        companyMap.put("companyName", companyName);
        companyMap.put("portalType", portalType);
        Integer company_id = null;
        boolean companyExists = false, companyWithLicense = false;
        
        // Primero validar si existe una empresa con el nombre companyName. Si no existe, crear una.
        // De existir, buscar por empresa hasta encontrar una que tenga licencia activa.
        List<LicenseCompany> companies = this.sqlSession.selectList("getCustomerCompanyByName", companyMap);
        for (LicenseCompany company : companies) {
        	if (company.getLicenseCompanyId() == null) {
        		if (!companyExists) {
            		company_id = company.getCustomerCompanyId();
            		companyWithLicense = false;
            		companyExists = true;
        		}
        	}
        	else {
        		company_id = company.getCustomerCompanyId();
        		companyExists = true;
        		companyWithLicense = true;
        		break;
        	}
        }
        
        CustomerUser customerUser = new CustomerUser();
        customerUser.setName(name);
        customerUser.setEmail(email);
        customerUser.setPassCurr(passwordEncoder.encode(passCurr));
        
        if (!companyExists) {
        	// Nueva empresa
        	this.sqlSession.insert("insertCompanyName",customerCompany);
            log.info("Inserted Company with ID: "+customerCompany.getCustomerCompanyId());
            response.addProperty("license", "create");
        }
        else {
        	customerCompany.setCustomerCompanyId(company_id);
        	if (!companyWithLicense) {
                response.addProperty("license", "create");
        	}
        	else {
        		// Si la compania tiene licencia, insertar usuario en 'Espera de validacion'
        		customerUser.setValidationStatus(0);
                response.addProperty("license", "exists");
        	}
        }
        
        customerUser.setCustomerCompanyId(customerCompany.getCustomerCompanyId());
        String sucessRegister = "Successfully Registered";
        String licensedRegister = ". Since you are on a licensed company, you have to contact the Administrator for your account validation";
        
        try {
            this.sqlSession.insert("insertUser",customerUser);
            response.add("data",Constants.GSON.toJsonTree(customerUser));
            response.addProperty("status", "success");
            if (companyExists && companyWithLicense) 
            	response.addProperty("message", sucessRegister + licensedRegister);
            else
                response.addProperty("message", sucessRegister);            	
            log.info("Inserted User with ID: "+customerUser.getCustomerUserId());
            //createConfirmationTokenAndSendEmail(customerUser, portalType);
            //Create ROLE
            /*Role adminRole = new Role();
            adminRole.setCustomerCompanyId(customerCompany.getCustomerCompantId());
            adminRole.setName("ROLE_ADMIN");
            adminRole.setStatus(1);
            this.sqlSession.insert("insertRoleAdmin", adminRole);
            log.info("Inserted ROLE_ADMIN with ID: "+adminRole.getRoleId());
            customerUser.setRoleName("ROLE_ADMIN");
            this.sqlSession.insert("insertUserRole",customerUser);*/
        } catch (final DuplicateKeyException e) {
            response.addProperty("status", "error");
            response.addProperty("message", "This email is already registered.");
            e.printStackTrace();
        } catch (final Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Error 500");
            e.printStackTrace();
        }

        return response.toString();
    }
    //POST PBM DVU SNI DISCOVERY
    @PostMapping("/updateUserRolesForProduct")
    @ResponseBody
    public String updateUserRolesForProduct(String customerUserId, String productName, String companyId, String roleId) {
    	JsonObject response = new JsonObject();
    	HashMap<String, String> options = new HashMap<String, String>();
    	options.put("customerUserId", customerUserId);
    	options.put("productName", productName);
    	options.put("companyId", companyId);
    	options.put("roleId", roleId);
    	
    	List<HashMap<String, Long>> rolesToDelete = sqlSession.selectList("selectRolesForProduct", options);
    	
    	for(int i = 0; i < rolesToDelete.size(); i++) {
    		Long _customerUserId = rolesToDelete.get(i).get("customer_user_id");
    		Long _roleId = rolesToDelete.get(i).get("role_id");
    		Long _licenseCompanyId = rolesToDelete.get(i).get("license_company_id");
    		    		
    		HashMap<String, Long> deleteOptions = new HashMap<String, Long>();
    		deleteOptions.put("customerUserId", _customerUserId);
    		deleteOptions.put("licenseCompanyId", _licenseCompanyId);
    		deleteOptions.put("roleId", _roleId);
    		sqlSession.delete("deleteUserRolesForProduct", deleteOptions);
    		
    	}
    	
    	if(roleId != null) {
    		sqlSession.update("updateUserRolesForProduct", options);
    	}
    	
    	return response.toString();
    }
    //POST PBM DVU SNI DISCOVERY
    @ApiIgnore
    @Deprecated
    @PostMapping("/createUpdateUser")
    @ResponseBody
    public String createUpdateUser(String name, String email, Integer companyId, Integer customerUserParentId, String passCurr, Integer roleId) {
    	JsonObject response = new JsonObject();
    	
    	CustomerUser user = null;
    	
    	if(customerUserParentId != null) {
    		user = (CustomerUser) this.sqlSession.selectOne("getUserById", customerUserParentId);
    	}
    	
    	if(user != null) {    		
    		user.setName(name);
            user.setEmail(email);
            
            if(passCurr != null) {
            	user.setPassCurr(passwordEncoder.encode(passCurr));
            	sqlSession.update("updateUserPass", user);
            }
            
            sqlSession.update("updateUser", user);
            
    	} else {
    		return this.registerNewUser(name, email, companyId, customerUserParentId, passCurr, null);
    	}
    	
    	return response.toString();
    }
    //POST PBM DVU SNI DISCOVERY
    @ApiIgnore
    @Deprecated
    @PostMapping("/registerNewUser")
    @ResponseBody
    public String registerNewUser(String name, String email, Integer companyId, Integer customerUserParentId, String passCurr, @RequestParam(required=false) String portalType) {
        JsonObject response = new JsonObject();

        CustomerUser customerUser = new CustomerUser();
        customerUser.setName(name);
        customerUser.setEmail(email);
        customerUser.setPassCurr(passwordEncoder.encode(passCurr));
        customerUser.setCustomerCompanyId(companyId);

        try {
            this.sqlSession.insert("insertAddedUser",customerUser);
            response.add("data",Constants.GSON.toJsonTree(customerUser));
            response.addProperty("status", "success");
            response.addProperty("message", "Successfully Registered");
            response.addProperty("customerUserId", customerUser.getCustomerUserId());
            log.info("Inserted User with ID: "+customerUser.getCustomerUserId());
            //createConfirmationTokenAndSendEmail(customerUser, portalType);
            //Create ROLE
            //customerUser.setRoleName("user");
            //this.sqlSession.insert("insertUserRole",customerUser);
        } catch (final DuplicateKeyException e) {
            response.addProperty("status", "error");
            response.addProperty("message", "This email is already registered.");
            e.printStackTrace();
        } catch (final Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Error 500");
            e.printStackTrace();
        }

        return response.toString();
    }

    private Token createToken(CustomerUser customerUser,int duration, int type) {
        return new Token(customerUser.getCustomerUserId(),UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(duration),type);
    }

    private void createConfirmationTokenAndSendEmail(CustomerUser customerUser, String portalType){
        Token token = createToken(customerUser,3, TokenType.CONFIRMATION);
        log.info("Email confirmation token created: "+customerUser.getCustomerUserId());
        this.sqlSession.insert("insertToken",token);
        new Thread(() -> {
            if (portalType == null || portalType.isEmpty())
                sendConfirmationEmail(customerUser,token);
            else if (portalType.equalsIgnoreCase("notificaciones"))
                sendConfirmationEmailESP(customerUser,token, pNotificacionesFeURL);
        }).start();
    }

    private String sendConfirmationEmail(CustomerUser user, Token token) {
        String url = frontendURL+"/#/activation?t="+token.getToken();
        if(emailService.sendConfirmationEmail(user.getEmail(),url))
            log.info("Confirmation Email sent to "+user.getEmail() );
        else
            log.error("Confirmation Email was NOT sent to "+user.getEmail() );
        return url;
    }

    private String sendConfirmationEmailESP(CustomerUser user, Token token, String feUrl) {
        String url = feUrl+"/#/activation?t="+token.getToken();
        if(emailService.sendConfirmationEmailESP(user.getEmail(),url))
            log.info("Confirmation Email sent to "+user.getEmail() );
        else
            log.error("Confirmation Email was NOT sent to "+user.getEmail() );
        return url;
    }

    /*
        FALTA: Cambiar busqueda de token por columna token, a userID + token
     */
    //POST PBM DVU SNI DISCOVERY ENTEL
    @PostMapping("/newPassword")
    @ResponseBody
    private String newPassword(String t, String password){
        JsonObject response = new JsonObject();
        log.info("Token: "+t);
        Token token = (Token) this.sqlSession.selectOne("getTokenByToken", t);
        if(token != null){
            log.info("Token found "+token.toString());
            log.info("Token expr date "+token.getExprDate());
            log.info("Token type "+token.getTokenType());
            log.info("Token user id "+token.getCustomerUserId());
            log.info("Current date: "+LocalDateTime.now());
            //Check token exp date
            if(token.getExprDate().isAfter(LocalDateTime.now())){
                //Check type
                if(token.getTokenType() == TokenType.PASS_RESET){
                    CustomerUser customerUser = (CustomerUser) this.sqlSession.selectOne("getUserById",token.getCustomerUserId());
                    //Check new pass is not repeat
                    if( passwordEncoder.matches(password,customerUser.getPassCurr())||
                        passwordEncoder.matches(password,customerUser.getPassPrev())||
                        passwordEncoder.matches(password,customerUser.getPassPrev2())){
                        response.addProperty("message","You can't use old passwords");
                        log.info("Old password for : "+token.getTokenType());
                        response.addProperty("status","error");
                    }else{
                    	log.info("new pass: " + password);
                        customerUser.setPassCurr(passwordEncoder.encode(password));
                        this.sqlSession.update("updateUserPass",customerUser);
                        log.info("Password updated for user "+token.getCustomerUserId());
                        response.addProperty("message","Your password has been successfully changed!");
                        response.addProperty("status","success");
                        this.sqlSession.delete("deleteToken",token);
                    }
                }else{
                    response.addProperty("message","Expired activation key");
                    log.info("Invalid Reset password Token Type: "+token.getTokenType());
                    response.addProperty("status","error");
                }
            }else{
                log.info("Token expired on "+token.getExprDate());
                response.addProperty("message","Key expired");
                response.addProperty("status","error");
            }
        }else{
            response.addProperty("message","Invalid Key");
            log.info("Token not found");
            response.addProperty("status","error");
        }

        return response.toString();
    }
    //POST PBM DVU SNI DISCOVERY ENTEL
    @PostMapping("/passReset")
    @ResponseBody
    private String passReset(String email, @RequestParam(required=false) String portalType){
        JsonObject response = new JsonObject();
        CustomerUser customerUser = (CustomerUser) this.sqlSession.selectOne("getUserByEmail",email);
        if(customerUser != null){
            Token token = createToken(customerUser,3, TokenType.PASS_RESET);
            log.info("Pass reset token created: "+customerUser.getCustomerUserId());
            this.sqlSession.insert("insertToken",token);
            new Thread(() -> {
                if (portalType == null || portalType.isEmpty())
                    sendPassResetEmail(customerUser,token);
                else if (portalType.equalsIgnoreCase("notificaciones"))
                    sendPassResetEmailESP(customerUser,token, pNotificacionesFeURL);
                else if(portalType.equalsIgnoreCase("dvu")) {
                	sendPassResetEmailESP(customerUser,token, pDvuURL);
                }
            }).start();
            response.addProperty("message","Password reset link has been sent to "+email);
            response.addProperty("status","success");
        }else{
            response.addProperty("message","User not found");
            response.addProperty("status","error");
        }

        return response.toString();
    }

    private String sendPassResetEmail(CustomerUser user, Token token) {
        String url = frontendURL+"/#/login?t="+token.getToken();
        if(emailService.sendPassResetEmail(user.getEmail(),url))
            log.info("Confirmation Email sent to "+user.getEmail() );
        else
            log.error("Confirmation Email was NOT sent to "+user.getEmail() );
        return url;
    }

    private String sendPassResetEmailESP(CustomerUser user, Token token, String feUrl) {
        String url = feUrl+"/#/login?t="+token.getToken();
        if(emailService.sendPassResetEmailESP(user.getEmail(),url))
            log.info("Confirmation Email sent to "+user.getEmail() );
        else
            log.error("Confirmation Email was NOT sent to "+user.getEmail() );
        return url;
    }
    //POST PBM DVU SNI DISCOVERY ENTEL
    @PostMapping("/confirmEmail")
    @ResponseBody
    public String confirmEmail(@RequestParam String t) {
        JsonObject response = new JsonObject();
        Token token = (Token) this.sqlSession.selectOne("getTokenByToken", t);
        if(token != null){
            log.info("Token found "+token.toString());
            log.info("Token expr date "+token.getExprDate());
            log.info("Token type "+token.getTokenType());
            log.info("Token user id "+token.getCustomerUserId());
            log.info("Current date: "+LocalDateTime.now());

            int validationStatus = (Integer) sqlSession.selectOne("getUserValidationStatus", token.getCustomerUserId());
            log.info("validationStatus: "+validationStatus);

            //Check token exp date
            if(token.getExprDate().isAfter(LocalDateTime.now())){
                //Check type
                if(token.getTokenType() == TokenType.CONFIRMATION){
                    //Check if already activated
                    if(validationStatus == 0){
                        this.sqlSession.insert("validateUser",token.getCustomerUserId());
                        log.info("User "+token.getCustomerUserId()+" Validated");
                        response.addProperty("message","Your account has been successfully activated!");
                        response.addProperty("status","success");
                        this.sqlSession.delete("deleteToken",token);
                    }else{
                        response.addProperty("message","This account is already activated, please log in.");
                        response.addProperty("status","login");
                    }
                }else{
                    response.addProperty("message","Invalid Key");
                    log.info("Invalid Confirmation Token Type: "+token.getTokenType());
                    response.addProperty("status","error");
                }
            }else{
                log.info("Token expired on "+token.getExprDate());
                response.addProperty("message","Key expired");
                response.addProperty("status","error");
            }
        }else{
            response.addProperty("message","Invalid Key");
            log.info("Token not found");
            response.addProperty("status","error");
        }

        return response.toString();
    }


    /*
    OTRSAuth Request creado especificamente para ser consultado por
    el backend multiclient de OTRS en los login con la cuenta de customerUser
    dentro del OTRS Dashboard.
     */

    @GetMapping("/otrsAuth")
    @ResponseBody
    public String otrsAuth(@RequestParam String username, @RequestParam String password) {

        JsonObject response = new JsonObject();
        CustomerUser customerUser = (CustomerUser) this.sqlSession.selectOne("getUserByEmail", username);
        log.info(customerUser.toString());
        log.info(password);
        log.info(passwordEncoder.encode(password));
        if(passwordEncoder.matches(password,customerUser.getPassCurr())){
            response.addProperty("status","success");
            response.addProperty("message","Access Granted");
            response.addProperty("id",customerUser.getCustomerUserId());
        }else{
            response.addProperty("message","Unauthorized");
            response.addProperty("status","error");
        }

        return response.toString();
    }

    @GetMapping("/getAllCompanyUsersBylLicenseCompanyId")
    @ResponseBody
        public String getAllCompanyUsersBylLicenseCompanyId(@RequestParam int licenseCompanyId) {
        Response response = new Response();
        try {
            List users = this.sqlSession.selectList("getAllCompanyUsersBylLicenseCompanyId", licenseCompanyId);
            response.setStatus(Status.SUCCESS);
            response.setData(users);
        }catch (Exception e){
            response.setStatus(Status.ERROR);
            response.setMessage(Messages.SELECT_FAIL);
            log.error(e.getMessage());
        }

        return response.toJson();
    }


    /**
     * Comprueba que el token pertenezca a la companyId y al producto.
     * @param token
     * @param companyId
     * @param productName
     * @return true si pertenece, falso en otro caso
     */
    @GetMapping("/checkToken")
    @ResponseBody
    public String checkToken(@RequestParam String token, @RequestParam String companyId, @RequestParam String productName) {
    	Response response = new Response();
    	
    	HashMap<String, Object> options = new HashMap<String, Object>();
    	options.put("cdkey", token);
    	options.put("company_id", companyId);
    	options.put("product_name", productName);
    	
    	LicenseCompany license = (LicenseCompany) this.sqlSession.selectOne("getLicenseWithToken", options);
    	
    	response.setData(license != null); 
    	
    	return response.toJson();
    }
    //GET PBM ,POST en DVU | EXCEPCION
    @RequestMapping("/getCompanyByUserEmail")
    @ResponseBody
    public String getCompanyByUserEmail(@RequestParam String privToken, @RequestParam String userEmail) {
    	Response response = new Response();
    	
    	if(privToken.compareTo(this.privToken) != 0) {
    		response.setStatus("error");
    		response.setMessage("You dont have authorization to access this resource");
    		return response.toJson();
    	}
    	
    	CustomerCompany company = (CustomerCompany) this.sqlSession.selectOne("getCompanyByUserEmail", userEmail);
    	
    	response.setData(company); 
    	
    	return response.toJson();
    }

    //region Settings
    //GET PBM SNI, NO ESTA EN DVU, GET ENTEL
    @GetMapping("/getUserById")
    @ResponseBody
    public String getUserById(@RequestParam int customerUserId) {
        Response response = new Response();
        try {
            CustomerUser user = (CustomerUser) this.sqlSession.selectOne("getUserById", customerUserId);
            response.setStatus(Status.SUCCESS);
            response.setData(user);
        }catch (Exception e){
            response.setStatus(Status.ERROR);
        }
        return response.toJson();
    }
    //GET PBM SNI, NO ESTA EN DVU, GET ENTEL
    @GetMapping("/getAllCountries")
    @ResponseBody
    public String getAllCountries() {
        return ControllerCommons.simpleSelectListResponse(sqlSession,"getAllCountries");
    }

    //GET PBM SNI , NO ESTA EN DVU, GET ENTEL
    @GetMapping("/getAllLanguages")
    @ResponseBody
    public String getAllLanguages() {
        return ControllerCommons.simpleSelectListResponse(sqlSession,"getAllLanguages");
    }



    //endregion
}
