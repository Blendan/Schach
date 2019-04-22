package schach.bot;

import javafx.application.Platform;
import schach.FigureList;
import schach.PlayingField;
import schach.figures.*;

import java.util.ArrayList;

public class Bot extends Thread
{
	private PlayingField playingField;
	@SuppressWarnings("FieldCanBeLocal")
	private int roundsToCheck = 2;
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

						moves.add(new Move(value, reachableFigure, checkMoves(temp, Integer.MIN_VALUE, Integer.MAX_VALUE, true, 1)));

					}
				}
				figureList.resetReachable();
			}
		}

		Platform.runLater(this::makeBestMove);
	}

	private void makeBestMove()
	{
		int bestValue = Integer.MAX_VALUE;
		ArrayList<Move> bestMoves = new ArrayList<>();
		System.out.println("---------\nBest Moves:\n---------");

		for (Move value : moves)
		{
			if (!value.getSource().isWhite() && !value.getSource().getType().equals(""))
			{
				if (bestValue > value.getValue())
				{
					bestValue = value.getValue();
					bestMoves.clear();
					bestMoves.add(value);
					System.out.println(value.getSource().getType() + "-");
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

	private int checkMoves(FigureList figureList, int alpha, int beta, boolean isWhiteNow, int i)
	{
		if (i <= roundsToCheck)
		{
			int bestMove = 0;
			boolean isFirst = true;

			for (Figure value : figureList)
			{
				value.setReachableFields(figureList);
				for (Figure reachableFigure : figureList)
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = copyList(figureList);
						temp.moveFigure(value, reachableFigure);

						int tempMove = checkMoves(temp, alpha, beta, !isWhiteNow, i + 1);

						if (isFirst)
						{
							isFirst = false;
							bestMove = tempMove;
						}
						else if (isWhiteNow)
						{

							if (bestMove < tempMove)
							{
								bestMove = tempMove;
								alpha = bestMove;
							}
						}
						else
						{
							if (bestMove > tempMove)
							{
								bestMove = tempMove;
								beta = bestMove;
							}
						}

						if (beta <= alpha)
						{
							return tempMove;
						}
					}
				}
				figureList.resetReachable();
			}

			return bestMove;
		}
		else
		{
			return getValue(figureList);
		}
	}

	private int getValue(FigureList figureList)
	{
		int i = 0;
		boolean lostKing = true;

		for (Figure value : figureList)
		{
			if (value.isWhite())
			{
				i += value.getValue();
			}
			else
			{
				i -= value.getValue();

				if(value.getType().equals("King"))
				{
					lostKing = false;
				}
			}
		}

		if(lostKing)
		{
			return Integer.MAX_VALUE;
		}

		return i;
	}
}
