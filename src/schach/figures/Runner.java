package schach.figures;

import schach.FigureList;

public class Runner extends Figure
{
	public Runner(boolean isWhite)
	{
		super();
		setValue(3);
		setType("Runner");
		this.setWhite(isWhite);
	}

	public Runner(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		lookInDirection(this.getX(),this.getY(),1,1, figureList);
		lookInDirection(this.getX(),this.getY(),-1,-1, figureList);
		lookInDirection(this.getX(),this.getY(),1,-1, figureList);
		lookInDirection(this.getX(),this.getY(),-1,1, figureList);
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
