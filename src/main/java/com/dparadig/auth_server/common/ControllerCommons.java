package com.dparadig.auth_server.common;

import org.apache.ibatis.session.SqlSession;

/**
 * @author JLabarca
 */
public class ControllerCommons {

    public static String simpleSelectListResponse(SqlSession sqlSession, String statement){
        Response response = new Response();
        try {
            response.setData(sqlSession.selectList(statement));
            response.setStatus(Status.SUCCESS);
        }catch (Exception e){
            response.setStatus(Status.ERROR);
        }
        return response.toJson();
    }
}
