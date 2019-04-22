package schach.bot;

import schach.FigureList;
import schach.PlayingField;
import schach.figures.*;

import java.util.ArrayList;

public class Bot extends Thread
{
	private PlayingField playingField;
	@SuppressWarnings("FieldCanBeLocal")
	private int roundsToCheck = 1;
	private int moveLength = 0, movesFinished = 0;
	private boolean madeAllMoves = false;
	private ArrayList<Move> moves = new ArrayList<>();

	public Bot(PlayingField playingField)
	{
		this.playingField = playingField;
	}

	private FigureList copyList(FigureList figureList)
	{
		FigureList temp = new FigureList();

		for (Figure value : figureList)
		{
			switch (value.getType())
			{
				case "":
					temp.add(new Empty(value));
					break;
				case "King":
					temp.add(new King(value));
					break;
				case "Queen":
					temp.add(new Queen(value));
					break;
				case "Runner":
					temp.add(new Runner(value));
					break;
				case "Horse":
					temp.add(new Horse(value));
					break;
				case "Tower":
					temp.add(new Tower(value));
					break;
				case "Peasant":
					temp.add(new Peasant(value));
					break;
			}
		}

		return temp;
	}

	private void waitForFinishing(int number)
	{
		movesFinished += number;

		if (movesFinished == moveLength && madeAllMoves)
		{
			makeBestMove();
		}
	}

	@Override
	public void run()
	{
		FigureList figureList = copyList(playingField.getFigures());
		figureList.sort();
		for (Figure value : copyList(playingField.getFigures()))
		{
			if (!value.isWhite() && !value.getType().equals(""))
			{
				value.setReachableFields(figureList);
				for (Figure reachableFigure : figureList)
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = copyList(figureList);
						temp.moveFigure(value, reachableFigure);
						temp.sort();

						int newValue;

						newValue = 0 - reachableFigure.getValue();

						moveLength ++;
						new Thread(() ->
						{
							moves.add(new Move(value, reachableFigure, checkMoves(temp, newValue, true, 1)));
							waitForFinishing(1);
						}).start();
					}
				}
				figureList.resetReachable();
			}
		}

		madeAllMoves = true;
		waitForFinishing(0);
	}

	private void makeBestMove()
	{
		int bestValue = 9999;
		ArrayList<Move> bestMoves = new ArrayList<>();
		System.out.println("---------");

		for (Move value : moves)
		{
			if (!value.getSource().isWhite() && !value.getSource().getType().equals(""))
			{
				if (bestValue > value.getValue())
				{
					bestValue = value.getValue();
					bestMoves.clear();
					bestMoves.add(value);
				}
				else if (bestValue == value.getValue())
				{
					bestMoves.add(value);
					System.out.println(value.getSource().getType() + "-");
				}
			}

			//System.out.println(value.getValue());
		}

		System.out.println("\n" + moves.size() + "|" + bestValue);

		int random = (int) (Math.random() * bestMoves.size());
		Figure source = bestMoves.get(random).getSource().getOriginal();
		Figure target = bestMoves.get(random).getTarget().getOriginal();
		playingField.moveFigure(source, target);

		System.out.println(bestMoves.get(random).getSource().getX() + "  " + source.getX() + "|" + source.getY() + "|" + source.getType() + "|" + bestMoves.get(random).getSource().getType());
		System.out.println(target.getX() + "|" + target.getY() + "|" + target.getType());
		playingField.setWhiteNow(true);
	}

	private int checkMoves(FigureList figureList, int valueBefore, boolean isWhiteNow, int i)
	{
		System.out.println(valueBefore);
		if (i <= roundsToCheck)
		{
			ArrayList<Move> moves = new ArrayList<>();
			for (Figure value : figureList)
			{
				value.setReachableFields(figureList);
				for (Figure reachableFigure : figureList)
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = copyList(figureList);
						temp.moveFigure(value, reachableFigure);

						int newValue;

						if (isWhiteNow)
						{
							newValue = valueBefore + reachableFigure.getValue();
						}
						else
						{
							newValue = valueBefore - reachableFigure.getValue();
						}

						moves.add(new Move(checkMoves(temp, newValue, !isWhiteNow, i + 1)));
					}
				}
				figureList.resetReachable();
			}


			int bestValue = 0;
			for (Move value : moves)
			{
				if (isWhiteNow)
				{
					if (bestValue < value.getValue())
					{
						bestValue = value.getValue();
					}
				}
				else
				{
					if (bestValue > value.getValue())
					{
						bestValue = value.getValue();

					}
				}
			}

			return bestValue;
		}
		else
		{
			return valueBefore;
		}
	}
}
