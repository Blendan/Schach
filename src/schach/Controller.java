package schach;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import schach.figures.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{

	@FXML
	private GridPane gridPaneMain;

	private PlayingField playingField;

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		playingField = new PlayingField(gridPaneMain, this);

		placeFigures();
	}

	private void placeFigures()
	{

		for (int i = 0; i < 8; i ++)
		{
			for (int j = 0; j < 8; j ++)
			{
				playingField.setFigureToCoordinate(i,j,new Empty());
			}
		}
		playingField.sortFigures();

		for (int i = 0 ; i < 8; i ++)
		{
			Figure peasantWhite = new Peasant(true);
			Figure peasantBlack = new Peasant(false);

			playingField.setFigureToCoordinate(i,1,peasantBlack);
			playingField.setFigureToCoordinate(i,6,peasantWhite);
		}

		playingField.setFigureToCoordinate(0,0,new Tower(false));
		playingField.setFigureToCoordinate(7,0,new Tower(false));

		playingField.setFigureToCoordinate(0,7,new Tower(true));
		playingField.setFigureToCoordinate(7,7,new Tower(true));

		playingField.setFigureToCoordinate(6,0,new Horse(false));
		playingField.setFigureToCoordinate(1,0,new Horse(false));

		playingField.setFigureToCoordinate(6,7,new Horse(true));
		playingField.setFigureToCoordinate(1,7,new Horse(true));

		playingField.setFigureToCoordinate(5,0,new Runner(false));
		playingField.setFigureToCoordinate(2,0,new Runner(false));

		playingField.setFigureToCoordinate(5,7,new Runner(true));
		playingField.setFigureToCoordinate(2,7,new Runner(true));

		playingField.setFigureToCoordinate(3,0,new Queen(false));
		playingField.setFigureToCoordinate(3,7,new Queen(true));

		King king = new King(false);
		playingField.setFigureToCoordinate(4,0,king);
		playingField.setKingBlack(king);

		king = new King(true);
		playingField.setFigureToCoordinate(4,7,king);
		playingField.setKingWhite(king);


		playingField.sortFigures();
		playingField.resetReachable();
		playingField.scaleField();
	}

	void end(boolean isWinnerWhite)
	{
		//TODO end message
	}
}
