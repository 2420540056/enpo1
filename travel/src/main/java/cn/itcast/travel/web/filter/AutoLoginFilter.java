package cn.itcast.travel.web.filter;


import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.CookieUtils;

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
        // 去session中判断是否user对象
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null){// 用户没关闭浏览器
            chain.doFilter(request,resp);
        }else{
            Cookie[] cookies = request.getCookies();
            Cookie cookie = CookieUtils.findCookie(cookies, "autoLogin");
            if (cookie == null){
                // 用户没有勾选自动登陆
                chain.doFilter(request, resp);
            }else {
                String value = cookie.getValue(); // zhangsan#123
                String[] user_pwd = value.split("#");
                String username = user_pwd[0];
                String password = user_pwd[1];

                // 调用业务层查询用户名和密码（Cookie 不安全，需要一个数据完整的user对象）
                User u = new User();
                u.setUsername(username);
                u.setPassword(password);

                UserService userService = new UserServiceImpl();
                user = userService.login(u);
                if (user != null) {
                    // 查询到了数据，登陆成功，将user对象保存到session中
                    session.setAttribute("user",user);
                    chain.doFilter(request, resp);
                } else{
                    // cookie已经被篡改了，登陆失败
                    chain.doFilter(request, resp);
                }

            }
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
