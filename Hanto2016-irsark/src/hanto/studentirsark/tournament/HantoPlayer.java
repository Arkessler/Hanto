/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentirsark.tournament;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPlayerColor;
import hanto.studentirsark.beta.BetaHantoPlayerState;
import hanto.studentirsark.common.GameBoard;
import hanto.studentirsark.common.HantoCoordinateImpl;
import hanto.studentirsark.common.HantoPieceImpl;
import hanto.studentirsark.common.PlayerState;
import hanto.studentirsark.common.ValidMoveGenerator;
import hanto.studentirsark.delta.DeltaHantoPlayerState;
import hanto.studentirsark.epsilon.EpsilonHantoPlayerState;
import hanto.studentirsark.gamma.GammaHantoPlayerState;
import hanto.tournament.HantoGamePlayer;
import hanto.tournament.HantoMoveRecord;

/**
 * Description
 * @version Oct 13, 2014
 */
public class HantoPlayer implements HantoGamePlayer
{
	private boolean moveFirst;
	private HantoPlayerColor playerColor;
	private HantoGameID gameVersion;
	private GameBoard gameBoard;
	private ValidMoveGenerator vmg = ValidMoveGenerator.getInstance();
	private PlayerState playerState;
	private HantoPlayerColor opponentsColor;
	private int turnsTaken;
	private HantoCoordinate butterflyLocation;

	/*
	 * @see hanto.tournament.HantoGamePlayer#startGame(hanto.common.HantoGameID, hanto.common.HantoPlayerColor, boolean)
	 */
	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst)
	{
		playerColor = myColor;
		moveFirst = doIMoveFirst;
		gameVersion = version;
		gameBoard = new GameBoard(new HashMap<HantoCoordinate, HantoPiece>());
		turnsTaken = 0;
		
		if(myColor == BLUE)
		{
			opponentsColor = RED;
		}
		else
		{
			opponentsColor = BLUE;
		}
		
		switch (version) {
		case BETA_HANTO:
			playerState = new BetaHantoPlayerState();
			break;
		case GAMMA_HANTO:
			playerState = new GammaHantoPlayerState();
			break;
		case DELTA_HANTO:
			playerState = new DeltaHantoPlayerState();
			break;
		case EPSILON_HANTO:
			playerState = new EpsilonHantoPlayerState();
			break;
		}
	}

	/*
	 * @see hanto.tournament.HantoGamePlayer#makeMove(hanto.tournament.HantoMoveRecord)
	 */
	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove)
	{
		Random random = new Random();
		
		//If we are moving first, make the first move
		if(moveFirst)
		{
			moveFirst = false;
			List<HantoMoveRecord> possibleMoves = vmg.generateValidMoves(gameVersion, gameBoard, playerState, playerColor, true, butterflyLocation, turnsTaken);
			HantoMoveRecord moveToMake = possibleMoves.get(random.nextInt(possibleMoves.size()));
			try
			{
				playerState = playerState.checkPieceCountValidity(moveToMake.getPiece());
			}
			catch (HantoException e)
			{
				return new HantoMoveRecord(null,null,null);
			}
			gameBoard.addPiece(new HantoCoordinateImpl(moveToMake.getTo()), new HantoPieceImpl(playerColor, moveToMake.getPiece()));
			turnsTaken++;
			if(moveToMake.getPiece() == BUTTERFLY)
			{
				butterflyLocation = moveToMake.getTo();
			}
			return moveToMake;
		}
		
		//Update the board with the opponents Move
		
		if(opponentsMove.getFrom() == null)
		{
			try
			{
				playerState = playerState.checkPieceCountValidity(opponentsMove.getPiece());
			}
			catch (HantoException e)
			{
				return new HantoMoveRecord(null,null,null);
			}
			gameBoard.addPiece(new HantoCoordinateImpl(opponentsMove.getTo()), new HantoPieceImpl(opponentsColor, opponentsMove.getPiece()));
		}
		else
		{
			gameBoard.removePiece(new HantoCoordinateImpl(opponentsMove.getTo()));
			gameBoard.addPiece(new HantoCoordinateImpl(opponentsMove.getTo()), new HantoPieceImpl(opponentsColor, opponentsMove.getPiece()));
		}
		
		
		//Choose a random move out of all possible moves and execute it
		List<HantoMoveRecord> possibleMoves = vmg.generateValidMoves(gameVersion, gameBoard, playerState, playerColor, moveFirst, butterflyLocation, turnsTaken);
		
		//If we cannot make any valid moves, resign
		if(possibleMoves.size() == 0)
		{
			return new HantoMoveRecord(null,null,null);
		}
		HantoMoveRecord moveToMake = possibleMoves.get(random.nextInt(possibleMoves.size()));
		
		if(moveToMake.getFrom() == null)
		{
			try
			{
				playerState = playerState.checkPieceCountValidity(moveToMake.getPiece());
			}
			catch (HantoException e)
			{
				return new HantoMoveRecord(null,null,null);
			}
			gameBoard.addPiece(new HantoCoordinateImpl(moveToMake.getTo()), new HantoPieceImpl(playerColor, moveToMake.getPiece()));
		}
		else
		{
			gameBoard.removePiece(new HantoCoordinateImpl(moveToMake.getTo()));
			gameBoard.addPiece(new HantoCoordinateImpl(moveToMake.getTo()), new HantoPieceImpl(playerColor, moveToMake.getPiece()));		}
		
		turnsTaken++;
		if(moveToMake.getPiece() == BUTTERFLY)
		{
			butterflyLocation = moveToMake.getTo();
		}
		return moveToMake;
	}

}
