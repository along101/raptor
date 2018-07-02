package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.common.RaptorConstants;
import com.ppdai.framework.raptor.rpc.RaptorContext;
import com.ppdai.framework.raptor.rpc.RaptorRequest;
import com.ppdai.framework.raptor.utils.NetUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @author yinzuolong
 */
@Order
public class HeaderTraceRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        //requestId
        template.header(RaptorConstants.HEADER_REQUEST_ID, RaptorContext.getContext().getRequest().getRequestId());

        //clientIp
        template.header(RaptorConstants.HEADER_HOST_CLIENT, NetUtils.getLocalIp());

        //request设置头
        RaptorRequest request = RaptorContext.getContext().getRequest();
        if (request != null) {
            for (Map.Entry<String, String> entry : request.getAttachments().entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }
        //context设置request头
        Map<String, String> requestAttachments = RaptorContext.getContext().getRequestAttachments();
        if (requestAttachments != null) {
            for (Map.Entry<String, String> entry : requestAttachments.entrySet()) {
                template.header(entry.getKey(), entry.getValue());
            }
        }

    }
}
