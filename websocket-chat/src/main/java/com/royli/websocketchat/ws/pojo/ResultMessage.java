package com.royli.websocketchat.ws.pojo;

import lombok.Data;

/**
 * @Description: 服務器發送給瀏覽器的websocket數據
 */
@Data
public class ResultMessage {
    private boolean isSystem;
    private String fromName;
    //如果系統消息是數組
    private Object message;
}
