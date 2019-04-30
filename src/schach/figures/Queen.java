package schach.figures;

import schach.FigureList;

public class Queen extends Figure
{
	public Queen(boolean isWhite)
	{
		super();
		setValue(9);
		setType("Queen");
		this.setWhite(isWhite);
	}

	public Queen(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFieldsForBot(FigureList figureList)
	{
		lookInDirection(this.getX(), this.getY(), 1, 1, figureList);
		lookInDirection(this.getX(), this.getY(), -1, -1, figureList);
		lookInDirection(this.getX(), this.getY(), 1, -1, figureList);
		lookInDirection(this.getX(), this.getY(), -1, 1, figureList);

		lookInDirection(this.getX(), this.getY(), 0, 1, figureList);
		lookInDirection(this.getX(), this.getY(), 0, -1, figureList);
		lookInDirection(this.getX(), this.getY(), 1, 0, figureList);
		lookInDirection(this.getX(), this.getY(), -1, 0, figureList);
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		lookInDirection(this.getX(), this.getY(), 1, 1, figureList);
		lookInDirection(this.getX(), this.getY(), -1, -1, figureList);
		lookInDirection(this.getX(), this.getY(), 1, -1, figureList);
		lookInDirection(this.getX(), this.getY(), -1, 1, figureList);

		lookInDirection(this.getX(), this.getY(), 0, 1, figureList);
		lookInDirection(this.getX(), this.getY(), 0, -1, figureList);
		lookInDirection(this.getX(), this.getY(), 1, 0, figureList);
		lookInDirection(this.getX(), this.getY(), -1, 0, figureList);
		checkForBadMove(figureList);
	}

	@Override
	public void setReachableFieldsForKing(FigureList figureList)
	{
		lookInDirectionForKing(this.getX(), this.getY(), 1, 1, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), -1, -1, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), 1, -1, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), -1, 1, figureList);

		lookInDirectionForKing(this.getX(), this.getY(), 0, 1, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), 0, -1, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), 1, 0, figureList);
		lookInDirectionForKing(this.getX(), this.getY(), -1, 0, figureList);
	}

	private void lookInDirection(int x, int y, int plusX, int plusY, FigureList figureList)
	{
		int newX = x + plusX, newY = y + plusY;
		Figure temp = figureList.getFigureAt(newX, newY);

		if (temp != null)
		{
			if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
			{
				temp.setReachable(true);
				getCanReach().add(temp);

				if (temp.getType().equals(""))
				{
					lookInDirection(newX, newY, plusX, plusY, figureList);
				}
			}
		}
	}

	private void lookInDirectionForKing(int x, int y, int plusX, int plusY, FigureList figureList)
	{
		int newX = x + plusX, newY = y + plusY;
		Figure temp = figureList.getFigureAt(newX, newY);

		if (temp != null)
		{
			temp.setReachable(true);
			getCanReach().add(temp);

			if (temp.getType().equals(""))
			{
				lookInDirection(newX, newY, plusX, plusY, figureList);
			}
		}
	}
}
