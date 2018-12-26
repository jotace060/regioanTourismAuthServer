package com.dparadig.auth_server.alias;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jlabarca & ebravo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseCompany implements Serializable {

    private static final long serialVersionUID = 5432154321L;
    private Integer licenseCompanyId;
    private int customerCompanyId;
    private int licenseTypeId;
    private int payElementId;
    private int customerUserId;
    private String instanceName;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime modificationDate = LocalDateTime.now();
    private LocalDateTime lastPayDate;
    private LocalDateTime exprDate;
    private String cdkey;
}