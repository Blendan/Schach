package schach.figures;

import schach.FigureList;

public class Empty extends Figure
{

	private boolean isRochadeTarget = false;

	public Empty()
	{
	}

	public Empty(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFields(FigureList playingField)
	{
		// has no reachable fields
	}

	public boolean isRochadeTarget()
	{
		return isRochadeTarget;
	}

	public void setRochadeTarget(boolean rochadeTarget)
	{
		isRochadeTarget = rochadeTarget;
	}
}
