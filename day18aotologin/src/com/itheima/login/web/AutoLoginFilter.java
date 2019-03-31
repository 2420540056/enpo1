package com.itheima.login.web;

import com.itheima.login.domain.User;
import com.itheima.login.service.UserService;
import com.itheima.login.service.impl.UserServiceImpl;
import com.itheima.login.utils.CookieUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AutoLoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        User existUser = (User) session.getAttribute("existUser");
        if(existUser != null){
            chain.doFilter(request, resp);
        }else{//用户关闭了浏览器，判断cookie
            Cookie[] cookies = request.getCookies();
            Cookie cookie = CookieUtils.findCookie(cookies, "autoLogin");
            if (cookie == null) {// 用户没有勾选自动登陆，直接放行
                chain.doFilter(request, resp);
            }else {//勾选了自动登陆
                User user = new User();
                String username_password = cookie.getValue();
                String username = username_password.split("#")[0];
                String password = username_password.split("#")[1];
                user.setUsername(username);
                user.setPassword(password);

                UserService userService = new UserServiceImpl();
                existUser = userService.login(user);
                if (existUser == null){
                    //被篡改了，直接放行
                    chain.doFilter(request, resp);
                }else {
                    //保存user到session
                    session.setAttribute("existUser", existUser);
                    //放行
                    chain.doFilter(request, resp);
                }

            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
