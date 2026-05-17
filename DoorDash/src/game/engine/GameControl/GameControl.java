package game.engine.GameControl;

import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import game.engine.Board;
import game.engine.Game;
import game.engine.Role;
import game.engine.GUI.GUI;
import game.engine.cells.CardCell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.cells.TransportCell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;

public class GameControl {
	private static Role choosen_role = Role.SCARER;
	private static Game game;
	private static double time;

	public static void handleChoosenRole() {
		if (choosen_role == Role.SCARER) {
			choosen_role = Role.LAUGHER;
			GUI.updateLabel(GUI.getRole_Question_Label(), "YOU ARE A LAUGHER!");
		} else {
			choosen_role = Role.SCARER;
			GUI.updateLabel(GUI.getRole_Question_Label(), "YOU ARE A SCARER!");
		}
		System.out.println(choosen_role.toString());
	}

	public static void handlePlayButton() {
		startGame();
	}

	public static void startGame() {
		try {
			game = new Game(choosen_role);
			System.out.println("kl");
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	// 1. UPDATED HELPER: It now only takes 'stepsToMove'
	public static TranslateTransition handleMoveMonsterOnBoardHelperX(int stepsToMove, Circle cir) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(cir);
		
		// FIX: The duration MUST be dynamically calculated based on steps. 
		// This syncs the physical animation speed with your 'time' accumulation variable (0.2s per step).
		double duration = Math.abs(stepsToMove) * 0.2;
		if (duration <= 0) duration = 0.01; // Safety fallback so transition doesn't break on 0
		
		transition.setDuration(Duration.seconds(duration)); 
		transition.setByX(stepsToMove * 0.0825 * GUI.getScreenHeight());
		return transition;
	}

	// 2. UPDATED MAIN METHOD
public static void handleMoveMonsterOnBoardForward(int startPosition ,int finalPosition, Circle cir) {
		
		int movedDistanceTillRowEndYaMohab = 9 - (startPosition % 10);

		// Track the actual ROW, not the cell position
		int currentRow = startPosition / 10;

		TranslateTransition transition1;
		TranslateTransition transition2;
		TranslateTransition transition3;

		SequentialTransition sequence = new SequentialTransition();

		int index = startPosition;

		if ((finalPosition - index) > movedDistanceTillRowEndYaMohab) {
			while ((finalPosition - index) > movedDistanceTillRowEndYaMohab) {

				// --- STEP 1: Move to the end of the current row ---
				if (currentRow % 2 == 0) {
					// Even Row: Move Right (Positive steps)
					transition1 = handleMoveMonsterOnBoardHelperX(movedDistanceTillRowEndYaMohab, cir);
				} else {
					// Odd Row: Move Left (Negative steps)
					transition1 = handleMoveMonsterOnBoardHelperX(-movedDistanceTillRowEndYaMohab, cir);
				}
				time += Math.abs(movedDistanceTillRowEndYaMohab * 0.2);
				
				// --- STEP 2: Move down a row ---
				transition2 = handleMoveMonsterOnBoardHelperYDOWN(cir);
				time += 1;
				
				// --- STEP 3: CRITICAL FIX - Update indices for the next iteration ---
				// You MUST update index FIRST, then calculate the distance for the new row
				index = index + movedDistanceTillRowEndYaMohab + 1;
				movedDistanceTillRowEndYaMohab = 9 - (index % 10); 
				
				currentRow = currentRow + 1;

				sequence.getChildren().addAll(transition1, transition2);
			}
			
			// We moved up, so we are on the final destination row
			if ((currentRow) % 2 == 0) {
				// New Row is Even: Move Right (Positive steps)
				transition3 = handleMoveMonsterOnBoardHelperX((finalPosition - index), cir);
			} else {
				// New Row is Odd: Move Left (Negative steps)
				transition3 = handleMoveMonsterOnBoardHelperX(-(finalPosition - index), cir);
			}
			time += Math.abs((finalPosition - index) * 0.2);
			sequence.getChildren().add(transition3);
			
		} else {
			// --- DOES NOT CHANGE ROWS ---
			int stepsToMove = finalPosition - startPosition;

			if (currentRow % 2 == 0) {
				transition1 = handleMoveMonsterOnBoardHelperX(stepsToMove, cir);
			} else {
				transition1 = handleMoveMonsterOnBoardHelperX(-stepsToMove, cir);
			}
			time += Math.abs((stepsToMove) * 0.2);
			sequence.getChildren().add(transition1);
		}

		sequence.play();
	}
	
	public static void handleMoveMonsterOnBoardBackward(int startPosition, int finalPosition, Circle cir) {
	    
	    int movedDistanceTillRowEndYaMohab = (startPosition % 10);

	    // Track the actual ROW, not the cell position
	    int currentRow = startPosition / 10;

	    TranslateTransition transition1;
	    TranslateTransition transition2;
	    TranslateTransition transition3;

	    SequentialTransition sequence = new SequentialTransition();

	    int index = startPosition;

	    if ((index - finalPosition) > movedDistanceTillRowEndYaMohab) {
	        while ((index - finalPosition) > movedDistanceTillRowEndYaMohab) {

	            // --- STEP 1: Move to the start of the current row ---
	            if (currentRow % 2 == 0) {
	                // Even Row (0, 2, 4): Backwards means moving Left (Negative steps)
	                transition1 = handleMoveMonsterOnBoardHelperX(
	                        -movedDistanceTillRowEndYaMohab, cir);
	            } else {
	                // Odd Row (1, 3, 5): Backwards means moving Right (Positive steps)
	                transition1 = handleMoveMonsterOnBoardHelperX(
	                        movedDistanceTillRowEndYaMohab, cir);
	            }
	            time+=Math.abs(movedDistanceTillRowEndYaMohab*0.2);
	            // --- STEP 2: Move UP a row ---
	            transition2 = handleMoveMonsterOnBoardHelperYUP(cir);
	            time+=1;
	            // --- STEP 3: Update indices for the next iteration ---
	            // CRITICAL FIX: Update `index` FIRST, then calculate the new `movedDistance`
	            index = index - movedDistanceTillRowEndYaMohab - 1;
	            movedDistanceTillRowEndYaMohab = (index % 10); // This will correctly evaluate to 9
	            currentRow = currentRow - 1;

	            sequence.getChildren().addAll(transition1, transition2);
	        }

	        // We moved up, so we are on the final row
	        if ((currentRow) % 2 == 0) {
	            // New Row is Even: Backwards means moving Left
	            // (finalPosition - index) is negative, which handles the Left direction automatically
	            transition3 = handleMoveMonsterOnBoardHelperX(
	                    (finalPosition - index), cir);
	        } else {
	            // New Row is Odd: Backwards means moving Right
	            // -(finalPosition - index) makes it positive, moving it Right
	            transition3 = handleMoveMonsterOnBoardHelperX(
	                    -(finalPosition - index), cir);
	        }
	        time+=Math.abs((finalPosition - index)*0.2);
	        sequence.getChildren().add(transition3);
	        
	    } else {
	        // --- DOES NOT CHANGE ROWS ---
	        int stepsToMove = finalPosition - startPosition; // This results in a negative number

	        if (currentRow % 2 == 0) {
	            transition1 = handleMoveMonsterOnBoardHelperX(stepsToMove, cir); // Moves Left
	        } else {
	            transition1 = handleMoveMonsterOnBoardHelperX(-stepsToMove, cir); // Moves Right
	        }
	        time+=Math.abs((stepsToMove)*0.2);
	        sequence.getChildren().add(transition1);
	    }

	    sequence.play();
	}

	public static TranslateTransition handleMoveMonsterOnBoardHelperYDOWN(Circle cir) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(cir);
		transition.setDuration(Duration.seconds(1)); // Takes 2 seconds to
														// complete one way
		transition.setByY(0.0825 * GUI.getScreenHeight());
		return transition;
	}
	
	public static TranslateTransition handleMoveMonsterOnBoardHelperYUP(Circle cir) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(cir);
		transition.setDuration(Duration.seconds(1)); // Takes 2 seconds to
														// complete one way
		transition.setByY(-0.0825 * GUI.getScreenHeight());
		return transition;
	}

	public static void handleUsePowerUpYES() {
		try {
			game.usePowerup();
			if (game.getCurrent() instanceof Dasher){
				GUI.displayAlert("POWERUP USED!",
						"You lost 500 energy!\n Gained 3x movement speed for the next 3 turns");
				if (game.getCurrent().equals(game.getPlayer())) {
					GUI.updateLabel(GUI.getPlayerStatus(),
							"Momentum Rush for 3 turns");
				} else {
					GUI.updateLabel(GUI.getOpponentStatus(),
							"Momentum Rush for 3 turns");
				}
			}else {
				if (game.getCurrent() instanceof Dynamo) {
					GUI.displayAlert("POWERUP USED!",
							"You lost 500 energy!\n The other opponent is frozen for 1 turn");
					if (game.getCurrent().equals(game.getPlayer())) {
						GUI.updateLabel(GUI.getOpponentMonsterFrozenLabel(),
								"Frozen");
					} else {
						GUI.updateLabel(GUI.getPlayerMonsterFrozenLabel(),
								"Frozen");
					}
				} else {
					if (game.getCurrent() instanceof MultiTasker) {
						GUI.displayAlert("POWERUP USED!",
								"You lost 500 energy!\n Move at normal speed for the next 2 turns");
						if (game.getCurrent().equals(game.getPlayer())) {
							GUI.updateLabel(GUI.getPlayerStatus(),
									"Focus Mode for 2 turns");
						} else {
							GUI.updateLabel(GUI.getOpponentStatus(),
									"Focus Mode for 2 turns");
						}
					} else {
						GUI.displayAlert(
								"POWERUP USED!",
								"You lost 500 energy!\n Steal Energy out of all monsters present (teammates and opponents)");
						if (game.getCurrent().equals(game.getPlayer())) {
							GUI.updateLabel(GUI.getPlayerStatus(),
									"CHAIN ATTACK!!!");
						} else {
							GUI.updateLabel(GUI.getOpponentStatus(),
									"CHAIN ATTACK!!!");
						}
					}
				}
			}

			if (game.getCurrent().equals(game.getPlayer())) {
				GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game
						.getCurrent().getEnergy() + "energy");
				
			} else {
				GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game
						.getCurrent().getEnergy() + "energy");
			}
		} catch (OutOfEnergyException e) {
			GUI.displayAlert("NOT ENOUGH ENERGY","You dont have 500+ energy");
		} finally {
			GUI.hidePowerup();
			GUI.showRollRice();
		}
	}
	
	public static void handleUsePowerUpNO() {
		GUI.hidePowerup();
		GUI.showRollRice();
	}
	
	
	public static void handleMoveMonsterOnBoard(int startPosition, int finalPosition, Circle cir) {
		if (finalPosition>startPosition)
			handleMoveMonsterOnBoardForward(startPosition, finalPosition, cir);
		else
			handleMoveMonsterOnBoardBackward(startPosition, finalPosition, cir);
	}
	

	
	public static void handleRollDice(){
		try {
			time = 0;

			// FIX 1: Capture the acting monster AND the opponent BEFORE playTurn() switches them
			Monster actingMonster = game.getCurrent();
			int startPos = actingMonster.getPosition();
			
			Monster opponentMonster = actingMonster.equals(game.getPlayer()) ? game.getOpponent() : game.getPlayer();
			int opponentStartPos = opponentMonster.getPosition();
			
			game.playTurn();
			
			// From this line onward, NEVER use game.getCurrent() to check who is moving!
			GUI.updateLabel(GUI.getRollDiceLabel(), "You rolled: " + game.getRoll());
			
			// FIX 2: Define both circles to be used in animations
			Circle currentCircle = actingMonster.equals(game.getPlayer()) ? GUI.getPlayerc() : GUI.getOpponentc();
			Circle opponentCircle = actingMonster.equals(game.getPlayer()) ? GUI.getOpponentc() : GUI.getPlayerc();
			
			int preEffectPos = Board.getPreEffectedPosition();

			handleMoveMonsterOnBoard(startPos, preEffectPos, currentCircle);
			
			PauseTransition pause = new PauseTransition(Duration.seconds(time));
			pause.setOnFinished(event -> {
				
				if (Board.getPreEffectedCell() instanceof CardCell){
					String cardName = CardCell.getDrawnCard().getName();
					
					if (cardName.equals("Position Swap")){
						if (preEffectPos != actingMonster.getPosition()){
							GUI.decrementCardCounter();
							GUI.displayAlert("Position Swap!", "Card drawn: Position Swap\n Your Positions are swapped!\n Cards remaining are: " + GUI.getCardCounter());
							GUI.getAlertStage().setOnHidden(e ->{
								
								// FIX 3: Use actingMonster and opponentMonster instead of game.getCurrent()
								int currentFinalPos = actingMonster.getPosition();
								int opponentFinalPos = opponentMonster.getPosition();
								
								handleMoveMonsterOnBoard(preEffectPos, currentFinalPos, currentCircle);
								handleMoveMonsterOnBoard(opponentStartPos, opponentFinalPos, opponentCircle);
								
								GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: " +game.getOpponent().getPosition()+"");
								GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: " +game.getPlayer().getPosition()+"");
								GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
							});
								
						} else {
							GUI.decrementCardCounter();
							GUI.displayAlert("Position Swap!", "Card drawn: Position Swap\n No swap occured!\n Cards remaining are: " + GUI.getCardCounter());
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						}
					} else if(cardName.equals("Super Shield")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Super Shield!", "Card drawn: Super Shield\n You are shielded!\n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							if (actingMonster.equals(game.getPlayer()))
								GUI.updateLabel(GUI.getPlayerMonsterShieldedLabel(), "Shielded");
							else
								GUI.updateLabel(GUI.getOpponentMonsterShieldedLabel(), "Shielded");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});
		
					} else if(cardName.equals("Small Snatcher")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Small Snatcher!", actingMonster.equals(game.getPlayer()) ? "Card drawn: Small Snatcher\n " + game.getPlayer().getName() + " snatched 50 energy from " + game.getOpponent().getName() + "\n Cards remaining are: " + GUI.getCardCounter() :  "Card drawn: Small Snatcher\n " + game.getOpponent().getName() + " snatched 50 energy from " + game.getPlayer().getName() + "\n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game.getPlayer().getEnergy()+" energy");
							GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game.getOpponent().getEnergy()+" energy");
							GUI.updateLabel(GUI.getPlayerMonsterShieldedLabel(), game.getPlayer().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getOpponentMonsterShieldedLabel(), game.getOpponent().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});

					} else if(cardName.equals("Sneaky Thief")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Sneaky Thief!", actingMonster.equals(game.getPlayer()) ? "Card drawn: Sneaky Thief\n " + game.getPlayer().getName() + " snatched 100 energy from " + game.getOpponent().getName() + "\n Cards remaining are: " + GUI.getCardCounter() :  "Card drawn: Sneaky Thief\n " + game.getOpponent().getName() + " snatched 100 energy from " + game.getPlayer().getName() + "\n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game.getPlayer().getEnergy()+" energy");
							GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game.getOpponent().getEnergy()+" energy");
							GUI.updateLabel(GUI.getPlayerMonsterShieldedLabel(), game.getPlayer().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getOpponentMonsterShieldedLabel(), game.getOpponent().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});

					} else if(cardName.equals("Mega Drain")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Mega Drain!", actingMonster.equals(game.getPlayer()) ? "Card drawn: Mega Drain\n " + game.getPlayer().getName() + " snatched 150 energy from " + game.getOpponent().getName() + "\n Cards remaining are: " + GUI.getCardCounter() :  "Card drawn: Mega Drain\n " + game.getOpponent().getName() + " snatched 150 energy from " + game.getPlayer().getName() + "\n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game.getPlayer().getEnergy()+" energy");
							GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game.getOpponent().getEnergy()+" energy");
							GUI.updateLabel(GUI.getPlayerMonsterShieldedLabel(), game.getPlayer().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getOpponentMonsterShieldedLabel(), game.getOpponent().isShielded() ? "Shielded" : "Not Shielded");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});

					} else if(cardName.equals("Contamination Code")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Contamination Code!", actingMonster.equals(game.getPlayer()) ? "Card drawn: Contamination Code\n " + game.getPlayer().getName() + " will move back to the start \n Cards remaining are: " + GUI.getCardCounter() :  "Card drawn: Contamination Code\n " + game.getOpponent().getName() + " will move back to the start \n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							handleMoveMonsterOnBoard(preEffectPos, 0, currentCircle);
							GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: " +game.getPlayer().getPosition()+"");
							GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: " +game.getOpponent().getPosition()+"");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});
						
					} else if(cardName.equals("2319 Alert")){
						GUI.decrementCardCounter();
						GUI.displayAlert("2319 Alert!", actingMonster.equals(game.getPlayer()) ? "Card drawn: 2319 Alert\n " + game.getOpponent().getName() + " will move back to the start \n Cards remaining are: " + GUI.getCardCounter() :  "Card drawn: 2319 Alert\n " + game.getPlayer().getName() + " will move back to the start \n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							handleMoveMonsterOnBoard(opponentStartPos, 0, opponentCircle);
							GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: " +game.getPlayer().getPosition()+"");
							GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: " +game.getOpponent().getPosition()+"");
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});
						
					} else if(cardName.equals("Mind Scramble")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Mind Scramble!", "Card drawn: Mind Scramble\n Both of the players are confused for 2 turns \n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							GUI.updateLabel(GUI.getPlayerMonsterCurrentRoleLabel(), game.getPlayer().getRole().toString());
							GUI.updateLabel(GUI.getOpponentMonsterCurrentRoleLabel(), game.getOpponent().getRole().toString());
							GUI.updateLabel(GUI.getPlayerMonsterConfusedLabel(), "Confused for: " + game.getPlayer().getConfusionTurns() + " turns" );
							GUI.updateLabel(GUI.getOpponentMonsterConfusedLabel(),"Confused for: " + game.getOpponent().getConfusionTurns() + " turns" );
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});
						
					} else if(cardName.equals("Total Confusion")){
						GUI.decrementCardCounter();
						GUI.displayAlert("Total Confusion!", "Card drawn: Total Confusion\n Both of the players are confused for 3 turns \n Cards remaining are: " + GUI.getCardCounter());
						GUI.getAlertStage().setOnHidden(e ->{
							GUI.updateLabel(GUI.getPlayerMonsterCurrentRoleLabel(), game.getPlayer().getRole().toString());
							GUI.updateLabel(GUI.getOpponentMonsterCurrentRoleLabel(), game.getOpponent().getRole().toString());
							GUI.updateLabel(GUI.getPlayerMonsterConfusedLabel(), "Confused for: " + game.getPlayer().getConfusionTurns() + " turns" );
							GUI.updateLabel(GUI.getOpponentMonsterConfusedLabel(),"Confused for: " + game.getOpponent().getConfusionTurns() + " turns" );
							GUI.updateLabel(GUI.getCardCounterLabel(), GUI.getCardCounter()+"");
						});
					}
					
				} else if (Board.getPreEffectedCell() instanceof ConveyorBelt){
					GUI.displayAlert("ConveyorBelt!", "You are going UP!");
					GUI.getAlertStage().setOnHidden(e ->{
						// FIX 4: Use actingMonster.getPosition() instead of game.getCurrent()
						handleMoveMonsterOnBoard(preEffectPos, actingMonster.getPosition(), currentCircle);
						GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: " +game.getPlayer().getPosition());
						GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: " +game.getOpponent().getPosition()+"");
					});
					
				} else if (Board.getPreEffectedCell() instanceof ContaminationSock){
					GUI.displayAlert("ContaminationSock!", "You are going DOWN!");
					GUI.getAlertStage().setOnHidden(e ->{
						// FIX 4: Use actingMonster.getPosition() instead of game.getCurrent()
						handleMoveMonsterOnBoard(preEffectPos, actingMonster.getPosition(), currentCircle);
						GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: " +game.getPlayer().getPosition());
						GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: " +game.getOpponent().getPosition());
					});
					
				} else if (Board.getPreEffectedCell() instanceof DoorCell){
					
					DoorCell door = (DoorCell) Board.getPreEffectedCell();
					
					boolean match = actingMonster.getRole().equals(door.getRole());
					String txt = match ? "ROLE MATCH Gaining " + door.getEnergy() : "ROLE MISMATCH Losing " + door.getEnergy();
					
					StringBuilder txt2 = new StringBuilder();
					
					for (int i=0; i < Board.getStationedMonsters().size(); i++){
						Monster stationed = Board.getStationedMonsters().get(i);
						
						if (stationed.getRole().equals(actingMonster.getRole())){
							txt2.append(stationed.getName())
								.append(": ")
								.append(stationed.getEnergy())
								.append("\n");
						}
					}
					
					GUI.displayAlert("DoorCell", txt + "\n" + txt2.toString());
					GUI.getAlertStage().setOnHidden(e ->{
						GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game.getPlayer().getEnergy()+" energy");
						GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game.getOpponent().getEnergy()+" energy");
					});
				}else if (Board.getPreEffectedCell() instanceof MonsterCell){
					
					MonsterCell monsterCell = (MonsterCell) Board.getPreEffectedCell();
					
					boolean match = actingMonster.getRole().equals(monsterCell.getCellMonster().getRole());
					
					String txt2=actingMonster.getEnergy() > opponentMonster.getEnergy() ? "SWAP ENERGIES!" : "NO SWAP OCCURS!";
					String txt = match ? "ROLE MATCH \nExecuting Powerup for free" : "ROLE MISMATCH \n" + txt2;
					
					GUI.displayAlert("MonsterCell", txt);
					
					GUI.getAlertStage().setOnHidden(e ->{
						if (match==true){
							if (actingMonster instanceof Dasher){
								GUI.displayAlert("POWERUP USED!",
										"Gained 3x movement speed for the next 3 turns");
								if (actingMonster.equals(game.getPlayer())) {
									GUI.updateLabel(GUI.getPlayerStatus(),
											"Momentum Rush for 3 turns");
								} else {
									GUI.updateLabel(GUI.getOpponentStatus(),
											"Momentum Rush for 3 turns");
								}
							}else {
								if (actingMonster instanceof Dynamo) {
									GUI.displayAlert("POWERUP USED!",
											"The other opponent is frozen for 1 turn");
									if (actingMonster.equals(game.getPlayer())) {
										GUI.updateLabel(GUI.getOpponentMonsterFrozenLabel(),
												"Frozen");
									} else {
										GUI.updateLabel(GUI.getPlayerMonsterFrozenLabel(),
												"Frozen");
									}
								} else {
									if (actingMonster instanceof MultiTasker) {
										GUI.displayAlert("POWERUP USED!",
												"Move at normal speed for the next 2 turns");
										if (actingMonster.equals(game.getPlayer())) {
											GUI.updateLabel(GUI.getPlayerStatus(),
													"Focus Mode for 2 turns");
										} else {
											GUI.updateLabel(GUI.getOpponentStatus(),
													"Focus Mode for 2 turns");
										}
									} else {
										GUI.displayAlert(
												"POWERUP USED!",
												"Steal Energy out of all monsters present (teammates and opponents)");
										if (actingMonster.equals(game.getPlayer())) {
											GUI.updateLabel(GUI.getPlayerStatus(),
													"CHAIN ATTACK!!!");
										} else {
											GUI.updateLabel(GUI.getOpponentStatus(),
													"CHAIN ATTACK!!!");
										}
									}
								}
							}
	
							
								
							
						}
						GUI.updateLabel(GUI.getPlayerMonsterEnergyLabel(), game.getPlayer().getEnergy() + " energy");
						GUI.updateLabel(GUI.getOpponentMonsterEnergyLabel(), game.getOpponent().getEnergy() + " energy");
					});
					//k
				}
				
				GUI.updateLabel(GUI.getPlayerMonsterPositionLabel(), "Position: "+game.getPlayer().getPosition());
				GUI.updateLabel(GUI.getOpponentMonsterPositionLabel(), "Position: "+game.getOpponent().getPosition());
			});
			
			pause.play();
			
		} catch (InvalidMoveException e) {
			GUI.displayAlert("Move Failed", e.getMessage());
		}
	}
		
		
	

	
	public static double getTime() {
		return time;
	}

	public static Game getGame() {
		return game;
	}

}