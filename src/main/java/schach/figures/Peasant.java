package schach.figures;

import schach.FigureList;

public class Peasant extends Figure
{
	public Peasant(boolean isWhite)
	{
		super();
		setValue(1);
		setType("Peasant");
		this.setWhite(isWhite);
	}

	public Peasant(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFieldsForBot(FigureList figureList)
	{
		Figure temp;
		int y1;
		int y2;

		if (isWhite())
		{
			y1 = this.getY() - 1;
			y2 = this.getY() - 2;
		}
		else
		{
			y1 = this.getY() + 1;
			y2 = this.getY() + 2;
		}

		//System.out.println(y1);

		temp = figureList.getFigureAt(this.getX(), y1);

		if (temp != null)
		{
			if (temp.getType().equals(""))
			{
				temp.setReachable(true);
				getCanReach().add(temp);

				temp = figureList.getFigureAt(this.getX(), y2);
				if (temp != null)
				{
					if (!isMoved() && temp.getType().equals(""))
					{
						temp.setReachable(true);
						getCanReach().add(temp);
					}
				}
			}
		}


		for (int i = -1; i <= 1; i += 2)
		{
			temp = figureList.getFigureAt(this.getX() + i, y1);
			if (temp != null)
			{
				if (!temp.getType().equals("") && temp.isWhite() != isWhite())
				{
					temp.setReachable(true);
					getCanReach().add(temp);
				}
			}
		}
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		Figure temp;
		int y1;
		int y2;

		if (isWhite())
		{
			y1 = this.getY() - 1;
			y2 = this.getY() - 2;
		}
		else
		{
			y1 = this.getY() + 1;
			y2 = this.getY() + 2;
		}

		//System.out.println(y1);

		temp = figureList.getFigureAt(this.getX(), y1);

		if (temp != null)
		{
			if (temp.getType().equals(""))
			{
				temp.setReachable(true);
				getCanReach().add(temp);

				temp = figureList.getFigureAt(this.getX(), y2);
				if (temp != null)
				{
					if (!isMoved() && temp.getType().equals(""))
					{
						temp.setReachable(true);
						getCanReach().add(temp);
					}
				}
			}
		}


		for (int i = -1; i <= 1; i += 2)
		{
			temp = figureList.getFigureAt(this.getX() + i, y1);
			if (temp != null)
			{
				if (!temp.getType().equals("") && temp.isWhite() != isWhite())
				{
					temp.setReachable(true);
					getCanReach().add(temp);
				}
			}
		}

		checkForBadMove(figureList);
	}

	@Override
	public void setReachableFieldsForKing(FigureList figureList)
	{
		Figure temp;
		int y;

		if (isWhite())
		{
			y = this.getY() - 1;
		}
		else
		{
			y = this.getY() + 1;
		}

		for (int i = -1; i <= 1; i += 2)
		{
			temp = figureList.getFigureAt(this.getX() + i, y);
			if (temp != null)
			{
				temp.setReachable(true);
			}
		}
	}
}
