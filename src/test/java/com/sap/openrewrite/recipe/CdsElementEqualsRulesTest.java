package com.sap.openrewrite.recipe;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

public class CdsElementEqualsRulesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new CdsElementEqualsRulesRecipes()).parser(JavaParser.fromJavaVersion().classpath("cds4j-api"));
    }

    @Test
    void testReplaceModifyUser() {
        rewriteRun(
            java(

                """
                    import com.sap.cds.reflect.CdsEntity;
                    import com.sap.cds.reflect.CdsElement;

                    class Test {

                        boolean test(CdsEntity entity, CdsElement elem) {
                            return entity.getElement("test") == elem;
                        }

                    }
                """,
                """
                    import com.sap.cds.reflect.CdsEntity;
                    import com.sap.cds.reflect.CdsElement;

                    class Test {

                        boolean test(CdsEntity entity, CdsElement elem) {
                            return entity.getElement("test").equals(elem);
                        }

                    }
                """
            )
        );

    }

}
