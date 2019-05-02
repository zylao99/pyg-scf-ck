package com.pyg.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.manager.service.BrandService;
import com.pyg.manager.service.SpecificationService;
import com.pyg.pojo.TbSpecification;
import com.pyg.pojo.TbSpecification;
import com.pyg.utils.PageResult;
import com.pyg.utils.PygResult;
import com.pyg.vo.Specification;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by on 2018/8/10.
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

    @Reference(timeout = 1000000)
    private SpecificationService specificationService;

   

    /**
     * 需求：规格分页查询展示
     * 参数：Integer page,Integer rows
     * 返回值：分页包装类对象
     */
    @RequestMapping("findByPage/{page}/{rows}")
    public PageResult findByPage(@PathVariable Integer page, @PathVariable Integer rows) {
        //调用远程服务对象方法
        PageResult result = specificationService.findByPage(page, rows);

        return result;
    }

    /**
     * 需求：实现规格数据添加
     * 参数：Specification
     * 返回值：
     * 成功
     * 失败
     * 注意：
     * 前台页面是angularJS负责传递参数，由于angularJS传递的参数都是json格式
     * 因此在后台接受参数时候，必须使用注解@RequestBody
     * @RequestBody自动json格式数据转换成pojo对象
     * Specification = {tbSpecification:{},specificationOptionList:[]}
     */
    @RequestMapping("add")
    public PygResult add(@RequestBody Specification specification) {
        try {
            //调用远程service服务对象方法
            specificationService.insert(specification);
            //保存成功
            return new PygResult(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            //保存失败
            return new PygResult(false, "保存失败");
        }
    }


    /**
     * 需求：根据id查询品牌数据
     */
    @RequestMapping("findOne/{id}")
    public Specification findOne(@PathVariable Long id){
        Specification specification = specificationService.findOne(id);
        return specification;
    }

    /**
     * 需求：更新品牌数据
     */
    @RequestMapping("update")
    public PygResult update(@RequestBody Specification specification){
        try {
            //调用远程service服务方法
           specificationService.update(specification);
            //成功
            return new PygResult(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false,"修改失败");
        }
    }


    /**
     * 需求：删除品牌数据
     */
    @RequestMapping("delete/{ids}")
    public PygResult delete(@PathVariable Long[] ids){
        try {
            //调用远程service服务方法
            specificationService.delete(ids);
            //成功
            return new PygResult(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new PygResult(false,"删除失败");
        }
    }

    /**
     * 定义方法，实现规格下拉列表，实现规格多项选择
     */
    @RequestMapping("findSpecList")
    public List<Map> findSpecList(){
        List<Map> specList = specificationService.findSpecList();
        return  specList;
    }

}
