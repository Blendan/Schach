package schach.figures;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import schach.Controller;
import schach.PlayingField;

public abstract class Figure extends Button
{
	private int x, y;
	private String type = "";
	private int value = 0;
	private boolean isReachable = false, isWhite, isMoved = false; //isWhite because isBlack would be racist

	public Figure()
	{
	}

	public Figure(boolean isWhite)
	{
		this.isWhite = isWhite;
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

	public void setType(String type)
	{
		this.type = type;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public boolean isWhite()
	{
		return isWhite;
	}

	public void setWhite(boolean white)
	{
		isWhite = white;
	}

	public boolean isMoved()
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

	public abstract void setReachableFields(PlayingField playingField);

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
}
