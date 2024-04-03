package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.mapper.cust.SkTokenMapperCust;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.SkTokenMapper;
import com.jiawa.train.business.req.SkTokenQuery;
import com.jiawa.train.business.req.SkTokenReq;
import com.jiawa.train.business.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SkTokenService {

    @Resource
    private SkTokenMapper skTokenMapper;

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @Resource
    private DailyTrainSeatService dailyTrainSeatService;

    @Resource
    private SkTokenMapperCust skTokenMapperCust;

    @Resource
    private RedisTemplate redisTemplate;


    private static final Logger Log = LoggerFactory.getLogger(SkTokenService.class);


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

    /**
     * 新增令牌
     * @param date
     * @param trainCode
     */
    public void genDaily(Date date, String trainCode) {
        Log.info("删除日期[{}]车次[{}]的令牌记录", DateUtil.formatDate(date), trainCode);
        Log.info("开始生成日期：{}车次{}的车站信息", date, trainCode);
//        删除某日某车次的车站信息
        SkTokenExample skTokenExample = new SkTokenExample();
        skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
        skTokenMapper.deleteByExample(skTokenExample);


        //            生成该车次的数据
        DateTime now = DateTime.now();
        SkToken skToken = new SkToken();
        skToken.setTrainCode(trainCode);
        skToken.setId(SnowUtil.getSnowflakeNextId());
        skToken.setCreateTime(now);
        skToken.setUpdateTime(now);
        skToken.setDate(date);

        int countSeat = dailyTrainSeatService.countSeat(date, trainCode);
        Log.info("车次[{}]，座位数[{}]", trainCode, countSeat);

        long stationCount = dailyTrainStationService.countByTrainCode(date, trainCode);
        Log.info("车次[{}]，到站数[{}]", trainCode, stationCount);
//        获取最多能卖的票，按照3/4的比例卖
        int count = (int) (countSeat * stationCount * 3 / 4);

        skToken.setCount(count);
        skTokenMapper.insert(skToken);
    }

    /**
     * 获取符合返回值，为0则表示没有修改
     * @param date
     * @param trainCode
     * @return
     */
    public boolean validSkToken(Date date, String trainCode,Long memberId) {
        String lockKey = DateUtil.formatDate(date)+"-"+trainCode+"-"+memberId;
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 5, TimeUnit.SECONDS);
        if(Boolean.TRUE.equals(setIfAbsent)){
            Log.info("抢到令牌锁{}",lockKey);
        }else {
            Log.info("没抢到令牌锁{}",lockKey);
            return false;
        }
        int decrease = skTokenMapperCust.decrease(date, trainCode);
        if(decrease>0){
            return true;
        }else{
            return false;
        }
    }
}
