package schach;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import schach.figures.Empty;
import schach.figures.Figure;

import java.util.ArrayList;

public class PlayingField
{
	private GridPane gridPaneMain;
	private Controller controller;
	private ArrayList<Figure> figures = new ArrayList<>();
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

		resetReachable();
		sortFigures();
		scaleField();
	}

	private void removeFigure(Figure figure)
	{
		figures.remove(figure);
		Platform.runLater(() -> gridPaneMain.getChildren().remove(figure));

		if(figure.getType().equals("King"))
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
		if (x + y * 8 < figures.size() && x + y * 8 >= 0 && x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return figures.get(x + y * 8);
		}
		else
		{
			return null;
		}
	}

	void sortFigures()
	{
		figures.sort((a, b) ->
		{
			if ((a.getX() + a.getY() * 8) < (b.getX() + b.getY() * 8))
			{
				return -1;
			}
			else if ((a.getX() + a.getY() * 8) > (b.getX() + b.getY() * 8))
			{
				return 1;
			}
			return 0;
		});

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


