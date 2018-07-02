package com.ppdai.raptor.codegen.java.option;

import com.google.common.collect.Lists;
import com.ppdai.framework.raptor.annotation.RaptorMethod;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.wire.schema.Options;
import com.squareup.wire.schema.ProtoMember;
import com.squareup.wire.schema.Rpc;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.squareup.wire.schema.Options.METHOD_OPTIONS;

/**
 * @author zhangchengxi
 * Date 2018/4/27
 */
@Data
@Builder
public class MethodMetaInfo {
    private static final String PATH_STR = "path";
    private static final String METHOD_STR = "method";
    private static final String PARAMS_STR = "pathParam";
    private static final String PATH_PARAMS_STR = "requestParam";
    private static final String HEAD_PARAMS_STR = "headerParam";

    private static final ProtoMember PATH = ProtoMember.get(METHOD_OPTIONS, PATH_STR);
    private static final ProtoMember METHOD = ProtoMember.get(METHOD_OPTIONS, METHOD_STR);
    private static final ProtoMember REQUEST_PARAMS = ProtoMember.get(METHOD_OPTIONS, PARAMS_STR);
    private static final ProtoMember PATH_PARAMS = ProtoMember.get(METHOD_OPTIONS, PATH_PARAMS_STR);
    private static final ProtoMember HEAD_PARAMS = ProtoMember.get(METHOD_OPTIONS, HEAD_PARAMS_STR);

    private static Pattern PATH_PARAM_PATTERN = Pattern.compile("\\{(.*?)}");


    private String path;
    private List<Param> pathParams;
    private Method method;
    private List<Param> requestParams;
    private List<Param> headerParams;
    private String summary;

    public static MethodMetaInfo readFrom(Rpc rpc) {
        Options options = rpc.options();

        String path = OptionUtil.readStringOption(options, PATH);
        String method = OptionUtil.readStringOption(options, METHOD);
        String summary = OptionUtil.readSummary(rpc.documentation());

        List<String> paramTypesStr = OptionUtil.readStringList(options, REQUEST_PARAMS);
        List<Param> pathParams = buildParams(paramTypesStr);

        List<String> requestParamsStr = OptionUtil.readStringList(options, PATH_PARAMS);
        List<Param> requestParams = buildParams(requestParamsStr);

        List<String> headerParamsStr = OptionUtil.readStringList(options, HEAD_PARAMS);
        List<Param> headerParams = buildParams(headerParamsStr);


        return MethodMetaInfo.builder()
                .path(path)
                .method(Method.get(method))
                .pathParams(pathParams)
                .requestParams(requestParams)
                .headerParams(headerParams)
                .summary(summary)
                .build();
    }

    private static List<Param> buildParams(List<String> paramStr) {
        List<Param> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(paramStr)) {
            return result;
        }

        //预处理
        List<String> params = paramStr.stream().map(String::trim).collect(Collectors.toList());
        for (String param : params) {
            int location = param.indexOf("=");

            String name;
            String type;
            if (location == -1) {
                name = param;
                type = "string";
            } else {
                name = param.substring(0, location);
                type = param.substring(location + 1);
            }
            Param p = Param.builder()
                    .name(name)
                    .type(type)
                    .build();
            result.add(p);
        }
        return result;
    }

    public AnnotationSpec generateRaptorMethod() {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(RaptorMethod.class);
        return builder.build();

    }

}
