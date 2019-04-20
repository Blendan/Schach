package fxPopup;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FxInput extends FxPopup
{
	private Label lblInfo = new Label("FxInput:");
	private Button btnOk = new Button("OK");
	private Button btnCancel = new Button("Cancel");
	private TextField input = new TextField();

	FxInput()
	{
		title = "FxInput";

		addContent(lblInfo);
		addContent(input);
		input.setOnAction(e->btnOk.requestFocus());

		btnOk.setOnAction(event -> close());
		btnCancel.setOnAction(event -> close());

		addButton(btnOk);
		addButton(btnCancel);

	}

	public Button getBtnOk()
	{
		return btnOk;
	}

	public TextField getInput()
	{
		return input;
	}

	public void setInfo(String info)
	{
		lblInfo.setText(info);
	}

	public Button getBtnCancel()
	{
		return btnCancel;
	}
}
