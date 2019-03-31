package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid,String rname) {
        //String sql = "select count(*) from tab_route where cid = ?";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null  && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }

        sql = sb.toString();
        System.out.println(sql);
        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
        //String sql = "select * from tab_route where cid = ? and rname like ?  limit ? , ?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0 && !"null".equals(rname)){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);
        System.out.println(sql);
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    @Override
    public List<Route> findMostPopu() {
        try {
            String sql = "SELECT * FROM tab_route t1,(SELECT rid,COUNT(rid) FROM tab_favorite GROUP BY rid ORDER BY  COUNT(rid) DESC LIMIT 0,4) t2 WHERE t1.rid = t2.rid";
            return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class));
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public List<Route> findRouteByCid(int cid) {
        try {
            String sql = "select * from tab_route where cid = ? limit ?";
            return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), cid, 6);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public int findTotalCountForRank(String rname, int min, int max) {
        String sql = "select count(*) from tab_route r,(select rid,count(rid) from tab_favorite group by rid having count(rid) > 10 order by count(rid) desc) temp where r.rid=temp.rid";
        StringBuilder sb = new StringBuilder(sql);
        List params = new ArrayList();
        if(!"".equals(rname)){
            sb.append(" and r.rname like ? ");
            params.add("%"+rname+"%");
        }

        if(min != 0 && max == 0){
            sb.append(" and r.price > ? ");
            params.add(min);
        }

        if(min == 0 && max != 0 ){
            sb.append(" and r.price < ? ");
            params.add(max);
        }

        if(min != 0 && max != 0 ){
            sb.append(" and r.price between ? and ? ");
            params.add(min);
            params.add(max);
        }

        sql = sb.toString();
        System.out.println(sql);
        Integer totalCount = template.queryForObject(sql, Integer.class, params.toArray());
        return totalCount;
    }

    @Override
    public List<Route> findRankByPage(String rname, int min, int max, int start, int pageSize) {
        try {
            String sql = "select * from tab_route r,(select rid,count(rid) from tab_favorite group by rid having count(rid) > 10 order by count(rid) desc) temp where r.rid=temp.rid";
            StringBuilder sb = new StringBuilder(sql);
            List params = new ArrayList();
            if(!"".equals(rname)){
                sb.append(" and r.rname like ? ");
                params.add("%"+rname+"%");
            }

            if(min != 0 && max == 0){
                sb.append(" and r.price > ? ");
                params.add(min);
            }

            if(min == 0 && max != 0 ){
                sb.append(" and r.price < ? ");
                params.add(max);
            }

            if(min != 0 && max != 0 ){
                sb.append(" and r.price between ? and ? ");
                params.add(min);
                params.add(max);
            }

            sb.append(" limit ?,?");
            params.add(start);
            params.add(pageSize);

            sql = sb.toString();
            System.out.println(sql);
            List<Route> routes = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
            return routes;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
