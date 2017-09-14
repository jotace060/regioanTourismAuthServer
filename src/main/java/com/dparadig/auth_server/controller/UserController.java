package com.dparadig.auth_server.controller;

import com.dparadig.auth_server.alias.CustomerCompany;
import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.alias.Token;
import com.dparadig.auth_server.common.Constants;
import com.dparadig.auth_server.common.TokenType;
import com.dparadig.auth_server.service.EmailService;
import com.google.gson.JsonObject;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Jlabarca
 */
@Controller
@CommonsLog
@CrossOrigin(origins = Constants.ORIGIN)
public class UserController {

    @Autowired
    private EmailService emailService;
    @Value("${frontend.url}")
    private String frontendURL;
    private final SqlSession sqlSession;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public UserController(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @RequestMapping("/getAllUser")
    @ResponseBody
    public String getAllUser() {
        return Constants.GSON.toJson(this.sqlSession.selectList("getAllUser"));
    }

    @RequestMapping("/insertUser")
    @ResponseBody
    public String insertUser(String name, String email, String companyName, String passCurr) {
        JsonObject response = new JsonObject();

        CustomerCompany customerCompany = new CustomerCompany();
        customerCompany.setCompanyName(companyName);
        this.sqlSession.insert("insertCompanyName",customerCompany);
        log.info("Inserted Company with ID: "+customerCompany.getCustomerCompantId());
        CustomerUser customerUser = new CustomerUser();
        customerUser.setName(name);
        customerUser.setEmail(email);
        customerUser.setPassCurr(passwordEncoder.encode(passCurr));
        customerUser.setCustomerCompanyId(customerCompany.getCustomerCompantId());

        try {
            this.sqlSession.insert("insertUser",customerUser);
            response.add("data",Constants.GSON.toJsonTree(customerUser));
            response.addProperty("status", "success");
            response.addProperty("message", "Registrado Exitosamente");
        } catch (final DuplicateKeyException e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Correo ya registrado");
        } catch (final Exception e) {
            response.addProperty("status", "error");
            response.addProperty("message", "Error Interno");
        }

        log.info("Inserted User with ID: "+customerUser.getCustomerUserId());
        createConfirmationTokenAndSendEmail(customerUser);


        return response.toString();
    }

    private Token createToken(CustomerUser customerUser,int duration, int type) {
        return new Token(customerUser.getCustomerUserId(),UUID.randomUUID().toString(),
                LocalDateTime.now().plusHours(duration),type);
    }

    private void createConfirmationTokenAndSendEmail(CustomerUser customerUser){
        Token token = createToken(customerUser,3, TokenType.CONFIRMATION);
        log.info("Email confirmation token created: "+customerUser.getCustomerUserId());
        this.sqlSession.insert("insertToken",token);
        new Thread(() -> {
            sendConfirmationEmail(customerUser,token);
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
    /*
        FALTA: Cambiar busqueda de token por columna token, a userID + token
     */
    @RequestMapping("/newPassword")
    @ResponseBody
    private String newPassword(String t, String password){
        JsonObject response = new JsonObject();
        log.info("Token: "+t);
        Token token = this.sqlSession.selectOne("getTokenByToken", t);
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
                    CustomerUser customerUser = this.sqlSession.selectOne("getUserById",token.getCustomerUserId());
                    //Check new pass is not repeat
                    if(!passwordEncoder.matches(password,customerUser.getPassCurr())&&
                        !passwordEncoder.matches(password,customerUser.getPassPrev())&&
                        !passwordEncoder.matches(password,customerUser.getPassPrev2())){
                        response.addProperty("message","You can't use old passwords");
                        log.info("Old password for : "+token.getTokenType());
                        response.addProperty("status","error");
                    }else{
                        customerUser.setPassCurr(passwordEncoder.encode(password));
                        this.sqlSession.update("updateUserPass",customerUser);
                        log.info("Password updated for user "+token.getCustomerUserId());
                        response.addProperty("message","Your password has been successfully changed!");
                        response.addProperty("status","success");
                        this.sqlSession.delete("deleteToken",token);
                    }
                }else{
                    response.addProperty("message","Invalid Key");
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

    @RequestMapping("/passReset")
    @ResponseBody
    private String passReset(String email){
        JsonObject response = new JsonObject();
        CustomerUser customerUser = this.sqlSession.selectOne("getUserByEmail",email);
        if(customerUser != null){
            Token token = createToken(customerUser,3, TokenType.PASS_RESET);
            log.info("Pass reset token created: "+customerUser.getCustomerUserId());
            this.sqlSession.insert("insertToken",token);
            new Thread(() -> {
                sendPassResetEmail(customerUser,token);
            }).start();
            response.addProperty("message","We sent a reset pass url to "+email+". You may need to check your Junk or Spam folder.");
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

    @RequestMapping("/confirmEmail")
    @ResponseBody
    public String confirmEmail(String t) {
        JsonObject response = new JsonObject();
        Token token = this.sqlSession.selectOne("getTokenByToken", t);
        if(token != null){
            log.info("Token found "+token.toString());
            log.info("Token expr date "+token.getExprDate());
            log.info("Token type "+token.getTokenType());
            log.info("Token user id "+token.getCustomerUserId());
            log.info("Current date: "+LocalDateTime.now());

            int validationStatus =  sqlSession.selectOne("getUserValidationStatus", token.getCustomerUserId());
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




}
