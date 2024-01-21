package com.royli.websocketchat.pojo;

import lombok.Data;

/**
 * @Description: 接受登入請求的數據
 */
@Data
public class User {
    private String userId;
    private String username;
    private String password;
}
