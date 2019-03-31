package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.Impl.UserServiceImpl;
import cn.itcast.travel.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activationServlet")
public class ActivationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");//获取激活码
        UserService service=new UserServiceImpl();
        boolean flag = service.findStatus(code);//判断用户激活状态
        String msg=null;
        if(flag){
            msg="注册成功请<a href='login.html'>登录</a>";//已经激活跳转登陆界面
        }else {
            msg="注册失败请联系管理员";
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
