package schach.bot;

import schach.figures.Figure;

class Move
{
	private Figure source, target;
	private int value;
	private int sortValue;

	Move(Figure source, Figure target, int value)
	{
		this.source = source;
		this.target = target;
		this.value = value;
	}

	Figure getSource()
	{
		return source;
	}

	Figure getTarget()
	{
		return target;
	}

	int getValue()
	{
		return value;
	}

	int getSortValue()
	{
		return sortValue;
	}

	void setSortValue(int sortValue)
	{
		this.sortValue = sortValue;
	}
}
