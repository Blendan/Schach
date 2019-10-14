package schach;

import javafx.application.Platform;
import schach.figures.*;

@SuppressWarnings("ClassEscapesDefinedScope")
public class FigureList
{
	private int value = 0;
	private Figure[][] figures = new Figure[8][8];

	public FigureList()
	{
		figures = new Figure[8][8];
	}

	public FigureList(Figure[][] figures, int value)
	{
		this.figures = figures;
		this.value = value;
	}

	void sort()
	{

	}

	void clear()
	{
		figures = new Figure[8][8];
	}

	void colorFields()
	{
		boolean isBlack = false;
		int lastY = 0;

		for (Figure[] valueArray : figures)
		{
			for (Figure value : valueArray)
			{
				if(value !=null)
				{
					try
					{
						value.getStyleClass().clear();
					}
					catch (NullPointerException e)
					{
						System.out.println("That shit again (" + e.getMessage() + ")");
					}

					if (lastY < value.getY())
					{
						lastY = value.getY();
						isBlack = !isBlack;

					}

					if (!value.isMarked())
					{
						if (isBlack)
						{
							value.getStyleClass().add("btn-black");
						}
						else
						{
							value.getStyleClass().add("btn-white");
						}
					}
				}

				isBlack = !isBlack;
			}
		}
	}

	public Figure getFigureAt(int x, int y)
	{
		if (x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return figures[x][y];
		}
		else
		{
			return null;
		}
	}

	public Figure getFigureAt(Figure figure)
	{
		int x = figure.getX();
		int y = figure.getY();

		if (x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return figures[x][y];
		}
		else
		{
			return null;
		}
	}

	public void setFigureToCoordinate(int x, int y, Figure figure)
	{
		figures[x][y] = figure;

		if(figure!=null)
		{
			figure.setCoordinate(x, y);
			System.out.println(x+"  "+y);
		}
	}

	//for the bot (no need to display list)
	public void moveFigure(Figure source, Figure target)
	{
		int x = source.getX();
		int y = source.getY();

		source = getFigureAt(source);

		int toX = target.getX();
		int toY = target.getY();

		target = getFigureAt(target);

		source.setMoved(true);

		if(source.getType().equals("Peasant")&&(toY==0&&source.isWhite()||toY==7&&!source.isWhite()))
		{
				this.setFigureToCoordinate(toX, toY, new Queen(source.isWhite()));
		}
		else
		{
			this.setFigureToCoordinate(toX, toY, source);
		}

		if(!target.getType().equals(""))
		{
			if(target.isWhite())
			{
				value -= target.getValue();
			}
			else
			{
				value += target.getValue();
			}
		}


		//adds an empty figure to the start position
		Figure empty = new Empty();
		this.setFigureToCoordinate(x, y, empty);

		if (source.getType().equals("King"))
		{
			King king = (King) source;
			if (king.isInRochade())
			{
				if (target.getType().equals(""))
				{
					if (((Empty) target).isRochadeTarget())
					{
						if (target.getX() == 2 && king.getTowerLeft() != null)
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
	}

	Figure moveFigure(Figure source, Figure target, PlayingField playingField)
	{
		int x = source.getX();
		int y = source.getY();

		int toX = target.getX();
		int toY = target.getY();

		source.setMoved(true);

		//removes the figure form the GridPane
		Platform.runLater(() -> playingField.getGridPaneMain().getChildren().remove(source));

		//removes the target figure
		playingField.removeFigure(target);

		//adds the figure to the target position
		if(source.getType().equals("Peasant")&&(toY==0&&source.isWhite()||toY==7&&!source.isWhite()))
		{
			playingField.setFigureToCoordinate(toX, toY, new Queen(source.isWhite()));
		}
		else
		{
			playingField.setFigureToCoordinate(toX, toY, source);
		}



		//adds an empty figure to the start position
		Figure empty = new Empty();
		playingField.setFigureToCoordinate(x, y, empty);

		//rochade
		if (source.getType().equals("King"))
		{
			//sort();
			King king = (King) source;
			if (king.isInRochade())
			{
				if (target.getType().equals(""))
				{
					if (((Empty) target).isRochadeTarget())
					{
						if (target.getX() == 2 && king.getTowerLeft() != null)
						{
							moveFigure(king.getTowerLeft(), getFigureAt(3, y),playingField);
						}
						else if (target.getX() == 6 && king.getTowerRight() != null)
						{
							moveFigure(king.getTowerRight(), getFigureAt(5, y),playingField);
						}
					}
				}
				king.setInRochade(false);
			}
		}

		resetReachable();
		colorFields();

		return empty;
	}

	public boolean isInCheck(boolean isWhiteNow)
	{
		this.resetReachable();
		Figure king = null;
		for (Figure[] valueArray :figures)
		{
			for (Figure value :valueArray)
			{
				if (value.isWhite() != isWhiteNow)
				{
					value.setReachableFieldsForKing(this);
				}
				else if (value.getType().equals("King"))
				{
					king = value;
				}
			}
		}

		if(king!=null)
		{
			return king.isReachable();
		}
		else
		{
			return true;
		}


	}

	int getGameState()
	{
		int gameState = 0;

		for (Figure value : getArray())
		{
			if(value != null)
			{
				if (value.isWhite())
				{
					gameState += value.getValue();
				}
				else
				{
					gameState -= value.getValue();
				}
			}
		}

		return gameState;
	}

	public FigureList copyList()
	{
		Figure[][] temp = new Figure[8][8];

		for (Figure[] valueArray :figures)
		{
			for (Figure value :valueArray)
			{
				switch (value.getType())
				{
					case "":
						temp[value.getX()][value.getY()] = new Empty(value);
						break;
					case "King":
						temp[value.getX()][value.getY()] = new King(value);
						break;
					case "Queen":
						temp[value.getX()][value.getY()] = new Queen(value);
						break;
					case "Runner":
						temp[value.getX()][value.getY()] = new Runner(value);
						break;
					case "Horse":
						temp[value.getX()][value.getY()] = new Horse(value);
						break;
					case "Tower":
						temp[value.getX()][value.getY()] = new Tower(value);
						break;
					case "Peasant":
						temp[value.getX()][value.getY()] = new Peasant(value);
						break;
				}
			}

		}

		return new FigureList(temp,value);
	}

	public void resetReachable()
	{
		for (Figure[] valueArray :figures)
		{
			for (Figure value :valueArray)
			{
				if(value != null)
				{
					value.setReachable(false);

					if (value.getType().equals(""))
					{
						((Empty) value).setRochadeTarget(false);
					}
					else if (value.getType().equals("King"))
					{
						((King) value).setInRochade(false);
					}
				}
			}
		}
	}

	public int getValue()
	{
		return value;
	}

	public Figure[] getArray()
	{
		Figure[] temp = new Figure[64];


		int index = 0;
		for (Figure[] valueArray: figures)
		{
			for (Figure value: valueArray)
			{
				temp[index] = value;
				index ++;
			}
		}

		return temp;
	}

	public Figure[][] getFigures()
	{
		return figures;
	}

	public void setFigures(Figure[][] figures)
	{
		this.figures = figures;
	}
}
