package com.yyxnb.module_music.db;

import android.arch.persistence.room.Dao;

import com.yyxnb.common_base.db.BaseDao;
import com.yyxnb.module_music.bean.MusicFavouriteBean;

/**
 * 喜欢/收藏
 */
@Dao
public interface FavouriteDao extends BaseDao<MusicFavouriteBean> {

//    @Query("SELECT * FROM favourite WHERE mid = :mid limit 1")
//    LiveData<MusicFavouriteBean> getFavouriteLive(String mid);
//
//    @Query("SELECT * FROM favourite WHERE mid = :mid  limit 1")
//    MusicFavouriteBean getFavourite(String mid);

}
