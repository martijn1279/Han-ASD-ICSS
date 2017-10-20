package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class Operation extends Expression {

    public static enum Operator {PLUS, MIN, OR, AND, EQ, LT, GT};

    public Operator operator;
    public Expression lhs;
    public Expression rhs;

    @Override
    public String getNodeLabel() {
        return "Operation (" + operator.toString() + ")";
    }

    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(lhs);
        children.add(rhs);
        return children;
    }

    @Override
    public void addChild(ASTNode child) {
        if(lhs == null) {
            lhs = (Expression) child;
        } else if(rhs == null) {
            rhs = (Expression) child;
        }
    }
}
