package com.regionalTourism.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author jlabarca & ebravo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseOption implements Serializable {

    private static final long serialVersionUID = 54128564321L;
    private Integer licenseCompanyId;
    private Integer productOptionId;
    private Integer active;
    private String optionValue;
    private String optionDefault;
    private String name;
    private String description;
}