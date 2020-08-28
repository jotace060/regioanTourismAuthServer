package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jlabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 1512254861L;
    private int roleId;
    private int customerCompanyId;
    private String name;
    private String productName;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime modificationDate = LocalDateTime.now();
    private int status;

}