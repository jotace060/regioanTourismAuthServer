package com.regionalTourism.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JLabarca
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCompany {

    private static final long serialVersionUID = 51818181861L;
    private int customerCompanyId;	// TO-DO: Error de tipeo
    private String companyName;
    private int identificationNumber;
    private String area;
    private int countryId;
    private int companyParentId;
    private int validation;
    private int status;
}

