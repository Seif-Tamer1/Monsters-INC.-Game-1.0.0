package game.engine.GUI;


////NOTE THAT W MAKES YOU WIN AUTOMATICALLY

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode; // Add this to your imports at the top!
import javafx.scene.input.KeyCombination;
import game.engine.Constants;
import game.engine.Role;
import game.engine.GameControl.GameControl;
import game.engine.cells.Cell;
import game.engine.cells.DoorCell;
import game.engine.monsters.Dasher;
import game.engine.monsters.Dynamo;
import game.engine.monsters.Monster;
import game.engine.monsters.MultiTasker;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

	static Stage alertStage;
	
	// MADE PUBLIC STATIC SO WE CAN SWITCH TO GAME OVER OR BACK TO START
	public static Scene mainScene;
	private static Scene introScene;
	
	
	
	
	private static HBox GameScreen;
	private VBox GameOverScreen;
	private StackPane introScreen;
	public static VBox MainScreen;
	private VBox chooseRoleScreen;
	private VBox InstructionsScreen;
	
	private static MediaPlayer bgMusicPlayer;

	private static Label Role_Question_Label;
	private Button SWITCH_Button;
	private Button PLAY_Button;
	private Button INSTRUCTIONS_Button;

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
	private static Label playerMonsterEnergyLabel;
	private static Label playerStatus;
	private static Label playerMonsterFrozenLabel;

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
	private static Label opponentMonsterEnergyLabel;
	private static Label opponentStatus;
	private static Label opponentMonsterFrozenLabel;
	
	private static Label usePowerup;
	private static Button yesButton;
	private static Button noButton;
	private static HBox yesNoPowerup;
	
	private static Label rollDiceLabel;
	private static Button rollDiceButton;
	
	private static Circle playerc;
	private static Circle opponentc;
	
	private static int cardCounter = 25;
	private static Label cardCounterLabel;

	public static Button[] boardButtons = new Button[100];
	// Arrays for specific cell types
		

	// Helper method to check if a cell index belongs to a specific type
	public static boolean contains(int[] array, int key) {
		for (int i : array) {
			if (i == key) return true;
		}
		return false;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Monsters INC.");
		
		
		buildIntroScreen(primaryStage);
		
		
		mainScene = new Scene(introScreen, 640, 480);
		primaryStage.setScene(mainScene);
		
		// THE 'W' CHEAT CODE
		mainScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.W) {
				if (GameControl.getGame() != null && GameControl.getGame().getCurrent() != null) {
					Monster current = GameControl.getGame().getCurrent();
					
					// Force the win conditions
					current.setPosition(99);
					current.setEnergy(1500); 
					
					// Trigger the end of turn logic to check for the winner and jump to Game Over
					GameControl.endOfTurn(); 
				}
			}
		});

		
		
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}
	
	
	
	public void buildIntroScreen(Stage primaryStage) {
		// 2. Set up the Video Player
		// This looks for "introVideo.mp4" in the same folder as GUI.java
		String videoPath = getClass().getResource("Monsters, Inc. Commercial (1080p60).mp4").toExternalForm();
		Media media = new Media(videoPath);
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		MediaView mediaView = new MediaView(mediaPlayer);

		// Make the video dynamically scale to fit the screen size
		mediaView.fitWidthProperty().bind(primaryStage.widthProperty());
		mediaView.fitHeightProperty().bind(primaryStage.heightProperty());
		mediaView.setPreserveRatio(true); // Keeps the video from looking stretched

		// 3. Create the Intro Layout with a "Skip" button
		Button skipButton = new Button("Skip Intro");
		skipButton.setStyle("-fx-font-size: 16px; -fx-background-color: rgba(255, 255, 255, 0.7); -fx-font-weight: bold;");
		StackPane.setAlignment(skipButton, Pos.BOTTOM_RIGHT);
		StackPane.setMargin(skipButton, new javafx.geometry.Insets(30));

		introScreen = new StackPane();
		introScreen.setStyle("-fx-background-color: black;"); // Black background for letterboxing
		introScreen.getChildren().addAll(mediaView, skipButton);

		// 4. Set up the Scene Transitions
		// What happens when the video finishes normally
		mediaPlayer.setOnEndOfMedia(() -> {
			buildMainScreen(primaryStage);
			switchToScreen(introScreen, MainScreen); // Swap to the actual game menu
			startBackgroundMusic();
		});

		// What happens if they click Skip
		skipButton.setOnAction(e -> {
			mediaPlayer.stop(); // Stop the audio/video immediately
			buildMainScreen(primaryStage);
			switchToScreen(introScreen, MainScreen);
			startBackgroundMusic();
		});

		// 5. START THE VIDEO
		mediaPlayer.play();
	}
	

	public void buildMainScreen(Stage primaryStage) {
	    javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);
	    
	    Label welcomeLabel = new Label("WELCOME");
	    Label monstersINCLabel = new Label("Monsters, INC.");
	    VBox mainLabelLayout = new VBox(welcomeLabel, monstersINCLabel);
	    mainLabelLayout.setAlignment(Pos.CENTER);
	    
	    Button playButton = new Button();
	    Label playlbl = new Label("PLAY");
	    String playMonsterImgUrl = getClass().getResource("PLAYMonster.png").toExternalForm(); 
	    ImageView playMonsterImgView = new ImageView(new Image(playMonsterImgUrl));
	    playMonsterImgView.setPreserveRatio(true);
	    VBox whitePlay = new VBox(playlbl, playMonsterImgView);
	    
	    Button rulesButton = new Button();
	    Label ruleslbl = new Label("RULES");
	    String rulesImgUrl = getClass().getResource("RULESMonster.png").toExternalForm(); 
	    ImageView rulesImgView = new ImageView(new Image(rulesImgUrl));
	    rulesImgView.setPreserveRatio(true);
	    VBox whiteRules = new VBox(ruleslbl, rulesImgView);
	    
	    HBox playRulesLayout = new HBox(playButton, rulesButton);
	    MainScreen = new VBox(mainLabelLayout, playRulesLayout);
	    
	    // Outer Button Styling (Acts as the thick border)
	    playButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", MainScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", MainScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", MainScreen.heightProperty().divide(60).asString(), "px;"));
	    
	    rulesButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", MainScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", MainScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", MainScreen.heightProperty().divide(60).asString(), "px;"));
	    
	    // Inner White Box Styling
	    whitePlay.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f7f3ee; ",
	                    "-fx-background-radius: ", MainScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    whiteRules.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f7f3ee; ",
	                    "-fx-background-radius: ", MainScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    // Text Styling
	    playlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", MainScreen.widthProperty().divide(30).asString(), "px;",
	                    " -fx-text-fill: #6a1eb5;"));
	    
	    ruleslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", MainScreen.widthProperty().divide(30).asString(), "px;",
	                    " -fx-text-fill: #6a1eb5;"));
	    
	    welcomeLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", MainScreen.widthProperty().divide(18).asString(), "px;",
	                    " -fx-text-fill: #1c113c;"));
	    
	    monstersINCLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", MainScreen.widthProperty().divide(18).asString(), "px;",
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    MainScreen.spacingProperty().bind(MainScreen.widthProperty().multiply(0.05));
	    playRulesLayout.spacingProperty().bind(MainScreen.widthProperty().multiply(0.12));
	    
	    playButton.prefHeightProperty().bind(MainScreen.heightProperty().multiply(0.55));
	    playButton.prefWidthProperty().bind(MainScreen.widthProperty().multiply(0.22));
	    rulesButton.prefHeightProperty().bind(MainScreen.heightProperty().multiply(0.55));
	    rulesButton.prefWidthProperty().bind(MainScreen.widthProperty().multiply(0.22));
	    
	    whitePlay.prefHeightProperty().bind(MainScreen.heightProperty().multiply(0.55).multiply(0.9));
	    whiteRules.prefWidthProperty().bind(MainScreen.widthProperty().multiply(0.22).multiply(0.9));
	    
	    playMonsterImgView.fitHeightProperty().bind(playButton.heightProperty().multiply(0.6));
	    rulesImgView.fitHeightProperty().bind(rulesButton.heightProperty().multiply(0.6));
	    
	    MainScreen.setAlignment(Pos.CENTER);
	    playRulesLayout.setAlignment(Pos.CENTER);
	    playButton.setAlignment(Pos.CENTER);
	    rulesButton.setAlignment(Pos.CENTER);
	    
	    playButton.setGraphic(whitePlay);
	    rulesButton.setGraphic(whiteRules);
	    
	    playButton.setContentDisplay(ContentDisplay.CENTER);
	    rulesButton.setContentDisplay(ContentDisplay.CENTER);
	    
	    addHoverEffect(playButton);
	    addHoverEffect(rulesButton);
	    
	    // Actions
	    playButton.setOnAction(e -> {
	    	if (chooseRoleScreen == null) {
	            buildchooseRoleScreen(); 
	        }
	        switchToScreen(MainScreen, chooseRoleScreen);
	    });
	    
	    rulesButton.setOnAction(e -> {
	    	if (InstructionsScreen == null) {
	    		buildInstructionsScreen(); 
	        }
	        switchToScreen(MainScreen, InstructionsScreen);
	    	
	    });
	    
	    String imageUrl = getClass().getResource("white background.png").toExternalForm();
	    String cssBackground = "-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;";
	    MainScreen.setStyle(cssBackground);
	}

	public void buildchooseRoleScreen() {
	    javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);
	    
	    Label chooseRoleLabel = new Label("CHOOSE YOUR ROLE");
	    
	    Button scarerButton = new Button();
	    Label scarerlbl = new Label("Scarer"); // Changed to Title Case to match mockup
	    String scarerImgUrl = getClass().getResource("scarerMonster2.jpg").toExternalForm();
	    ImageView scarerImgView = new ImageView(new Image(scarerImgUrl));
	    scarerImgView.setPreserveRatio(true);
	    VBox whiteScarer = new VBox(scarerlbl, scarerImgView);
	    
	    Button laugherButton = new Button();
	    Label laugherlbl = new Label("Laugher"); // Changed to Title Case
	    String laugherImgUrl = getClass().getResource("laugherMonster2.jpg").toExternalForm();
	    ImageView laugherImgView = new ImageView(new Image(laugherImgUrl));
	    laugherImgView.setPreserveRatio(true);
	    
	    // FIX: Corrected to hold the laugher components, not the scarer ones
	    VBox whiteLaugher = new VBox(laugherlbl, laugherImgView); 
	    
	    HBox scarerLaugherLayout = new HBox(scarerButton, laugherButton);
	    chooseRoleScreen = new VBox(chooseRoleLabel, scarerLaugherLayout);
	    
	    // Outer Button Styling (Thick Borders)
	    scarerButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", chooseRoleScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", chooseRoleScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", chooseRoleScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    laugherButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", chooseRoleScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", chooseRoleScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", chooseRoleScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    // Inner White Box Styling
	    whiteScarer.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", chooseRoleScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    whiteLaugher.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", chooseRoleScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    // Text Styling (Colors adjusted to match mockup 2)
	    scarerlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", chooseRoleScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #6a1eb5;")); // Purple
	    
	    laugherlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", chooseRoleScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #1faaae;")); // Teal/Cyan to match the mockup
	    
	    chooseRoleLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", chooseRoleScreen.widthProperty().divide(18).asString(), "px;",
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    chooseRoleScreen.spacingProperty().bind(chooseRoleScreen.widthProperty().multiply(0.05));
	    scarerLaugherLayout.spacingProperty().bind(chooseRoleScreen.widthProperty().multiply(0.12));
	    whiteScarer.spacingProperty().bind(chooseRoleScreen.heightProperty().multiply(0.55).multiply(0.07));
	    whiteLaugher.spacingProperty().bind(chooseRoleScreen.heightProperty().multiply(0.55).multiply(0.07));
	    
	    scarerButton.prefHeightProperty().bind(chooseRoleScreen.heightProperty().multiply(0.55));
	    scarerButton.prefWidthProperty().bind(chooseRoleScreen.widthProperty().multiply(0.22));
	    laugherButton.prefHeightProperty().bind(chooseRoleScreen.heightProperty().multiply(0.55));
	    laugherButton.prefWidthProperty().bind(chooseRoleScreen.widthProperty().multiply(0.22));
	    
	    whiteScarer.prefHeightProperty().bind(chooseRoleScreen.heightProperty().multiply(0.55).multiply(0.9));
	    whiteLaugher.prefWidthProperty().bind(chooseRoleScreen.widthProperty().multiply(0.22).multiply(0.9));
	    
	    scarerImgView.fitHeightProperty().bind(scarerButton.heightProperty().multiply(0.6));
	    laugherImgView.fitHeightProperty().bind(laugherButton.heightProperty().multiply(0.6));
	    
	    chooseRoleScreen.setAlignment(Pos.CENTER);
	    scarerLaugherLayout.setAlignment(Pos.CENTER);
	    scarerButton.setAlignment(Pos.CENTER);
	    laugherButton.setAlignment(Pos.CENTER);
	    
	    scarerButton.setGraphic(whiteScarer);
	    laugherButton.setGraphic(whiteLaugher);
	    
	    scarerButton.setContentDisplay(ContentDisplay.CENTER);
	    laugherButton.setContentDisplay(ContentDisplay.CENTER);
	    
	    addHoverEffect(scarerButton);
	    addHoverEffect(laugherButton);
	    
	    // Actions
	    scarerButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.SCARER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(chooseRoleScreen, GameScreen);
	        
	    });
	    
	    laugherButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.LAUGHER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(chooseRoleScreen, GameScreen);
	    });
	    
	    String imageUrl = getClass().getResource("white background.png").toExternalForm();
	    String cssBackground = "-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;";
	    chooseRoleScreen.setStyle(cssBackground);
	}
	
	public void buildInstructionsScreen() {
	    javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/fonts/LilitaOne-Regular.ttf"), 12);
	    
	    Label instructionsLabel = new Label("Instructions");
	    
	    Button monstersButton = new Button();
	    Label monsterslbl = new Label("Monsters"); // Changed to Title Case to match mockup
	    String monstersImgUrl = getClass().getResource("scarerMonster2.jpg").toExternalForm();
	    ImageView monstersImgView = new ImageView(new Image(monstersImgUrl));
	    monstersImgView.setPreserveRatio(true);
	    VBox whiteMonsters = new VBox(monsterslbl, monstersImgView);
	    
	    Button cardsButton = new Button();
	    Label cardslbl = new Label("Cards"); // Changed to Title Case to match mockup
	    String cardsImgUrl = getClass().getResource("scarerMonster2.jpg").toExternalForm();
	    ImageView cardsImgView = new ImageView(new Image(cardsImgUrl));
	    cardsImgView.setPreserveRatio(true);
	    VBox whiteCards = new VBox(cardslbl, cardsImgView);
	    
	    Button cellsButton = new Button();
	    Label cellslbl = new Label("Cells"); // Changed to Title Case
	    String cellsImgUrl = getClass().getResource("laugherMonster2.jpg").toExternalForm();
	    ImageView cellsImgView = new ImageView(new Image(cellsImgUrl));
	    cellsImgView.setPreserveRatio(true);
	    VBox whiteCells = new VBox(cellslbl, cellsImgView);
	    
	    Button gameRulesButton = new Button();
	    Label gameRuleslbl = new Label("Rules"); // Changed to Title Case to match mockup
	    String gameRulesImgUrl = getClass().getResource("scarerMonster2.jpg").toExternalForm();
	    ImageView gameRulesImgView = new ImageView(new Image(gameRulesImgUrl));
	    gameRulesImgView.setPreserveRatio(true);
	    VBox whitegameRules = new VBox(gameRuleslbl, gameRulesImgView);
	    
	    
	    
	    HBox widgetsLayout = new HBox(monstersButton, cardsButton, cellsButton, gameRulesButton);
	    InstructionsScreen = new VBox(instructionsLabel, widgetsLayout);
	    
	    // Outer Button Styling (Thick Borders)
	    monstersButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", InstructionsScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    cardsButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", InstructionsScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    cellsButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", InstructionsScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    gameRulesButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", InstructionsScreen.widthProperty().divide(150).asString(), "px; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px; ",
	                    "-fx-border-radius: ", InstructionsScreen.heightProperty().divide(100).asString(), "px;"));
	    
	    // Inner White Box Styling
	    whiteMonsters.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    whiteCells.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    whiteCards.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    whitegameRules.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", InstructionsScreen.heightProperty().divide(50).asString(), "px; ",
	                    "-fx-alignment: center;"));
	    
	    // Text Styling (Colors adjusted to match mockup 2)
	    monsterslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", InstructionsScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #6a1eb5;")); // Purple
	    
	    cellslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", InstructionsScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #1faaae;")); // Teal/Cyan to match the mockup
	    
	    cardslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", InstructionsScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #6a1eb5;")); // Purple
	    
	    gameRuleslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", InstructionsScreen.widthProperty().divide(20).asString(), "px;",
	                    " -fx-text-fill: #1faaae;")); // Teal/Cyan to match the mockup
	    
	    instructionsLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", InstructionsScreen.widthProperty().divide(18).asString(), "px;",
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    InstructionsScreen.spacingProperty().bind(InstructionsScreen.widthProperty().multiply(0.05));
	    widgetsLayout.spacingProperty().bind(InstructionsScreen.widthProperty().multiply(0.05));
	    whiteMonsters.spacingProperty().bind(InstructionsScreen.heightProperty().multiply(0.55).multiply(0.07));
	    whiteCards.spacingProperty().bind(InstructionsScreen.heightProperty().multiply(0.55).multiply(0.07));
	    whiteCells.spacingProperty().bind(InstructionsScreen.heightProperty().multiply(0.55).multiply(0.07));
	    whitegameRules.spacingProperty().bind(InstructionsScreen.heightProperty().multiply(0.55).multiply(0.07));
	    
	    monstersButton.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4));
	    monstersButton.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15));
	    cardsButton.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4));
	    cardsButton.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15));
	    cellsButton.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4));
	    cellsButton.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15));
	    gameRulesButton.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4));
	    gameRulesButton.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15));
	    
	    // Bind Heights
	    whiteMonsters.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4).multiply(0.9));
	    whiteCards.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4).multiply(0.9));
	    whiteCells.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4).multiply(0.9));
	    whitegameRules.prefHeightProperty().bind(InstructionsScreen.heightProperty().multiply(0.4).multiply(0.9));

	    // Bind Widths
	    whiteMonsters.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15).multiply(0.9));
	    whiteCards.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15).multiply(0.9));
	    whiteCells.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15).multiply(0.9));
	    whitegameRules.prefWidthProperty().bind(InstructionsScreen.widthProperty().multiply(0.15).multiply(0.9));
	    
	    monstersImgView.fitHeightProperty().bind(monstersButton.heightProperty().multiply(0.5));
	    cardsImgView.fitHeightProperty().bind(cardsButton.heightProperty().multiply(0.5));
	    cellsImgView.fitHeightProperty().bind(cellsButton.heightProperty().multiply(0.5));
	    gameRulesImgView.fitHeightProperty().bind(gameRulesButton.heightProperty().multiply(0.5));
	    
	    InstructionsScreen.setAlignment(Pos.CENTER);
	    widgetsLayout.setAlignment(Pos.CENTER);
	    monstersButton.setAlignment(Pos.CENTER);
	    cardsButton.setAlignment(Pos.CENTER);
	    cellsButton.setAlignment(Pos.CENTER);
	    gameRulesButton.setAlignment(Pos.CENTER);
	    
	    monstersButton.setGraphic(whiteMonsters);
	    cardsButton.setGraphic(whiteCards);
	    cellsButton.setGraphic(whiteCells);
	    gameRulesButton.setGraphic(whitegameRules);
	    
	    monstersButton.setContentDisplay(ContentDisplay.CENTER);
	    cardsButton.setContentDisplay(ContentDisplay.CENTER);
	    cellsButton.setContentDisplay(ContentDisplay.CENTER);
	    gameRulesButton.setContentDisplay(ContentDisplay.CENTER);
	    
	    addHoverEffect(monstersButton);
	    addHoverEffect(cardsButton);
	    addHoverEffect(cellsButton);
	    addHoverEffect(gameRulesButton);
	    // Actions
	    monstersButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.SCARER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(InstructionsScreen, GameScreen);
	        
	    });
	    
	    cardsButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.LAUGHER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(InstructionsScreen, GameScreen);
	    });
	    
	    cellsButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.SCARER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(InstructionsScreen, GameScreen);
	        
	    });
	    
	    gameRulesButton.setOnAction(e -> {
	        GameControl.setChoosen_role(Role.LAUGHER);
	        if (GameScreen == null) {
	        	buildGameScreen();
	        }
	        switchToScreen(InstructionsScreen, GameScreen);
	    });
	    
	    String imageUrl = getClass().getResource("white background.png").toExternalForm();
	    String cssBackground = "-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;";
	    InstructionsScreen.setStyle(cssBackground);
	}
	
	public void buildGameScreen() {
		GameControl.startGame();

		StackPane BoardPane = new StackPane();
		GridPane Board = new GridPane();
		Pane tokenLayer = new Pane();
		tokenLayer.setMouseTransparent(true);

		StackPane RightLayoutFrame = new StackPane();
		VBox RightLayout = new VBox();
		HBox upperlayout = new HBox();
		VBox playerLayout = new VBox();
		VBox VSLayout = new VBox();
		VBox opponentLayout = new VBox();

		usePowerup = new Label("USE POWERUP?");
		yesButton = new Button("YES!");
		noButton = new Button("NO!");
		yesNoPowerup = new HBox(yesButton, noButton);
		
		rollDiceButton = new Button("Roll the Dice!");
		rollDiceLabel = new Label("You rolled: ");
		
		cardCounterLabel = new Label(cardCounter + "");
		StackPane cardsLayout = new StackPane(cardCounterLabel);
		cardsLayout.setAlignment(Pos.CENTER);
		
		VBox actionCenter = new VBox(usePowerup, yesNoPowerup, rollDiceButton, rollDiceLabel);
		HBox DownLayout = new HBox(cardsLayout, actionCenter);
		GameScreen = new HBox(BoardPane, RightLayoutFrame);

		playerTurnLabel = new Label("YOUR TURN!");
		youLabel = new Label("YOU (Blue)");
		playerWidget = new StackPane();
		playerMonsterNameLabel = new Label(GameControl.getGame().getPlayer().getName());
		playerMonsterOriginalRoleLabel = new Label(GameControl.getGame().getPlayer().getOriginalRole().toString() + "(Original)");
		playerMonsterCurrentRoleLabel = new Label(GameControl.getGame().getPlayer().getRole().toString() + "(Current)");
		playerMonsterType = (GameControl.getGame().getPlayer() instanceof Dasher ? "Dasher" : GameControl.getGame().getPlayer() instanceof MultiTasker ? "MultiTasker" : GameControl.getGame().getPlayer() instanceof Dynamo ? "Dynamo" : "Schemer");
		playerMonsterTypeLabel = new Label(playerMonsterType);
		playerMonsterEnergyLabel = new Label(GameControl.getGame().getPlayer().getEnergy() + " energy");
		playerMonsterPositionLabel = new Label("Position: " + GameControl.getGame().getPlayer().getPosition());
		playerMonsterShieldedLabel = new Label(GameControl.getGame().getPlayer().isShielded() ? "Shielded" : "Not Shielded");
		playerMonsterConfusedLabel = new Label("Confused for: " + GameControl.getGame().getPlayer().getConfusionTurns() + " turns");
		playerMonsterFrozenLabel = new Label(GameControl.getGame().getPlayer().isFrozen() ? "Frozen" : "Not Frozen");
		playerStatus = new Label(GameControl.getGame().getPlayer() instanceof Dasher ? "Momentum Rush for 0 turns" : GameControl.getGame().getPlayer() instanceof MultiTasker ? "Focus Mode for 0 turns" : GameControl.getGame().getPlayer() instanceof Dynamo ? "No Energy Freeze" : "No Chain Attack");

		opponentTurnLabel = new Label("OPPONENT TURN!");
		opponentLabel = new Label("OPPONENT (Red)");
		opponentWidget = new StackPane();
		opponentMonsterNameLabel = new Label(GameControl.getGame().getOpponent().getName());
		opponentMonsterOriginalRoleLabel = new Label(GameControl.getGame().getOpponent().getOriginalRole().toString() + "(Original)");
		opponentMonsterCurrentRoleLabel = new Label(GameControl.getGame().getOpponent().getRole().toString() + "(Current)");
		opponentMonsterType = (GameControl.getGame().getOpponent() instanceof Dasher ? "Dasher" : GameControl.getGame().getOpponent() instanceof MultiTasker ? "MultiTasker" : GameControl.getGame().getOpponent() instanceof Dynamo ? "Dynamo" : "Schemer");
		opponentMonsterTypeLabel = new Label(opponentMonsterType);
		opponentMonsterEnergyLabel = new Label(GameControl.getGame().getOpponent().getEnergy() + " energy");
		opponentMonsterPositionLabel = new Label("Position: " + GameControl.getGame().getOpponent().getPosition());
		opponentMonsterShieldedLabel = new Label(GameControl.getGame().getOpponent().isShielded() ? "Shielded" : "Not Shielded");
		opponentMonsterConfusedLabel = new Label("Confused for: " + GameControl.getGame().getOpponent().getConfusionTurns() + " turns");
		opponentMonsterFrozenLabel = new Label(GameControl.getGame().getOpponent().isFrozen() ? "Frozen" : "Not Frozen");
		opponentStatus = new Label(GameControl.getGame().getOpponent() instanceof Dasher ? "Momentum Rush for 0 turns" : GameControl.getGame().getOpponent() instanceof MultiTasker ? "Focus Mode for 0 turns" : GameControl.getGame().getOpponent() instanceof Dynamo ? "No Energy Freeze" : "No Chain Attack");

		// Board drawing
				int c = 0;
				playerc = new Circle();
				opponentc = new Circle();
				playerc.setFill(Color.BLUE);
				opponentc.setFill(Color.RED);
				playerc.radiusProperty().bind(BoardPane.heightProperty().divide(60));
				opponentc.radiusProperty().bind(BoardPane.heightProperty().divide(60));
				
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						Button btn = new Button();
						
						// 1. NEW CELL INDEX BADGE (Clearer Index)
						Label textLabel = new Label(String.valueOf(c));
						textLabel.styleProperty().bind(Bindings.concat("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: ", BoardPane.heightProperty().divide(65).asString(), "px;"));
						StackPane badge = new StackPane(textLabel);
						badge.setStyle("-fx-background-color: rgba(28, 17, 60, 0.7); -fx-background-radius: 5px;");
						badge.setPadding(new javafx.geometry.Insets(2, 5, 2, 5));
						// Prevent the badge from stretching across the cell
						badge.setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
						badge.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
						StackPane.setAlignment(badge, Pos.TOP_LEFT);
						StackPane.setMargin(badge, new javafx.geometry.Insets(3, 0, 0, 3));

						// 2. Cell Content Layout
						VBox cellContent = new VBox(2);
						cellContent.setAlignment(Pos.CENTER);
						
						Label iconLabel = new Label();
						iconLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", BoardPane.heightProperty().divide(30).asString(), "px;"));
						
						Label detailLabel = new Label();
						detailLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", BoardPane.heightProperty().divide(60).asString(), "px; -fx-text-fill: #1c113c; -fx-font-weight: bold;"));
						detailLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

						String bgColor = "#f2efea"; // Default Normal Cell color
						
						int monsterIndex = indexOf(Constants.MONSTER_CELL_INDICES, c);

						// 3. Determine Cell Type and Apply Visuals
						if (c == 0) {
							iconLabel.setText("🏁");
							detailLabel.setText("START");
							bgColor = "#a8e6cf"; 
						} else if (c == 99) {
							iconLabel.setText("🚪👧");
							detailLabel.setText("BOO");
							bgColor = "#ff8b94"; 
						} else if (monsterIndex != -1) {
							
							// NEW: DYNAMIC STATIONED MONSTERS
							String mName = "Unknown";
							String mIcon = "👾";
							bgColor = "#dcb0ff"; 
							
							// Safely grab the stationed monster for this specific cell
							if (GameControl.getGame() != null && monsterIndex < GameControl.getGame().getBoard().getStationedMonsters().size()) {
								Monster stationed = GameControl.getGame().getBoard().getStationedMonsters().get(monsterIndex);
								mName = stationed.getName().equals("James P. Sullivan") ? "Sullivan" :
									stationed.getName().equals("Mike Wazowski") ? "Mike":
										stationed.getName().equals("Randall Boggs") ? "Boggs":
											stationed.getName().equals("Celia Mae") ? "Celia":
												stationed.getName().equals("Roz") ? "Roz":
													stationed.getName().equals("Fungus") ? "Fungus":
														stationed.getName().equals("Henry J. Waternoose") ? "Henry":
															"Yeti";
								
								
								
								// Visual difference between Scarers and Laughers
								if (stationed.getRole().toString().equals("SCARER")) {
									mIcon = "👿"; // Aggressive Scarer Icon
									bgColor = "#ff8b94"; // Red tint for Scarers
								} else {
									mIcon = "🤡"; // Friendly Laugher Icon
									bgColor = "#4dd0e1"; // Yellow/Orange tint for Laughers
								}
							}
							
							iconLabel.setText(mIcon);
							detailLabel.setText(mName);
							
						} else if (contains(Constants.CONVEYOR_CELL_INDICES, c)) {
							iconLabel.setText("⬆️");
							detailLabel.setText("Belt");
							bgColor = "#bae1ff"; 
						} else if (contains(Constants.SOCK_CELL_INDICES, c)) {
							iconLabel.setText("🧦");
							detailLabel.setText("Sock");
							bgColor = "#baffc9"; 
						} else if (contains(Constants.CARD_CELL_INDICES, c)) {
							iconLabel.setText("🃏");
							detailLabel.setText("Card");
							bgColor = "#ffffba"; 
						} else if (c % 2 != 0) {
							// Doors
							int doorEnergy = 10 + (c % 4) * 10; 
							if ((c / 2) % 2 == 0) { 
								iconLabel.setText("🚪👹");
								detailLabel.setText("Scare\n⚡ " + doorEnergy);
								bgColor = "#ffdfba"; 
							} else {
								iconLabel.setText("🚪😂");
								detailLabel.setText("Laugh\n⚡ " + doorEnergy);
								bgColor = "#fceab8"; 
							}
						}

						cellContent.getChildren().addAll(iconLabel, detailLabel);

						StackPane customLayout = new StackPane();
						customLayout.getChildren().addAll(cellContent, badge);
						btn.setGraphic(customLayout);

						btn.styleProperty().bind(
								Bindings.concat("-fx-background-color: ", bgColor, "; ",
										"-fx-border-color: #1c113c; ",
										"-fx-border-width: ", BoardPane.heightProperty().divide(400).asString(), "px; ",
										"-fx-border-radius: ", BoardPane.heightProperty().divide(400).asString(), "px;"));

						// NEW: Save the generated button to our global array using its specific cell index
						boardButtons[c] = btn;

						// 5. Grid progression logic (Zig-Zag)
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

						btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
						GridPane.setHgrow(btn, Priority.ALWAYS);
						GridPane.setVgrow(btn, Priority.ALWAYS);

						Board.add(btn, j, i);
					}
				}
		playerc.centerXProperty().bind(tokenLayer.widthProperty().divide(10).multiply(0).add(playerc.radiusProperty()).add(tokenLayer.widthProperty().divide(100)));
		playerc.centerYProperty().bind(tokenLayer.heightProperty().divide(10).multiply(1).subtract(playerc.radiusProperty()).subtract(tokenLayer.heightProperty().divide(100)));

		opponentc.centerXProperty().bind(tokenLayer.widthProperty().divide(10).multiply(1).subtract(opponentc.radiusProperty()).subtract(tokenLayer.widthProperty().divide(100)));
		opponentc.centerYProperty().bind(tokenLayer.heightProperty().divide(10).multiply(1).subtract(opponentc.radiusProperty()).subtract(tokenLayer.heightProperty().divide(100)));

		String imageUrl = getClass().getResource("white background.png").toExternalForm();
		String cssBackground = "-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;";
		GameScreen.setStyle(cssBackground);

//		RightLayoutFrame.setStyle("-fx-background-color: blue;");
//		upperlayout.setStyle("-fx-background-color: #ff0000;");
//		playerLayout.setStyle("-fx-background-color: orange;");
//		VSLayout.setStyle("-fx-background-color: blue;");
//		opponentLayout.setStyle("-fx-background-color: green;");
//		DownLayout.setStyle("-fx-background-color: black;");
		cardsLayout.setStyle("-fx-background-color: yellow;");

		RightLayoutFrame.getChildren().add(RightLayout);
		upperlayout.getChildren().addAll(playerLayout, VSLayout, opponentLayout);
		playerLayout.getChildren().addAll(playerTurnLabel, youLabel, playerWidget, playerMonsterNameLabel, playerMonsterOriginalRoleLabel, playerMonsterCurrentRoleLabel, playerMonsterTypeLabel, playerMonsterEnergyLabel, playerMonsterPositionLabel, playerMonsterShieldedLabel, playerMonsterFrozenLabel, playerMonsterConfusedLabel, playerStatus);
		opponentLayout.getChildren().addAll(opponentTurnLabel, opponentLabel, opponentWidget, opponentMonsterNameLabel, opponentMonsterOriginalRoleLabel, opponentMonsterCurrentRoleLabel, opponentMonsterTypeLabel, opponentMonsterEnergyLabel, opponentMonsterPositionLabel, opponentMonsterShieldedLabel, opponentMonsterFrozenLabel, opponentMonsterConfusedLabel, opponentStatus);
		tokenLayer.getChildren().addAll(playerc, opponentc);
		BoardPane.getChildren().addAll(Board, tokenLayer);
		RightLayout.getChildren().addAll(upperlayout, DownLayout);

		GameScreen.setAlignment(Pos.CENTER);
		RightLayout.setAlignment(Pos.CENTER);
		opponentLayout.setAlignment(Pos.CENTER);
		playerLayout.setAlignment(Pos.CENTER);
		DownLayout.setAlignment(Pos.CENTER);
		yesNoPowerup.setAlignment(Pos.CENTER);
		actionCenter.setAlignment(Pos.CENTER);
		RightLayoutFrame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		BoardPane.prefWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.minWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.minHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.maxWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.maxHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));

		RightLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		RightLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));

		upperlayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.7));
		upperlayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));

		DownLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.2));
		DownLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));
		
		cardsLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.2));
		cardsLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.88)));
		
		actionCenter.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.2));
		actionCenter.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.12)));

		playerLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		playerLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.4));

		VSLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		VSLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.2));

		opponentLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		opponentLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.4));

		GameScreen.spacingProperty().bind(GameScreen.widthProperty().divide(125));

		opponentTurnLabel.setVisible(false);
		rollDiceLabel.setVisible(false);
		rollDiceButton.setVisible(false);

		
		// ==========================================
				// DYNAMIC FONT SIZING FOR UPPER & DOWN LAYOUTS
				// ==========================================
				
				// --- PLAYER LAYOUT FONTS ---
				// Divisors increased significantly to guarantee text fits (smaller fonts)
				playerTurnLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(50).asString(), "px; -fx-font-weight: bold;"));
				youLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(55).asString(), "px; -fx-font-weight: bold;"));
				playerMonsterNameLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-font-weight: bold;"));
				playerMonsterOriginalRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerMonsterCurrentRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerMonsterTypeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(65).asString(), "px;"));
				playerMonsterEnergyLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-font-weight: bold;"));
				playerMonsterPositionLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerMonsterShieldedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerMonsterFrozenLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerMonsterConfusedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				playerStatus.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));

				// --- OPPONENT LAYOUT FONTS ---
				opponentTurnLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(50).asString(), "px; -fx-font-weight: bold;"));
				opponentLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(55).asString(), "px; -fx-font-weight: bold;"));
				opponentMonsterNameLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-font-weight: bold;"));
				opponentMonsterOriginalRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentMonsterCurrentRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentMonsterTypeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(65).asString(), "px;"));
				opponentMonsterEnergyLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-font-weight: bold;"));
				opponentMonsterPositionLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentMonsterShieldedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentMonsterFrozenLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentMonsterConfusedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				opponentStatus.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));

				// --- DOWN LAYOUT FONTS (Cards & Action Center) ---
				// Replaced 'white' with 'yellow' so it shows up on the black background without violating the rule
				cardCounterLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(30).asString(), "px; -fx-font-weight: bold; -fx-font-family: 'Forte';"));
				
				usePowerup.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-font-weight: bold; -fx-text-fill: black;"));
				yesButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				noButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(70).asString(), "px;"));
				
				rollDiceButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(55).asString(), "px; -fx-font-weight: bold;"));
				rollDiceLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", GameScreen.widthProperty().divide(60).asString(), "px; -fx-text-fill: black; -fx-font-weight: bold;"));
				
				
		yesButton.setOnAction(e -> GameControl.handleUsePowerUpYES());
		noButton.setOnAction(e -> GameControl.handleUsePowerUpNO());
		rollDiceButton.setOnAction(e -> GameControl.handleRollDice());
	}
	
	public static void updateTurnUI() {
		Monster current = GameControl.getGame().getCurrent();
		Monster player = GameControl.getGame().getPlayer();

		// FORCE SYNC ALL LABELS (This fixes the visual bugs!)
		updateLabel(getPlayerMonsterConfusedLabel(), "Confused for: " + GameControl.getGame().getPlayer().getConfusionTurns() + " turns");
		updateLabel(getOpponentMonsterConfusedLabel(), "Confused for: " + GameControl.getGame().getOpponent().getConfusionTurns() + " turns");
		updateLabel(getPlayerMonsterCurrentRoleLabel(), GameControl.getGame().getPlayer().getRole().toString() + "(Current)");
		updateLabel(getOpponentMonsterCurrentRoleLabel(), GameControl.getGame().getOpponent().getRole().toString() + "(Current)");
		updateLabel(getPlayerMonsterFrozenLabel(), GameControl.getGame().getPlayer().isFrozen() ? "Frozen" : "Not Frozen");
		updateLabel(getOpponentMonsterFrozenLabel(), GameControl.getGame().getOpponent().isFrozen() ? "Frozen" : "Not Frozen");
		
		// Force sync Shields and Energy so they never desync from the backend!
		updateLabel(getPlayerMonsterShieldedLabel(), GameControl.getGame().getPlayer().isShielded() ? "Shielded" : "Not Shielded");
		updateLabel(getOpponentMonsterShieldedLabel(), GameControl.getGame().getOpponent().isShielded() ? "Shielded" : "Not Shielded");
		updateLabel(getPlayerMonsterEnergyLabel(), GameControl.getGame().getPlayer().getEnergy() + " energy");
		updateLabel(getOpponentMonsterEnergyLabel(), GameControl.getGame().getOpponent().getEnergy() + " energy");

		// NEW: FORCE SYNC POWERUP STATUS (This fixes the countdowns!)
		updateLabel(getPlayerStatus(), getMonsterStatusString(GameControl.getGame().getPlayer()));
		updateLabel(getOpponentStatus(), getMonsterStatusString(GameControl.getGame().getOpponent()));

		// NEW: VISUAL UPDATE FOR EXHAUSTED DOORS
				if (GameControl.getGame() != null && GameControl.getGame().getBoard() != null) {
					Cell[][] engineBoard = GameControl.getGame().getBoard().getBoardCells();
					
					for (int idx = 0; idx < 100; idx++) {
						if (idx % 2 != 0) { // Door cells are strictly on odd indices
							
							// Convert 1D index to the Engine's 2D array row/col
							int row = idx / 10;
							int col = idx % 10;
							if (row % 2 == 1) { col = 9 - col; } // Account for Zig-Zag
							
							Cell cell = engineBoard[row][col];
							
							// If it is a door and is activated, fade it out!
							if (cell instanceof DoorCell && ((DoorCell) cell).isActivated()) {
								boardButtons[idx].setOpacity(0.35); // Lowers opacity to 35% making it look "ghosted" or deactivated
							}
						}
					}
				}
		// Toggle turn labels
		if (current.equals(player)) {
			playerTurnLabel.setVisible(true);
			opponentTurnLabel.setVisible(false);
		} else {
			playerTurnLabel.setVisible(false);
			opponentTurnLabel.setVisible(true);
		}

		// Reset action center for the new turn
		usePowerup.setVisible(true);
		yesButton.setVisible(true);
		noButton.setVisible(true);
		yesNoPowerup.setVisible(true);
		
		rollDiceLabel.setVisible(false);
		rollDiceButton.setVisible(false);
	}
	
	// NEW: Builds and displays the game over screen
	public static void showGameOverScreen(Monster winner) {
        VBox gameOverLayout = new VBox(30);
        gameOverLayout.setAlignment(Pos.CENTER);
        gameOverLayout.setStyle("-fx-background-color: #1c113c;");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setFont(new Font("Forte", 60));

        Label winnerLabel = new Label("WINNER: " + winner.getName() + " (" + winner.getRole() + ")");
        winnerLabel.setTextFill(Color.GOLD);
        winnerLabel.setFont(new Font("Forte", 40));

        HBox energyBox = new HBox(50);
        energyBox.setAlignment(Pos.CENTER);
        
        Label pEnergy = new Label(GameControl.getGame().getPlayer().getName() + " (You): " + GameControl.getGame().getPlayer().getEnergy() + " Energy");
        pEnergy.setTextFill(Color.WHITE);
        pEnergy.setFont(new Font("Arial", 20));

        Label oEnergy = new Label(GameControl.getGame().getOpponent().getName() + " (Opp): " + GameControl.getGame().getOpponent().getEnergy() + " Energy");
        oEnergy.setTextFill(Color.WHITE);
        oEnergy.setFont(new Font("Arial", 20));
        
        energyBox.getChildren().addAll(pEnergy, oEnergy);

        Button startButton = new Button("Return to Start Window");
        startButton.setStyle("-fx-font-size: 20px; -fx-background-color: #f2efea; -fx-text-fill: #1c113c; -fx-font-weight: bold;");
        startButton.setOnAction(e -> {
            mainScene.setRoot(MainScreen);
        });

        gameOverLayout.getChildren().addAll(gameOverLabel, winnerLabel, energyBox, startButton);
        mainScene.setRoot(gameOverLayout);
    }
	
	public void openInstructionsWindow() {
		Stage instructionsStage = new Stage();
		instructionsStage.setTitle("Game Rules & Instructions");

		VBox layout = new VBox(15);
		layout.setAlignment(Pos.CENTER);
		layout.setStyle("-fx-background-color: #f2efea; -fx-padding: 20px;");

		Label title = new Label("How to Play DooR DasH");
		title.setStyle("-fx-font-family: 'Forte'; -fx-font-size: 24px; -fx-text-fill: #1c113c;");

		Label rules = new Label(
			"OBJECTIVE:\n" +
			"Be the first to reach Boo's Door (Cell 99) with at least 1000 Energy!\n\n" +
			"TURN SEQUENCE:\n" +
			"1. Powerup (Optional): Spend 500 energy to use your monster's special ability.\n" +
			"2. Roll Dice: Move forward 1 to 6 steps.\n" +
			"3. Cell Effects: Activate the cell you land on.\n\n" +
			"CELL TYPES:\n" +
			"- Door Cells: Gain energy if your role matches, lose it if it's a mismatch.\n" +
			"- Monster Cells: Free powerup if role matches, otherwise energy might swap.\n" +
			"- Conveyor Belts & Socks: Move up or down the board.\n" +
			"- Card Cells: Draw a game-changing card!\n\n" +
			"Remember: If you land on an occupied cell, the move is invalid and you must roll again!"
		);
		rules.setStyle("-fx-font-size: 14px; -fx-text-fill: #1c113c;");

		Button closeBtn = new Button("Understood!");
		closeBtn.setStyle("-fx-font-size: 16px; -fx-background-color: #1c113c; -fx-text-fill: white;");
		closeBtn.setOnAction(e -> instructionsStage.close());

		layout.getChildren().addAll(title, rules, closeBtn);

		Scene scene = new Scene(layout, 500, 450);
		instructionsStage.setScene(scene);
		instructionsStage.show();
	}
	
	// Helper 1: Smoothly fade to a new screen
	private void switchToScreen(Parent oldRoot, Parent newRoot) {
		
		// 1. Create a Fade Out animation for the intro screen (takes 1 second)
	    FadeTransition fadeOut = new FadeTransition(Duration.seconds(1.0), oldRoot);
	    fadeOut.setFromValue(1.0);   // Fully visible
	    fadeOut.setToValue(0.0);     // Fully transparent

	    // 2. Tell the animation what to do AFTER it finishes fading out
	    fadeOut.setOnFinished(event -> {
	        
	        
	        // Start the main screen totally transparent
	        newRoot.setOpacity(0.0); 
	        mainScene.setRoot(newRoot); // Swap the roots while it's dark

	        // 3. Create a Fade In animation for the main screen
	        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), newRoot);
	        fadeIn.setFromValue(0.0);
	        fadeIn.setToValue(1.0);
	        fadeIn.play(); // Play the fade in!
	    });

	    // Start the fade out!
	    fadeOut.play();
	    
	    
	    
	    
	}

	// Helper 2: Make buttons pop out when hovered
	private void addHoverEffect(Node node) {
	    ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), node);
	    scaleUp.setToX(1.05); // Grow by 5%
	    scaleUp.setToY(1.05);

	    ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), node);
	    scaleDown.setToX(1.0); // Return to normal size
	    scaleDown.setToY(1.0);

	    node.setOnMouseEntered(e -> {
	        node.setCursor(Cursor.HAND); // Change mouse pointer to a hand
	        scaleUp.playFromStart();
	    });
	    
	    node.setOnMouseExited(e -> {
	        node.setCursor(Cursor.DEFAULT);
	        scaleDown.playFromStart();
	    });
	}

	
	public static void startBackgroundMusic() {
	    try {
	        // Find the audio file
	        String musicPath = GUI.class.getResource("Monsters University Theme.mp3").toExternalForm();
	        Media bgMusic = new Media(musicPath);
	        bgMusicPlayer = new MediaPlayer(bgMusic);

	        // This is the magic line for endless looping!
	        bgMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	        
	        // Lower the volume a bit so it doesn't overpower the game
	        bgMusicPlayer.setVolume(0.15); 

	        bgMusicPlayer.play();
	    } catch (Exception e) {
	        System.out.println("Background music file not found or failed to load.");
	    }
	}
	// NEW: Helper method to dynamically fetch the exact powerup status from the engine
	public static String getMonsterStatusString(Monster m) {
		if (m instanceof Dasher) {
			return "Momentum Rush for " + ((Dasher) m).getMomentumTurns() + " turns";
		} else if (m instanceof MultiTasker) {
			return "Focus Mode for " + ((MultiTasker) m).getNormalSpeedTurns() + " turns";
		} else if (m instanceof Dynamo) {
			return "Energy Freeze";
		} else {
			return "No Chain Attack";
		}
	}
	
	// Helper method to find the exact index of a cell in our arrays
		public static int indexOf(int[] array, int key) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == key) return i;
			}
			return -1;
		}
		
	public static void updateLabel(Label label, String newText) {
		label.setText(newText);
	}

	public static void displayAlert(String title, String message) {
		alertStage = new Stage();
		alertStage.setTitle(title);

		Label label = new Label(message);
		Button closeButton = new Button("Continue Playing");
		closeButton.setOnAction(event -> alertStage.close());

		BorderPane pane = new BorderPane();
		pane.setTop(label);
		BorderPane.setAlignment(label, Pos.TOP_CENTER);
		pane.setCenter(closeButton);

		Scene scene = new Scene(pane, 600, 400);
		alertStage.setScene(scene);
		alertStage.show();
	}

	public static void hidePowerup(){
		usePowerup.setVisible(false);
		yesButton.setVisible(false);
		noButton.setVisible(false);
		yesNoPowerup.setVisible(false);
	}
	
	public static void showRollRice(){
		rollDiceLabel.setVisible(true);
		rollDiceButton.setVisible(true);
	}
	
	public static void decrementCardCounter(){
		if (cardCounter != 1)
			cardCounter--;
		else
			cardCounter = 25;
	}
	
	//Getters
	public static double getScreenHeight() { return Screen.getPrimary().getBounds().getHeight(); }
	public static Label getRole_Question_Label() { return Role_Question_Label; }
	public static Label getPlayerMonsterEnergyLabel() { return playerMonsterEnergyLabel; }
	public static Label getPlayerStatus() { return playerStatus; }
	public static Label getOpponentMonsterEnergyLabel() { return opponentMonsterEnergyLabel; }
	public static Label getOpponentStatus() { return opponentStatus; }
	public static Label getPlayerMonsterFrozenLabel() { return playerMonsterFrozenLabel; }
	public static Label getOpponentMonsterFrozenLabel() { return opponentMonsterFrozenLabel; }
	public static Label getPlayerTurnLabel() { return playerTurnLabel; }
	public static Label getYouLabel() { return youLabel; }
	public static StackPane getPlayerWidget() { return playerWidget; }
	public static Label getPlayerMonsterNameLabel() { return playerMonsterNameLabel; }
	public static Label getPlayerMonsterOriginalRoleLabel() { return playerMonsterOriginalRoleLabel; }
	public static Label getPlayerMonsterCurrentRoleLabel() { return playerMonsterCurrentRoleLabel; }
	public static String getPlayerMonsterType() { return playerMonsterType; }
	public static Label getPlayerMonsterTypeLabel() { return playerMonsterTypeLabel; }
	public static Label getPlayerMonsterPositionLabel() { return playerMonsterPositionLabel; }
	public static Label getPlayerMonsterShieldedLabel() { return playerMonsterShieldedLabel; }
	public static Label getPlayerMonsterConfusedLabel() { return playerMonsterConfusedLabel; }
	public static Label getOpponentTurnLabel() { return opponentTurnLabel; }
	public static Label getOpponentLabel() { return opponentLabel; }
	public static StackPane getOpponentWidget() { return opponentWidget; }
	public static Label getOpponentMonsterNameLabel() { return opponentMonsterNameLabel; }
	public static Label getOpponentMonsterOriginalRoleLabel() { return opponentMonsterOriginalRoleLabel; }
	public static Label getOpponentMonsterCurrentRoleLabel() { return opponentMonsterCurrentRoleLabel; }
	public static String getOpponentMonsterType() { return opponentMonsterType; }
	public static Label getOpponentMonsterTypeLabel() { return opponentMonsterTypeLabel; }
	public static Label getOpponentMonsterPositionLabel() { return opponentMonsterPositionLabel; }
	public static Label getOpponentMonsterShieldedLabel() { return opponentMonsterShieldedLabel; }
	public static Label getOpponentMonsterConfusedLabel() { return opponentMonsterConfusedLabel; }
	public static Label getRollDiceLabel() { return rollDiceLabel; }
	public static Circle getPlayerc() { return playerc; }
	public static Circle getOpponentc() { return opponentc; }
	public static Stage getAlertStage() { return alertStage; }
	public static int getCardCounter() { return cardCounter; }
	public static Label getCardCounterLabel() { return cardCounterLabel; }
}