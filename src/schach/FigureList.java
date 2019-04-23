package schach;

import javafx.application.Platform;
import schach.figures.*;

import java.util.ArrayList;

public class FigureList extends ArrayList<Figure>
{

	public FigureList()
	{

	}

	void sort()
	{
		ArrayList<Figure> toRemove = new ArrayList<>();
		super.sort((a, b) ->
		{
			if ((a.getX() + a.getY() * 8) < (b.getX() + b.getY() * 8))
			{
				return -1;
			}
			else if ((a.getX() + a.getY() * 8) > (b.getX() + b.getY() * 8))
			{
				return 1;
			}
			else
			{
				toRemove.add(a);
			}
			return 0;
		});

		this.removeAll(toRemove);

		if(toRemove.size()!=0)
		{
			sort();
		}
	}

	void colorFields()
	{
		boolean isBlack = false;
		int lastY = 0;

		for (Figure value : this)
		{
			try
			{
				value.getStyleClass().clear();
			}
			catch (NullPointerException e)
			{
				System.out.println("That shit again ("+e.getMessage()+")");
			}

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

	public Figure getFigureAt(int x, int y)
	{
		if (x + y * 8 < this.size() && x + y * 8 >= 0 && x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return this.get(x + y * 8);
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

		if (x + y * 8 < this.size() && x + y * 8 >= 0 && x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return this.get(x + y * 8);
		}
		else
		{
			return null;
		}
	}

	private void setFigureToCoordinate(int x, int y, Figure figure)
	{
		Figure temp = getFigureAt(x, y);
		if (temp != null)
		{
			if (temp.getX() == x && temp.getY() == y)
			{
				this.remove(temp);
			}
		}

		if (!this.contains(figure))
		{
			this.add(figure);
		}

		figure.setCoordinate(x, y);

		sort();
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
				this.remove(source);
				this.sort();
		}
		else
		{
			this.setFigureToCoordinate(toX, toY, source);
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
		sort();
	}

	void moveFigure(Figure source, Figure target, PlayingField playingField)
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
			this.remove(source);
			this.sort();
		}
		else
		{
			playingField.setFigureToCoordinate(toX, toY, source);
		}



		//adds an empty figure to the start position
		Figure empty = new Empty();
		playingField.setFigureToCoordinate(x, y, empty);

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
		sort();
		colorFields();
	}

	public void resetReachable()
	{
		for (Figure value : this)
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
