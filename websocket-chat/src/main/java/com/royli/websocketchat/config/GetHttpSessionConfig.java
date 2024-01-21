package com.royli.websocketchat.config;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 獲取HttpSession對象
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        //將httpSession對象保存起來
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
