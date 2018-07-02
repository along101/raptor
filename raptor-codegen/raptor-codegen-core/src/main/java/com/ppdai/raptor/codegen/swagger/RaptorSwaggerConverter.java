package com.ppdai.raptor.codegen.swagger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ppdai.raptor.codegen.java.option.InterfaceMetaInfo;
import com.ppdai.raptor.codegen.java.option.Method;
import com.ppdai.raptor.codegen.java.option.MethodMetaInfo;
import com.ppdai.raptor.codegen.java.option.ProtoFileMetaInfo;
import com.squareup.wire.schema.*;
import io.swagger.v3.core.util.PathUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangchengxi
 * Date 2018/5/23
 */
public class RaptorSwaggerConverter extends SwaggerConverter {
    public RaptorSwaggerConverter(Schema schema) {
        super(schema);
    }


    /**
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#info-object
     *
     * @param protoFile
     * @return
     */
    @Override
    protected Info getInfo(ProtoFile protoFile, Service service) {
        Info info = new Info();

        ProtoFileMetaInfo protoFileMetaInfo = ProtoFileMetaInfo.readFrom(protoFile);
        InterfaceMetaInfo interfaceMetaInfo = InterfaceMetaInfo.readFrom(protoFile, service);


        //required
        info.title(service.name());
        info.version(protoFileMetaInfo.getVersion());

        //optional
        // TODO: 2018/5/23 将 protoFileMetaInfo 和 interfaceMetaInfo 整合到description中
        info.description(service.documentation());
        info.contact(getContact());
        info.license(getLicense());

        return info;
    }

    /**
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#paths-object
     *
     * @param protoFile
     * @param service
     * @return
     */
    @Override
    protected Paths getPath(ProtoFile protoFile, Service service) {
        Paths paths = new Paths();
        String basePath = protoFile.packageName();

        InterfaceMetaInfo interfaceMetaInfo = InterfaceMetaInfo.readFrom(protoFile, service);
        String servicePath = interfaceMetaInfo.getServicePath();

        for (Rpc rpc : service.rpcs()) {
            String defaultName = PathUtils.collectPath(basePath , rpc.name());

            MethodMetaInfo methodMetaInfo = MethodMetaInfo.readFrom(rpc);

            String path = methodMetaInfo.getPath();
            path = StringUtils.isBlank(path) && StringUtils.isBlank(servicePath) ? defaultName : PathUtils.collectPath(servicePath, path);

            // TODO: 2018/5/23 处理path 相同,方法不同的问题,
            paths.addPathItem(path, getPathItem(rpc));
        }
        return paths;
    }

    /**
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#path-item-object
     *
     * @param rpc
     * @return
     */
    @Override
    protected PathItem getPathItem(Rpc rpc) {
        PathItem pathItem = new PathItem();
        // 原版raptor 只支持post

        MethodMetaInfo methodMetaInfo = MethodMetaInfo.readFrom(rpc);
        Method method = methodMetaInfo.getMethod();

        if (Objects.isNull(method)) {
            method = Method.POST;
        }
        switch (method) {
            case GET:
                pathItem.get(getOperation(rpc));
                break;
            case HEAD:
                pathItem.head(getOperation(rpc));
                break;
            case POST:
                pathItem.post(getOperation(rpc));
                break;
            case PUT:
                pathItem.put(getOperation(rpc));
                break;
            case PATCH:
                pathItem.patch(getOperation(rpc));
                break;
            case DELETE:
                pathItem.delete(getOperation(rpc));
                break;
            case OPTIONS:
                pathItem.options(getOperation(rpc));
                break;
            case TRACE:
                pathItem.trace(getOperation(rpc));
                break;
            default:
                break;
        }

        return pathItem;

    }

    /**
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#operation-object
     *
     * @param rpc
     * @return
     */
    @Override
    protected Operation getOperation(Rpc rpc) {
        Operation operation = new Operation();


        MethodMetaInfo methodMetaInfo = MethodMetaInfo.readFrom(rpc);
        operation.summary(methodMetaInfo.getSummary());
        operation.description(rpc.documentation());

        operation.responses(getApiResponses(rpc));

        // TODO: 2018/5/23 如果是get ,特殊处理request
        if( Method.GET.equals(methodMetaInfo.getMethod())){
            operation.parameters(getParameters(rpc));
        }else{
            operation.requestBody(getRequestBody(rpc));
        }
        return operation;
    }

    /**
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.1.md#parameter-object
     *
     * @param rpc
     * @return
     */
    @Override
    protected List<Parameter> getParameters(Rpc rpc) {
        ArrayList<Parameter> parameters = Lists.newArrayList();

        ProtoType protoType = rpc.requestType();

        Type type = schmea.getType(protoType);
        if( type instanceof MessageType){
            ImmutableList<Field> fields = ((MessageType) type).fields();
            for (Field field : fields) {
                Parameter parameter = new Parameter();
                parameter.name(field.name());
                parameter.in("query");
                parameter.schema(refHelper.get().getSchemaByType(field.type()));
                parameter.description(field.documentation());
                parameters.add(parameter);
            }

        }
        return parameters;
    }



}
