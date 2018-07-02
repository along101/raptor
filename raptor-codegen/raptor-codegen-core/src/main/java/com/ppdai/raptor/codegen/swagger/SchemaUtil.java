package com.ppdai.raptor.codegen.swagger;

import com.squareup.wire.schema.ProtoType;
import io.swagger.v3.oas.models.media.*;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/5/22
 */
public class SchemaUtil {

    private static final Map<String, Schema> DEFAULT_TYPE_SCHEMA;

    static {
        DEFAULT_TYPE_SCHEMA = new HashMap<>();
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Timestamp", new DateTimeSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.StringValue", new StringSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Int32Value", new IntegerSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Int64Value", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.FloatValue", new NumberSchema().format("float"));
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.DoubleValue", new NumberSchema().format("double"));
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.BoolValue", new BooleanSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Struct", new ObjectSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Value", new ObjectSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.ListValue", new ObjectSchema());
        DEFAULT_TYPE_SCHEMA.put("google.protobuf.Duration", new StringSchema());


        DEFAULT_TYPE_SCHEMA.put("bool", new BooleanSchema());
        DEFAULT_TYPE_SCHEMA.put("bytes", new ByteArraySchema());
        DEFAULT_TYPE_SCHEMA.put("double", new NumberSchema().format("double"));
        DEFAULT_TYPE_SCHEMA.put("float", new NumberSchema().format("double"));
        DEFAULT_TYPE_SCHEMA.put("int32", new IntegerSchema());
        DEFAULT_TYPE_SCHEMA.put("sint32", new IntegerSchema());
        DEFAULT_TYPE_SCHEMA.put("sfixed32", new IntegerSchema());
        DEFAULT_TYPE_SCHEMA.put("uint32", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("fixed32", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("int64", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("sint64", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("fixed64", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("uint64", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("sfixed64", new IntegerSchema().format("int64"));
        DEFAULT_TYPE_SCHEMA.put("string", new StringSchema());


    }


    public static Schema getSchema(ProtoType protoType,RefHelper refHelper) {
        Schema schema = DEFAULT_TYPE_SCHEMA.get(protoType.toString());
        if(Objects.isNull(schema)){
            schema = new Schema().$ref(refHelper.getRefer(protoType));
        }
        Schema result = new Schema();
        BeanUtils.copyProperties(schema, result);
        return result;
    }


//    private static Schema formatSchema(FieldType fieldType, String typeDefPrefix, String basePackage) {
//        Schema property = DEFAULT_TYPE_SCHEMA.get(fieldType.getFullyQualifiedPathName());
//
//        if (!fieldType.getFullyQualifiedPathName().startsWith("google.protobuf") && property == null) {
////            property = new AbstractSchema() {};
//
//            switch (fieldType.getType()) {
//                case TYPE_BYTES:
//                    property = new ByteArraySchema();
//                    break;
//                case TYPE_INT32:
//                case TYPE_SINT32:
//                case TYPE_SFIXED32:
//                    property = new IntegerSchema();
//                    break;
//                case TYPE_UINT32:
//                case TYPE_FIXED32:
//                case TYPE_INT64:
//                case TYPE_SINT64:
//                case TYPE_SFIXED64:
//                    property = new LongSchema();
//                    break;
//                case TYPE_UINT64:
//                case TYPE_FIXED64:
//                    property = new StringSchema("uint64");
//                    break;
//                case TYPE_FLOAT:
//                    property = new FloatSchema();
//                    break;
//                case TYPE_DOUBLE:
//                    property = new DoubleSchema();
//                    break;
//                case TYPE_BOOL:
//                    property = new BooleanSchema();
//                    break;
//                case TYPE_STRING:
//                    property = new StringSchema();
//                    break;
//                case TYPE_ENUM:
//                case TYPE_MESSAGE:
//                case TYPE_GROUP:
//                    property = new RefSchema();
//                    if (CommonUtils.getPackageNameFromFullyQualifiedPathName(fieldType.getFullyQualifiedPathName()).equals(basePackage)) {
//                        ((RefSchema) property)
//                                .set$ref("#/" + typeDefPrefix + "/" + fieldType.getClassName() + ProtobufConstant.PACKAGE_SEPARATOR + fieldType.getTypeName());
//                    } else {
//                        ((RefSchema) property).set$ref("#/" + typeDefPrefix + "/" + fieldType.getFullyQualifiedClassName());
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        if (property == null) {
//            throw new SwaggerGenException("field name: " + fieldType.getName()
//                    + ", type: " + fieldType.getFullyQualifiedPathName()
//                    + " in message: " + fieldType.getMessage()
//                    + " is unsupported");
//        }
//
//        if (fieldType.getLabel().equals(LABEL_REPEATED)
//                || "google.protobuf.ListValue".equals(fieldType.getFullyQualifiedPathName())) {
//            property = new ArraySchema(property);
//        }
//
//        return property;
//    }
//
//    public static Schema formatTypeSwagger2(FieldType fieldType, String basePackage) {
//        return formatSchema(fieldType, "definitions", basePackage);
//    }
}
