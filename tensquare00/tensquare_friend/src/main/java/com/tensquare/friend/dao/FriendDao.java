package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend,String> {

    /**
     * 是否已经添加好友
     * @param userid
     * @param friendid
     * @return
     */
    @Query("select count(f) from Friend f where f.userid = ?1 and f.friendid = ?2")
    int selectCount(String userid, String friendid);

    /**
     * 更新 islike
     * @param friendid
     * @param s
     */
    @Modifying
    @Query("update Friend f set f.islike = ?3 where f.userid = ?1 and f.friendid = ?2 ")
    void updateLike(String userid , String friendid, String s);


    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Modifying
    @Query("delete from Friend f where f.userid = ?1 and f.friendid = ?2")
    void deleteFriend(String userid, String friendid);
}
