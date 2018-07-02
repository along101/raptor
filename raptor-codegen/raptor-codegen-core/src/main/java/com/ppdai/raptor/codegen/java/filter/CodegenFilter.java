package com.ppdai.raptor.codegen.java.filter;

import com.squareup.wire.schema.ProtoFile;
import com.squareup.wire.schema.Type;

/**
 * 根据protoName, packageName,appId ,summary等条件决定是否生成java文件
 *
 * @author zhangchengxi
 * Date 2018/5/8
 */
public class CodegenFilter {
    private static final String GOOGLE_PACKAGE = "google.protobuf";

    public boolean filterByProtoFile(ProtoFile protoFile) {
        if (isGoogle(protoFile)) {
            return false;
        }
        return true;
    }

    private boolean isGoogle(ProtoFile protoFile) {
        return GOOGLE_PACKAGE.equals(protoFile.packageName());
    }

    public boolean filterByType(Type type) {
        return true;
    }
}
