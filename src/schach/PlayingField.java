package schach;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import schach.figures.Empty;
import schach.figures.Figure;
import schach.figures.King;

import java.util.ArrayList;

public class PlayingField
{
	private GridPane gridPaneMain;
	private Controller controller;
	private FigureList figures = new FigureList();
	private Figure active = null;
	private boolean isWhiteNow = true;

	PlayingField(GridPane gridPaneMain, Controller controller)
	{
		this.gridPaneMain = gridPaneMain;
		this.controller = controller;

		InvalidationListener scaleField = e -> scaleField();
		gridPaneMain.heightProperty().addListener(scaleField);
		gridPaneMain.widthProperty().addListener(scaleField);

		makeGrid();
	}

	private boolean checkIfMovePossible()
	{
		for (Figure value : figures)
		{
			if (value.isWhite() == isWhiteNow && !value.getType().equals(""))
			{
				value.setReachableFields(this);
				for (Figure check : figures)
				{
					if(check.isReachable())
					{
						return true;
					}
				}
				resetReachable();
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

	void resetReachable()
	{
		for (Figure value : figures)
		{
			value.setReachable(false);

			if (value.getType().equals(""))
			{
				((Empty) value).setRochadeTarget(false);
			}
			else if (value.getType().equals("King"))
			{
				((King) value).setInRochade(false);
				((King) value).setTowerLeft(null);
				((King) value).setTowerRight(null);
			}
		}
	}

	//moves a figure from a starting to a end position
	private void moveFigure(Figure source, Figure target)
	{
		int x = source.getX();
		int y = source.getY();

		int toX = target.getX();
		int toY = target.getY();

		source.setMoved(true);

		//removes the figure form the GridPane
		Platform.runLater(() -> gridPaneMain.getChildren().remove(source));

		//removes the target figure
		removeFigure(target);

		//adds the figure to the target position
		setFigureToCoordinate(toX, toY, source);


		//adds an empty figure to the start position
		Figure empty = new Empty();
		setFigureToCoordinate(x, y, empty);

		if (source.getType().equals("King"))
		{
			King king = (King) source;
			if (king.isInRochade())
			{
				if (target.getType().equals(""))
				{
					if (((Empty) target).isRochadeTarget())
					{
						if (target.getX() == 2 && king.getTowerRight() != null)
						{
							moveFigure(king.getTowerLeft(), getFigureAt(3, y));
						}
						else if (target.getX() == 6 && king.getTowerRight() != null)
						{
							moveFigure(king.getTowerRight(), getFigureAt(5, y));
						}
					}
				}
				king.setInRochade(false);
			}
		}

		resetReachable();
		sortFigures();
		scaleField();
	}

	private void removeFigure(Figure figure)
	{
		figures.remove(figure);
		Platform.runLater(() -> gridPaneMain.getChildren().remove(figure));

		if (figure.getType().equals("King"))
		{
			if (figure.isWhite())
			{
				controller.end(false);
			}
			else
			{
				controller.end(true);
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
		figures.forEach(v -> v.setPrefSize(finalSize, finalSize));
	}

	void setFigureToCoordinate(int x, int y, Figure figure)
	{
		boolean removedOne = false;
		Figure temp = getFigureAt(x, y);
		if (temp != null)
		{
			if (temp.getX() == x && temp.getY() == y)
			{
				System.out.println("r");
				removeFigure(temp);
				removedOne = true;
			}
		}

		if (!figures.contains(figure))
		{
			figure.setOnAction(e ->
					{
						System.out.println(figure.getType());
						System.out.println(figure.getX() + "|" + figure.getY());
						System.out.println(figure.getX() + figure.getY() * 8 + "|" + figures.lastIndexOf(figure));

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
							this.resetReachable();
							this.setActive(null);
							this.sortFigures();

							isWhiteNow = !isWhiteNow;
						}
						else
						{
							this.resetReachable();
							if (isWhiteNow == figure.isWhite())
							{
								this.setActive(figure);
								figure.setReachableFields(this);
							}
						}
					}
			);
			figures.add(figure);
		}

		figure.setCoordinate(x, y);
		Platform.runLater(() -> gridPaneMain.add(figure, x, y));

		if (removedOne)
		{
			sortFigures();
		}
	}

	public Figure getFigureAt(int x, int y)
	{
		return figures.getFigureAt(x,y);
	}

	void sortFigures()
	{
		figures.sort();

		boolean isBlack = false;
		int lastY = 0;

		for (Figure value : figures)
		{
			value.getStyleClass().clear();

			if (lastY < value.getY())
			{
				lastY = value.getY();
				isBlack = !isBlack;

			}

			if (isBlack)
			{
				value.getStyleClass().add("btn-black");
			}
			else
			{
				value.getStyleClass().add("btn-white");
			}

			isBlack = !isBlack;
		}
	}

	private Figure getActive()
	{
		return active;
	}

	private void setActive(Figure active)
	{
		this.active = active;
	}
}


