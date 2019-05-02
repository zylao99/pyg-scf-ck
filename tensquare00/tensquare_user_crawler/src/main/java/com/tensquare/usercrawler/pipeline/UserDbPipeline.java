package com.tensquare.usercrawler.pipeline;

import com.tensquare.usercrawler.dao.UserDao;
import com.tensquare.usercrawler.pojo.User;
import com.tensquare.usercrawler.util.DownloadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import utils.IdWorker;

import java.io.IOException;

/**
 * 用户入库类
 */
@Component
public class UserDbPipeline implements Pipeline{

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserDao userDao;

    @Value("${avatar.savepath}")
    private String savePath;


    @Override
    public void process(ResultItems resultItems, Task task) {
        //获取数据
        String nickname = (String) resultItems.get("nickname");
        String avatar = (String) resultItems.get("avatar");

        //保存用户数据到数据库
        User user = new User();
        user.setId(idWorker.nextId() + "");
        user.setNickname(nickname);

        //处理头像名称
        String newAvatar = avatar.substring(avatar.lastIndexOf("/") + 1);
        user.setAvatar(newAvatar);

        userDao.save(user);

        //下载头像图片内容
        try {
            DownloadUtil.download(avatar, newAvatar, savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
