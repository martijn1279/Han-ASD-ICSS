package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;

public class Generator {

	public String generate(AST ast) {
        StringBuilder builder = new StringBuilder();
        for (ASTNode node : ast.root.getChildren()) {
            if (node instanceof Stylerule)
                addStylerule((Stylerule) node, builder);
        }
        return builder.toString();
    }

    private void addStylerule(Stylerule stylerule, StringBuilder builder) {
        builder.append(stylerule.selector)
                .append(" {")
                .append(System.lineSeparator());
        for (ASTNode node : stylerule.getChildren()) {
            if (node instanceof Declaration)
                addDecleration(builder, (Declaration) node);
        }
        builder.append('}')
                .append(System.lineSeparator());
    }

    private void addDecleration(StringBuilder builder, Declaration node) {
        builder.append("    ")
                .append(node.property)
                .append(": ")
                .append(getValue(node.expression))
                .append(';')
                .append(System.lineSeparator());
    }

    private String getValue(Expression expression) {
        if (expression instanceof PercentageLiteral)
            return ((PercentageLiteral) expression).value + "%";
        if (expression instanceof PixelLiteral)
            return ((PixelLiteral) expression).value + "px";
        if (expression instanceof ColorLiteral)
            return ((ColorLiteral) expression).value;
        return null;
    }
}
