package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.util.BeanFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = (RouteService) BeanFactory.getBean("routeService");
    private FavoriteService favoriteService = (FavoriteService) BeanFactory.getBean("favoriteService");// 把对象的创建权反转给了工厂类

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接受参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");

        //接受rname 线路名称
        String rname = request.getParameter("rname");
        if(rname != null){
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }

        int cid = 0;//类别id
        //2.处理参数
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 1;//当前页码，如果不传递，则默认为第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }

        int pageSize = 5;//每页显示条数，如果不传递，默认每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        //3. 调用service查询PageBean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);

        //4. 将pageBean对象序列化为json，返回
        writeValue(pb, response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收id
        String rid = request.getParameter("rid");
        //2.调用service查询route对象(信息完整的route对象)
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登录用户是否收藏过该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路id
        String rid = request.getParameter("rid");

        //2. 获取当前登录的用户 user
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            uid = 0;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //4. 写回客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. 获取线路rid
        String rid = request.getParameter("rid");
        //2. 获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id
        if (user == null) {
            //用户尚未登录
            return;
        } else {
            //用户已经登录
            uid = user.getUid();
        }

        //3. 调用service添加
        favoriteService.add(rid, uid);

    }

    /**
     * 查询我的收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQueryMyFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int uid = 0;
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            uid = user.getUid();
        }

        String currentPage_str = request.getParameter("currentPage");
        int currentPage = 1;
        if (currentPage_str != null && currentPage_str.length() > 0 && !"null".equals(currentPage_str)) {
            currentPage = Integer.parseInt(currentPage_str);
        }

        String pageSize_str = request.getParameter("pageSize");
        int pageSize = 12;
        if (pageSize_str != null && pageSize_str.length() > 0 && !"null".equals(pageSize_str)) {
            pageSize = Integer.parseInt(pageSize_str);
        }

        PageBean<Route> pb = favoriteService.pageQuery(uid, currentPage, pageSize);
        writeValue(pb, response);

    }

    /**
     *查询人气旅游
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findMostPopu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Route> list = routeService.findMostPopu();
        writeValue(list, response);
    }

    /**
     * 根据分类id查询旅游线路（首页的国内游和出境游）
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteByCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cid = 0;
        String cid_str = request.getParameter("cid");
        if (cid_str != null && cid_str.length() > 0) {
            cid = Integer.parseInt(cid_str);
        }
        List<Route> list = routeService.findRouteByCid(cid);
        writeValue(list, response);
    }

    /**
     * 收藏排行榜
     */
    public void findFavoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String rname = request.getParameter("rname");
        if (rname != null && rname.length()>0 && !"undefined".equals(rname)){
            if ("get".equals(request.getMethod())){
                rname = new String(rname.getBytes("ISO-8859-1"), "UTF-8");
            }
        }else {
            rname = "";
        }
        
        int min = 0;
        String min_str = request.getParameter("min");
        if (min_str != null && min_str.length()>0 ){
            min = Integer.parseInt(min_str);
        }

        int max = 0;
        String max_str = request.getParameter("max");
        if (max_str != null && max_str.length()>0){
            max = Integer.parseInt(max_str);
        }

        String currentPage_str = request.getParameter("currentPage");
        int currentPage = 1;
        if (currentPage_str != null && currentPage_str.length() > 0 && !"null".equals(currentPage_str)) {
            currentPage = Integer.parseInt(currentPage_str);
        }

        String pageSize_str = request.getParameter("pageSize");
        int pageSize = 8;
        if (pageSize_str != null && pageSize_str.length() > 0 && !"null".equals(pageSize_str)) {
            pageSize = Integer.parseInt(pageSize_str);
        }

        PageBean<Route> pb = routeService.findFavoriteRank(currentPage, pageSize, rname, min, max);
        writeValue(pb, response);

    }
}
