package com.dparadig.auth_server.alias;

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
public class ProductOption implements Serializable {

    private static final long serialVersionUID = 54123154321L;
    private Integer productOptionId;
    private Integer productId;
    private String name;
    private String optionType;
    private String description;
    private String optionDefault;
}