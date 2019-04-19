package schach.figures;

import schach.PlayingField;

public class Peasant extends Figure
{
	public Peasant(boolean isWhite)
	{
		super();
		setValue(1);
		setType("Peasant");
		this.setWhite(isWhite);
	}


	@Override
	public void setReachableFields(PlayingField playingField)
	{
		Figure temp;
		int y1;
		int y2;

		if(isWhite())
		{
			y1 = this.getY()-1;
			y2 = this.getY()-2;
		}
		else
		{
			y1 = this.getY()+1;
			y2 = this.getY()+2;
		}

		System.out.println(y1);

		temp = playingField.getFigureAt(this.getX(),y1);

		if(temp!=null)
		{
			if (temp.getType().equals(""))
			{
				playingField.getFigureAt(this.getX(), y1).setReachable(true);

				temp = playingField.getFigureAt(this.getX(),y2);
				if(temp!=null)
				{
					if (!isMoved() &&  temp.getType().equals(""))
					{
						playingField.getFigureAt(this.getX(), y2).setReachable(true);
					}
				}
			}
		}



		for(int i = -1; i <= 1; i += 2)
		{
			temp = playingField.getFigureAt(this.getX()+i,y1);
			if(temp != null)
			{
				if (!temp.getType().equals("") && temp.isWhite() != isWhite())
				{
					temp.setReachable(true);
				}
			}
		}

		//TODO stuff that happens when end zone is reached

	}
}
