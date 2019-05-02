package com.pyg.vo;

import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * Created by on 2018/8/13.
 */
public class Specification implements Serializable{
    //规格对象
    private TbSpecification tbSpecification;
    //规格选项集合，一个规格对应多个规格选项
    private List<TbSpecificationOption> specificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<TbSpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
