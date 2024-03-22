package com.sap.openrewrite.recipe;

import org.openrewrite.java.template.RecipeDescriptor;

import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import com.sap.cds.services.runtime.RequestContextRunner;

public class RequestContexRunnerRules {

	@RecipeDescriptor(
			name = "Replaces setTenant() with systemUser() using u as lambda name",
			description = "Descriptions")
	public static class SetTenantToSystemUserWithU {

		@BeforeTemplate
		public RequestContextRunner modifyUserBefore(RequestContextRunner runner, String tenant) {
			return runner.modifyUser(u -> u.setTenant(tenant));
		}

		@AfterTemplate
		public RequestContextRunner systemUserAfter(RequestContextRunner runner, String tenant) {
			return runner.systemUser(tenant);
		}

	}

	@RecipeDescriptor(
		name = "Replaces setTenant() with systemUser() using user as lambda name",
		description = "Descriptions")
	public static class SetTenantToSystemUserWithUser {

		@BeforeTemplate
		public RequestContextRunner modifyUserBefore(RequestContextRunner runner, String tenant) {
			return runner.modifyUser(user -> user.setTenant(tenant));
		}

		@AfterTemplate
		public RequestContextRunner systemUserAfter(RequestContextRunner runner, String tenant) {
			return runner.systemUser(tenant);
		}

	}

}
