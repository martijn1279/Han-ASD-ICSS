package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;

public class EliminateOptionals implements Transform {
    private ArrayList<ASTNode> astNodes = new ArrayList<>();
    private AST ast;

    @Override
    public void apply(AST ast) {
        this.ast = ast;
        for (ASTNode node : ast.root.body) {
            remove(node);
        }
        ast.root.body.removeAll(astNodes);
    }

    public void remove(ASTNode node) {
        if (node instanceof Stylerule)
            if (((Stylerule) node).condition != null) {
                if (((Stylerule) node).condition instanceof BoolLiteral) {
                    if (!((BoolLiteral) ((Stylerule) node).condition).value)
                        astNodes.add(node);
                } else {
                    Expression lhs = ((Operation) ((Stylerule) node).condition).lhs;
                    Expression rhs = ((Operation) ((Stylerule) node).condition).rhs;
                    Operation.Operator operator = ((Operation) ((Stylerule) node).condition).operator;
                    if (((Stylerule) node).condition instanceof Operation && lhs instanceof BoolLiteral && rhs instanceof BoolLiteral) {
                        if ((!((BoolLiteral) lhs).value && !((BoolLiteral) rhs).value) && operator == Operation.Operator.OR)
                            astNodes.add(node);
                        else if ((!((BoolLiteral) lhs).value || !((BoolLiteral) rhs).value) && operator == Operation.Operator.AND)
                            astNodes.add(node);
                    }
                }
            }
    }
}
