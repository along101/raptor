package com.ppdai.raptor.codegen.java;

import com.ppdai.raptor.codegen.java.maven.PomModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author zhangchengxi
 * Date 2018/5/17
 */
@Slf4j
public class PomGenerator {
    public static final String EXAMPLE_POM_PATH = "/ExamplePom.xml";

    private PomModel pomModel;

    public PomGenerator(PomModel pomModel) {
        this.pomModel = pomModel;
    }

    public PomGenerator() {
        this.pomModel = PomModel.builder().build();
    }


    public Model readExamplePom() {
        InputStream resourceAsStream;
        if (StringUtils.isNotBlank(pomModel.getExamplePom())) {
            resourceAsStream = new ByteArrayInputStream(pomModel.getExamplePom().getBytes());
        } else {
            resourceAsStream = getClass().getResourceAsStream(EXAMPLE_POM_PATH);
        }

        Model read;
        try {
            read = new MavenXpp3Reader().read(resourceAsStream);
        } catch (IOException | XmlPullParserException e) {
            log.error("read ExamplePom.xml error", e);
            throw new RuntimeException("read ExamplePom.xml error", e);
        }
        return read;
    }

    public Model buildModel() {
        Model model = readExamplePom();
        model.setArtifactId(pomModel.getArtifactId());
        model.setGroupId(pomModel.getGroupId());
        model.setVersion(pomModel.getVersion());
        return model;
    }

    public void writeTo(OutputStream outputStream, Model model) {
        MavenXpp3Writer mavenXpp3Writer = new MavenXpp3Writer();
        try {
            mavenXpp3Writer.write(outputStream, model);
        } catch (IOException e) {
            log.error("write pom model error", e);
            throw new RuntimeException("write pom model error", e);
        }
    }

    public void writeTo(OutputStream outputStream) {
        writeTo(outputStream, buildModel());
    }


}
