package cn.itcast.travel.service.Impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.Impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategroyService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategroyServiceImpl implements CategroyService {
    /**
     * 查询所有数据
     */
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        List<Category> categories =null;
        //先去redis中查询
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> category = jedis.zrangeWithScores("category", 0, -1);
        if(category==null||category.size()==0){
             categories = categoryDao.findAll();//获取数据库中的数据
            for (Category category1 : categories) {//将数据库中查询到数据存入redis中
                jedis.zadd("category",category1.getCid(),category1.getCname());
            }
            System.out.println("shujuku");
        }else {
            System.out.println("redis");
            categories =new ArrayList<>();
            //直接从redis中获取数据
            for (Tuple tuple : category) {
                Category c=new Category();
                c.setCid((int)tuple.getScore());
                c.setCname(tuple.getElement());
                categories.add(c);
            }
        }
        jedis.close();
        return categories;
    }
}
