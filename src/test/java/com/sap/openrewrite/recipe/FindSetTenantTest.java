package com.sap.openrewrite.recipe;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

public class FindSetTenantTest implements RewriteTest {

	@Override
	public void defaults(RecipeSpec spec) {
		spec.recipe(new FindSetTenant()).parser(JavaParser.fromJavaVersion().classpath("cds-services-api"));
	}

	@Test
	void testSetTenantInModifyUser() {
		rewriteRun(
			java(

				"""
					import com.sap.cds.services.runtime.CdsRuntime;

					class Test {

						void test(CdsRuntime runtime) {
							runtime.requestContext().modifyUser(u ->
								u.setTenant(null)
							);
						}

					}
				""",
				"""
					import com.sap.cds.services.runtime.CdsRuntime;

					class Test {

						void test(CdsRuntime runtime) {
							runtime.requestContext().modifyUser(u ->
								/*~~>*/u.setTenant(null)
							);
						}

					}
				"""
			)
		);

	}

	@Test
	void testSetTenant() {
		rewriteRun(
			java(
				"""
					import com.sap.cds.services.request.UserInfo;

					class Test {

						void test() {
							UserInfo.create().setTenant(null);
						}

					}
				"""
			)
		);
	}
}
