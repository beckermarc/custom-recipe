package com.sap.openrewrite.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.J.MethodInvocation;
import org.openrewrite.marker.SearchResult;

import com.sap.openrewrite.recipe.table.SetTenant;

public class FindSetTenant extends Recipe {

	private final transient SetTenant table = new SetTenant(this);

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
		MethodMatcher setTenantMatcher = new MethodMatcher("com.sap.cds.services.request.ModifiableUserInfo setTenant(..)");
		MethodMatcher modifyUserMatcher = new MethodMatcher("com.sap.cds.services.runtime.RequestContextRunner modifyUser(..)");
		return new JavaIsoVisitor<ExecutionContext>() {

			@Override
			public MethodInvocation visitMethodInvocation(MethodInvocation method, ExecutionContext p) {
				MethodInvocation m = super.visitMethodInvocation(method, p);

				// JavaTemplate matcher = Semantics.expression(this, "setTenantMatcher",
				// 		(RequestContextRunner r, String s) -> r.modifyUser(u -> u.setTenant(s)))
				// 		.build();
				// if (matcher.matches(getCursor())) {
				// 	return SearchResult.found(m);
				// }

				if (setTenantMatcher.matches(m)) {
					MethodInvocation parent = getCursor().getParent().firstEnclosing(J.MethodInvocation.class);
					if (modifyUserMatcher.matches(parent)) {
						table.insertRow(p, new SetTenant.Row(m.printTrimmed(getCursor())));
						return SearchResult.found(m);
					}
				}
				return m;
			}

		};
	}

}
