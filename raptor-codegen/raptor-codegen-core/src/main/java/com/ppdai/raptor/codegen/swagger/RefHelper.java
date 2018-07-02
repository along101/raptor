package com.ppdai.raptor.codegen.swagger;


import com.google.common.collect.ImmutableList;
import com.squareup.wire.schema.*;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangchengxi
 * Date 2018/5/21
 */
public class RefHelper {

    private final ProtoFile protofile;
    private final Service service;
    private com.squareup.wire.schema.Schema schema;

    private HashSet<ProtoType> record;
    private Queue<ProtoType> protoTypeQueue;
    private SchemaUtil schemaUtil;


    public RefHelper(com.squareup.wire.schema.Schema schema, ProtoFile protoFile, Service service) {
        this.schema = schema;
        this.protofile = protoFile;
        this.service = service;

        record = new HashSet<>();
        protoTypeQueue = new ArrayDeque<>();
        schemaUtil = new SchemaUtil();
    }

    public String getRefer(ProtoType type) {
        if (!record.contains(type)) {
            record.add(type);
            protoTypeQueue.add(type);
        }
        return "#/components/schemas/" + type.toString();
    }

    public Map<String, Schema> buildSchemas() {
        Map<String, Schema> schemas = new HashMap();

        ProtoType protoType = protoTypeQueue.poll();
        while (Objects.nonNull(protoType)) {
            Schema schema = buildSchema(protoType);
            schemas.put(protoType.toString(), schema);
            protoType = protoTypeQueue.poll();
        }

        return schemas;
    }

    private Schema buildSchema(ProtoType protoType) {
        Schema schema = new Schema();
        Type type = this.schema.getType(protoType);
        if (type instanceof EnumType) {
            schema.type("integer");
            // TODO: 2018/5/22 追加特别的描述
            schema.description(getDescriptionFromEnumType((EnumType) type));
            schema.setEnum(((EnumType) type).constants().stream().map(EnumConstant::tag).collect(Collectors.toList()));
        } else if (type instanceof MessageType) {
            schema.properties(buildProperties((MessageType) type));
        }
        return schema;
    }

    private String getDescriptionFromEnumType(EnumType type) {
        String format = "* %d - %s %s\n";
        StringBuffer sb = new StringBuffer();
        sb.append(type.documentation());
        sb.append("\n");
        for (EnumConstant enumConstant : type.constants()) {
            sb.append(String.format(format, enumConstant.tag(), enumConstant.name(),enumConstant.documentation()));
        }
        return sb.toString();
    }

    private Map<String, Schema> buildProperties(MessageType protoType) {
        Map<String, Schema> properties = new HashMap<>();
        // 不处理oneof
        ImmutableList<Field> fields = protoType.fields();
        for (Field field : fields) {
            ProtoType fieldType = field.type();
            String fieldName = field.name();


            Schema property = getSchemaByType(fieldType);
            if (field.isRepeated()) {
                property = new ArraySchema().items(property);
            }
            property.description(field.documentation());
            properties.put(fieldName, property);
        }
        return properties;
    }

    public  Schema getSchemaByType(ProtoType protoType) {
        if (protoType.isScalar()) {
            return SchemaUtil.getSchema(protoType, this);
        } else if (protoType.isMap()) {
            // TODO: 2018/5/21 deal with map
            return new Schema().additionalProperties(getSchemaByType(protoType.valueType()));
        } else {
            // TODO: 2018/5/22 看看protobuf内部message 怎么处理
            return new Schema().$ref(getRefer(protoType));
        }
    }
}
