package schach.figures;

import schach.FigureList;

import java.util.ArrayList;

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
		towerRight = null;
		towerLeft = null;

		ArrayList<Figure> reachable = new ArrayList<>();

		for (Figure value : figureList)
		{
			if (value.isWhite() != isWhite() && !value.getType().equals("") && !value.getType().equals("King"))
			{
				if(value.getType().equals("Peasant"))
				{
					((Peasant)value).setReachableFieldsForKing(figureList);
				}
				else
				{
					value.setReachableFields(figureList);
				}
			}
		}

		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (!(i == 0 && j == 0))
				{
					Figure temp = figureList.getFigureAt(this.getX() + i, this.getY() + j);

					if (temp != null)
					{
						if(!temp.isReachable())
						{
							if (temp.isWhite() != this.isWhite() || temp.getType().equals(""))
							{
								reachable.add(temp);
							}
						}
					}
				}
			}
		}

		//Rochade

		Empty empty1 = null, empty2 = null;
		int y;
		if (isWhite())
		{
			y = 7;
		}
		else
		{
			y = 0;
		}

		if (!isMoved())
		{


			Tower tower;
			if (figureList.getFigureAt(0, y).getType().equals("Tower"))
			{
				tower = (Tower) figureList.getFigureAt(0, y);

				if (!tower.isMoved())
				{
					Figure temp = figureList.getFigureAt(2, y);

					if (temp != null)
					{
						if (temp.getType().equals("") && !temp.isReachable())
						{
							if (figureList.getFigureAt(3, y) != null)
							{
								if (figureList.getFigureAt(3, y).getType().equals(""))
								{
									reachable.add(temp);
									empty1 = (Empty)temp;
									towerLeft = tower;
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
						if (temp.getType().equals("") && !temp.isReachable())
						{
							if (figureList.getFigureAt(5, y) != null)
							{
								if (figureList.getFigureAt(5, y).getType().equals(""))
								{
									reachable.add(temp);
									empty2 = (Empty)temp;
									towerRight = tower;
								}
							}
						}
					}
				}
			}
		}

		figureList.resetReachable();

		if(towerLeft!=null)
		{
			isInRochade = true;
			empty1.setRochadeTarget(true);
		}

		if(towerRight!=null)
		{
			isInRochade = true;
			empty2.setRochadeTarget(true);
		}

		reachable.forEach(v->v.setReachable(true));
	}

	public void setInRochade(boolean inRochade)
	{
		isInRochade = inRochade;
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
