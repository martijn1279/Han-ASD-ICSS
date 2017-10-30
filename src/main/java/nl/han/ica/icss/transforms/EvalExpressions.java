package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;
import java.util.HashMap;

public class EvalExpressions implements Transform {

    private HashMap<String, Assignment> symboltable;

    @Override
    public void apply(AST ast) {
        symboltable = ast.symboltable;
        ArrayList<ASTNode> body = ast.root.body;

        for (ASTNode nodes : body) {
            if (nodes instanceof Assignment)
                symboltable.put(((Assignment) nodes).name.name, (Assignment) nodes);
        }
        replace(ast.root);
        calculate(ast.root);
    }

    private void replace(ASTNode node) {
        if (node instanceof Operation) {
            Operation operation = (Operation) node;
            operation.lhs = getVariableRefrence(((Operation) node).lhs);
            operation.rhs = getVariableRefrence(((Operation) node).rhs);
        }
        if (node instanceof Declaration) {
            Declaration declaration = (Declaration) node;
            declaration.expression = getVariableRefrence(declaration.expression);
        }
        if (node instanceof Stylerule) {
            Stylerule stylerule = (Stylerule) node;
            if (stylerule.condition != null)
                ((Stylerule) node).condition = getVariableRefrence(((Stylerule) node).condition);
        }
        for (ASTNode child : node.getChildren())
            replace(child);
    }

    private void calculate(ASTNode node) {
        if (node instanceof Operation) {
            Operation operation = (Operation) node;
            if (operation.lhs instanceof Operation)
                operation.lhs = execute((Operation) operation.lhs);
            if (operation.rhs instanceof Operation)
                operation.rhs = execute((Operation) operation.rhs);
        }
        if (node instanceof Declaration) {
            Declaration declaration = (Declaration) node;
            if (declaration.expression instanceof Operation)
                declaration.expression = execute((Operation) declaration.expression);
        }
        if (node instanceof Assignment) {
            Assignment assignment = (Assignment) node;
            if (assignment.expression instanceof Operation)
                assignment.expression = execute((Operation) assignment.expression);
        }
        for (ASTNode child : node.getChildren())
            calculate(child);

    }

    private Expression getVariableRefrence(ASTNode nodes) {
        if (nodes instanceof VariableReference) {
            Assignment assignment = symboltable.get(((VariableReference) (nodes)).name);
            if (assignment == null)
                return null;
            return assignment.expression;
        }
        return (Expression) nodes;
    }

    private Literal execute(Operation expression) {
        Expression l = expression.lhs;
        Expression r = expression.rhs;
        Operation.Operator operator = expression.operator;
        switch (operator) {
            case PLUS:
            case MIN:
            case DIV:
            case MUL:
                if (l instanceof PixelLiteral)
                    return new PixelLiteral(calc(((PixelLiteral) l).value, ((PixelLiteral) r).value, operator));
                if (l instanceof PercentageLiteral)
                    return new PercentageLiteral(calc(((PercentageLiteral) l).value, ((PercentageLiteral) r).value, operator));
                break;
            case EQ:
            case GT:
            case LW:
                if (l instanceof PixelLiteral)
                    return new BoolLiteral(relational(((PixelLiteral) l).value, ((PixelLiteral) r).value, operator));
                if (l instanceof PercentageLiteral)
                    return new BoolLiteral(relational(((PercentageLiteral) l).value, ((PercentageLiteral) r).value, operator));
                break;
            case OR:
            case AND:
                if (operator == Operation.Operator.AND)
                    return new BoolLiteral(((BoolLiteral) l).value && ((BoolLiteral) l).value);
                else if (operator == Operation.Operator.OR)
                    return new BoolLiteral(((BoolLiteral) l).value || ((BoolLiteral) l).value);
                break;
        }
        return null;
    }

    private int calc(int l, int r, Operation.Operator operator) {
        switch (operator) {
            case MIN:
                return l - r;
            case PLUS:
                return l + r;
            case DIV:
                return l + r;
            case MUL:
                return l + r;
            default:
                return 0;
        }
    }

    private boolean relational(int left, int right, Operation.Operator operator) {
        switch (operator) {
            case EQ:
                return left == right;
            case GT:
                return left > right;
            case LW:
                return left < right;
            default:
                return false;
        }
    }
}
