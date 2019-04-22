package schach.figures;

import schach.FigureList;

public class King extends Figure
{
	private boolean isInRochade = false;
	private Tower towerLeft = null, towerRight = null;

	public King(boolean isWhite)
	{
		super();
		setValue(9999999);
		setType("King");
		this.setWhite(isWhite);
	}

	public King(Figure figure)
	{
		super(figure);
	}

	@Override
	public void setReachableFields(FigureList figureList)
	{
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (!(i == 0 && j == 0))
				{
					Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

					if (temp != null)
					{
						if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
						{
							temp.setReachable(true);
						}
					}
				}
			}
		}

		//Rochade

		if (!isMoved())
		{
			int y;
			if (isWhite())
			{
				y = 7;
			}
			else
			{
				y = 0;
			}

			Tower tower;
			if (figureList.getFigureAt(0, y).getType().equals("Tower"))
			{
				tower = (Tower) figureList.getFigureAt(0, y);

				if (!tower.isMoved())
				{
					Figure temp = figureList.getFigureAt(2, y);

					if (temp != null)
					{
						if (temp.getType().equals(""))
						{
							if (figureList.getFigureAt(3, y) != null)
							{
								if (figureList.getFigureAt(3, y).getType().equals(""))
								{
									temp.setReachable(true);
									((Empty)temp).setRochadeTarget(true);
									towerLeft = tower;
									isInRochade = true;
								}
							}
						}
					}
				}
			}

			if (figureList.getFigureAt(7, y).getType().equals("Tower"))
			{
				tower = (Tower) figureList.getFigureAt(7, y);

				if (!tower.isMoved())
				{
					Figure temp = figureList.getFigureAt(6, y);

					if (temp != null)
					{
						if (temp.getType().equals(""))
						{
							if (figureList.getFigureAt(5, y) != null)
							{
								if (figureList.getFigureAt(5, y).getType().equals(""))
								{
									temp.setReachable(true);
									((Empty)temp).setRochadeTarget(true);
									towerRight = tower;
									isInRochade = true;
								}
							}
						}
					}
				}
			}
		}
	}

	public void setInRochade(boolean inRochade)
	{
		isInRochade = inRochade;
	}

	public void setTowerLeft(Tower towerLeft)
	{
		this.towerLeft = towerLeft;
	}

	public void setTowerRight(Tower towerRight)
	{
		this.towerRight = towerRight;
	}

	public boolean isInRochade()
	{
		return isInRochade;
	}

	public Tower getTowerLeft()
	{
		return towerLeft;
	}

	public Tower getTowerRight()
	{
		return towerRight;
	}
}
