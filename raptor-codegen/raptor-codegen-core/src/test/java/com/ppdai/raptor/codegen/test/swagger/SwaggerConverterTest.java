package com.ppdai.raptor.codegen.test.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ppdai.raptor.codegen.swagger.SwaggerConverter;
import com.ppdai.raptor.codegen.test.AbstractCodegenTest;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.Test;

import java.util.List;

/**
 * @author zhangchengxi
 * Date 2018/5/21
 */
public class SwaggerConverterTest extends AbstractCodegenTest {



    @Test
    public void testGenerateApis() throws JsonProcessingException {
        SwaggerConverter swaggerConverter = new SwaggerConverter(schema);
        List<OpenAPI> convert = swaggerConverter.convert();
//        for (OpenAPI openAPI : convert) {
//            Json.prettyPrint(openAPI);
//        }


        Json.prettyPrint(convert.get(0));
    }



}
