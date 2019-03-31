package cn.itcast.travel.web.filter;

import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.Impl.UserServiceImpl;
import cn.itcast.travel.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class CharacterFilter implements Filter {
    public void destroy() {
    }

    ///***
// 过滤器解决中文乱码问题
// */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String method = request.getMethod();
        if (method.equalsIgnoreCase("post")) {
            request.setCharacterEncoding("utf-8");
        }
        response.setContentType("text/html;charset=utf-8");
        //自动 登陆
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Cookie[] cookies = request.getCookies();
            String username = null;
            String password = null;
            if (cookies == null || cookies.length == 0) {
                response.sendRedirect("login.html");
            } else {
                for (Cookie cookie : cookies) {//获取到用户名和密码
                    if ("autologin".equals(cookie.getName())) {
                        String value = cookie.getValue();
                        username = value.split("#")[0];
                        password = value.split("#")[1];
                    }
                }
                User autoUser = new User();
                autoUser.setUsername(username);
                autoUser.setPassword(password);//将cookie中的用户名和密码存入2user对象
                UserService service = new UserServiceImpl();
                User loginUser = service.login(autoUser);//根据user对象查询数据库中的user对象
                if (loginUser != null) {
                    session.setAttribute("user", loginUser);
                    chain.doFilter(request, response);
                } else {

                    chain.doFilter(request, response);
                }
            }
        } else {
            chain.doFilter(request, response);
        }
    }


    public void init(FilterConfig config) throws ServletException {

    }

}
