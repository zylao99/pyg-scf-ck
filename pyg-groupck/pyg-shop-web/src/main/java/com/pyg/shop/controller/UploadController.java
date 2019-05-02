package com.pyg.shop.controller;

import com.alibaba.dubbo.common.json.JSON;
import com.pyg.utils.FastDFSClient;
import com.pyg.utils.PygResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by on 2018/8/19.
 */
@RestController
@RequestMapping("/upload")
public class UploadController {



    //注入图片服务器地址
    @Value("${fast_dfs_url}")
    private String fast_dfs_url;


    /**
     * 需求：商家系统进行商品录入，上传商品图片
     */
    @RequestMapping("pic")
    public PygResult uploadPic(MultipartFile file) {


        try {
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //获取文件扩展名
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //创建fastdfs文件上传工具类对象
            FastDFSClient fdfs = new FastDFSClient("classpath:conf/client.conf");
            //上传图片
            //上传成功：返回图片存储地址
            //group1/M00/00/00/wKhCQ1qSGhiAddXMAAX2TtXI2Rs649.jpg
            String url = fdfs.uploadFile(file.getBytes(), extName);

            //组装图片绝对对象
            url = fast_dfs_url+url;
            //上传成功
            return new PygResult(true,url);

        } catch (Exception e) {
            e.printStackTrace();
            //上传失败
            return  new PygResult(false,"上传失败");
        }


    }

}
