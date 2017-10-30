package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Checker {

    private HashMap<String, Assignment> symboltable;

    public void check(AST ast) {
        symboltable = new HashMap<>();
        ArrayList<ASTNode> body = ast.root.body;

        for (ASTNode nodes : body) {
            if (nodes instanceof Assignment)
                symboltable.put(((Assignment) nodes).name.name, (Assignment) nodes);
        }
        for (ASTNode node : body) {
            if (node instanceof Stylerule)
                checkUndefinedVariables(node);
            checkOperation(node);
            checkStyleType(node);
            checkBooleanExpression(node);
        }
        ast.symboltable = symboltable;

        if (ast.getErrors().isEmpty()) {
            ast.checked = true;
        }
    }


    private void checkUndefinedVariables(ASTNode astNode) {
        for (ASTNode node : astNode.getChildren()) {
            if (node instanceof VariableReference) {
                boolean undefined = false;
                for (Map.Entry<String, Assignment> entry : symboltable.entrySet()) {
                    if (entry.getValue().name.name.equals(((VariableReference) node).name))
                        undefined = true;
                }
                if (!undefined)
                    node.setError("Reference " + ((VariableReference) node).name + " is referencing an unassigned constant.");
            }
            if (!node.getChildren().isEmpty())
                checkUndefinedVariables(node);
        }
    }

    private void checkOperation(ASTNode astNode) {
        for (ASTNode nodes : astNode.getChildren()) {
            if (nodes instanceof Operation) {
                if (((Operation) nodes).operator != Operation.Operator.AND && ((Operation) nodes).operator != Operation.Operator.OR) {
                    Expression left = (((Operation) nodes).lhs instanceof VariableReference) ? getVariableRefrence(((Operation) nodes).lhs) : ((Operation) nodes).lhs;
                    Expression right = (((Operation) nodes).rhs instanceof VariableReference) ? getVariableRefrence(((Operation) nodes).rhs) : ((Operation) nodes).rhs;
                    if ((left != null && right != null) && (!left.getClass().equals(right.getClass()) || (left instanceof BoolLiteral && left.getClass().equals(right.getClass()))))
                        nodes.setError("Reference is referencing an unassigned constant.");
                } else if (((Operation) nodes).operator == Operation.Operator.AND || ((Operation) nodes).operator == Operation.Operator.OR) {
                    if (((Operation) nodes).lhs instanceof Operation && (((Operation) ((Operation) nodes).lhs).operator == Operation.Operator.PLUS || ((Operation) ((Operation) nodes).lhs).operator == Operation.Operator.MIN))
                        ((Operation) nodes).lhs.setError("Must be a Boolean ");
                    if (((Operation) nodes).rhs instanceof Operation && (((Operation) ((Operation) nodes).rhs).operator == Operation.Operator.PLUS || ((Operation) ((Operation) nodes).rhs).operator == Operation.Operator.MIN))
                        ((Operation) nodes).rhs.setError("Must be a Boolean ");
                }
            }
            if (!nodes.getChildren().isEmpty())
                checkOperation(nodes);
        }
    }

    private void checkStyleType(ASTNode ast) {
        for (ASTNode nodes : ast.getChildren()) {
            if (nodes instanceof Declaration) {
                Expression expression;
                if (((Declaration) nodes).expression instanceof VariableReference)
                    expression = getVariableRefrence(((Declaration) nodes).expression);
                else if (((Declaration) nodes).expression instanceof Operation) {
                    if (((Operation) ((Declaration) nodes).expression).lhs instanceof VariableReference) {
                        expression = getVariableRefrence(((Operation) ((Declaration) nodes).expression).lhs);
                    } else
                        expression = ((Operation) ((Declaration) nodes).expression).lhs;
                } else {
                    expression = ((Declaration) nodes).expression;
                }
                switch (((Declaration) nodes).property) {
                    case "color":
                    case "background-color":
                        if (!(expression instanceof ColorLiteral))
                            nodes.setError("Not the correct style type");
                        break;
                    case "width":
                    case "height":
                        if (!(expression instanceof PixelLiteral) && !(expression instanceof PercentageLiteral))
                            nodes.setError("Not the correct style type");
                        break;
                    default:
                        break;
                }
            }
            if (!nodes.getChildren().isEmpty())
                checkOperation(nodes);
        }
    }

    private void checkBooleanExpression(ASTNode ast) {
        for (ASTNode nodes : ast.getChildren()) {
            if (nodes instanceof Operation) {
                if (((Operation) nodes).operator == Operation.Operator.OR || ((Operation) nodes).operator == Operation.Operator.AND) {
                    if (((Operation) nodes).rhs instanceof PercentageLiteral || ((Operation) nodes).rhs instanceof PixelLiteral || ((Operation) nodes).rhs instanceof ColorLiteral)
                        ((Operation) nodes).rhs.setError("Not the correct condition type");
                    if (((Operation) nodes).lhs instanceof PercentageLiteral || ((Operation) nodes).lhs instanceof PixelLiteral || ((Operation) nodes).lhs instanceof ColorLiteral)
                        ((Operation) nodes).lhs.setError("Not the correct condition type");
                }
            }
            if (!nodes.getChildren().isEmpty())
                checkBooleanExpression(nodes);
        }
        if (ast instanceof Stylerule && ((Stylerule) ast).condition != null)
            if (((Stylerule) ast).condition instanceof PercentageLiteral || ((Stylerule) ast).condition instanceof ColorLiteral || ((Stylerule) ast).condition instanceof PixelLiteral)
                ast.setError("Not the correct condition type");
    }

    private Expression getVariableRefrence(ASTNode nodes) {
        Assignment assignment = symboltable.get(((VariableReference) (nodes)).name);
        if (assignment == null)
            return null;
        return assignment.expression;
    }
}

