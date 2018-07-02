package com.ppdai.framework.raptor.spring.service;

import com.ppdai.framework.raptor.annotation.RaptorMessage;
import com.ppdai.framework.raptor.spring.converter.RaptorMessageConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import javax.servlet.ServletRequest;
import java.util.Collections;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class RaptorHandlerMethodProcessor extends AbstractMessageConverterMethodProcessor {

    private ByteArrayBase64PropertyEditor byteArrayBase64PropertyEditor = new ByteArrayBase64PropertyEditor();

    public RaptorHandlerMethodProcessor(RaptorMessageConverter converter) {
        super(Collections.singletonList(converter));
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        RaptorMessage annotation = AnnotationUtils.findAnnotation(parameter.getParameterType(), RaptorMessage.class);
        return annotation != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        HttpMethod httpMethod = inputMessage.getMethod();
        switch (httpMethod) {
            case GET:
            case HEAD:
                return bindData(parameter, mavContainer, webRequest, binderFactory);
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
            case OPTIONS:
                return convertBody(parameter, mavContainer, webRequest, binderFactory);
            default:
                return null;
        }
    }

    private Object bindData(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String name = ModelFactory.getNameForParameter(parameter);
        Object arg = BeanUtils.instantiateClass(parameter.getParameterType());
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        binder.registerCustomEditor(byte[].class, this.byteArrayBase64PropertyEditor);

        ServletRequest servletRequest = webRequest.getNativeRequest(ServletRequest.class);
        ((ServletRequestDataBinder) binder).bind(servletRequest);
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);
        return arg;
    }

    private Object convertBody(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        parameter = parameter.nestedIfOptional();
        return readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());

    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        RaptorMessage annotation = AnnotationUtils.findAnnotation(returnType.getParameterType(), RaptorMessage.class);
        return annotation != null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        //设置Response header,本应该放在拦截器中实现,但是handler处理完后,response已经关闭了,拦截器无法后置处理response
        RaptorContextInitHandlerInterceptor.applyResponse(outputMessage.getServletResponse());

        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }

}
