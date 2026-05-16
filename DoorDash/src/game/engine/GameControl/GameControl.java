package game.engine.GameControl;

import java.io.IOException;

import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import game.engine.Game;
import game.engine.Role;
import game.engine.GUI.GUI;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.MultiTasker;

public class GameControl {
	private static Role choosen_role = Role.SCARER;
	private static Game game;

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
	public static TranslateTransition handleMoveMonsterOnBoardHelperX(
			int stepsToMove, Circle cir) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(cir);
		transition.setDuration(Duration.seconds(2)); // Faster duration so it
														// doesn't take forever

		// Positive stepsToMove = Right. Negative stepsToMove = Left.
		transition.setByX(stepsToMove * 0.0825 * GUI.getScreenHeight());
		return transition;
	}

	// 2. UPDATED MAIN METHOD
	public static void handleMoveMonsterOnBoard(int finalPosition, Circle cir) {
		int startPosition = game.getCurrent().getPosition();
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
					transition1 = handleMoveMonsterOnBoardHelperX(
							movedDistanceTillRowEndYaMohab, cir);
				} else {
					// Odd Row: Move Left (Negative steps)
					transition1 = handleMoveMonsterOnBoardHelperX(
							-movedDistanceTillRowEndYaMohab, cir);
				}

				// --- STEP 2: Move down a row ---
				transition2 = handleMoveMonsterOnBoardHelperY(cir);

				// --- STEP 3: Move across the new row ---
				// We calculate how many steps are left over after finishing
				// Step 1

				int remainingSteps = (finalPosition - startPosition)
						- movedDistanceTillRowEndYaMohab - 1;
				movedDistanceTillRowEndYaMohab = 9 - (index % 10);
				index = index + movedDistanceTillRowEndYaMohab + 1;

				currentRow = currentRow + 1;

				sequence.getChildren().addAll(transition1, transition2);

			}
			// We moved up, so we are on the next row

			if ((currentRow) % 2 == 0) {
				// New Row is Even: Move Right (Positive steps)
				transition3 = handleMoveMonsterOnBoardHelperX(
						(finalPosition - index), cir);
			} else {
				// New Row is Odd: Move Left (Negative steps)
				transition3 = handleMoveMonsterOnBoardHelperX(
						-(finalPosition - index), cir);
			}
			sequence.getChildren().add(transition3);
		} else {
			// --- DOES NOT CHANGE ROWS ---
			int stepsToMove = finalPosition - startPosition;

			if (currentRow % 2 == 0) {
				transition1 = handleMoveMonsterOnBoardHelperX(stepsToMove, cir);
			} else {
				transition1 = handleMoveMonsterOnBoardHelperX(-stepsToMove, cir);
			}

			sequence.getChildren().add(transition1);
		}

		sequence.play();
	}

	public static TranslateTransition handleMoveMonsterOnBoardHelperY(Circle cir) {
		TranslateTransition transition = new TranslateTransition();
		transition.setNode(cir);
		transition.setDuration(Duration.seconds(2)); // Takes 2 seconds to
														// complete one way
		transition.setByY(0.0825 * GUI.getScreenHeight());
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

		}
	}

	public static Game getGame() {
		return game;
	}

}