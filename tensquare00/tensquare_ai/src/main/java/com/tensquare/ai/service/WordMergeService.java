package com.tensquare.ai.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.FileUtil;

import java.io.IOException;
import java.util.List;

/**
 * 合并分词语料库
         */
@Service
public class WordMergeService {

    @Value("${ai.dataPath}")
    private String dataPath; // 分词语料库路径

    @Value("${ai.wordLib}")
    private String wordLib;//分词语料库合并文本路径

    /**
     * 合并分词语料库
     */
    public void merge(){
        //获取分词语料库目录下的所有文件名称
        List<String> files = FileUtil.getFiles(dataPath);

        //合并所有文件名内容
        try {
            FileUtil.merge(wordLib,files);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
