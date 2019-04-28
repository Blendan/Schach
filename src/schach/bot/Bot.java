package schach.bot;

import javafx.application.Platform;
import schach.FigureList;
import schach.PlayingField;
import schach.figures.*;

import java.util.ArrayList;
import java.util.Random;

public class Bot extends Thread
{
	private PlayingField playingField;
	@SuppressWarnings("FieldCanBeLocal")
	private int roundsToCheck, rounds = 0;
	private ArrayList<Move> moves = new ArrayList<>();
	private Random random;

	public Bot(PlayingField playingField, Random random, int roundsToCheck)
	{
		this.roundsToCheck = roundsToCheck;
		this.random = random;
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

	private void removeMarked()
	{
		playingField.getFigures().forEach(v->v.setMarked(false));
	}

	@Override
	public void run()
	{
		removeMarked();
		FigureList figureList = copyList(playingField.getFigures());

		for (Figure value : figureList)
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
					System.out.println("###");
					bestValue = value.getValue();
					bestMoves.clear();
					bestMoves.add(value);
					System.out.println(value.getSource().getType() + "-" + value.getValue());
				}
				else if (bestValue == value.getValue())
				{
					bestMoves.add(value);
					System.out.println(value.getSource().getType() + "-");
				}
			}

			//System.out.println(value.getValue());
		}

		System.out.println("\n" + rounds);
		System.out.println(moves.size() + "|" + bestValue);

		int randomInt = random.nextInt(bestMoves.size());
		Figure source = bestMoves.get(randomInt).getSource().getOriginal();
		Figure target = bestMoves.get(randomInt).getTarget().getOriginal();
		playingField.moveFigure(source, target).setMarked(true);
		source.setMarked(true);

		System.out.println(source.getX() + "|" + source.getY() + "|" + source.getType());
		System.out.println(target.getX() + "|" + target.getY() + "|" + target.getType());
		playingField.setWhiteNow(true);
	}

	private int checkMoves(FigureList figureList, int alpha, int beta, boolean isWhiteNow, int i)
	{
		if (beta <= alpha)
		{
			if (isWhiteNow)
			{
				return alpha;
			}
			else
			{
				return beta;
			}
		}

		if (i < roundsToCheck)
		{
			int bestMove;

			if (isWhiteNow)
			{
				bestMove = Integer.MIN_VALUE;
			}
			else
			{
				bestMove = Integer.MAX_VALUE;
			}

			for (Figure value : figureList)
			{
				value.setReachableFields(figureList);
				for (Figure reachableFigure : figureList)
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = copyList(figureList);
						temp.moveFigure(temp.getFigureAt(value), temp.getFigureAt(reachableFigure));

						int tempMove;


						if (reachableFigure.getType().equals("King"))
						{
							if (reachableFigure.isWhite())
							{
								tempMove = Integer.MIN_VALUE;
							}
							else
							{
								tempMove = Integer.MAX_VALUE;
							}
						}
						else
						{
							tempMove = checkMoves(temp, alpha, beta, !isWhiteNow, i + 1);
						}

						if (isWhiteNow)
						{
							if (bestMove < tempMove)
							{
								bestMove = tempMove;
								if (beta < bestMove)
								{
									beta = bestMove;
								}
							}
						}
						else
						{
							if (bestMove > tempMove)
							{
								bestMove = tempMove;
								if (beta > bestMove)
								{
									beta = bestMove;
								}
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
			rounds++;
			return getValue(figureList);
		}
	}

	private int getValue(FigureList figureList)
	{
		int i = 0;
		boolean lostKing = true;

		for (Figure value : figureList)
		{
			if (!value.getType().equals(""))
			{
				if (value.isWhite())
				{
					i += value.getValue();
				}
				else
				{
					i -= value.getValue();


					if (value.getType().equals("King"))
					{
						lostKing = false;
					}
				}
			}
		}

		if (lostKing)
		{
			return Integer.MAX_VALUE;
		}

		return i;
	}
}
