package schach;

import fxPopup.FxInfo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import schach.figures.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

	@FXML
	private Label lblGameState;

	@FXML
	private ComboBox<String> comboBoxDifficulty;

	@FXML
	private GridPane gridPaneMain;

	@FXML
	private Button btnBot;

	@FXML
	private Button btnReset;

	private PlayingField playingField;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		playingField = new PlayingField(gridPaneMain, this);

		for(int i = 1; i <= 4; i ++)
		{
			comboBoxDifficulty.getItems().add(i+"");
		}

		comboBoxDifficulty.getSelectionModel().select(0);

		btnBot.setOnAction(e->toggleBot());
		comboBoxDifficulty.setOnAction(e->setDifficulty());

		placeFigures();

		btnReset.setOnAction(e->placeFigures());
	}

	private void setDifficulty()
	{
		if(comboBoxDifficulty.getSelectionModel().getSelectedItem()!=null)
		{
			playingField.setDifficulty(Integer.parseInt(comboBoxDifficulty.getSelectionModel().getSelectedItem()));
		}
	}

	private void toggleBot()
	{
		playingField.setBotActive(!playingField.isBotActive());

		if(playingField.isBotActive())
		{
			btnBot.setText("Bot ON");
		}
		else
		{
			btnBot.setText("Bot OFF");
		}
	}

	private void placeFigures()
	{
		playingField.setWhiteNow(true);
		playingField.getFigures().clear();

		gridPaneMain.getChildren().clear();
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				playingField.setFigureToCoordinate(i, j, new Empty());
			}
		}
		playingField.sortFigures();

		for (int i = 0; i < 8; i++)
		{
			Figure peasantWhite = new Peasant(true);
			Figure peasantBlack = new Peasant(false);

			playingField.setFigureToCoordinate(i, 1, peasantBlack);
			playingField.setFigureToCoordinate(i, 6, peasantWhite);
		}

		playingField.setFigureToCoordinate(0, 0, new Tower(false));
		playingField.setFigureToCoordinate(7, 0, new Tower(false));

		playingField.setFigureToCoordinate(0, 7, new Tower(true));
		playingField.setFigureToCoordinate(7, 7, new Tower(true));

		playingField.setFigureToCoordinate(6, 0, new Horse(false));
		playingField.setFigureToCoordinate(1, 0, new Horse(false));

		playingField.setFigureToCoordinate(6, 7, new Horse(true));
		playingField.setFigureToCoordinate(1, 7, new Horse(true));

		playingField.setFigureToCoordinate(5, 0, new Runner(false));
		playingField.setFigureToCoordinate(2, 0, new Runner(false));

		playingField.setFigureToCoordinate(5, 7, new Runner(true));
		playingField.setFigureToCoordinate(2, 7, new Runner(true));

		playingField.setFigureToCoordinate(3, 0, new Queen(false));
		playingField.setFigureToCoordinate(3, 7, new Queen(true));

		playingField.setFigureToCoordinate(4, 0, new King(false));

		playingField.setFigureToCoordinate(4, 7, new King(true));

		playingField.getFigures().sort();
		playingField.getFigures().colorFields();
		playingField.getFigures().resetReachable();
		playingField.scaleField();
	}

	public void end(boolean isWinnerWhite)
	{
		stopGame();
		FxInfo fxInfo = new FxInfo();
		fxInfo.setTitle("Someone Won");

		if (isWinnerWhite)
		{
			fxInfo.setInfo("Player White has Won");
		}
		else
		{
			fxInfo.setInfo("Player Black has Won");
		}

		fxInfo.show();
	}

	void setGameState(int state)
	{
		Platform.runLater(()->lblGameState.setText(Integer.toString(state)));
	}

	void end()
	{
		stopGame();
		FxInfo fxInfo = new FxInfo();
		fxInfo.setTitle("Someone Won");
		fxInfo.setInfo("Draw");
		fxInfo.show();
	}

	private void stopGame()
	{
		playingField.getFigures().forEach(v->v.setOnAction(e->{}));
	}
}
