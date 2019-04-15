package schach.figures;

import schach.PlayingField;

public class Horse extends Figure
{
	public Horse(boolean isWhite)
	{
		this.setWhite(isWhite);
		setValue(3);
		setType("Horse");
		setText(getType());
	}

	@Override
	public void setReachableFields(PlayingField playingField)
	{
		for(int i = -2; i <= 2; i += 4)
		{
			for (int j = -1; j <= 1; j += 2)
			{
				Figure temp = playingField.getFigureAt(this.getX()+i,this.getY()+j);

				if(temp != null)
				{
					if(temp.isWhite()!=this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
					}
				}

				temp = playingField.getFigureAt(this.getX()+j,this.getY()+i);

				if(temp != null)
				{
					if(temp.isWhite()!=this.isWhite() || temp.getType().equals(""))
					{
						temp.setReachable(true);
					}
				}
			}
		}
	}
}
