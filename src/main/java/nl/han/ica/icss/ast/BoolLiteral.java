package nl.han.ica.icss.ast;

public class BoolLiteral extends Literal {
    public boolean value;

    public BoolLiteral(boolean value) {
        this.value = value;
    }
    public BoolLiteral(String text) {
        this.value = text.equals("true");
    }
    @Override
    public String getNodeLabel() {
        return "Bool literal (" + (this.value ? "true" : "false") + ")";
    }
}
