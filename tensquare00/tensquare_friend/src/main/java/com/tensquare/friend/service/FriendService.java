package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.pojo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 添加haoy
     * @param userid
     * @param friendid
     * @return
     */
    @Transactional
    public int addFriend(String userid,String friendid){
        //判断是否已经添加了这个好友，如果是，则不进行操作，返回0
        if (friendDao.selectCount(userid,friendid)>0){
            return 0;
        }

        //添加好友记录
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");

        friendDao.save(friend);

        //判断对方是否添加你，如果是，islike设置为1
        if (friendDao.selectCount(friendid,userid)>0){
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }

        //远程更新用户的关注数和粉丝数
        //关注数
        userClient.updateFollowcount(userid,1);
        //粉丝数
        userClient.updateFanswcount(friendid,1);

        return 1;
    }


    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid, String friendid) {
        friendDao.deleteFriend(userid,friendid);

        //更改对方的islike
        friendDao.updateLike(friendid,userid,"0");

        //更新关注数和粉丝数
        userClient.updateFollowcount(userid,-1);
        userClient.updateFanswcount(friendid,-1);
    }
}
