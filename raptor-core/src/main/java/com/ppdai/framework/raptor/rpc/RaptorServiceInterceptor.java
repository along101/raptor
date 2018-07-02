package com.ppdai.framework.raptor.rpc;

/**
 * @author yinzuolong
 */
public interface RaptorServiceInterceptor {

    /**
     * 调用服务方法之前处理
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void preHandle(RaptorRequest request, RaptorResponse response) throws Exception;

    /**
     * 调用服务方法成功后处理
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void postHandle(RaptorRequest request, RaptorResponse response) throws Exception;

    /**
     * 调用服务方法完成后处理，通过<code>response.getException()</code>获取异常
     *
     * @param request
     * @param response
     * @throws Exception
     */
    void afterCompletion(RaptorRequest request, RaptorResponse response) throws Exception;
}
