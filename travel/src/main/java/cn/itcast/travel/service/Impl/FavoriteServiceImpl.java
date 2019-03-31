package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.util.BeanFactory;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {

    private FavoriteDao favoriteDao = (FavoriteDao) BeanFactory.getBean("favoriteDao");

    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;//如果对象有值，则为true，反之，则为false
    }

    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }

    @Override
    public PageBean<Route> pageQuery(int uid, int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<Route>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        int totalCount = favoriteDao.findCountByUid(uid);
        pb.setTotalCount(totalCount);

        int totalPage = (totalCount - 1 + pageSize)/pageSize;

        pb.setTotalPage(totalPage);
        int start = (currentPage -1) * pageSize;
        List<Route> list = favoriteDao.findByPage(uid, start, pageSize);
        pb.setList(list);

        return  pb;
    }
}
