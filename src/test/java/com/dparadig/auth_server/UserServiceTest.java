package com.dparadig.auth_server;
import com.dparadig.auth_server.alias.CustomerUser;
import com.dparadig.auth_server.service.UserService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    protected final Logger log = LoggerFactory.getLogger(UserService.class);
    @Mock
    SqlSession sqlSession;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserService userServiceI;
    String email = "test_pbm@dparadig.com";
    @Test
    public void getUserByEmailNull(){
        CustomerUser customerUser  =userServiceI.getUserByEmail(null);
        log.info(String.valueOf(customerUser));
        Assert.assertEquals(null,customerUser);
    }
    @Test
    public void getUserIdByEmail(){
        Mockito.doReturn(3)
                .when(sqlSession)
                .selectOne("getUserIdByEmail", email);
        int id =userServiceI.getUserIdByEmail(email);
        log.info(String.valueOf(id));
        Assert.assertEquals(3,id);
    }
    @Test
    public void getUserIdByEmailNull(){
        Integer id =userServiceI.getUserIdByEmail(null);
        log.info(String.valueOf(id));
        Assert.assertEquals(null,id);
    }
    @Test
    public void checkAccessNullTest(){
        Mockito.doReturn(null).when(sqlSession).selectOne("getUserByEmail", null);
        ResponseEntity<Integer> responseEntity= userServiceI.checkAccess(null);
        log.info(responseEntity.toString());
        Assert.assertEquals(ResponseEntity.ok().body(1),responseEntity);
    }
    @Test
    public void checkAccessTest(){
        CustomerUser customerUser = new CustomerUser();
        customerUser.setEmail(email);
        Mockito.doReturn(customerUser)
                .when(sqlSession)
                .selectOne("getUserByEmail", email);
        Mockito.doReturn(3)
                .when(sqlSession)
                .selectOne("getUserIdByEmail", email);
        Mockito.doReturn(0)
                .when(sqlSession)
                .selectOne("getAccessValue", 3);
        Mockito.doReturn(25)
                .when(sqlSession)
                .selectOne("getTimeAccess", 3);
        ResponseEntity<Integer> responseEntity= userServiceI.checkAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(ResponseEntity.ok().body(0),responseEntity);
    }
    @Test
    public void configureUserAccess(){
        Mockito.doReturn(null)
                .when(sqlSession)
                .selectOne("getUserByEmail", null);
        ResponseEntity<String> responseEntity= userServiceI.configureUserAccess(null);
        log.info(responseEntity.toString());
        Assert.assertEquals(ResponseEntity.badRequest().body("El usuario no se encuentra registrado"),responseEntity);
    }
    @Test
    public void configureUserSuspendAccess(){
        CustomerUser customerUser = new CustomerUser();
        customerUser.setEmail(email);
        Mockito.doReturn(customerUser)
                .when(sqlSession)
                .selectOne("getUserByEmail", email);
        Mockito.doReturn(3)
                .when(sqlSession)
                .selectOne("getUserIdByEmail", email);
        Mockito.doReturn(2)
                .when(sqlSession)
                .selectOne("getSessionsFailed", 3);
        Mockito.doReturn(0)
                .when(sqlSession)
                .update("denyAccess", 3);
        ResponseEntity<String> responseEntity= userServiceI.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(ResponseEntity.ok().body("Tu cuenta ha sido suspendida temporalmente, por favor comunícate con soporte."),responseEntity);
    }
    @Test
    public void configureUserIncorrectUserOrPasswordAccess(){
        CustomerUser customerUser = new CustomerUser();
        customerUser.setEmail(email);
        Mockito.doReturn(customerUser)
                .when(sqlSession)
                .selectOne("getUserByEmail", email);
        Mockito.doReturn(3)
                .when(sqlSession)
                .selectOne("getUserIdByEmail", email);
        Mockito.doReturn(1)
                .when(sqlSession)
                .selectOne("getSessionsFailed", 3);
        Mockito.doReturn(3)
                .when(sqlSession)
                .insert("registerNewSessionFailed", 3);
        ResponseEntity<String> responseEntity= userServiceI.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(ResponseEntity.ok().body("Usuario y contraseña no coinciden"),responseEntity);
    }
    @Test
    public void checkAccessAcceptedTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .checkAccess(email);
        ResponseEntity<Integer> response= userService.checkAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void checkAccessBadRequestTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .checkAccess(email);
        ResponseEntity<Integer> response= userService.checkAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void checkAccessForbiddenTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .checkAccess(email);
        ResponseEntity<Integer> response= userService.checkAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void checkAccessInternalServerErrorTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .checkAccess(email);
        ResponseEntity<Integer> response= userService.checkAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void configureUserAccessAcceptedTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.ACCEPTED);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .configureUserAccess(email);
        ResponseEntity<String> response= userService.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void configureUserAccessBadRequestTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .configureUserAccess(email);
        ResponseEntity<String> response= userService.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void configureUserAccessForbiddenTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .configureUserAccess(email);
        ResponseEntity<String> response= userService.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
    @Test
    public void configureUserAccessInternalServerErrorTest(){
        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.doReturn(responseEntity)
                .when(userService)
                .configureUserAccess(email);
        ResponseEntity<String> response= userService.configureUserAccess(email);
        log.info(responseEntity.toString());
        Assert.assertEquals(response,responseEntity);
        Assert.assertEquals(response.getBody(),responseEntity.getBody());
    }
}