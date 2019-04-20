package schach.figures;

import schach.FigureList;

public class Tower extends Figure
{

	public Tower(boolean isWhite)
	{
		super();
		setValue(5);
		setType("Tower");
		this.setWhite(isWhite);
	}

	public Tower(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		lookInDirection(this.getX(),this.getY(),0,1, figureList);
		lookInDirection(this.getX(),this.getY(),0,-1, figureList);
		lookInDirection(this.getX(),this.getY(),1,0, figureList);
		lookInDirection(this.getX(),this.getY(),-1,0, figureList);
	}

	private void lookInDirection(int x, int y, int plusX, int plusY, FigureList figureList)
	{
		int newX = x+plusX, newY = y+plusY;
		Figure temp = figureList.getFigureAt(newX,newY);

		if(temp != null)
		{
			if(temp.isWhite()!=this.isWhite() || temp.getType().equals(""))
			{
				temp.setReachable(true);

				if(temp.getType().equals(""))
				{
					lookInDirection(newX, newY, plusX, plusY, figureList);
				}
			}
		}
	}
}
