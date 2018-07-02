package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.rpc.RaptorContext;
import com.ppdai.framework.raptor.rpc.RaptorRequest;
import com.ppdai.framework.raptor.rpc.RaptorResponse;
import com.ppdai.framework.raptor.rpc.RaptorServiceInterceptor;
import com.ppdai.framework.raptor.spring.utils.RaptorHandlerUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yinzuolong
 */
public class RaptorHandlerInterceptorAdapter extends HandlerInterceptorAdapter {

    private final RaptorServiceInterceptor raptorServiceInterceptor;

    public RaptorHandlerInterceptorAdapter(RaptorServiceInterceptor raptorServiceInterceptor) {
        this.raptorServiceInterceptor = raptorServiceInterceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (RaptorHandlerUtils.isRaptorService(handler)) {
            raptorServiceInterceptor.preHandle(RaptorContext.getContext().getRequest(), RaptorContext.getContext().getResponse());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (RaptorHandlerUtils.isRaptorService(handler)) {
            RaptorRequest raptorRequest = RaptorContext.getContext().getRequest();
            RaptorResponse raptorResponse = RaptorContext.getContext().getResponse();
            raptorServiceInterceptor.postHandle(raptorRequest, raptorResponse);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (RaptorHandlerUtils.isRaptorService(handler)) {
            RaptorRequest raptorRequest = RaptorContext.getContext().getRequest();
            RaptorResponse raptorResponse = RaptorContext.getContext().getResponse();
            raptorResponse.setException(ex);
            raptorServiceInterceptor.afterCompletion(raptorRequest, raptorResponse);
        }
    }
}
