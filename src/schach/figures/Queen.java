package schach.figures;

import schach.PlayingField;

public class Queen extends Figure
{
	public Queen(boolean isWhite)
	{
		this.setWhite(isWhite);
		setValue(9);
		setType("Queen");
		setText(getType());
	}

	@Override
	public void setReachableFields(PlayingField playingField)
	{
		lookInDirection(this.getX(),this.getY(),1,1,playingField);
		lookInDirection(this.getX(),this.getY(),-1,-1,playingField);
		lookInDirection(this.getX(),this.getY(),1,-1,playingField);
		lookInDirection(this.getX(),this.getY(),-1,1,playingField);

		lookInDirection(this.getX(),this.getY(),0,1,playingField);
		lookInDirection(this.getX(),this.getY(),0,-1,playingField);
		lookInDirection(this.getX(),this.getY(),1,0,playingField);
		lookInDirection(this.getX(),this.getY(),-1,0,playingField);
	}

	private void lookInDirection(int x, int y, int plusX, int plusY, PlayingField playingField)
	{
		int newX = x+plusX, newY = y+plusY;
		Figure temp = playingField.getFigureAt(newX,newY);

		if(temp != null)
		{
			if(temp.isWhite()!=this.isWhite() || temp.getType().equals(""))
			{
				temp.setReachable(true);

				if(temp.getType().equals(""))
				{
					lookInDirection(newX, newY, plusX, plusY, playingField);
				}
			}
		}
	}
}
