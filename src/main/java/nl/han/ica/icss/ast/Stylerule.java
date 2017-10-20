package nl.han.ica.icss.ast;

import java.util.ArrayList;

public class Stylerule extends ASTNode {
	
	public Selector selector;
	public Expression condition;
	public ArrayList<ASTNode> body;

	public Stylerule() {
		selector = null;
		condition = null;
		body = new ArrayList<>();
	}

	@Override
	public String getNodeLabel() {

        if(condition != null)
            return "Conditional stylerule";
        else
            return "Stylerule";
	}
	@Override
	public ArrayList<ASTNode> getChildren() {
		ArrayList<ASTNode> children = new ArrayList<>();
		if(selector != null)
			children.add(selector);
		if(condition != null)
			children.add(condition);
		children.addAll(body);

		return children;
	}

    @Override
    public void addChild(ASTNode child) {
		if(child instanceof Selector)
			selector = (Selector) selector;
		if(child instanceof Expression)
		    condition = (Expression) child;
		else
        	body.add(child);
    }
    @Override
    public void removeChild(ASTNode child) {
        if(selector == child)
            selector = null;
        else if(condition == child)
            condition = null;
        else
            body.remove(child);
    }

}
