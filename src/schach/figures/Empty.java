package schach.figures;

import schach.PlayingField;

public class Empty extends Figure
{

	private boolean isRochadeTarget = false;

	@Override
	public void setReachableFields(PlayingField playingField)
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
