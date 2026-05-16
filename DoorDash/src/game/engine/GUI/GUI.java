
package game.engine.GUI;

import java.io.IOException;

import game.engine.Role;
import game.engine.GameControl.GameControl;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.MultiTasker;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	private Scene mainScene;

	private VBox StartScreen;
	private VBox AbousaVideo;
	private VBox MainScreen;
	private static HBox GameScreen;
	private VBox GameOverScreen;

	private static Label Role_Question_Label;
	private Button SWITCH_Button;
	private Button PLAY_Button;
	private Button INSTRUCTIONS_Button;

	private static Label playerMonsterEnergyLabel;
	private static Label playerStatus;
	private static Label opponentMonsterEnergyLabel;
	private static Label opponentStatus;
	private static Label playerMonsterFrozenLabel;
	private static Label opponentMonsterFrozenLabel;

	private static Label playerTurnLabel;
	private static Label youLabel;
	private static StackPane playerWidget;
	private static Label playerMonsterNameLabel;
	private static Label playerMonsterOriginalRoleLabel;
	private static Label playerMonsterCurrentRoleLabel;
	private static String playerMonsterType;
	private static Label playerMonsterTypeLabel;
	private static Label playerMonsterPositionLabel;
	private static Label playerMonsterShieldedLabel;
	private static Label playerMonsterConfusedLabel;

	private static Label opponentTurnLabel;
	private static Label opponentLabel;
	private static StackPane opponentWidget;
	private static Label opponentMonsterNameLabel;
	private static Label opponentMonsterOriginalRoleLabel;
	private static Label opponentMonsterCurrentRoleLabel;
	private static String opponentMonsterType;
	private static Label opponentMonsterTypeLabel;
	private static Label opponentMonsterPositionLabel;
	private static Label opponentMonsterShieldedLabel;
	private static Label opponentMonsterConfusedLabel;

	@Override
	public void start(Stage primaryStage) throws Exception {

		buildMainScreen(primaryStage);

		primaryStage.setTitle("Monsters INC.");
		mainScene = new Scene(MainScreen, 640, 480);
		primaryStage.setScene(mainScene);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}

	public void buildMainScreen(Stage primaryStage) {
		// Elements of mainScreen
		Role_Question_Label = new Label("YOU ARE A SCARER!");

		SWITCH_Button = new Button("Switch");
		SWITCH_Button.setOnAction(e -> {
			GameControl.handleChoosenRole();
		});

		PLAY_Button = new Button("PLAY!");
		PLAY_Button.setOnAction(e -> {
			buildGameScreen();
			mainScene.setRoot(GameScreen);
		});

		INSTRUCTIONS_Button = new Button("RULES");

		MainScreen = new VBox(Role_Question_Label, SWITCH_Button, PLAY_Button,
				INSTRUCTIONS_Button);

		// Bind Role_Question_Label button text size
		Role_Question_Label.styleProperty()
				.bind(Bindings.concat(
						"-fx-font-family: 'Forte'; -fx-font-size: ", MainScreen
								.widthProperty().divide(30).asString(), "px;"));

		// Bind SCARER button text size
		SWITCH_Button.styleProperty().bind(
				Bindings.concat("-fx-font-size: ", MainScreen.widthProperty()
						.divide(30.0).asString(), "px;"));

		// Bind PLAY button text size
		PLAY_Button.styleProperty().bind(
				Bindings.concat("-fx-font-size: ", MainScreen.widthProperty()
						.divide(30.0).asString(), "px;"));

		// Bind INSTRUCTIONS button text size
		INSTRUCTIONS_Button.styleProperty().bind(
				Bindings.concat("-fx-font-size: ", MainScreen.widthProperty()
						.divide(30.0).asString(), "px;"));

		// Elements Size of mainScreen
		Role_Question_Label.prefHeightProperty().bind(
				MainScreen.heightProperty().divide(9));

		SWITCH_Button.prefWidthProperty().bind(
				MainScreen.widthProperty().divide(5));
		SWITCH_Button.prefHeightProperty().bind(
				MainScreen.heightProperty().divide(9));

		PLAY_Button.prefWidthProperty().bind(
				MainScreen.widthProperty().divide(5));
		PLAY_Button.prefHeightProperty().bind(
				MainScreen.heightProperty().divide(9));

		INSTRUCTIONS_Button.prefWidthProperty().bind(
				MainScreen.widthProperty().divide(5));
		INSTRUCTIONS_Button.prefHeightProperty().bind(
				MainScreen.heightProperty().divide(9));

		MainScreen.spacingProperty()
				.bind(MainScreen.heightProperty().divide(9));

		MainScreen.setAlignment(Pos.CENTER);

	}

	public void buildGameScreen() {
		GameControl.startGame();

		// LEFT LAYOUT
		StackPane BoardPane = new StackPane();
		GridPane Board = new GridPane();
		Pane tokenLayer = new Pane();
		// Make it mouse transparent so you can still click the buttons
		// underneath
		tokenLayer.setMouseTransparent(true);

		// RIGHT LAYOUT
		StackPane RightLayoutFrame = new StackPane();
		VBox RightLayout = new VBox();
		HBox upperlayout = new HBox();
		VBox playerLayout = new VBox();
		VBox VSLayout = new VBox();
		VBox opponentLayout = new VBox();

		Label usePowerup = new Label("USE POWERUP?");
		Button yesButton = new Button("YES!");
		Button noButton = new Button("NO!");
		HBox yesNoPowerup = new HBox(yesButton, noButton);
		VBox DownLayout = new VBox(usePowerup, yesNoPowerup);
		GameScreen = new HBox(BoardPane, RightLayoutFrame);

		// PlayerLayoutElements
		playerTurnLabel = new Label("YOUR TURN!");
		youLabel = new Label("YOU");
		playerWidget = new StackPane();
		playerMonsterNameLabel = new Label(GameControl.getGame().getPlayer()
				.getName());
		playerMonsterOriginalRoleLabel = new Label(GameControl.getGame()
				.getPlayer().getOriginalRole().toString()
				+ "(Original)");
		playerMonsterCurrentRoleLabel = new Label(GameControl.getGame()
				.getPlayer().getRole().toString()
				+ "(Current)");
		playerMonsterType = (GameControl.getGame().getPlayer() instanceof Dasher ? "Dasher"
				: GameControl.getGame().getPlayer() instanceof MultiTasker ? "MultiTasker"
						: GameControl.getGame().getPlayer() instanceof Dynamo ? "Dynamo"
								: "Schemer");
		playerMonsterTypeLabel = new Label(playerMonsterType);
		playerMonsterEnergyLabel = new Label(GameControl.getGame().getPlayer()
				.getEnergy()
				+ " energy");
		playerMonsterPositionLabel = new Label("Position: "
				+ GameControl.getGame().getPlayer().getPosition());
		playerMonsterShieldedLabel = new Label(GameControl.getGame()
				.getPlayer().isShielded() ? "Shielded" : "Not Shielded");
		playerMonsterConfusedLabel = new Label("Confused for: "
				+ GameControl.getGame().getPlayer().getConfusionTurns()
				+ " turns");
		playerMonsterFrozenLabel = new Label(GameControl.getGame().getPlayer()
				.isFrozen() ? "Frozen" : "Not Frozen");
		playerStatus = new Label(
				GameControl.getGame().getPlayer() instanceof Dasher ? "Momentum Rush for 0 turns"
						: GameControl.getGame().getPlayer() instanceof MultiTasker ? "Focus Mode for 0 turns"
								: GameControl.getGame().getPlayer() instanceof Dynamo ? "Energy Freeze"
										: "No Chain Attack");

		// OpponentLayoutElements
		opponentTurnLabel = new Label("OPPONENT TURN!");
		opponentLabel = new Label("OPPONENT");
		opponentWidget = new StackPane();
		opponentMonsterNameLabel = new Label(GameControl.getGame()
				.getOpponent().getName());
		opponentMonsterOriginalRoleLabel = new Label(GameControl.getGame()
				.getOpponent().getOriginalRole().toString()
				+ "(Original)");
		opponentMonsterCurrentRoleLabel = new Label(GameControl.getGame()
				.getOpponent().getRole().toString()
				+ "(Current)");
		opponentMonsterType = (GameControl.getGame().getOpponent() instanceof Dasher ? "Dasher"
				: GameControl.getGame().getOpponent() instanceof MultiTasker ? "MultiTasker"
						: GameControl.getGame().getOpponent() instanceof Dynamo ? "Dynamo"
								: "Schemer");
		opponentMonsterTypeLabel = new Label(opponentMonsterType);
		opponentMonsterEnergyLabel = new Label(GameControl.getGame()
				.getOpponent().getEnergy()
				+ " energy");
		opponentMonsterPositionLabel = new Label("Position: "
				+ GameControl.getGame().getOpponent().getPosition());
		opponentMonsterShieldedLabel = new Label(GameControl.getGame()
				.getOpponent().isShielded() ? "Shielded" : "Not Shielded");
		opponentMonsterConfusedLabel = new Label("Confused for: "
				+ GameControl.getGame().getOpponent().getConfusionTurns()
				+ " turns");
		opponentMonsterFrozenLabel = new Label(GameControl.getGame()
				.getOpponent().isFrozen() ? "Frozen" : "Not Frozen");
		opponentStatus = new Label(
				GameControl.getGame().getOpponent() instanceof Dasher ? "Momentum Rush for 0 turns"
						: GameControl.getGame().getOpponent() instanceof MultiTasker ? "Focus Mode for 0 turns"
								: GameControl.getGame().getOpponent() instanceof Dynamo ? "Energy Freeze"
										: "No Chain Attack");

		// Board drawing
		int c = 0;
		Circle playerc = new Circle();
		Circle opponentc = new Circle();
		playerc.setFill(Color.BLUE);
		opponentc.setFill(Color.RED);
		playerc.radiusProperty().bind(BoardPane.heightProperty().divide(60));
		opponentc.radiusProperty().bind(BoardPane.heightProperty().divide(60));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Button btn = new Button();
				Label textLabel = new Label(" " + c);
				StackPane customLayout = new StackPane();
				customLayout.getChildren().addAll(textLabel);
				StackPane.setAlignment(textLabel, Pos.TOP_LEFT);
				btn.setGraphic(customLayout);

				btn.styleProperty().bind(
						Bindings.concat("-fx-background-color: #f2efea; ",
								"-fx-border-color: #1c113c; ",
								"-fx-border-width: ", BoardPane
										.heightProperty().divide(400)
										.asString(), "px; ",
								"-fx-border-radius: ", BoardPane
										.heightProperty().divide(400)
										.asString(), "px;"));
				textLabel.setStyle(

				"-fx-text-fill: #1c113c;");

				if ((c / 10) % 2 == 0 && c % 10 != 9) {
					c++;

				} else {
					if ((c / 10) % 2 == 0 && c % 10 == 9) {
						c = c + 10;
					} else {
						if ((c / 10) % 2 != 0 && c % 10 != 0) {
							c--;
						} else {
							c = c + 10;
						}
					}
				}

				// 1. Tell the button to expand to its maximum possible limits
				btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

				// 2. Tell the GridPane that this node should always grow to
				// fill space
				GridPane.setHgrow(btn, Priority.ALWAYS);
				GridPane.setVgrow(btn, Priority.ALWAYS);

				// i represents the column index, j represents the row index
				Board.add(btn, j, i);

			}

		}

		// Circles Monsters
		// Blue (Player): Bottom-Left of Cell (0,0)
		playerc.centerXProperty().bind(
				tokenLayer.widthProperty().divide(10).multiply(0)
						.add(playerc.radiusProperty())
						.add(tokenLayer.widthProperty().divide(100)) // Dynamic
																		// padding
																		// instead
																		// of 5
				);

		playerc.centerYProperty().bind(
				tokenLayer.heightProperty().divide(10).multiply(1)
						.subtract(playerc.radiusProperty())
						.subtract(tokenLayer.heightProperty().divide(100)) // Dynamic
																			// padding
																			// instead
																			// of
																			// 5
				);

		// Red (Opponent): Bottom-Right of Cell (0,0)
		opponentc.centerXProperty().bind(
				tokenLayer.widthProperty().divide(10).multiply(1)
						.subtract(opponentc.radiusProperty())
						.subtract(tokenLayer.widthProperty().divide(100)) // Dynamic
																			// padding
																			// instead
																			// of
																			// 5
				);

		opponentc.centerYProperty().bind(
				tokenLayer.heightProperty().divide(10).multiply(1)
						.subtract(opponentc.radiusProperty())
						.subtract(tokenLayer.heightProperty().divide(100)) // Dynamic
																			// padding
																			// instead
																			// of
																			// 5
				);

		// Background cover
		// 1. Dynamically get the correct path to the image in this specific
		// package
		String imageUrl = getClass().getResource("white background.png")
				.toExternalForm();

		// 2. Inject that path into your CSS (Make sure to remove 'file:')
		String cssBackground = "-fx-background-image: url('" + imageUrl + "');"
				+ "-fx-background-size: cover;"
				+ "-fx-background-position: center center;"
				+ "-fx-background-repeat: no-repeat;";

		GameScreen.setStyle(cssBackground);

		// Layout Colors
		RightLayoutFrame.setStyle("-fx-background-color: blue;");
		upperlayout.setStyle("-fx-background-color: #ff0000;");
		playerLayout.setStyle("-fx-background-color: orange;");
		VSLayout.setStyle("-fx-background-color: blue;");
		opponentLayout.setStyle("-fx-background-color: green;");
		DownLayout.setStyle("-fx-background-color: black;");

		// Layout children
		RightLayoutFrame.getChildren().add(RightLayout);
		upperlayout.getChildren()
				.addAll(playerLayout, VSLayout, opponentLayout);
		playerLayout.getChildren().addAll(playerTurnLabel, youLabel,
				playerWidget, playerMonsterNameLabel,
				playerMonsterOriginalRoleLabel, playerMonsterCurrentRoleLabel,
				playerMonsterTypeLabel, playerMonsterEnergyLabel,
				playerMonsterPositionLabel, playerMonsterShieldedLabel,
				playerMonsterFrozenLabel, playerMonsterConfusedLabel,
				playerStatus);
		opponentLayout.getChildren().addAll(opponentTurnLabel, opponentLabel,
				opponentWidget, opponentMonsterNameLabel,
				opponentMonsterOriginalRoleLabel,
				opponentMonsterCurrentRoleLabel, opponentMonsterTypeLabel,
				opponentMonsterEnergyLabel, opponentMonsterPositionLabel,
				opponentMonsterShieldedLabel, opponentMonsterFrozenLabel,
				opponentMonsterConfusedLabel, opponentStatus);
		tokenLayer.getChildren().addAll(playerc, opponentc);
		BoardPane.getChildren().addAll(Board, tokenLayer);
		RightLayout.getChildren().addAll(upperlayout, DownLayout);

		// Layout alignment
		GameScreen.setAlignment(Pos.CENTER);
		RightLayout.setAlignment(Pos.CENTER);
		opponentLayout.setAlignment(Pos.CENTER);
		playerLayout.setAlignment(Pos.CENTER);
		DownLayout.setAlignment(Pos.CENTER);
		yesNoPowerup.setAlignment(Pos.CENTER);
		RightLayoutFrame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		// Board size
		BoardPane.prefWidthProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		BoardPane.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));

		BoardPane.minWidthProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		BoardPane.minHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));

		BoardPane.maxWidthProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		BoardPane.maxHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));

		// RightLayout size

		RightLayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		RightLayout.prefWidthProperty().bind(
				GameScreen.widthProperty().subtract(
						GameScreen.heightProperty().multiply(0.95)));

		upperlayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.7));
		upperlayout.prefWidthProperty().bind(
				GameScreen.widthProperty().subtract(
						GameScreen.heightProperty().multiply(0.95)));

		DownLayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.2));
		DownLayout.prefWidthProperty().bind(
				GameScreen.widthProperty().subtract(
						GameScreen.heightProperty().multiply(0.95)));

		playerLayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		playerLayout.prefWidthProperty().bind(
				GameScreen.widthProperty()
						.subtract(GameScreen.heightProperty().multiply(0.95))
						.multiply(0.4));

		VSLayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		VSLayout.prefWidthProperty().bind(
				GameScreen.widthProperty()
						.subtract(GameScreen.heightProperty().multiply(0.95))
						.multiply(0.2));

		opponentLayout.prefHeightProperty().bind(
				GameScreen.heightProperty().multiply(0.9));
		opponentLayout.prefWidthProperty().bind(
				GameScreen.widthProperty()
						.subtract(GameScreen.heightProperty().multiply(0.95))
						.multiply(0.4));

		GameScreen.spacingProperty().bind(
				GameScreen.widthProperty().divide(125));

		opponentTurnLabel.setVisible(false);

		// ButtonEvents
		yesButton.setOnAction(e -> {
			GameControl.handleUsePowerUpYES();
		});

	}

	public static void updateLabel(Label label, String newText) {
		label.setText(newText);
	}

	public static void displayAlert(String title, String message) {
		Stage alertStage = new Stage();
		alertStage.setTitle(title);

		Label label = new Label(message);
		Button closeButton = new Button("Continue Playing");
		closeButton.setOnAction(event -> alertStage.close());

		BorderPane pane = new BorderPane();
		pane.setTop(label);
		BorderPane.setAlignment(label, Pos.TOP_CENTER);
		pane.setCenter(closeButton);

		Scene scene = new Scene(pane, 400, 100);
		alertStage.setScene(scene);
		alertStage.show();
	}

	public static double getScreenHeight() {
		return Screen.getPrimary().getBounds().getHeight();
	}

	public static Label getRole_Question_Label() {
		return Role_Question_Label;
	}

	public static Label getPlayerMonsterEnergyLabel() {
		return playerMonsterEnergyLabel;
	}

	public static Label getPlayerStatus() {
		return playerStatus;
	}

	public static Label getOpponentMonsterEnergyLabel() {
		return opponentMonsterEnergyLabel;
	}

	public static Label getOpponentStatus() {
		return opponentStatus;
	}

	public static Label getPlayerMonsterFrozenLabel() {
		return playerMonsterFrozenLabel;
	}

	public static Label getOpponentMonsterFrozenLabel() {
		return opponentMonsterFrozenLabel;
	}

	public static Label getPlayerTurnLabel() {
		return playerTurnLabel;
	}

	public static Label getYouLabel() {
		return youLabel;
	}

	public static StackPane getPlayerWidget() {
		return playerWidget;
	}

	public static Label getPlayerMonsterNameLabel() {
		return playerMonsterNameLabel;
	}

	public static Label getPlayerMonsterOriginalRoleLabel() {
		return playerMonsterOriginalRoleLabel;
	}

	public static Label getPlayerMonsterCurrentRoleLabel() {
		return playerMonsterCurrentRoleLabel;
	}

	public static String getPlayerMonsterType() {
		return playerMonsterType;
	}

	public static Label getPlayerMonsterTypeLabel() {
		return playerMonsterTypeLabel;
	}

	public static Label getPlayerMonsterPositionLabel() {
		return playerMonsterPositionLabel;
	}

	public static Label getPlayerMonsterShieldedLabel() {
		return playerMonsterShieldedLabel;
	}

	public static Label getPlayerMonsterConfusedLabel() {
		return playerMonsterConfusedLabel;
	}

	public static Label getOpponentTurnLabel() {
		return opponentTurnLabel;
	}

	public static Label getOpponentLabel() {
		return opponentLabel;
	}

	public static StackPane getOpponentWidget() {
		return opponentWidget;
	}

	public static Label getOpponentMonsterNameLabel() {
		return opponentMonsterNameLabel;
	}

	public static Label getOpponentMonsterOriginalRoleLabel() {
		return opponentMonsterOriginalRoleLabel;
	}

	public static Label getOpponentMonsterCurrentRoleLabel() {
		return opponentMonsterCurrentRoleLabel;
	}

	public static String getOpponentMonsterType() {
		return opponentMonsterType;
	}

	public static Label getOpponentMonsterTypeLabel() {
		return opponentMonsterTypeLabel;
	}

	public static Label getOpponentMonsterPositionLabel() {
		return opponentMonsterPositionLabel;
	}

	public static Label getOpponentMonsterShieldedLabel() {
		return opponentMonsterShieldedLabel;
	}

	public static Label getOpponentMonsterConfusedLabel() {
		return opponentMonsterConfusedLabel;
	}

}


















