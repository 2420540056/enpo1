package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.Impl.UserServiceImpl;
import cn.itcast.travel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        String checkcode = request.getParameter("check");
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        //校验验证码
        HttpSession session = request.getSession();
        String checkcodeServer = (String) session.getAttribute("CHECKCODE_SERVER");

        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只用一次
        if (!checkcode.equalsIgnoreCase(checkcodeServer) || checkcode == null) {
            //验证码错误
            //返回错误信息
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg( "验证码错误");

            //将存储错误信息的对象转为json返回给客户端页面
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;//结束查询
        }
        //验证码正确 校验用户名和密码
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);//将查询到的结果存入user对象
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        //调用service层方法将user对象存入数据库
        boolean flag = userService.register(user);
        if(flag){
            resultInfo.setFlag(true);
            resultInfo.setErrorMsg("注册成功");
            String json = mapper.writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
           // request.getRequestDispatcher("register_ok.html").forward(request,response);
        }else {
            resultInfo.setErrorMsg("注册失败;该用户已存在");
            resultInfo.setFlag(false);
            String json = mapper.writeValueAsString(resultInfo);

            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
