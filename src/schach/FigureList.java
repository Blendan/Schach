package schach;

import schach.figures.Figure;

import java.util.ArrayList;

class FigureList extends ArrayList<Figure>
{
	void sort()
	{
		super.sort((a, b) ->
		{
			if ((a.getX() + a.getY() * 8) < (b.getX() + b.getY() * 8))
			{
				return -1;
			}
			else if ((a.getX() + a.getY() * 8) > (b.getX() + b.getY() * 8))
			{
				return 1;
			}
			return 0;
		});
	}

	Figure getFigureAt(int x, int y)
	{
		if (x + y * 8 < this.size() && x + y * 8 >= 0 && x < 8 && y < 8 && x >= 0 && y >= 0)
		{
			return this.get(x + y * 8);
		}
		else
		{
			return null;
		}
	}
}
