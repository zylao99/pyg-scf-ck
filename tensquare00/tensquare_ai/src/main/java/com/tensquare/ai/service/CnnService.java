package com.tensquare.ai.service;

import com.tensquare.ai.util.CnnUtil;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import util.IKUtil;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Map;

/**
 * 利用卷积神经网络算法生成卷积神经网络模型
 */
@Service
public class CnnService {

    @Value("${ai.dataPath}")
    private String dataPath;// 分词语料库

    @Value("${ai.vecModel}")
    private String vecModel; //词向量文件

    @Value("${ai.cnnModel}")
    private String cnnModel;//卷积神经网络模型文件


    /**
     * 构建卷积神经网络模型
     */
    public boolean build(){
        boolean flag = false;

        //创建计算机图对象
        //算法
        ComputationGraph cg = CnnUtil.createComputationGraph(100);

        //数据（词向量模型）
        WordVectors wordVectors = WordVectorSerializer.loadStaticModel(new File(vecModel));

        //数据(分词语料库)
        String[] childPaths = {"ai","db","web"};
        DataSetIterator dataSetIterator = CnnUtil.getDataSetIterator(dataPath,childPaths,wordVectors,32,256,new Random(123456));

        //训练->学习模型
        cg.fit(dataSetIterator);

        //生成卷积神经网络模型模型
        try {
            ModelSerializer.writeModel(cg,cnnModel,true);

            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 结果预测
     */
    public Map classify(String content){
        //对content进行IK分词
        try {
            content = IKUtil.split(content," ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] childPaths = {"ai","db","web"};

        try {
            return CnnUtil.predictions(vecModel,cnnModel,dataPath,childPaths,content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
