package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = " select * from tab_favorite where rid = ? and uid = ?";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {

        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";

        return template.queryForObject(sql,Integer.class,rid);

    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";

        template.update(sql,rid,new Date(),uid);
    }

    @Override
    public int findCountByUid(int uid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE uid = ?";
        return template.queryForObject(sql, Integer.class, uid);
    }

    @Override
    public List<Route> findByPage(int uid, int start, int pageSize) {
        try {
            String sql = "select * from tab_route where rid in(select rid from tab_favorite where uid =?) limit ?,?";
            List<Route> list = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
            return  list;
        } catch (DataAccessException e) {

        }
        return null;
    }
}
