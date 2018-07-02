package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.common.RaptorConstants;
import com.ppdai.framework.raptor.rpc.RaptorContext;
import com.ppdai.framework.raptor.rpc.RaptorRequest;
import com.ppdai.framework.raptor.rpc.RaptorResponse;
import com.ppdai.framework.raptor.spring.utils.RaptorInterfaceUtils;
import com.ppdai.framework.raptor.utils.NetUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class RaptorContextInitHandlerInterceptor extends HandlerInterceptorAdapter {
    public static final String NAME_HTTP_URI = "raptor-service-http-uri";
    public static final String NAME_HTTP_METHOD = "raptor-service-http-method";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initRaptorContext(request, response, handler);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    protected void initRaptorContext(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //初始化
        initRaptorContext(handler);

        //设置request头
        Map<String, String> headers = getRequestHeaders(request);
        RaptorRequest raptorRequest = RaptorContext.getContext().getRequest();
        raptorRequest.setRequestId(headers.get(RaptorConstants.HEADER_REQUEST_ID));
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            raptorRequest.setAttachment(entry.getKey(), entry.getValue());

            //传递request头
            if (entry.getKey().toLowerCase().startsWith(RaptorConstants.HEADER_TRACE_PREFIX)) {
                RaptorContext.getContext().putRequestAttachment(entry.getKey(), entry.getValue());
            }
        }
        //将http url/method 放到RaptorContext的Attribute中
        RaptorContext.getContext().putAttribute(NAME_HTTP_URI, request.getRequestURI());
        RaptorContext.getContext().putAttribute(NAME_HTTP_METHOD, request.getMethod());
    }

    protected void initRaptorContext(Object handler) {
        RaptorRequest raptorRequest = new RaptorRequest();
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String method = handlerMethod.getMethod().getName();
            String interfaceName = RaptorInterfaceUtils.getInterfaceName(ClassUtils.getUserClass(handlerMethod.getBean()), handlerMethod.getMethod());
            raptorRequest.setMethodName(method);
            raptorRequest.setInterfaceName(interfaceName);
        }

        RaptorResponse raptorResponse = new RaptorResponse();

        RaptorContext.getContext().setRequest(raptorRequest);
        RaptorContext.getContext().setResponse(raptorResponse);
    }

    protected Map<String, String> getRequestHeaders(HttpServletRequest httpRequest) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headName = headerNames.nextElement();
            String headerValue = httpRequest.getHeader(headName);
            headers.putIfAbsent(headName, headerValue);
        }
        return headers;
    }

    public static void applyResponse(HttpServletResponse response) {
        response.addHeader(RaptorConstants.HEADER_HOST_SERVER, NetUtils.getLocalIp());

        RaptorResponse raptorResponse = RaptorContext.getContext().getResponse();
        if (raptorResponse != null) {
            raptorResponse.setCode(response.getStatus());
            for (Map.Entry<String, String> entry : raptorResponse.getAttachments().entrySet()) {
                if (entry.getKey().toLowerCase().startsWith(RaptorConstants.HEADER_TRACE_PREFIX)) {
                    response.addHeader(entry.getKey(), entry.getValue());
                }
            }
        }

        //responseAttachments，自定义头
        Map<String, String> responseAttachments = RaptorContext.getContext().getRequestAttachments();
        if (responseAttachments != null) {
            for (Map.Entry<String, String> entry : responseAttachments.entrySet()) {
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}
