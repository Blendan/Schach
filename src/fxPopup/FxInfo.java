package fxPopup;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FxInfo extends FxPopup
{
	private Label lblInfo = new Label("FxInfo");
	private Button btnOk = new Button("OK");

	public FxInfo()
	{
		super();
		title = "FxInfo";

		addContent(lblInfo);

		btnOk.setOnAction(event->close());

		addButton(btnOk);
	}

	public Button getBtnOk()
	{
		return btnOk;
	}

	public void setInfo(String info)
	{
		lblInfo.setText(info);
	}
}
