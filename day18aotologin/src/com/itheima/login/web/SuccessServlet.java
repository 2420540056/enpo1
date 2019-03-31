package com.itheima.login.web;

import com.itheima.login.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/SuccessServlet")
public class SuccessServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");

        User existUser = (User) request.getAttribute("existUser");
        String nickName = existUser.getNickname();
        response.getWriter().write("欢迎您:"+nickName);

        String agent = "IE";
        String header = request.getHeader("User-Agent");
        if(header.contains("Chrome")){
            agent = "Chrome";
        }else if(header.contains("Firefox")){
            agent = "Firefox";
        }

        String referer = request.getHeader("Referer");
        response.getWriter().write("您是通过"+agent+"从"+referer+"登陆成功！");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
