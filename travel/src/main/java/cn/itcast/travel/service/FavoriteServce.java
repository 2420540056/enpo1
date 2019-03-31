package cn.itcast.travel.service;

public interface FavoriteServce {
    public boolean findByFavorite(String rid,int uid);
    public int updateFavorite(String rid,int uid);
}
