package com.tensquare.ai.service;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * 词向量转换
 */
@Service
public class Word2VecService {

    @Value("${ai.vecModel}")
    private String vecModel;//词向量模型文件路径

    @Value("${ai.wordLib}")
    private String wordLib;

    /**
     * 构建词向量模型
     */
    public boolean build(){
        boolean flag = false;

        //指定分词语料库的位置
        SentenceIterator sentenceIterator = new LineSentenceIterator(new File(wordLib));

        //使用Word2Vec对象构建词向量模型
        Word2Vec word2Vec = new Word2Vec.Builder()
                //minWordFrequency: 转换的词最小出现的次数，如果词出现5次就转换成词向量
                .minWordFrequency(5)
                //iterations: 学习频率
                .iterations(1)
                //layerSize: 词向量数量
                .layerSize(100)
                .seed(40)
                .windowSize(5)
                //指定语料库的位置
                .iterate(sentenceIterator)
                .build();

        word2Vec.fit();

        //写出文件
        try {
            //删除已经存在的文件
            new File(vecModel).delete();

            WordVectorSerializer.writeWordVectors(word2Vec,vecModel);

            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
