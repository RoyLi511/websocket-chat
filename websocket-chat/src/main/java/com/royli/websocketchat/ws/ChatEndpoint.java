package com.royli.websocketchat.ws;

import com.alibaba.fastjson2.JSON;
import com.royli.websocketchat.config.GetHttpSessionConfig;
import com.royli.websocketchat.utils.MessageUtils;
import com.royli.websocketchat.ws.pojo.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO(一句話描述該類的功能)
 */
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfig.class)
@Component
public class ChatEndpoint {

    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();

    private HttpSession httpSession;

    /**
     * 建立websocket連接後，被調用
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //1. 將session進行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.put(user, session);
        //2. 廣播消息。需要將登入的所有用戶推送給所有用戶，此時是系統消息
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(message);
    }

    public Set getFriends() {
        Set<String> set = onlineUsers.keySet();
        return set;
    }

    //常用到
    private void broadcastAllUsers(String message) {
        try {
            //遍歷Map集合
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                //獲取到所有用戶對應的session對象即可，因為這時是要給所有用戶去推消息
                Session session = entry.getValue();
                //發送消息，getBasicRemote()發送同步消息使用
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            //紀錄日誌
        }

    }

    /**
     * 瀏覽器發送消息到服務端，該方法被調用
     * <p>
     * 舉例: 張三 ---> 李四
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) {

        try {
            //將消息推送給指定的用戶
            Message msg = JSON.parseObject(message, Message.class);
            //獲取 消息接收方的用戶名
            String toName = msg.getToName();
            String mess = msg.getMessage();
            //獲取消息接收方用戶對象的session對象
            Session session = onlineUsers.get(toName);
            String user = (String) this.httpSession.getAttribute("user");
            String msg1 = MessageUtils.getMessage(false, user, mess);
            session.getBasicRemote().sendText(msg1);
        } catch (Exception e) {
            //紀錄日誌
        }
    }

    /**
     * 斷開 websocket 連接時被調用
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        //1. 從 onlineUsers 中剃除當前用戶的session對象
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.remove(user);
        //2. 通知其他所有的用戶，當前用戶下線了
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAllUsers(message);
    }
}
