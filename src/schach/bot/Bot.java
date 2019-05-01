package schach.bot;

import javafx.application.Platform;
import schach.FigureList;
import schach.PlayingField;
import schach.figures.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Bot extends Thread
{
	private PlayingField playingField;
	@SuppressWarnings("FieldCanBeLocal")
	private int roundsToCheck, rounds = 0;
	private ArrayList<Move> moves = new ArrayList<>();
	private Random random;
	private boolean isInCheck = false;

	public Bot(PlayingField playingField, Random random, int roundsToCheck)
	{
		this.roundsToCheck = roundsToCheck;
		this.random = random;
		this.playingField = playingField;
	}

	private void removeMarked()
	{
		playingField.getFigures().forEach(v->v.setMarked(false));
	}

	@Override
	public void run()
	{
		removeMarked();
		FigureList figureList = playingField.getFigures().copyList();
		int beta = Integer.MAX_VALUE;
		Instant then = Instant.now();

		isInCheck = figureList.isInCheck(false);

		if(isInCheck)
		{
			roundsToCheck = 2;
		}

		for (Figure value : figureList)
		{
			if (!value.isWhite() && !value.getType().equals(""))
			{
				value.setReachableFieldsForBot(figureList);
				value.getCanReach().sort(Comparator.comparingInt(Figure::getValue));

				for (Figure reachableFigure : value.getCanReach())
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = figureList.copyList();

						temp.moveFigure(value, reachableFigure);

						Move tempMove = new Move(value, reachableFigure, checkMoves(temp, Integer.MIN_VALUE, beta, true, 1));

						moves.add(tempMove);

						if (beta > tempMove.getValue()&&!isInCheck)
						{
							beta = tempMove.getValue();
						}
					}
				}
				figureList.resetReachable();
			}
		}

		System.out.println("##############\n"+then.getEpochSecond()+"\n"+Instant.now().toEpochMilli()+"\n##############");


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

		if(!playingField.checkIfMovePossible())
		{
			playingField.getController().end(false);
		}
		else
		{
			playingField.checkDraw(target);
		}
	}

	private int checkMoves(FigureList figureList, int alpha, int beta, boolean isWhiteNow, int i)
	{
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
				value.setReachableFieldsForBot(figureList);
				value.getCanReach().sort(Comparator.comparingInt(Figure::getValue));
				for (Figure reachableFigure : value.getCanReach())
				{
					if (reachableFigure.isReachable())
					{
						FigureList temp = figureList.copyList();
						temp.moveFigure(temp.getFigureAt(value), temp.getFigureAt(reachableFigure));

						int tempMove;


						if (reachableFigure.getType().equals("King"))
						{
							if (reachableFigure.isWhite())
							{
								tempMove = Integer.MIN_VALUE;
								if(!isWhiteNow)
									return tempMove;
							}
							else
							{
								tempMove = Integer.MAX_VALUE;
								if(isWhiteNow)
									return tempMove;
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
								if (alpha < bestMove)
								{
									alpha = bestMove;
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
