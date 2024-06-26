package com.jiawa.train.common.controller;
import com.jiawa.train.common.exception.BusinessException;
import com.jiawa.train.common.resp.AxiosResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 所有异常统一处理
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AxiosResult exceptionHandler(Exception e) {

        AxiosResult commonResp = new AxiosResult();
        LOG.error("系统异常：", e);
        commonResp.setCode(201);
        commonResp.setMessage("系统出现异常，请联系管理员");
        return commonResp;
    }

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public AxiosResult exceptionHandler(BusinessException e) {
        AxiosResult commonResp = new AxiosResult();
//        括号为占位符
        LOG.error("业务异常：{}", e.getE().getDesc());
        commonResp.setCode(201);
        commonResp.setMessage(e.getE().getDesc());
        return commonResp;
    }

    /**
     * 业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public AxiosResult exceptionHandler(BindException e) {
        AxiosResult commonResp = new AxiosResult();
        String defaultMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        括号为占位符
        LOG.error("校验异常异常：{}", defaultMessage);
        commonResp.setCode(201);
        commonResp.setMessage(defaultMessage);
        return commonResp;
    }
}
