package com.sap.openrewrite.recipe;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

public class FindCdsModelEqualsTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new FindCdsModelEquals()).parser(JavaParser.fromJavaVersion().classpath("cds4j-api"));
    }

    @Test
    void testReplaceModifyUser() {
        rewriteRun(
            java(

                """
                    import com.sap.cds.reflect.CdsModel;

                    class Test {

                        boolean test(CdsModel m1, CdsModel m2) {
                            return m1 == m2;
                        }

                    }
                """,
                """
                    import com.sap.cds.reflect.CdsModel;

                    class Test {

                        boolean test(CdsModel m1, CdsModel m2) {
                            return /*~~>*/m1 == m2;
                        }

                    }
                """
            )
        );

    }

}
