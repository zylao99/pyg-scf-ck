package com.pyg.fdfs;

import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * Created by on 2018/8/17.
 */
public class MyFastDfs {

    /**
     * 需求：测试图片上传
     */
    @Test
    public void uploadPic() throws Exception {
        //指定图片绝对地址
        String pic = "C:\\images\\timg.jpg";
        //指定客户端配置文件绝对地址
        String conf = "C:\\workspace\\heima85\\pyg-shop-web\\" +
                "src\\main\\resources\\conf\\client.conf";

        //加载客户端配置文件，连接fastdfs图片服务器
        ClientGlobal.init(conf);

        //创建tracker调度客户端对象
        TrackerClient tc = new TrackerClient();
        //从调度对象中获取连接
        TrackerServer trackerServer = tc.getConnection();

        //定义存储对象
        StorageServer storageServer = null;

        //创建存储服务器客户端对象
        StorageClient sc = new StorageClient(trackerServer, storageServer);

        //上传
        String[] urls = sc.upload_file(pic, "jpg", null);

        for (String url : urls) {

            System.out.println(url);
        }


    }

}
