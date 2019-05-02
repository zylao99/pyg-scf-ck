package com.pyg.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pyg.mapper.TbUserMapper;
import com.pyg.pojo.TbUser;
import com.pyg.pojo.TbUserExample;
import com.pyg.pojo.TbUserExample.Criteria;
import com.pyg.user.service.UserService;
import com.pyg.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class UserServiceImpl implements UserService {

   /* @Autowired
    private TbUserMapper userMapper;

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入消息发送模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    //短信模板id
    @Value("${templateId}")
    private int templateId;

    //短信签名
    @Value("${smsSign}")
    private String smsSign;

    //短信过期时间
    @Value("${minutes}")
    private int minutes;*/


    @Autowired
    private TbUserMapper userMapper;

    //注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //注入消息发送模板对象
    @Autowired
    private JmsTemplate jmsTemplate;

    //注入签名
    @Value("${sign_name}")
    private String sign_name;

    //注入模板
    @Value("${template_code}")
    private String template_code;


    /**
     * 查询全部
     */
    @Override
    public List<TbUser> findAll() {
        return userMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }



    /**
     * 增加
     * 验证验证码是否正确
     */
    @Override
    public void add(TbUser user,String smsCode) {
        //密码加密
        String newPwd = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(newPwd);

        //创建时间
        Date date = new Date();
        user.setUpdated(date);
        user.setCreated(date);



        userMapper.insertSelective(user);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbUser user) {
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbUser findOne(String id) {
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(id);
        return userMapper.selectByExample(example).get(0);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbUser user, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        if (user != null) {
            if (user.getUsername() != null && user.getUsername().length() > 0) {
                criteria.andUsernameLike("%" + user.getUsername() + "%");
            }
            if (user.getPassword() != null && user.getPassword().length() > 0) {
                criteria.andPasswordLike("%" + user.getPassword() + "%");
            }
            if (user.getPhone() != null && user.getPhone().length() > 0) {
                criteria.andPhoneLike("%" + user.getPhone() + "%");
            }
            if (user.getEmail() != null && user.getEmail().length() > 0) {
                criteria.andEmailLike("%" + user.getEmail() + "%");
            }
            if (user.getSourceType() != null && user.getSourceType().length() > 0) {
                criteria.andSourceTypeLike("%" + user.getSourceType() + "%");
            }
            if (user.getNickName() != null && user.getNickName().length() > 0) {
                criteria.andNickNameLike("%" + user.getNickName() + "%");
            }
            if (user.getName() != null && user.getName().length() > 0) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
            if (user.getStatus() != null && user.getStatus().length() > 0) {
                criteria.andStatusLike("%" + user.getStatus() + "%");
            }
            if (user.getHeadPic() != null && user.getHeadPic().length() > 0) {
                criteria.andHeadPicLike("%" + user.getHeadPic() + "%");
            }
            if (user.getQq() != null && user.getQq().length() > 0) {
                criteria.andQqLike("%" + user.getQq() + "%");
            }
            if (user.getIsMobileCheck() != null && user.getIsMobileCheck().length() > 0) {
                criteria.andIsMobileCheckLike("%" + user.getIsMobileCheck() + "%");
            }
            if (user.getIsEmailCheck() != null && user.getIsEmailCheck().length() > 0) {
                criteria.andIsEmailCheckLike("%" + user.getIsEmailCheck() + "%");
            }
            if (user.getSex() != null && user.getSex().length() > 0) {
                criteria.andSexLike("%" + user.getSex() + "%");
            }

        }

        Page<TbUser> page = (Page<TbUser>) userMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }




    /**
     * 需求：发送消息：手机号，模板，验证码，签名，获取短信验证码
     * 参数：phone
     * 返回值：void
     * 业务步骤：
     * 1，生成6位数验证码
     * 2，把验证码存储在redis服务器
     * 3，设置过期时间
     * 4，发送消息（手机号，模板，验证码，签名） 发送给mq服务器
     */
    public void sendSms(String phone) {
       /* try {
            //1.生成6位数验证码
            String code = (long)(Math.random() * 1000000) + "";
            //把验证码存储在redis服务器
            redisTemplate.boundValueOps("smsCode" + phone).set(code);
            //设置过期时间，配置文件中配置
            redisTemplate.boundValueOps("smsCode" + phone).expire(minutes, TimeUnit.MINUTES);

            //创建Map对象，封装需要发送的数据
            Map<String,String> maps = new HashMap<>();
            maps.put("phoneNumber",phone);
            maps.put("code",code);
            maps.put("minutes",Integer.toString(minutes));
            maps.put("templateId",Integer.toString(templateId));
            //解决乱码
            String sign = new String(smsSign.getBytes("ISO-8859-1"),"utf-8");
            maps.put("smsSign",sign);

            //发送消息
            jmsTemplate.convertAndSend("sms",maps);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        try {
            // 1，生成6位数验证码
            //小于1的随机数 ： 0.23432432432432499999
            String code = (long)(Math.random()*1000000)+"";
            //2，把验证码存储在redis服务器
            redisTemplate.boundValueOps("smsCode"+phone).set(code);
            //3，设置过期时间 5分钟
            redisTemplate.boundValueOps("smsCode"+phone).expire(5, TimeUnit.MINUTES);

            //创建map对象，封装发送消息数据
            Map maps = new HashMap();
            //解决乱码
            String sname = new String(sign_name.getBytes("ISO-8859-1"),"utf-8");
            maps.put("sign_name",sname);
            maps.put("template_code",template_code);
            maps.put("code",code);
            maps.put("phone",phone);
            //4，发送消息
            jmsTemplate.convertAndSend("sms",maps);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 判断验证码是否匹配
     * @param phone
     * @param smsCode
     * @return
     */
    public boolean checkCode(String phone, String smsCode) {
        //获取redis服务器验证码
       String code =  (String)redisTemplate.boundValueOps("smsCode"+phone).get();
       if(code.equals(smsCode)){
           return  true;
       }
        return false;
    }

    /**
     * 保存个人信息
     */
    @Override
    public void savePersonalData(String name,TbUser user) {
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
        userMapper.updateByExampleSelective(user, example);
    }

    //修改新的手机号
    public void updatePhone(String loginName, String phone) {

        TbUserExample example=new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(loginName);
        List<TbUser> tbUsers = userMapper.selectByExample(example);

        if (tbUsers!=null&&tbUsers.size()>0){
            for (TbUser tbUser : tbUsers) {
                tbUser.setPhone(phone);
                userMapper.updateByPrimaryKey(tbUser);
            }
        }

    }

}
