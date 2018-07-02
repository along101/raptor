package com.ppdai.framework.raptor.spring.client.feign;

import com.ppdai.framework.raptor.spring.client.feign.AnnotatedParameterProcessor;
import feign.MethodMetadata;
import org.springframework.web.bind.annotation.RequestHeader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import static feign.Util.checkState;
import static feign.Util.emptyToNull;

public class RequestHeaderParameterProcessor implements AnnotatedParameterProcessor {

    private static final Class<RequestHeader> ANNOTATION = RequestHeader.class;

    @Override
    public Class<? extends Annotation> getAnnotationType() {
        return ANNOTATION;
    }

    @Override
    public boolean processArgument(AnnotatedParameterProcessor.AnnotatedParameterContext context, Annotation annotation, Method method) {
        int parameterIndex = context.getParameterIndex();
        Class<?> parameterType = method.getParameterTypes()[parameterIndex];
        MethodMetadata data = context.getMethodMetadata();

        if (Map.class.isAssignableFrom(parameterType)) {
            checkState(data.headerMapIndex() == null, "Header map can only be present once.");
            data.headerMapIndex(parameterIndex);

            return true;
        }

        String name = ANNOTATION.cast(annotation).value();
        checkState(emptyToNull(name) != null,
                "RequestHeader.value() was empty on parameter %s", parameterIndex);
        context.setParameterName(name);

        Collection<String> header = context.setTemplateParameter(name, data.template().headers().get(name));
        data.template().header(name, header);
        return true;
    }
}
