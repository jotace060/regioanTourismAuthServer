package com.regionalTourism.alias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author JLabarca
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private int customerUserId;
    private String token;
    private LocalDateTime exprDate;
    private int tokenType;

}
