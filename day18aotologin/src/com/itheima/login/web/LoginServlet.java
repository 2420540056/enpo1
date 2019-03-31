package com.itheima.login.web;

import com.itheima.login.domain.User;
import com.itheima.login.service.UserService;
import com.itheima.login.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1 解决post请求中文乱码
        request.setCharacterEncoding("utf-8");

   /*     // 校验验证码
        String checkCode_session = (String) request.getSession().getAttribute("checkCode_session");
        request.getSession().removeAttribute("checkCode_session");
        String chcekCode_input = request.getParameter("chcekcode");
        if(checkCode_session == null || !checkCode_session.equalsIgnoreCase(chcekCode_input)){
            request.setAttribute("msg_error","验证码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
*/

        //接收和封装参数
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // 调用业务层完成登陆
        UserService userService = new UserServiceImpl();
        User existUser = userService.login(user);
        if (existUser != null){ // 登陆成功
            if("on".equals(request.getParameter("remember"))){
                Cookie c = new Cookie("remember",existUser.getUsername());
                c.setMaxAge(60*60*24*7);
                response.addCookie(c);
            }else {
                Cookie c = new Cookie("remember", null);
                c.setMaxAge(0);
                response.addCookie(c);
            }

            if("on".equals(request.getParameter("autoLogin"))){
                Cookie c = new Cookie("autoLogin", existUser.getUsername()+"#"+existUser.getPassword());
                c.setMaxAge(60*60*24*7);
                response.addCookie(c);
            }else {
                Cookie c = new Cookie("autoLogin",null);
                c.setMaxAge(0);
                response.addCookie(c);
            }

            request.getSession().setAttribute("existUser", existUser);
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        }else {
            request.setAttribute("msg_error","用户名或密码错误");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
