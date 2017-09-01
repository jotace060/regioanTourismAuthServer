package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jlabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1512345861L;
    private int privilegeID;
    private String name;
    private String description;
    private String type;
    private String category;
    private int status;

}