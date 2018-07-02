package com.ppdai.raptor.codegen.java.option;

import com.ppdai.framework.raptor.annotation.RaptorMessage;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.wire.schema.MessageType;
import com.squareup.wire.schema.ProtoFile;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangchengxi
 * Date 2018/4/28
 */
@Data
@Builder
public class MessageMetaInfo {
    private String summary;
    private ProtoFileMetaInfo protoFileMetaInfo;


    public static MessageMetaInfo readFrom(ProtoFile protoFile, MessageType messageType) {
        ProtoFileMetaInfo protoFileMetaInfo = ProtoFileMetaInfo.readFrom(protoFile);
        String summary = OptionUtil.readSummary(messageType.documentation());

        return MessageMetaInfo.builder()
                .summary(summary)
                .protoFileMetaInfo(protoFileMetaInfo)
                .build();
    }

    public AnnotationSpec generateMessageSpec() {
        AnnotationSpec.Builder builder = AnnotationSpec.builder(RaptorMessage.class);
        OptionUtil.setAnnotationMember(builder, "version", "$S", protoFileMetaInfo.getVersion());
        OptionUtil.setAnnotationMember(builder, "protoFile", "$S", protoFileMetaInfo.getProtoFile());
        OptionUtil.setAnnotationMember(builder, "crc32", "$S", protoFileMetaInfo.getCrc32());
        return builder.build();
    }
}
