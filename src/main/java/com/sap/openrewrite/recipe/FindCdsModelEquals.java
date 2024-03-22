package com.sap.openrewrite.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Preconditions;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.search.UsesType;
import org.openrewrite.java.template.Semantics;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.SearchResult;

import com.sap.cds.reflect.CdsModel;

public class FindCdsModelEquals extends Recipe {

    @Override
    public String getDisplayName() {
        return "Finds CdsModel == comparisons";
    }

    @Override
    public String getDescription() {
        return "Finds usages of CdsModel ==.";
    }

	@Override
	public TreeVisitor<?, ExecutionContext> getVisitor() {
		JavaVisitor<ExecutionContext> javaVisitor = new JavaIsoVisitor<>() {
			final JavaTemplate useObjectIdentity = Semantics.expression(this, "useObjectIdentity",
					(CdsModel m1, CdsModel m2) -> m1 == m2).build();

			@Override
			public J.Binary visitBinary(J.Binary elem, ExecutionContext ctx) {
				J.Binary b = super.visitBinary(elem, ctx);
				if (useObjectIdentity.matches(getCursor())) {
					return SearchResult.found(elem);
				}
				return b;
			}

		};
		return Preconditions.check(
				new UsesType<>("com.sap.cds.reflect.CdsModel", true),
				javaVisitor
		);
	}

}
