package com.ppdai.raptor.codegen.java.option;

import com.google.common.collect.ImmutableList;
import com.ppdai.framework.raptor.annotation.RaptorInterface;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.wire.schema.Options;
import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.ProtoMember;
import com.squareup.wire.schema.Service;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


/**
 * @author zhangchengxi
 * Date 2018/4/28
 */
@Data
@Builder
public class InterfaceMetaInfo {
    private static final ProtoMember SERVICE_PATH = ProtoMember.get(Options.SERVICE_OPTIONS, "servicePath");


    private String servicePath;
    private String summary;
    private ProtoFileMetaInfo protoFileMetaInfo;


    public static InterfaceMetaInfo readFrom(ProtoFile protoFile, Service service) {
        String servicePath = OptionUtil.readStringOption(service.options(), SERVICE_PATH);
        String summary = OptionUtil.readSummary(service.documentation());
        ProtoFileMetaInfo protoFileMetaInfo = ProtoFileMetaInfo.readFrom(protoFile);

        InterfaceMetaInfoBuilder builder = InterfaceMetaInfo.builder();
        builder.servicePath(servicePath)
                .protoFileMetaInfo(protoFileMetaInfo)
                .summary(summary);

        return builder.build();
    }

    public AnnotationSpec generateInterfaceSpec() {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(RaptorInterface.class);
        OptionUtil.setAnnotationMember(builder, "appId", "$S", protoFileMetaInfo.getAppId());
        OptionUtil.setAnnotationMember(builder, "appName", "$S", protoFileMetaInfo.getAppName());
        OptionUtil.setAnnotationMember(builder, "version", "$S", protoFileMetaInfo.getVersion());
        OptionUtil.setAnnotationMember(builder, "protoFile", "$S", protoFileMetaInfo.getProtoFile());
        OptionUtil.setAnnotationMember(builder, "crc32", "$S", protoFileMetaInfo.getCrc32());
        // TODO: 2018/5/16 先写死spring
        OptionUtil.setAnnotationMember(builder, "library", "$S", "spring");

        return builder.build();

    }

    public AnnotationSpec generateRequestMappingSpec() {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(RequestMapping.class);
         OptionUtil.setAnnotationMember(builder, "path", "$S", servicePath);
         return builder.build();
    }
    public List<AnnotationSpec> generateAnnotations() {
        return ImmutableList.of(generateInterfaceSpec(), generateRequestMappingSpec());
    }

}
