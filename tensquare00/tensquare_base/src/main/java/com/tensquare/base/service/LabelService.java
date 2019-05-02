package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 标签业务
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加标签
     * @param label
     */
    public void add(Label label){
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    /**
     * 查询全部标签
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据ID查找标签
     * @param id
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 修改标签
     * @param label
     */
    public void update(Label label){
        labelDao.save(label);
    }

    /**
     * 根据ID 删除标签
     * @param id
     */
    public void delete(String id){
        labelDao.deleteById(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    public List<Label> search(Map searchMap){
        Specification<Label> specification = createSpecification(searchMap);
        return labelDao.findAll(specification);
    }

    /**
     * 条件+分页
     * @return
     */
    public Page<Label> search(Map searchMap, int page, int size){
        Specification<Label> specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return labelDao.findAll(specification,pageRequest);
    }

    /**
     * 生成查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String labelname = (String) searchMap.get("labelname");
                String state = (String) searchMap.get("state");
                String recommend = (String) searchMap.get("recommend");
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (labelname != null && !"".equals(labelname)){
                    predicates.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+labelname+"%"));
                }
                if (state != null && !"".equals(state)){
                    predicates.add(criteriaBuilder.equal(root.get("state").as(String.class),state));
                }
                if (recommend != null && !"".equals(recommend)){
                    predicates.add(criteriaBuilder.equal(root.get("recommend").as(String.class),recommend));
                }
                Predicate[] predicatesArray = new Predicate[predicates.size()];
                return criteriaBuilder.and(predicates.toArray(predicatesArray));
            }
        };
    }
}
