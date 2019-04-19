package schach.figures;

import schach.PlayingField;

public class King extends Figure
{
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
	}
}
