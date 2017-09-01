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
public class CustomerUser implements Serializable {

    private static final long serialVersionUID = 15154861L;
    private int customerUserID;
    private int customerCompanyID;
    private String email;
    private String name;
    private String idDocument;
    private int countryID;
    private String passCurr;
    private String passPrev;
    private String passPrev2;
    private Date passExprDate;
    private int validationStatus;
    private Date createDate;
    private Date modificationDate;
    private int status;
}