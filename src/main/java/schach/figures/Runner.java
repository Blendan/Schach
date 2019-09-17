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
	public void setReachableFieldsForBot(FigureList figureList)
	{
		int index =  lookInDirection(this.getX(),this.getY(),1,1, figureList, 0);
		index = lookInDirection(this.getX(),this.getY(),-1,-1, figureList, index);
		index = lookInDirection(this.getX(),this.getY(),1,-1, figureList, index);
		lookInDirection(this.getX(),this.getY(),-1,1, figureList, index);
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		int index = lookInDirection(this.getX(),this.getY(),1,1, figureList, 0);
		index = lookInDirection(this.getX(),this.getY(),-1,-1, figureList, index);
		index = lookInDirection(this.getX(),this.getY(),1,-1, figureList, index);
		lookInDirection(this.getX(),this.getY(),-1,1, figureList, index);
		checkForBadMove(figureList);
	}

	@Override
	public void setReachableFieldsForKing(FigureList figureList)
	{
		int index = lookInDirectionForKing(this.getX(),this.getY(),1,1, figureList, 0);
		index = lookInDirectionForKing(this.getX(),this.getY(),-1,-1, figureList, index);
		index = lookInDirectionForKing(this.getX(),this.getY(),1,-1, figureList, index);
		lookInDirectionForKing(this.getX(),this.getY(),-1,1, figureList, index);
	}

	private int lookInDirection(int x, int y, int plusX, int plusY, FigureList figureList, int index)
	{
		int newX = x + plusX, newY = y + plusY;
		Figure temp = figureList.getFigureAt(newX, newY);

		if (temp != null)
		{
			if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
			{
				temp.setReachable(true);
				canReach[index] = temp;
				index ++;

				if (temp.getType().equals(""))
				{
					lookInDirection(newX, newY, plusX, plusY, figureList, index);
				}
			}
		}
		return index;
	}

	private int lookInDirectionForKing(int x, int y, int plusX, int plusY, FigureList figureList , int index)
	{
		int newX = x + plusX, newY = y + plusY;
		Figure temp = figureList.getFigureAt(newX, newY);

		if (temp != null)
		{
			temp.setReachable(true);
			canReach[index] = temp;
			index ++;

			if (temp.getType().equals(""))
			{
				lookInDirection(newX, newY, plusX, plusY, figureList, index);
			}
		}
		return index;
	}
}
