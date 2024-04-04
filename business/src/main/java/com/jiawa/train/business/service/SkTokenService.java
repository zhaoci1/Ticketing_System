package com.jiawa.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jiawa.train.business.domain.*;
import com.jiawa.train.business.enums.LockKeyPreEnum;
import com.jiawa.train.business.mapper.cust.SkTokenMapperCust;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.PageResp;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.business.mapper.SkTokenMapper;
import com.jiawa.train.business.req.SkTokenQuery;
import com.jiawa.train.business.req.SkTokenReq;
import com.jiawa.train.business.resp.SkTokenQueryResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate redisTemplate;


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
     *
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
        int count = (int) (countSeat * stationCount);

        skToken.setCount(count);
        skTokenMapper.insert(skToken);
    }

    /**
     * 获取符合返回值，为0则表示没有修改
     *
     * @param date
     * @param trainCode
     * @return
     */
    public boolean validSkToken(Date date, String trainCode, Long memberId) {
//        线程之间抢令牌锁
        String lockKey = LockKeyPreEnum.SK_TOKEN + "-" + DateUtil.formatDate(date) + "-" + trainCode + "-" + memberId;
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, lockKey, 5, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(setIfAbsent)) {
            Log.info("抢到令牌锁{}", lockKey);
        } else {
            Log.info("没抢到令牌锁{}", lockKey);
            return false;
        }

//        生成key，用于令牌存入缓存
        String skTokenCountKey = LockKeyPreEnum.SK_TOKEN_COUNT + "-" + DateUtil.formatDate(date) + "-" + trainCode;
//        获取令牌数量
        Object skTokenCount = redisTemplate.opsForValue().get(skTokenCountKey);
        if (skTokenCount != null) {
            Log.info("缓存中有该车次令牌", skTokenCountKey);
//            对缓存进行-1的操作
            Long count = redisTemplate.opsForValue().decrement(skTokenCountKey, 1);
//            判断减完之后的count是否小于0
            if (count < 0L) {
                Log.info("获取令牌失败", skTokenCountKey);
                return false;
            } else {
                Log.info("获取到令牌，令牌余数：{}", count);
                //            并发量比较大的时候，一直刷新key的持续时间
                redisTemplate.expire(skTokenCountKey, 60, TimeUnit.SECONDS);
//                每获取5个令牌就更新一次数据库
                if (count % 5 == 0) {
                    skTokenMapperCust.decrease(date, trainCode, 5);
                }
                return true;
            }
        } else {
            Log.info("缓存中没有该车次的令牌key：{}", skTokenCountKey);
            SkTokenExample skTokenExample = new SkTokenExample();
            skTokenExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode);
            List<SkToken> skTokens = skTokenMapper.selectByExample(skTokenExample);
            if (CollUtil.isEmpty(skTokens)) {
                Log.info("找不到日期：【{}】车次【{}】的令牌记录", DateUtil.formatDate(date), trainCode);
                return false;
            }
            SkToken skToken = skTokens.get(0);
            if (skToken.getCount() <= 0) {
                Log.info("找不到日期：【{}】车次【{}】的令牌余量为0", DateUtil.formatDate(date), trainCode);
                return false;
            }
//            走到这儿来表示令牌还有余量
//                    令牌余量-1
            Integer count = skToken.getCount() - 1;
            skToken.setCount(count);
            Log.info("将该车次令牌放入缓存中，key：{}，count：{}", skTokenCountKey, count);
            redisTemplate.opsForValue().set(skTokenCountKey, String.valueOf(count), 60, TimeUnit.SECONDS);
            return true;
        }

    }

}
