package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.domain.SkToken;
import com.jiawa.train.business.domain.SkTokenExample;
import com.jiawa.train.business.mapper.SkTokenMapper;
import com.jiawa.train.business.req.SkTokenQuery;
import com.jiawa.train.business.req.SkTokenReq;
import com.jiawa.train.business.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkTokenService {

    @Resource
    private SkTokenMapper skTokenMapper;

    /**
     * 新增乘车人
     *
     * @param req
     * @return
     */
    public int save(SkTokenReq req) {
        DateTime now = DateTime.now();
        SkToken skToken = BeanUtil.copyProperties(req, SkToken.class);
        int state;

        if (ObjectUtil.isNull(skToken.getId())) {
            //        获取当前登录的会员id
            skToken.setId(SnowUtil.getSnowflakeNextId());
            skToken.setCreateTime(now);
            skToken.setUpdateTime(now);
            state = skTokenMapper.insert(skToken);
        } else {
            skToken.setUpdateTime(now);
            state = skTokenMapper.updateByPrimaryKey(skToken);
        }

        return state;
    }

    public PageResp<SkTokenQueryResp> queryList(SkTokenQuery req) {
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.setOrderByClause("id desc");
        SkTokenExample.Criteria criteria = skTokenExample.createCriteria();

//        类似于limit(1,2);
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条语句执行时，会将上面一行的语句条件加入进去
        List<SkToken> skTokens = skTokenMapper.selectByExample(skTokenExample);
        List<SkTokenQueryResp> list = BeanUtil.copyToList(skTokens, SkTokenQueryResp.class);
        PageInfo<SkToken> pageInfo = new PageInfo<>(skTokens);

        PageResp pageResp = new PageResp();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        System.out.println(pageResp);
        return pageResp;
    }

    public int delete(Long id) {
        return skTokenMapper.deleteByPrimaryKey(id);
    }
}
