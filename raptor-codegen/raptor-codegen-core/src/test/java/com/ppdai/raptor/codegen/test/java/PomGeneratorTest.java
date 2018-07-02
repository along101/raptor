package com.ppdai.raptor.codegen.test.java;

import com.ppdai.raptor.codegen.java.PomGenerator;
import com.ppdai.raptor.codegen.java.maven.PomModel;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

/**
 * @author zhangchengxi
 * Date 2018/5/17
 */
public class PomGeneratorTest {

    @Test
    public void testReadPom() {
        PomGenerator pomGenerator = new PomGenerator();
        Model model = pomGenerator.readExamplePom();
        Assert.assertNotNull(model);
    }

    @Test
    public void testWriteTo() throws IOException, XmlPullParserException {
        String artifactId = "artifactId";
        String version = "version";
        String groupId = "groupId";

        PomModel pomModel = PomModel.builder().artifactId(artifactId).groupId(groupId).version(version).build();
        PomGenerator pomGenerator = new PomGenerator(pomModel);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pomGenerator.writeTo(baos);
        String s = baos.toString();

        MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
        InputStream is = new ByteArrayInputStream(s.getBytes());

        Model generated = mavenXpp3Reader.read(is);

        Assert.assertEquals(artifactId,generated.getArtifactId());
        Assert.assertEquals(version,generated.getVersion());
        Assert.assertEquals(groupId,generated.getGroupId());

    }



}
