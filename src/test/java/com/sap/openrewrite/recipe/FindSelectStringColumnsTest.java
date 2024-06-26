package com.sap.openrewrite.recipe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import com.sap.openrewrite.recipe.table.SelectStringColumns;

public class FindSelectStringColumnsTest implements RewriteTest {

	@Override
	public void defaults(RecipeSpec spec) {
		spec.recipe(new FindSelectStringColumns()).parser(JavaParser.fromJavaVersion().classpath("cds4j-api"));
	}

	@Test
	void testSelect() {
		rewriteRun(
			spec -> spec.dataTable(SelectStringColumns.Row.class, (rows) -> {
				assertThat(rows).containsExactly(new SelectStringColumns.Row("MyElement"));
			}),
			java(

				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns("MyElement");
						}

					}
				""",
				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns(/*~~>*/"MyElement");
						}

					}
				"""
			)
		);

	}

	@Test
	void testSelectWithMultipleStrings() {
		rewriteRun(
			spec -> spec.dataTable(SelectStringColumns.Row.class, (rows) -> {
				assertThat(rows).containsExactly(new SelectStringColumns.Row("MyElement"), new SelectStringColumns.Row("MyOtherElement"));
			}),
			java(

				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns("MyElement", "MyOtherElement");
						}

					}
				""",
				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns(/*~~>*/"MyElement", /*~~>*/"MyOtherElement");
						}

					}
				"""
			)
		);

	}

	@Test
	void testSelectWithArray() {
		rewriteRun(
			spec -> spec.dataTable(SelectStringColumns.Row.class, (rows) -> {
				assertThat(rows).containsExactly(new SelectStringColumns.Row("MyElement"), new SelectStringColumns.Row("MyOtherElement"));
			}),
			java(

				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns(new String[] {"MyElement", "MyOtherElement"});
						}

					}
				""",
				"""
					import com.sap.cds.ql.Select;

					class Test {

						void test() {
							Select.from("MyEntity").columns(new String[] {/*~~>*/"MyElement", /*~~>*/"MyOtherElement"});
						}

					}
				"""
			)
		);

	}

}
