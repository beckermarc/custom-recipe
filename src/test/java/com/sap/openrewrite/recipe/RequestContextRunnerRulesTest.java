package com.sap.openrewrite.recipe;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

public class RequestContextRunnerRulesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new RequestContexRunnerRulesRecipes()).parser(JavaParser.fromJavaVersion().classpath("cds-services-api"));
    }

    @Test
    void testReplaceModifyUser() {
        rewriteRun(
            java(

                """
                    import com.sap.cds.services.runtime.CdsRuntime;

                    class Test {

                        void test(CdsRuntime runtime) {
                            String test = "test";
                            runtime.requestContext().modifyUser(u -> u.setTenant(test));
                        }

                    }
                """,
                """
                    import com.sap.cds.services.runtime.CdsRuntime;

                    class Test {

                        void test(CdsRuntime runtime) {
                            String test = "test";
                            runtime.requestContext().systemUser(test);
                        }

                    }
                """
            )
        );

    }

}
