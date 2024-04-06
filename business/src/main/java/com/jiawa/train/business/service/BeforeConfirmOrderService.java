package com.jiawa.train.business.service;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.esotericsoftware.minlog.Log;
import com.jiawa.train.business.domain.ConfirmOrder;
import com.jiawa.train.business.enums.ConfirmOrderStatusEnum;
import com.jiawa.train.business.enums.RocketMQTopicEnum;
import com.jiawa.train.business.mapper.ConfirmOrderMapper;
import com.jiawa.train.business.req.ConfirmOrderDoReq;
import com.jiawa.train.business.req.ConfirmOrderTicketReq;
import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.util.SnowUtil;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BeforeConfirmOrderService {



    @Resource
    private SkTokenService skTokenService;

    @Resource
    public RocketMQTemplate rocketMQTemplate;

    @Resource
    private ConfirmOrderMapper confirmOrderMapper;

    public void beforeDoConfirm(ConfirmOrderDoReq req) {
        req.setMemberId(LoginMemberContext.getId());
//        检验令牌余量
        boolean validSkToen = skTokenService.validSkToken(req.getDate(), req.getTrainCode(), LoginMemberContext.getMember().getId());
        if (validSkToen) {
            Log.info("令牌校验通过");
        } else {
            Log.info("令牌未通过");
            throw new BusinessException(BusinessExceptionEnum.CONFIRM_ORDER_SK_TOKEN_FAIL);
        }

        //        保存确认订单表，状态初始
        DateTime now = DateTime.now();
        Date date = req.getDate();
        String trainCode = req.getTrainCode();
        String start = req.getStart();
        String end = req.getEnd();
        List<ConfirmOrderTicketReq> tickets = req.getTickets();

        ConfirmOrder confirmOrder = new ConfirmOrder();
        confirmOrder.setId(SnowUtil.getSnowflakeNextId());
        confirmOrder.setMemberId(req.getMemberId());
        confirmOrder.setDate(date);
        confirmOrder.setTrainCode(trainCode);
        confirmOrder.setStart(start);
        confirmOrder.setEnd(end);
        confirmOrder.setDailyTrainTicketId(req.getDailyTrainTicketId());
        confirmOrder.setStatus(ConfirmOrderStatusEnum.INIT.getCode());
        confirmOrder.setCreateTime(now);
        confirmOrder.setUpdateTime(now);
        confirmOrder.setTickets(JSON.toJSONString(tickets));
        confirmOrderMapper.insert(confirmOrder);

        String reqJson = JSON.toJSONString(req);
        Log.info("排队购票，发送mq开始，消息：{}", reqJson);
        rocketMQTemplate.convertAndSend(RocketMQTopicEnum.CONFIRM_ORDER.getCode(),reqJson);
        Log.info("排队购票，发送mq结束");
    }
}
