package com.ppdai.raptor.codegen.test;

import com.ppdai.raptor.codegen.java.Profile;
import com.ppdai.raptor.codegen.java.ProfileLoader;
import com.squareup.wire.schema.Schema;
import com.squareup.wire.schema.SchemaLoader;
import org.junit.Before;

import java.io.File;
import java.io.IOException;

/**
 * @author zhangchengxi
 * Date 2018/5/21
 */
public class AbstractCodegenTest {

    protected static final String PROTO_FILE_DIR = "src/test/resources/proto";
    protected static final String GENERATED_SOURCE_DIR = "target/generated-sources/annotations";
    protected Schema schema;
    protected Profile profile;


    public void setup() throws IOException {
        SchemaLoader schemaLoader = new SchemaLoader();
        schemaLoader.addSource(new File(PROTO_FILE_DIR));
        schema = schemaLoader.load();
        profile = loadProfile(schema);
    }

    private Profile loadProfile(Schema schema) throws IOException {
        return new ProfileLoader("java").schema(schema).load();
    }

    @Before
    public void before() throws IOException {
        setup();
    }


}
