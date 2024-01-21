package com.royli.websocketchat.controller;

import com.royli.websocketchat.pojo.Result;
import com.royli.websocketchat.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("user")
public class UserController {

    /**
     * 登入
     * @param user 提交的用戶數據，包含用戶名和密碼
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public Result login(User user, HttpSession session){
        Result result = new Result();

        if (user != null && "123".equals(user.getPassword())){
            result.setFlag(true);
            //將數據存儲到session對象中
            session.setAttribute("user", user.getUsername());
        } else {
            result.setFlag(false);
            result.setMessage("登入失敗");
        }
        return result;
    }
}
