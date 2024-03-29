package com.jiawa.train.business.service;

import com.jiawa.train.business.domain.DailyTrainSeat;
import com.jiawa.train.business.domain.DailyTrainTicket;
import com.jiawa.train.business.feign.MemberFeign;
import com.jiawa.train.business.mapper.DailyTrainSeatMapper;
import com.jiawa.train.business.mapper.cust.DailyTrainTicketCustMapper;
import com.jiawa.train.business.req.ConfirmOrderTicketReq;
import com.jiawa.train.common.context.LoginMemberContext;
import com.jiawa.train.common.req.TicketReq;
import com.jiawa.train.common.resp.AxiosResult;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AfterConfirmOrderService {

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private DailyTrainTicketCustMapper dailyTrainTicketCustMapper;

    @Resource
    private MemberFeign memberFeign;

    private static final Logger Log = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    /**
     * 将选好的座位售票信息进行更新
     *
     * @param finalSeatList 选好的座位列表
     */
    @Transactional
    public void afterDoConfirm(
            DailyTrainTicket dailyTrainTicket,
            List<DailyTrainSeat> finalSeatList,
            List<ConfirmOrderTicketReq> tickets
    ) {
        System.out.println(dailyTrainTicket);
        for (int j = 0; j < finalSeatList.size(); j++) {
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            DailyTrainSeat dailyTrainSeat = finalSeatList.get(j);

            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setUpdateTime(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);

            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            char[] charArray = seatForUpdate.getSell().toCharArray();
            Integer maxStartIndex = endIndex - 1;
            Integer minEndIndex = startIndex + 1;

//            最小的开始区间
            Integer minStartIndex = 0;
            for (int i = startIndex - 1; i >= 0; i--) {
                char aChar = charArray[i];
                if (aChar == '1') {
                    minStartIndex = i + 1;
                    break;
                }
            }
            //            最小的出发站，最大的出发站
            Log.info("影响到达站的区间：" + minStartIndex + "-" + maxStartIndex);

//            最大的结束区间
            Integer maxEndIndex = seatForUpdate.getSell().length();
            for (int i = endIndex; i < seatForUpdate.getSell().length(); i++) {
                char aChar = charArray[i];
                if (aChar == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
//            最小的到达站，最大的到达站
            Log.info("影响到达站的区间：" + minEndIndex + "-" + maxEndIndex);

            dailyTrainTicketCustMapper.updateCountBySell(
                    dailyTrainSeat.getDate(),
                    dailyTrainSeat.getTrainCode(),
                    dailyTrainSeat.getSeatType(),
                    minStartIndex,
                    maxStartIndex,
                    minEndIndex,
                    maxEndIndex
            );
            TicketReq memberTicketReq = new TicketReq();
            memberTicketReq.setMemberId(LoginMemberContext.getId());
            memberTicketReq.setPassengerId(tickets.get(j).getPassengerId());
            memberTicketReq.setPassengerName(tickets.get(j).getPassengerName());
            memberTicketReq.setTrainDate(dailyTrainTicket.getDate());
            memberTicketReq.setTrainCode(dailyTrainTicket.getTrainCode());
            memberTicketReq.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            memberTicketReq.setSeatRow(dailyTrainSeat.getRow());
            memberTicketReq.setSeatCol(dailyTrainSeat.getCol());
            memberTicketReq.setStartStation(dailyTrainTicket.getStart());
            memberTicketReq.setStartTime(dailyTrainTicket.getStartTime());
            memberTicketReq.setEndStation(dailyTrainTicket.getEnd());
            memberTicketReq.setEndTime(dailyTrainTicket.getEndTime());
            memberTicketReq.setSeatType(dailyTrainSeat.getSeatType());
            AxiosResult<Object> commonResp = memberFeign.save(memberTicketReq);
            Log.info("调用member接口，返回：{}", commonResp);
        }
    }
}
