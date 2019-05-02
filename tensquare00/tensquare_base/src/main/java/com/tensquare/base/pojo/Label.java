package com.tensquare.base.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 标签
 */
@Entity
@Table(name = "tb_label")
public class Label implements Serializable {
    @Id
    private String id; //标签ID（主键）
    private String labelname;//标签名称
    private String state;//状态 0：无效  1：有效
    private Integer count;//数量
    private String recommend;//是否推荐 0：不推荐  1：推荐
    private Integer fans;//粉丝数

    public Label() {
    }

    public Label(String id, String labelname, String state, Integer count, String recommend, Integer fans) {
        this.id = id;
        this.labelname = labelname;
        this.state = state;
        this.count = count;
        this.recommend = recommend;
        this.fans = fans;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabelname() {
        return labelname;
    }

    public void setLabelname(String labelname) {
        this.labelname = labelname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }
}
