package schach.figures;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import schach.FigureList;

public abstract class Figure extends Button
{
	private int x, y;
	private String type = "";
	private int value = 0;
	private boolean isReachable = false, isWhite, isMoved = false , isMarked = false; //isWhite because isBlack would be racist
	private Text text = new Text();
	private Figure original;
	Figure[] canReach = new Figure[64];

	public Figure()
	{
		setTextAlignment(TextAlignment.CENTER);
		setAlignment(Pos.CENTER);
		this.setGraphic(text);
		text.setTextAlignment(TextAlignment.CENTER);
		text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));
	}

	public Figure(Figure figure)
	{
		this.original = figure;
		this.x = figure.getX();
		this.y = figure.getY();
		this.value = figure.getValue();
		this.isReachable = figure.isReachable();
		this.isWhite = figure.isWhite();
		this.isMoved = figure.isMoved();

		setType(figure.getType());
		if(!type.equals(""))
		{
			setTextAlignment(TextAlignment.CENTER);
			setAlignment(Pos.CENTER);
			this.setGraphic(text);
			text.setTextAlignment(TextAlignment.CENTER);
			text.setFont(Font.font("verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 30));

			if(isWhite)
			{
				text.setFill(Color.WHITE);
				text.setStroke(Color.BLACK);
				text.setStrokeWidth(2);
			}
			else
			{
				text.setStroke(Color.WHITE);
				text.setStrokeWidth(2);
				text.setFill(Color.BLACK);
			}
		}
	}

	public Figure[] getCanReach()
	{
		return canReach;
	}

	void checkForBadMove(FigureList figureList)
	{
		/*ArrayList<Figure> toRemove = new ArrayList<>();
		for(Figure value : canReach)
		{
			FigureList temp = figureList.copyList();
			temp.resetReachable();

			temp.moveFigure(temp.getFigureAt(this),temp.getFigureAt(value));
			if(temp.isInCheck(isWhite))
			{
				value.setReachable(false);
				toRemove.add(value);
			}
		}

		canReach.removeAll(toRemove); */
	}

	public String getType()
	{
		return type;
	}

	public int getValue()
	{
		return value;
	}

	public void setCoordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	void setType(String type)
	{
		this.type = type;

		if(!type.equals(""))
		{
			text.setText(type.substring(0, 1));
		}
	}

	void setValue(int value)
	{
		this.value = value;
	}

	public boolean isWhite()
	{
		return isWhite;
	}

	void setWhite(boolean white)
	{
		isWhite = white;

		if(white)
		{
			text.setFill(Color.WHITE);
			text.setStroke(Color.BLACK);
			text.setStrokeWidth(2);
		}
		else
		{
			text.setStroke(Color.WHITE);
			text.setStrokeWidth(2);
			text.setFill(Color.BLACK);
		}
	}

	boolean isMoved()
	{
		return isMoved;
	}

	public void setMoved(boolean moved)
	{
		isMoved = moved;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public abstract void setReachableFieldsForBot(FigureList figureList);

	public abstract void setReachableFields(FigureList figureList);

	public void setReachable(boolean reachable)
	{
		isReachable = reachable;

		if(isReachable)
		{
			this.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
		}
		else
		{
			this.setBorder(null);
		}
	}

	public boolean isReachable()
	{
		return isReachable;
	}

	public Figure getOriginal()
	{
		return original;
	}

	public boolean isMarked()
	{
		return isMarked;
	}

	public void setMarked(boolean marked)
	{
		isMarked = marked;

		if(isMarked)
		{
			getStyleClass().clear();
			getStyleClass().add("btn-marked");
		}
	}

	abstract public void setReachableFieldsForKing(FigureList figureList);

	public void setCanReach(Figure[] canReach)
	{
		this.canReach = canReach;
	}
}
