package schach;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import schach.bot.Bot;
import schach.figures.Figure;

import java.util.Random;

public class PlayingField
{
	private GridPane gridPaneMain;
	private Controller controller;
	private FigureList figures = new FigureList();
	private Figure active = null;
	private boolean isBotActive = true;
	private boolean isWhiteNow = true;
	private Random random = new Random();
	private int difficulty = 1;
	private int roundWithoutStuff = 0;

	PlayingField(GridPane gridPaneMain, Controller controller)
	{
		this.gridPaneMain = gridPaneMain;
		this.controller = controller;

		InvalidationListener scaleField = e -> scaleField();
		gridPaneMain.heightProperty().addListener(scaleField);
		gridPaneMain.widthProperty().addListener(scaleField);

		makeGrid();
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean checkIfMovePossible()
	{
		for (Figure value : figures.getArray())
		{
			if (value.isWhite() == isWhiteNow && !value.getType().equals(""))
			{
				value.setReachableFields(figures);
				for (Figure check : value.getCanReach())
				{
					if (check.isReachable())
					{
						return true;
					}
				}
				figures.resetReachable();
				value.getCanReach().clear();
			}
		}
		return false;
	}

	private void makeGrid()
	{
		for (int i = 0; i < 8; i++)
		{
			gridPaneMain.getColumnConstraints().add(new ColumnConstraints());
			gridPaneMain.getRowConstraints().add(new RowConstraints());
		}

		sortFigures();
	}

	//moves a figure from a starting to a end position
	public Figure moveFigure(Figure source, Figure target)
	{
		Figure figure = figures.moveFigure(source, target, this);

		scaleField();

		return figure;
	}

	void removeFigure(Figure figure)
	{
		figures.setFigureToCoordinate(figure.getX(),figure.getY(),null);
		Platform.runLater(() -> gridPaneMain.getChildren().remove(figure));

		if (figure.getType().equals("King"))
		{
			if (figure.isWhite())
			{
				Platform.runLater(() -> controller.end(false));
			}
			else
			{
				Platform.runLater(() -> controller.end(true));
			}
		}
	}

	void scaleField()
	{
		int size;

		if (gridPaneMain.getHeight() < gridPaneMain.getWidth())
		{
			size = (int) gridPaneMain.getHeight() / 8 - 10;
		}
		else
		{
			size = (int) gridPaneMain.getWidth() / 8 - 10;
		}

		int finalSize = size;
		for (Figure v : figures.getArray())
		{
			v.setPrefSize(finalSize, finalSize);
		}
	}

	void setFigureToCoordinate(int x, int y, Figure figure)
	{
		Figure temp = figures.getFigureAt(x, y);
		int index =  x + y * 8;

		figure.setOnAction(e ->
				{
					//noinspection ConstantConditions
					if (isWhiteNow || !isWhiteNow && !isBotActive)
					{
						System.out.println(figure.getType());
						System.out.println(figure.getX() + "|" + figure.getY());

						if (figure.isWhite())
						{
							System.out.println("W");
						}
						else
						{
							System.out.println("B");
						}
						System.out.println("-----");


						if (this.getActive() != null && figure.isReachable())
						{
							this.moveFigure(this.getActive(), figure);
							this.setActive(null);
							this.sortFigures();

							setWhiteNow(!isWhiteNow);

							if (!checkIfMovePossible())
							{
								controller.end(!isWhiteNow);
							}
							else
							{
								boolean draw = checkDraw(figure);
								if (isBotActive&&!draw)
								{
									new Bot(this, random, difficulty).start();
								}
								else if(draw)
								{
									System.out.println("Draw");
								}
							}



							figures.resetReachable();

						}
						else
						{
							figures.resetReachable();
							if (isWhiteNow == figure.isWhite())
							{
								this.setActive(figure);
								figure.setReachableFields(figures);
							}
						}
					}
				}
		);

		figures.setFigureToCoordinate(x,y,figure);

		if (temp != null)
		{
			if (temp.getX() == x && temp.getY() == y)
			{
				removeFigure(temp);
			}
		}

		figure.setCoordinate(x, y);
		Platform.runLater(() -> gridPaneMain.add(figure, x, y));
	}

	public boolean checkDraw(Figure figure)
	{
		if (figure.getType().equals(""))
		{
			roundWithoutStuff++;

			if (roundWithoutStuff >= 50)
			{
				roundWithoutStuff = 0;
				controller.end();
				return true;
			}
		}
		else
		{
			roundWithoutStuff = 0;
		}
		return false;
	}

	void sortFigures()
	{
		figures.sort();
	}

	private Figure getActive()
	{
		return active;
	}

	private void setActive(Figure active)
	{
		this.active = active;
	}

	GridPane getGridPaneMain()
	{
		return gridPaneMain;
	}

	public FigureList getFigures()
	{
		return figures;
	}

	public void setWhiteNow(boolean whiteNow)
	{
		isWhiteNow = whiteNow;
		controller.setGameState(figures.getGameState());
	}

	boolean isBotActive()
	{
		return isBotActive;
	}

	void setBotActive(boolean botActive)
	{
		isBotActive = botActive;
	}

	void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}

	public Controller getController()
	{
		return controller;
	}
}


