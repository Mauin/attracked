package com.mtramin.attracked;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * TODO: JAVADOC
 */
public class AttrackedProcessorTest {
    @Test
    public void testProcessor() throws Exception {
        JavaFileObject sampleEventClass = JavaFileObjects.forSourceString("com.example.TestEvent",
                "package com.mtramin.attracked_test;\n" +
                        "\n" +
                        "import com.mtramin.attracked.Attribute;\n" +
                        "import com.mtramin.attracked.Event;\n" +
                        "\n" +
                        "@Event(name=\"test\")\n" +
                        "public class TestEvent {\n" +
                        "    @Attribute(name=\"one\")\n" +
                        "    String one;\n" +
                        "\n" +
                        "    @Attribute(name=\"two\")\n" +
                        "    String two;\n" +
                        "}"
        );

        assert_().about(javaSource())
                .that(sampleEventClass)
                .processedWith(new AttrackedProcessor())
                .compilesWithoutError();
    }
}
