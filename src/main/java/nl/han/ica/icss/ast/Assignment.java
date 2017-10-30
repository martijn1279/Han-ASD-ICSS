package nl.han.ica.icss.ast;

import java.util.ArrayList;

/**
 * An assignment binds a expression to an identifier.
 */
public class Assignment extends ASTNode {

    public VariableReference name;
    public Expression expression;

    @Override
    public String getNodeLabel() {
        return "Assignment (" + name.name + ")";
    }

    @Override
    public void addChild(ASTNode child) {
        expression = (Expression) child;
    }

    @Override
    public ArrayList<ASTNode> getChildren() {

        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(name);
        children.add(expression);
        return children;
    }
}
