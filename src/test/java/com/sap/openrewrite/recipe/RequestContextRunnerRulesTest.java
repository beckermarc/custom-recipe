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
    void testReplaceModifyUserWithU() {
        rewriteRun(
            java(
                """
                    import com.sap.cds.services.runtime.RequestContextRunner;
                    import java.util.Map;

                    class Test {

                        void test(RequestContextRunner cdsContextRunner, Map<String, String> map) {
                            cdsContextRunner.modifyUser(u -> u.setTenant(map.get("test").toString()));
                        }
                    }
                """,
                """
                    import com.sap.cds.services.runtime.RequestContextRunner;
                    import java.util.Map;

                    class Test {

                        void test(RequestContextRunner cdsContextRunner, Map<String, String> map) {
                            cdsContextRunner.systemUser(map.get("test").toString());
                        }
                    }
                """
            )
        );

    }

    @Test
    void testReplaceModifyUserWithUser() {
        rewriteRun(
            java(
                """
                    import com.sap.cds.services.runtime.CdsRuntime;

                    class Test {

                        void test(CdsRuntime runtime) {
                            String test = "tenant";
                            runtime.requestContext().modifyUser(user -> user.setTenant(test));
                        }
                    }
                """,
                """
                    import com.sap.cds.services.runtime.CdsRuntime;

                    class Test {

                        void test(CdsRuntime runtime) {
                            String test = "tenant";
                            runtime.requestContext().systemUser(test);
                        }
                    }
                """
            )
        );

    }

}

