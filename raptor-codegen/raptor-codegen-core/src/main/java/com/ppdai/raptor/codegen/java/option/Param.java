package com.ppdai.raptor.codegen.java.option;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.ppdai.raptor.codegen.java.JavaGenerator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.squareup.wire.schema.ProtoMember;
import com.squareup.wire.schema.ProtoType;
import lombok.Builder;
import lombok.Data;
import okio.ByteString;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/4/27
 */
@Data
@Builder
public class Param {
    private static final ProtoType PATH_PARAM_TYPE = ProtoType.get("PathParam");
    private static final ProtoMember REQUEST_MAPPING_NAME = ProtoMember.get(PATH_PARAM_TYPE, "name");
    private static final ProtoMember REQUEST_MAPPING_TYPE = ProtoMember.get(PATH_PARAM_TYPE, "type");
    private static final Map<String, TypeName> BUILD_IN_TYPE_MAP =
            ImmutableMap.<String, TypeName>builder()
                    .put("double", TypeName.DOUBLE)
                    .put("float", TypeName.FLOAT)
                    .put("int64", TypeName.LONG)
                    .put("uint64", TypeName.LONG)
                    .put("int32", TypeName.INT)
                    .put("fixed64", TypeName.LONG)
                    .put("fixed32", TypeName.INT)
                    .put("bool", TypeName.BOOLEAN)
                    .put("string", ClassName.get(String.class))
                    .put("bytes", ClassName.get(ByteString.class))
                    .put("uint32", TypeName.INT)
                    .put("sfixed32", TypeName.INT)
                    .put("sfixed64", TypeName.LONG)
                    .put("sint32", TypeName.INT)
                    .put("sint64", TypeName.LONG)
                    .put("int",TypeName.INT)
                    .put("boolean",TypeName.BOOLEAN)
                    .build();

    private String name;
    private String type;




}
