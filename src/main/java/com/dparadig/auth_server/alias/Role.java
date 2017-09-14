package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jlabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 1512254861L;
    private int role_id;
    private int licence_type_id;
    private String name;
    private Date create_date;
    private Date modification_date;
    private int status;

}