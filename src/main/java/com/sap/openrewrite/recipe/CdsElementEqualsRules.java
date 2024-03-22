package com.sap.openrewrite.recipe;

import org.openrewrite.java.template.RecipeDescriptor;

import com.google.errorprone.refaster.annotation.AfterTemplate;
import com.google.errorprone.refaster.annotation.BeforeTemplate;
import com.sap.cds.reflect.CdsElement;

public class CdsElementEqualsRules {

	@RecipeDescriptor(
		name = "Replace CdsElement == comparisons",
		description = "Replaces CdsElement == comparions with equals")
	public static class SetTenantToSystemUser {

		@BeforeTemplate
		public boolean useObjectIdentity(CdsElement e1, CdsElement e2) {
			return e1 == e2;
		}

		@AfterTemplate
		public boolean useEquals(CdsElement e1, CdsElement e2) {
			return e1.equals(e2);
		}
	}

}
