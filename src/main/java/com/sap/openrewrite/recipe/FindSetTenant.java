package com.sap.openrewrite.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J.MethodInvocation;
import org.openrewrite.marker.SearchResult;

public class FindSetTenant extends Recipe {

	// private final transient SetTenant table = new SetTenant(this);

	@Override
	public String getDisplayName() {
		return "Find setTenant()";
	}

	@Override
	public String getDescription() {
		return "Finds all setTenant() inside of modifyUser().";
	}

	@Override
	public TreeVisitor<?, ExecutionContext> getVisitor() {
		MethodMatcher setTenantMatcher = new MethodMatcher("com.sap.cds.services.request.ModifiableUserInfo#setTenant(..)");
		MethodMatcher modifyUserMatcher = new MethodMatcher("com.sap.cds.services.request.RequestContext#modifyUser(..)");
		return new JavaIsoVisitor<ExecutionContext>() {

			@Override
			public MethodInvocation visitMethodInvocation(MethodInvocation method, ExecutionContext p) {
				MethodInvocation m = super.visitMethodInvocation(method, p);
				if (setTenantMatcher.matches(m)) {
					//MethodInvocation enclosing = getCursor().firstEnclosing(J.MethodInvocation.class);
					//if (modifyUserMatcher.matches(enclosing)) {
						return SearchResult.found(method);
					//}
				}
				return m;
			}

		};
	}

}
