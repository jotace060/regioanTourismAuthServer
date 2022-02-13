package com.regionalTourism.common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jlabarca
 */
public class Constants {
    public static final String ORIGIN = "http://localhost";
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();    public static final int SERVER_INTERNAL_ERROR = 1000;
    public static final int PARAMETER_MISSING_ERROR = 1001;
    public static final int PARAMETER_ILLEGAL_ERROR = 1002;
    public static final int RESOURCE_NOT_FOUND_ERROR = 1003;

    /**
     * Prevent instantiation
     */
    private Constants() {
    }
}