/*getUserByEmail*/
SELECT * FROM customer_user WHERE email = #{email} AND status = 1
 
/*getUserIdByEmail*/
SELECT cu.customer_user_id FROM customer_user cu WHERE cu.email = #{email} AND cu.status = 1
 
/*getAccessValue*/
SELECT validate_access  FROM customer_user cu WHERE customer_user_id = #{id}

/*getTimeAccess*/
 SELECT TIMESTAMPDIFF(SECOND, session_attempt_datetime, NOW())
        FROM user_sessions us
        WHERE customer_user_id = #{id}
        ORDER BY session_id
        DESC LIMIT 1
        
/*updateAccess*/
UPDATE customer_user
        SET validate_access = 1
        WHERE customer_user_id = #{id}
        
/*deleteFailedSessions*/
DELETE FROM user_sessions WHERE customer_user_id = #{id}

/*denyAccess*/
UPDATE customer_user
        SET validate_access = 0
        WHERE customer_user_id = #{id}
        
/*registerNewSessionFailed*/
INSERT INTO user_sessions (customer_user_id) VALUES (#{id})