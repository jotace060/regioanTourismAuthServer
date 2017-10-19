package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import java.io.Serializable;

/**
 * @author JLabarca
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCompany implements Serializable{

    private static final long serialVersionUID = 15154552861L;
    private int licenseCompanyId;
    private int customerCompanyId;
    private int licenseTypeId;
    private int payElementId;
    private int customerUserId;
    private String instanceName;
    private Date exprDate;
    private int autoRenew;
    private Date createDate;
    private Date modificationDate;
    private Date lastPayDate;
    private int status;
    private String cdkey;
}
