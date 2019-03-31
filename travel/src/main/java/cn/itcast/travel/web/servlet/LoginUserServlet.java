package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.Impl.UserServiceImpl;
import cn.itcast.travel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginUserServlet")
public class LoginUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
     /*   //校验验证码
        String check = request.getParameter("check");

        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(check==null||!check.equalsIgnoreCase(checkcode_server)){
            resultInfo.setFlag(false);
            //验证码错误将错误信息存入对象中并返回错误信息
            resultInfo.setErrorMsg("验证码错误");
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }*/
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }//调用数据库查询是否存在该用户信息

        UserService userService = new UserServiceImpl();
        User login = userService.login(user);

        if (login == null) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登陆失败 账号或者密码错误");
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
        } else {
            //激活码校验成功
            if ("Y".equals(login.getStatus())) {
                resultInfo.setFlag(true);
                resultInfo.setErrorMsg("登陆成功");
                //设置自动登陆
                String autoLogin = request.getParameter("autoLogin");
                if("on".equals(autoLogin)){//创建cookie对象存入用户名和密码
                    Cookie cookie = new Cookie("autologin",login.getUsername()+"#"+login.getPassword());
                    /*System.out.println(login.getUsername()+login.getPassword());*/
                    cookie.setMaxAge(60*60*24*7);
                    response.addCookie(cookie);
                }else {
                    //如果用户取消自动登陆就清空cookie对象
                    Cookie cookie = new Cookie("autologin", null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                request.getSession().setAttribute("user", login);
                String json = mapper.writeValueAsString(resultInfo);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(json);
            } else {
                //用户没设置激活码
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("登陆失败请设置激活码");
                String json = mapper.writeValueAsString(resultInfo);
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(json);
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
