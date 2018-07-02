package com.ppdai.framework.raptor.codegen.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

/**
 * @author zhangchengxi
 * Date 2018/5/3
 */
public class Proto2JavaTest extends AbstractMojoTestCase {

    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();

    }

    @Test
    public void testProto2java() throws IllegalAccessException, MojoFailureException, MojoExecutionException {
        Proto2Java mojo = new Proto2Java();

        setVariableValueToObject(mojo, "protoSourceDirectory", "src/test/resources/proto");
        setVariableValueToObject(mojo, "generatedSourceDirectory", "target/generated-sources/");

        mojo.execute();
    }

}