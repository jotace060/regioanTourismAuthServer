package com.dparadig.auth_server.alias;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jlabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUser implements Serializable {

    private static final long serialVersionUID = 15154861L;
    private int customerUserId;
    private int customerCompanyId;
    private String email;
    private String name;
    private String idDocument;
    private int countryID;
    private String passCurr;
    private String passPrev;
    private String passPrev2;
    private LocalDateTime passExprDate;
    private int validationStatus;
    private LocalDateTime createDate = LocalDateTime.now();
    private LocalDateTime modificationDate = LocalDateTime.now();
    private int status;
    private int languageListId;
    private int usedTrial = 0;
    private String roleName;
}