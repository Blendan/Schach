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
						getCanReach().add(temp);
					}
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						getCanReach().add(temp);
					}
				}
			}
		}
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
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
						getCanReach().add(temp);
					}
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
						getCanReach().add(temp);
					}
				}
			}
		}
		checkForBadMove(figureList);
	}

	@Override
	public void setReachableFieldsForKing(FigureList figureList)
	{
		for (int i = -2; i <= 2; i += 4)
		{
			for (int j = -1; j <= 1; j += 2)
			{
				Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

				if (temp != null)
				{
					temp.setReachable(true);
					getCanReach().add(temp);
				}

				temp = figureList.getFigureAt(this.getX() + j, this.getY() + i);

				if (temp != null)
				{
					temp.setReachable(true);
					getCanReach().add(temp);
				}
			}
		}
	}
}
