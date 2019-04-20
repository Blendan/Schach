package schach.figures;

import schach.PlayingField;

public class King extends Figure
{
	private boolean isInRochade = false;
	private Tower towerLeft = null, towerRight = null;

	public King(boolean isWhite)
	{
		super();
		setValue(999999);
		setType("King");
		this.setWhite(isWhite);
	}

	@Override
	public void setReachableFields(PlayingField playingField)
	{
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (!(i == 0 && j == 0))
				{
					Figure temp = playingField.getFigureAt(this.getX() + i, this.getY() + j);

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
			if (playingField.getFigureAt(0, y).getType().equals("Tower"))
			{
				tower = (Tower) playingField.getFigureAt(0, y);

				if (!tower.isMoved())
				{
					Figure temp = playingField.getFigureAt(2, y);

					if (temp != null)
					{
						if (temp.getType().equals(""))
						{
							if (playingField.getFigureAt(3, y) != null)
							{
								if (playingField.getFigureAt(3, y).getType().equals(""))
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

			if (playingField.getFigureAt(7, y).getType().equals("Tower"))
			{
				tower = (Tower) playingField.getFigureAt(7, y);

				if (!tower.isMoved())
				{
					Figure temp = playingField.getFigureAt(6, y);

					if (temp != null)
					{
						if (temp.getType().equals(""))
						{
							if (playingField.getFigureAt(5, y) != null)
							{
								if (playingField.getFigureAt(5, y).getType().equals(""))
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
