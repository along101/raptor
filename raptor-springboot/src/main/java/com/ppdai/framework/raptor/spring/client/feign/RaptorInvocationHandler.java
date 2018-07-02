package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.rpc.RaptorClientInterceptor;
import com.ppdai.framework.raptor.rpc.RaptorContext;
import com.ppdai.framework.raptor.rpc.RaptorRequest;
import com.ppdai.framework.raptor.rpc.RaptorResponse;
import com.ppdai.framework.raptor.utils.RequestIdUtils;
import feign.InvocationHandlerFactory;
import feign.Target;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static feign.Util.checkNotNull;

/**
 * @author yinzuolong
 */
@Slf4j
public class RaptorInvocationHandler implements InvocationHandler {

    @Getter
    @Setter
    private List<RaptorClientInterceptor> interceptors = new LinkedList<>();
    private Target<?> target;
    private Map<Method, InvocationHandlerFactory.MethodHandler> dispatch;

    public RaptorInvocationHandler(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch) {
        this.target = checkNotNull(target, "target");
        this.dispatch = checkNotNull(dispatch, "dispatch for %s", target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return equals(otherHandler);
            } catch (IllegalArgumentException e) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }
        initRequestResponse(method, args);
        Object result = null;
        Exception ex = null;
        try {
            applyPreHandle();
            result = dispatch.get(method).invoke(args);
            return applyPostHandle(result);
        } catch (Exception e) {
            ex = e;
            throw e;
        } finally {
            triggerAfterCompletion(result, ex);
        }
    }

    protected void initRequestResponse(Method method, Object[] args) throws Exception {
        RaptorRequest request = new RaptorRequest();
        request.setArguments(args);
        request.setInterfaceName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setRequestId(RequestIdUtils.getRequestId());
        RaptorContext.getContext().setRequest(request);

        RaptorResponse response = new RaptorResponse(request.getRequestId());
        RaptorContext.getContext().setResponse(response);
    }

    protected void applyPreHandle() throws Exception {
        if (interceptors != null) {
            RaptorRequest request = RaptorContext.getContext().getRequest();
            RaptorResponse response = RaptorContext.getContext().getResponse();
            for (RaptorClientInterceptor interceptor : interceptors) {
                interceptor.preHandle(request, response);
            }
        }
    }

    protected Object applyPostHandle(Object result) throws Exception {
        RaptorRequest request = RaptorContext.getContext().getRequest();
        RaptorResponse response = RaptorContext.getContext().getResponse();
        response.setValue(result);
        if (interceptors != null) {
            for (RaptorClientInterceptor interceptor : interceptors) {
                interceptor.postHandle(request, response);
            }
        }
        return response.getValue();
    }

    protected void triggerAfterCompletion(Object result, Exception ex) {
        RaptorRequest request = RaptorContext.getContext().getRequest();
        RaptorResponse response = RaptorContext.getContext().getResponse();
        response.setValue(result);
        response.setException(ex);
        if (interceptors != null) {
            for (RaptorClientInterceptor interceptor : interceptors) {
                try {
                    interceptor.afterCompletion(request, response);
                } catch (Exception e) {
                    log.error("RaptorInvocationHandler afterCompletion threw exception", e);
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RaptorInvocationHandler) {
            RaptorInvocationHandler other = (RaptorInvocationHandler) obj;
            return target.equals(other.target);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
