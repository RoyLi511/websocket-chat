package com.royli.websocketchat.pojo;

import lombok.Data;

/**
 * @Description: 用來封裝 http 請求的響應數據
 */
@Data
public class Result {
    private boolean flag;
    private String message;
}
