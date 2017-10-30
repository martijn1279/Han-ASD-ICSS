package nl.han.ica.icss.parser;

import nl.han.ica.icss.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {

    private AST ast;

    public ASTListener() {
        ast = new AST();
    }

    public AST getAST() {
        return ast;
    }

    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.setRoot(new Stylesheet());
    }

    @Override
    public void enterStylesheetPart(ICSSParser.StylesheetPartContext ctx) {
        if (ctx.stylerule() != null) {
            Stylerule stylerule = new Stylerule();
            stylerule.selector = createSelector(ctx.stylerule().selector());
            stylerule.body = createDeclaration(ctx.stylerule().declaration());
            stylerule.condition = createCondition(ctx.stylerule().statement());
            ast.root.addChild(stylerule);
        } else if (ctx.assignment() != null) {
            Assignment assignment = new Assignment();
            assignment.name = new VariableReference(ctx.assignment().variableReference().getText());
            assignment.expression = createExpression(ctx.assignment().expression());
            ast.root.addChild(assignment);
        }
    }

    private Expression createCondition(ICSSParser.StatementContext statement) {
        if (statement != null) {
            if (statement.additionalExpression().size() > 0) {
                int size = statement.additionalExpression().size();
                Expression operation = null;
                if (size >= 2) {
                    for (int i = size; i >= 2; i--) {
                        if (operation == null) {
                            Expression tmpLeft = createExpression(statement.additionalExpression(i - 2).expression());
                            Expression tmpRight = createExpression(statement.additionalExpression(i - 1).expression());
                            operation = createLogicalOperation(statement.additionalExpression(i - 1).logicalOperator(), tmpLeft, tmpRight);
                        } else {
                            Expression tmpLeft = createExpression(statement.additionalExpression(i - 2).expression());
                            operation = createLogicalOperation(statement.additionalExpression(i - 1).logicalOperator(), tmpLeft, operation);
                        }
                    }
                } else {
                    operation = createExpression(statement.additionalExpression(0).expression());
                }
                Expression tmpLeft = createExpression(statement.expression());
                return createLogicalOperation(statement.additionalExpression(0).logicalOperator(), tmpLeft, operation);
            } else {
                return createExpression(statement.expression());
            }
        }
        return null;
    }

    private Operation createLogicalOperation(ICSSParser.LogicalOperatorContext logicalOperatorContext, Expression tmpLeft, Expression tmpRight) {
        Operation operation = new Operation();
        operation.operator = createOperatorLogical(logicalOperatorContext);
        operation.lhs = tmpLeft;
        operation.rhs = tmpRight;
        return operation;
    }

    private ArrayList<ASTNode> createDeclaration(List<ICSSParser.DeclarationContext> declarations) {
        ArrayList<ASTNode> declarationList = new ArrayList<>();
        for (ICSSParser.DeclarationContext decl : declarations) {
            Declaration temp = new Declaration();
            temp.property = decl.ATTRIBUTES().getText();
            temp.expression = createExpression(decl.expression());
            declarationList.add(temp);
        }
        return declarationList;
    }

    private Expression createExpression(ICSSParser.ExpressionContext expressionContext) {
        if (expressionContext.value() != null)
            return createValue(expressionContext.value());
        if (expressionContext.operation() != null) {
            if (expressionContext.operation().operator().calcOperator() != null)
                return createOperation(createOperatorCalc(expressionContext.operation().operator().calcOperator()),
                        expressionContext.operation().value().get(0),
                        expressionContext.operation().value().get(1));
            if (expressionContext.operation().operator().relationalOperator() != null)
                return createOperation(createOperatorRelational(expressionContext.operation().operator().relationalOperator()),
                        expressionContext.operation().value().get(0),
                        expressionContext.operation().value().get(1));
        }
        return null;
    }

    private Expression createOperation(Operation.Operator calcOperatorContexts, ICSSParser.ValueContext valueContext, ICSSParser.ValueContext valueContext1) {
        Operation operation = new Operation();
        operation.operator = calcOperatorContexts;
        operation.lhs = createValue(valueContext);
        operation.rhs = createValue(valueContext1);
        return operation;
    }

    private Expression createValue(ICSSParser.ValueContext valueContext) {
        if (valueContext.literal() != null)
            return createLiteral(valueContext.literal());
        if (valueContext.variableReference() != null)
            return createVariable(valueContext.variableReference());
        return null;
    }

    private VariableReference createVariable(ICSSParser.VariableReferenceContext variableReferenceContext) {
        return new VariableReference(variableReferenceContext.getText());
    }

    private Operation.Operator createOperatorCalc(ICSSParser.CalcOperatorContext termNode) {
        if (termNode.getText().equals("+"))
            return Operation.Operator.PLUS;
        if (termNode.getText().equals("-"))
            return Operation.Operator.MIN;
        if (termNode.getText().equals("/"))
            return Operation.Operator.DIV;
        if (termNode.getText().equals("*"))
            return Operation.Operator.MUL;
        return null;
    }

    private Operation.Operator createOperatorRelational(ICSSParser.RelationalOperatorContext termNode) {
        if (termNode.getText().equals(">"))
            return Operation.Operator.GT;
        if (termNode.getText().equals("<"))
            return Operation.Operator.LW;
        if (termNode.getText().equals("=="))
            return Operation.Operator.EQ;
        return null;
    }

    private Operation.Operator createOperatorLogical(ICSSParser.LogicalOperatorContext termNode) {
        if (termNode.getText().equals("||"))
            return Operation.Operator.OR;
        if (termNode.getText().equals("&&"))
            return Operation.Operator.AND;
        return null;
    }

    private Literal createLiteral(ICSSParser.LiteralContext literal) {
        if (literal.LITERAL_BOOL() != null)
            return new BoolLiteral(literal.LITERAL_BOOL().getText());
        if (literal.LITERAL_COLOR() != null)
            return new ColorLiteral(literal.LITERAL_COLOR().getText());
        if (literal.LITERAL_PERCENTAGE() != null)
            return new PercentageLiteral(literal.LITERAL_PERCENTAGE().getText());
        if (literal.LITERAL_PIXELS() != null)
            return new PixelLiteral(literal.LITERAL_PIXELS().getText());
        return null;
    }

    private Selector createSelector(ICSSParser.SelectorContext selector) {
        if (selector.SELECTOR_TAG() != null)
            return new TagSelector(selector.SELECTOR_TAG().getText());
        if (selector.SELECTOR_CLASS() != null)
            return new ClassSelector(selector.SELECTOR_CLASS().getText());
        if (selector.SELECTOR_ID() != null)
            return new IdSelector(selector.SELECTOR_ID().getText());
        return null;
    }
}

