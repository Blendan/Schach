package schach.figures;

import schach.FigureList;

public class Horse extends Figure
{
	public Horse(boolean isWhite)
	{
		super();
		setValue(3);
		setType("Horse");
		this.setWhite(isWhite);
	}

	public Horse(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFieldsForBot(FigureList figureList)
	{
		int index = 0;
		for (int i = -2; i <= 2; i += 4)
		{
			for (int j = -1; j <= 1; j += 2)
			{
				Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						canReach[index] = temp;
						index ++;
					}
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						canReach[index] = temp;
						index ++;
					}
				}
			}
		}
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		int index = 0;
		for (int i = -2; i <= 2; i += 4)
		{
			for (int j = -1; j <= 1; j += 2)
			{
				Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						canReach[index] = temp;
						index ++;
					}
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						canReach[index] = temp;
						index ++;
					}
				}
			}
		}
		checkForBadMove(figureList);
	}

	@Override
	public void setReachableFieldsForKing(FigureList figureList)
	{
		int index = 0;
		for (int i = -2; i <= 2; i += 4)
		{
			for (int j = -1; j <= 1; j += 2)
			{
				Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

				if (temp != null)
				{
					temp.setReachable(true);
					canReach[index] = temp;
					index ++;
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					temp.setReachable(true);
					canReach[index] = temp;
					index ++;
				}
			}
		}
	}
}
