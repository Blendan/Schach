package fxPopup;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

class Custom extends FxPopup
{
	Custom()
	{
		super();
		title = "Custom";
	}

	public void addContent(Node node)
	{
		super.addContent(node);
	}

	public void addButton(Button button)
	{
		super.addButton(button);
	}
}
