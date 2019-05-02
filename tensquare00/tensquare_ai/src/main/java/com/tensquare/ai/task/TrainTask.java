package com.tensquare.ai.task;

import com.tensquare.ai.service.CnnService;
import com.tensquare.ai.service.Word2VecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.tensquare.ai.service.WordMergeService;

/**
 * 人工智能训练任务
 */
@Component
public class TrainTask {

    @Autowired
    private WordMergeService wordMergeService;

    @Autowired
    private Word2VecService word2VecService;

    @Autowired
    private CnnService cnnService;

    /**
     * 训练
     */
    @Scheduled(cron = "0 31 12 * * ?")
    public void train(){
      /*  System.out.println("开始合并分词语料库");
        wordMergeService.merge();
        System.out.println("合并分词语料库结束");*/

       /* System.out.println("开始构建词向量模型");
        word2VecService.build();
        System.out.println("构建词向量模型结束");*/

        System.out.println("构建卷积神经网络模型");
        cnnService.build();
        System.out.println("构建卷积网络神经模型结束");
    }


}
