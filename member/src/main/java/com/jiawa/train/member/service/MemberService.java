package com.jiawa.train.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.jwt.JWTUtil;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.exception.BusinessExceptionEnum;
import com.jiawa.train.common.resp.AxiosResult;
import com.jiawa.train.common.util.SnowUtil;
import com.jiawa.train.member.domain.Member;
import com.jiawa.train.member.domain.MemberExample;
import com.jiawa.train.member.mapper.MemberMapper;
import com.jiawa.train.member.req.MemberLoginReq;
import com.jiawa.train.member.req.MemberReq;
import com.jiawa.train.member.req.MemberSendCodeReq;
import com.jiawa.train.member.resp.MemberLoginResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MemberService {
    private static final Logger Log = LoggerFactory.getLogger(MemberService.class);
    @Resource
    private MemberMapper memberMapper;

    private String code;

    public int count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public AxiosResult register(MemberReq memberReq) {
        String mobile = memberReq.getMobile();
        Member memberDB = selectByMembers(mobile);

//        判断是否已注册
        if (ObjectUtil.isNotNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        return AxiosResult.success(member);
    }

    public String sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member memberDB = selectByMembers(mobile);

//        如果手机号不存在，则插入一条记录
        if (ObjectUtil.isNull(memberDB)) {
            Log.info("手机号不存在，插入一条记录");
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            Log.info("手机号存在，不插入记录");
        }

//        生成验证码
        code = RandomUtil.randomString(4);
        Log.info("生成短信验证码：{}", code);
//        保存短信记录表：手机号，短信验证码，有效期，是否已使用,业务类型，发送时间，使用时间
        Log.info("生成短信验证表");
        //        对接短信通道，发送短信
        Log.info("对接短信通道");
        return code;
    }

    public MemberLoginResp login(MemberLoginReq req) {
        Member memberDB = selectByMembers(req.getMobile());

//        如果手机号不存在，则抛出异常
        if (ObjectUtil.isNull(memberDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }
//        校验短信验证码
        if (!code.equals(req.getCode())) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }

        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(memberDB, MemberLoginResp.class);

        Map<String, Object> map = BeanUtil.beanToMap(memberLoginResp);
        String key = "Jiawa12306";
        String token = JWTUtil.createToken(map, key.getBytes());
        memberLoginResp.setToken(token);
        System.out.println(memberLoginResp);
        return memberLoginResp;
    }

    private Member selectByMembers(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> members = memberMapper.selectByExample(memberExample);

        if (CollUtil.isEmpty(members)) {
            return null;
        } else {
            return members.get(0);
        }

    }
}
