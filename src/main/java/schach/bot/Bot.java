package schach.bot;

import javafx.application.Platform;
import schach.FigureList;
import schach.PlayingField;
import schach.figures.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Bot extends Thread
{
	private static int direction = -1;

	private PlayingField playingField;
	@SuppressWarnings("FieldCanBeLocal")
	private int roundsToCheck, rounds = 0;
	private ArrayList<Move> moves = new ArrayList<>();
	private Random random;
	@SuppressWarnings("FieldCanBeLocal")
	private boolean isInCheck = false;

	public Bot(PlayingField playingField, Random random, int roundsToCheck)
	{
		this.roundsToCheck = roundsToCheck;
		this.random = random;
		this.playingField = playingField;
	}

	private void removeMarked()
	{
		playingField.getFigures().forEach(v -> v.setMarked(false));
	}

	@Override
	public void run()
	{
		removeMarked();
		FigureList figureList = playingField.getFigures().copyList();
		int beta = Integer.MAX_VALUE;
		Instant then = Instant.now();

		ArrayList<Move> possibleMoves = new ArrayList<>();

		isInCheck = figureList.isInCheck(false);

		if (isInCheck)
		{
			roundsToCheck = 2;
		}

		for (Figure value : figureList)
		{
			if (!value.isWhite() && !value.getType().equals(""))
			{
				value.setReachableFieldsForBot(figureList);
				//value.getCanReach().sort(Comparator.comparingInt(Figure::getValue));

				for (Figure reachableFigure : value.getCanReach())
				{
					Move tempMove = new Move(value, reachableFigure, reachableFigure.getValue());

					tempMove.setSortValue(0);

					if(!reachableFigure.getType().equals(""))
					{
						tempMove.setSortValue(3);
					}
					else if(reachableFigure.getY()<value.getX())
					{
						tempMove.setSortValue(1);
					}

					possibleMoves.add(tempMove);
				}

				figureList.resetReachable();
			}
		}

		System.out.println("#########  " + possibleMoves.size());
		possibleMoves.sort((a,b)-> Integer.compare(a.getSortValue(),b.getSortValue())*direction);

		for (Move move : possibleMoves)
		{
			FigureList temp = figureList.copyList();

			temp.moveFigure(temp.getFigureAt(move.getSource()), temp.getFigureAt(move.getTarget()));

			Move tempMove = new Move(move.getSource(), move.getTarget(), checkMoves(temp, Integer.MIN_VALUE, beta, true, 1));

			moves.add(tempMove);

			if (beta > tempMove.getValue() && !isInCheck)
			{
				beta = tempMove.getValue();
			}
		}

		System.out.println("##############\n" + then.getEpochSecond() + "\n" + Instant.now().toEpochMilli() + "\n##############");


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
					System.out.println(value.getSource().getType());
				}
			}

			//System.out.println(value.getValue());
		}

		System.out.println("\n rounds:" + rounds);
		System.out.println(moves.size() + "| best val:" + bestValue + "|" + bestMoves.size());

		int randomInt = random.nextInt(bestMoves.size());
		Figure source = bestMoves.get(randomInt).getSource().getOriginal();
		Figure target = bestMoves.get(randomInt).getTarget().getOriginal();
		playingField.moveFigure(source, target).setMarked(true);
		source.setMarked(true);

		playingField.setWhiteNow(true);

		if (!playingField.checkIfMovePossible())
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
			ArrayList<Move> possibleMoves = new ArrayList<>();
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

				for (Figure reachableFigure : value.getCanReach())
				{
					Move tempMove = new Move(value, reachableFigure, reachableFigure.getValue());

					tempMove.setSortValue(0);

					if(!reachableFigure.getType().equals(""))
					{
						tempMove.setSortValue(3);
					}
					else if(reachableFigure.getY()<value.getX()&&!isWhiteNow)
					{
						tempMove.setSortValue(1);
					}
					else if(reachableFigure.getY()>value.getX())
					{
						tempMove.setSortValue(1);
					}

					possibleMoves.add(tempMove);
				}

				figureList.resetReachable();
			}

			possibleMoves.sort((a,b)-> Integer.compare(a.getSortValue(),b.getSortValue())*direction);


			for (Move move : possibleMoves)
			{
				FigureList temp = figureList.copyList();

				temp.moveFigure(temp.getFigureAt(move.getSource()), temp.getFigureAt(move.getTarget()));

				int tempMove;

				if (move.getTarget().getType().equals("King"))
				{
					if (move.getTarget().isWhite())
					{
						tempMove = Integer.MIN_VALUE;
						if (!isWhiteNow)
						{
							return tempMove;
						}
					}
					else
					{
						tempMove = Integer.MAX_VALUE;
						if (isWhiteNow)
						{
							return tempMove;
						}
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
						if (alpha < bestMove && !isInCheck)
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
						if (beta > bestMove && !isInCheck)
						{
							beta = bestMove;
						}
					}
				}

				if (beta <= alpha)
				{
					System.out.println("-#-#-#-#-#   "+bestMove + "  " + isWhiteNow);
					return bestMove;
				}
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
		/*int i = 0;
		//int anz = 0;
		boolean lostKing = true;

		for (Figure value : figureList)
		{
			if (!value.getType().equals(""))
			{
				//anz ++;

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

		//System.out.println(anz);

		if (lostKing)
		{
			return Integer.MAX_VALUE;
		}*/

		return figureList.getValue();
	}
}
