package com.ppdai.raptor.codegen.java.option;

import com.squareup.wire.schema.Options;
import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.ProtoMember;
import lombok.Builder;
import lombok.Data;

/**
 * @author zhangchengxi
 * Date 2018/4/28
 */
@Data
@Builder
public class ProtoFileMetaInfo {
    private static final ProtoMember APP_ID = ProtoMember.get(Options.FILE_OPTIONS, "appId");
    private static final ProtoMember APP_NAME = ProtoMember.get(Options.FILE_OPTIONS, "appName");
    private static final ProtoMember VERSION = ProtoMember.get(Options.FILE_OPTIONS, "version");
    private static final ProtoMember SUMMARY = ProtoMember.get(Options.FILE_OPTIONS, "summary");

    private String appId;
    private String appName;
    private String version;
    private String protoFile;
    private String summary;
    private String crc32;


    public static ProtoFileMetaInfo readFrom(ProtoFile protoFile) {
        String appId = OptionUtil.readStringOption(protoFile.options(), APP_ID);
        String appName = OptionUtil.readStringOption(protoFile.options(), APP_NAME);
        String version = OptionUtil.readStringOption(protoFile.options(), VERSION);
        String summary = OptionUtil.readStringOption(protoFile.options(), SUMMARY);
        String protoFileName = protoFile.name();
        // TODO: 2018/4/28
        String crc32 = null;

        return ProtoFileMetaInfo.builder()
                .appId(appId)
                .appName(appName)
                .version(version)
                .protoFile(protoFileName)
                .summary(summary)
                .crc32(crc32)
                .build();
    }
}
