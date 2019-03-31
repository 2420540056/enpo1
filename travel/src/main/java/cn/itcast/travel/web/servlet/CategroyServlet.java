package cn.itcast.travel.web.servlet;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.Impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategroyService;
import cn.itcast.travel.service.Impl.CategroyServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/categroy/*")
public class CategroyServlet extends BaseServlet {
   private CategroyService categroyService=new CategroyServiceImpl();
   public void findAll(HttpServletRequest request,HttpServletResponse response){
       List<Category> all = categroyService.findAll();

       wirteValue(all,response);
   }
}
