package com.dparadig.auth_server.common;
import com.google.gson.Gson;

/**
 * @author jlabarca
 */
public class Constants {
    public static final String ORIGIN = "http://localhost";
    public static final Gson GSON = new Gson();
    public static final int SERVER_INTERNAL_ERROR = 1000;
    public static final int PARAMETER_MISSING_ERROR = 1001;
    public static final int PARAMETER_ILLEGAL_ERROR = 1002;
    public static final int RESOURCE_NOT_FOUND_ERROR = 1003;

    /**
     * Prevent instantiation
     */
    private Constants() {
    }
}