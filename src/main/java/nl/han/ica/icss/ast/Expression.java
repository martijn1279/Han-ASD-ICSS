package nl.han.ica.icss.ast;

public abstract class Expression extends ASTNode {

    public enum Type {
        PIXEL,
        PERCENTAGE,
        COLOR,
        MIXED,  // the result of a calculation existing of mulitiple types
        UNKNOWN // not (yet) known because the value is not yet found.
    }
}
