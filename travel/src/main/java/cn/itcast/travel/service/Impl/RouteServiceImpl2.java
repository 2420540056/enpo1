package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.util.BeanFactory;

import java.util.List;

public class RouteServiceImpl2 implements RouteService {
    private RouteDao routeDao = (RouteDao) BeanFactory.getBean("routeDao");

    private RouteImgDao routeImgDao = (RouteImgDao) BeanFactory.getBean("routeImgDao");

    private SellerDao sellerDao = (SellerDao) BeanFactory.getBean("sellerDao");

    private FavoriteDao favoriteDao = (FavoriteDao) BeanFactory.getBean("favoriteDao");

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();
        //设置当前页码
        pb.setCurrentPage(currentPage);
        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pb.setTotalCount(totalCount);
        //设置当前页显示的数据集合
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname);
        pb.setList(list);

        //设置总页数 = 总记录数/每页显示条数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        pb.setTotalPage(totalPage);

        return pb;
    }

    @Override
    public Route findOne(String rid) {
        //1.根据id去route表中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2.根据route的id 查询图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //2.2将集合设置到route对象
        route.setRouteImgList(routeImgList);
        //3.根据route的sid（商家id）查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);

        //4. 查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);

        return route;
    }

    @Override
    public List<Route> findMostPopu() {
        return routeDao.findMostPopu();
    }

    @Override
    public List<Route> findRouteByCid(int cid) {
        return routeDao.findRouteByCid(cid);
    }

    @Override
    public PageBean<Route> findFavoriteRank(int currentPage, int pageSize, String rname, int min, int max) {
        PageBean<Route> pb = new PageBean<Route>();

        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = routeDao.findTotalCountForRank(rname, min, max);
        pb.setTotalCount(totalCount);

        int totalPage = (totalCount - 1 + pageSize) / pageSize;
        pb.setTotalPage(totalPage);

        int start = (currentPage - 1) * pageSize;
        List<Route> routes = routeDao.findRankByPage(rname, min, max, start, pageSize);
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            int count = favoriteDao.findCountByRid(route.getRid());
            route.setCount(count);
        }
        pb.setList(routes);
        return pb;
    }
}
