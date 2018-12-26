package com.dparadig.auth_server.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jlabarca & ebravo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibLicenseRequest implements Serializable {

    private static final long serialVersionUID = 7652154321L;
    private String cdkey;
    private String mac;
}