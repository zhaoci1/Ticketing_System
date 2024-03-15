package com.jiawa.train.${module}.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.${module}.domain.${Domain};
import com.jiawa.train.${module}.domain.${Domain}Example;
import com.jiawa.train.${module}.mapper.${Domain}Mapper;
import com.jiawa.train.${module}.req.${Domain}Query;
import com.jiawa.train.${module}.req.${Domain}Req;
import com.jiawa.train.${module}.resp.${Domain}QueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${Domain}Service {

    @Resource
    private ${Domain}Mapper ${domain}Mapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(${Domain}Req req) {
        DateTime now = DateTime.now();
        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);
        int state;

        if (ObjectUtil.isNull(${domain}.getId())) {
            //        获取当前登录的会员id
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(now);
            ${domain}.setUpdateTime(now);
            state = ${domain}Mapper.insert(${domain});
        } else {
            ${domain}.setUpdateTime(now);
            state = ${domain}Mapper.updateByPrimaryKey(${domain});
        }

        return state;
    }

    public PageResp<${Domain}QueryResp> queryList(${Domain}Query req) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${domain}Example.setOrderByClause("id desc");
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<${Domain}> ${domain}s = ${domain}Mapper.selectByExample(${domain}Example);
        List<${Domain}QueryResp> list = BeanUtil.copyToList(${domain}s, ${Domain}QueryResp.class);
        PageInfo<${Domain}QueryResp> pageInfo = new PageInfo<>(list);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(pageInfo.getList());
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
