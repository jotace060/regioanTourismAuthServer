package com.regionalTourism.service;
import com.regionalTourism.alias.CustomerUser;
import com.regionalTourism.alias.Privilege;
import com.regionalTourism.alias.Role;
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
    // Timer en segundos para bloquear los accesos de las cuentas
    private final int timer = 30;
    @Autowired
    public UserService() {
    }
    public CustomerUser getUserByEmail(String email) {
        log.info("JOR - getUserByEmail: email "+email);
        if(email==null) {
            return null;
        }
        return this.sqlSession.selectOne("getUserByEmail",email);
    }
    public Integer getUserIdByEmail(String email) {
        log.info("JOR - getUserIdByEmail: email "+email);
        if(email==null) {
            return null;
        }
        return this.sqlSession.selectOne("getUserIdByEmail", email);
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
    public ResponseEntity<Integer> checkAccess(String email){
        CustomerUser user = this.sqlSession.selectOne("getUserByEmail", email);
        if (user == null) {
            return ResponseEntity.ok().body(1);
        }
        // Revisar si el usuario tiene acceso al sistema
        int user_id = this.sqlSession.selectOne("getUserIdByEmail", email);
        int validate_access = this.sqlSession.selectOne("getAccessValue", user_id);
        if (validate_access == 0) {
            int get_time = this.sqlSession.selectOne("getTimeAccess", user_id);
            if (get_time >= this.timer) {
                this.sqlSession.update("updateAccess", user_id);
                this.sqlSession.delete("deleteFailedSessions", user_id);
                return ResponseEntity.ok().body(1);
            } else {
                return ResponseEntity.ok().body(0);
            }
        } else {
            return ResponseEntity.ok().body(1);
        }
    }
    public ResponseEntity<String> configureUserAccess(String email) {
        CustomerUser user = this.sqlSession.selectOne("getUserByEmail", email);
        if (user == null) {
            return ResponseEntity.badRequest().body("El usuario no se encuentra registrado");
        }
        int user_id = this.sqlSession.selectOne("getUserIdByEmail", email);
        int sessions_failed = this.sqlSession.selectOne("getSessionsFailed", user_id);
        if (sessions_failed == 2) {
            this.sqlSession.update("denyAccess", user_id);
            return ResponseEntity.ok().body("Tu cuenta ha sido suspendida temporalmente, por favor comunícate con soporte.");
        } else {
            this.sqlSession.insert("registerNewSessionFailed", user_id);
            return ResponseEntity.ok().body("Usuario y contraseña no coinciden");
        }
    }
}