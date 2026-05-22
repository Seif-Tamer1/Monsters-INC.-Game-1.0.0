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
	public static GUI instance;
	static Stage alertStage;
	
	// MADE PUBLIC STATIC SO WE CAN SWITCH TO GAME OVER OR BACK TO START
	public static Scene mainScene;
	private static Scene introScene;
	
	
	
	
	public HBox GameScreen;
	public  VBox GameOverScreen;
	private StackPane introScreen;
	public  VBox MainScreen;
	private  VBox chooseRoleScreen;
	private VBox InstructionsScreen;
	private VBox monstersScreen;
	private VBox CardsScreen;
	private VBox CellsScreen;
	private VBox RulesScreen;
	
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
		instance = this;
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
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: Switched to heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(60).asString("%.0f"), "px;")); 
	    
	    rulesButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: Switched to heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(60).asString("%.0f"), "px;")); 
	    
	    // Inner White Box Styling
	    whitePlay.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f7f3ee; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    whiteRules.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f7f3ee; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    // Text Styling
	    playlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(18).asString("%.0f"), "px;", // EDITED: Switched to heightProperty to prevent overflow
	                    " -fx-text-fill: #6a1eb5;"));
	    
	    ruleslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(18).asString("%.0f"), "px;", // EDITED: Switched to heightProperty
	                    " -fx-text-fill: #6a1eb5;"));
	    
	    welcomeLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(10).asString("%.0f"), "px;", // EDITED: Switched to heightProperty
	                    " -fx-text-fill: #1c113c;"));
	    
	    monstersINCLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(10).asString("%.0f"), "px;", // EDITED: Switched to heightProperty
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    MainScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05)); // EDITED: Switched to heightProperty
	    playRulesLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.12)); // EDITED: Switched to heightProperty
	    
	    // EDITED: Removed individual pref bindings and used bindDynamicSize helper
	    bindDynamicSize(playButton, mainScene.heightProperty().multiply(0.40), mainScene.heightProperty().multiply(0.55));
	    bindDynamicSize(rulesButton, mainScene.heightProperty().multiply(0.40), mainScene.heightProperty().multiply(0.55));
	    
	    bindDynamicSize(whitePlay, mainScene.heightProperty().multiply(0.40).multiply(0.9), mainScene.heightProperty().multiply(0.55).multiply(0.9));
	    bindDynamicSize(whiteRules, mainScene.heightProperty().multiply(0.40).multiply(0.9), mainScene.heightProperty().multiply(0.55).multiply(0.9));
	    
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
	    Label scarerlbl = new Label("Scarer");
	    String scarerImgUrl = getClass().getResource("scarerMonster2.jpg").toExternalForm();
	    ImageView scarerImgView = new ImageView(new Image(scarerImgUrl));
	    scarerImgView.setPreserveRatio(true);
	    VBox whiteScarer = new VBox(scarerlbl, scarerImgView);
	    
	    Button laugherButton = new Button();
	    Label laugherlbl = new Label("Laugher"); 
	    String laugherImgUrl = getClass().getResource("laugherMonster2.jpg").toExternalForm();
	    ImageView laugherImgView = new ImageView(new Image(laugherImgUrl));
	    laugherImgView.setPreserveRatio(true);
	    
	    VBox whiteLaugher = new VBox(laugherlbl, laugherImgView); 
	    
	    HBox scarerLaugherLayout = new HBox(scarerButton, laugherButton);
	    
	    // NEW: Home Button for Choose Role Screen
	    Button homeBtn = new Button("🏠 HOME");
	    homeBtn.styleProperty().bind(Bindings.concat(
	            "-fx-background-color: #1c113c; -fx-text-fill: #1faaae; -fx-font-family: 'Lilita One';",
	            "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;",
	            "-fx-background-radius: 10px; -fx-cursor: hand;"));
	    bindDynamicSize(homeBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));
	    
	    HBox topBar = new HBox(homeBtn);
	    topBar.setAlignment(Pos.CENTER_LEFT);
	    topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10)); // Slight padding from the edge
	    
	    homeBtn.setOnAction(e -> switchToScreen(chooseRoleScreen, MainScreen)); // Action to go back

	    // EDITED: Added topBar to the VBox
	    chooseRoleScreen = new VBox(topBar, chooseRoleLabel, scarerLaugherLayout);
	    
	    
	    // Outer Button Styling
	    scarerButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ",
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    laugherButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    // Inner White Box Styling
	    whiteScarer.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    whiteLaugher.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    // Text Styling 
	    scarerlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(18).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #6a1eb5;")); 
	    
	    laugherlbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(18).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #1faaae;")); 
	    
	    chooseRoleLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(10).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    chooseRoleScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05)); // EDITED: heightProperty
	    scarerLaugherLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.12)); // EDITED: heightProperty
	    whiteScarer.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); // EDITED: heightProperty
	    whiteLaugher.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); // EDITED: heightProperty
	    
	    // EDITED: Used bindDynamicSize helper
	    bindDynamicSize(scarerButton, mainScene.heightProperty().multiply(0.40), mainScene.heightProperty().multiply(0.55));
	    bindDynamicSize(laugherButton, mainScene.heightProperty().multiply(0.40), mainScene.heightProperty().multiply(0.55));
	    
	    bindDynamicSize(whiteScarer, mainScene.heightProperty().multiply(0.40).multiply(0.9), mainScene.heightProperty().multiply(0.55).multiply(0.9));
	    bindDynamicSize(whiteLaugher, mainScene.heightProperty().multiply(0.40).multiply(0.9), mainScene.heightProperty().multiply(0.55).multiply(0.9));
	    
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
	    Label monsterslbl = new Label("Monsters"); 
	    String monstersImgUrl = getClass().getResource("monstericon.png").toExternalForm();
	    ImageView monstersImgView = new ImageView(new Image(monstersImgUrl));
	    monstersImgView.setPreserveRatio(true);
	    VBox whiteMonsters = new VBox(monsterslbl, monstersImgView);
	    
	    Button cardsButton = new Button();
	    Label cardslbl = new Label("Cards"); 
	    String cardsImgUrl = getClass().getResource("cardicon.png").toExternalForm();
	    ImageView cardsImgView = new ImageView(new Image(cardsImgUrl));
	    cardsImgView.setPreserveRatio(true);
	    VBox whiteCards = new VBox(cardslbl, cardsImgView);
	    
	    Button cellsButton = new Button();
		Label cellslbl = new Label("Cells"); 
		
		// NEW: Created a styled Region to look exactly like a game board cell
		javafx.scene.layout.Region cellIcon = new javafx.scene.layout.Region();
		cellIcon.styleProperty().bind(Bindings.concat(
				"-fx-background-color: #dcb0ff; ", // A nice purple matching your palette
				"-fx-border-color: #1c113c; ",     // Dark border
				"-fx-border-width: 4px; ",
				"-fx-background-radius: 12px; ",
				"-fx-border-radius: 10px;"
		));
		
		VBox whiteCells = new VBox(cellslbl, cellIcon);
	    
	    Button gameRulesButton = new Button();
		Label gameRuleslbl = new Label("Rules"); 
		
		// NEW: Created a large "?" text label instead of an ImageView
		Label gameRulesIcon = new Label("?");
		gameRulesIcon.styleProperty().bind(Bindings.concat(
				"-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", 
				mainScene.heightProperty().divide(6).asString("%.0f"), "px;"));
		
		VBox whitegameRules = new VBox(gameRuleslbl, gameRulesIcon);
	    
	    HBox widgetsLayout = new HBox(monstersButton, cardsButton, cellsButton, gameRulesButton);
	    
	 // NEW: Home Button for Instructions Screen
	    Button homeBtn = new Button("🏠 HOME");
	    homeBtn.styleProperty().bind(Bindings.concat(
	            "-fx-background-color: #1c113c; -fx-text-fill: #1faaae; -fx-font-family: 'Lilita One';",
	            "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;",
	            "-fx-background-radius: 10px; -fx-cursor: hand;"));
	    bindDynamicSize(homeBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));
	    
	    HBox topBar = new HBox(homeBtn);
	    topBar.setAlignment(Pos.CENTER_LEFT);
	    topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10));
	    
	    homeBtn.setOnAction(e -> switchToScreen(InstructionsScreen, MainScreen)); // Action to go back

	    // EDITED: Added topBar to the VBox
	    InstructionsScreen = new VBox(topBar, instructionsLabel, widgetsLayout);
	   
	    
	    // Outer Button Styling
	    monstersButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    cardsButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    cellsButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    gameRulesButton.styleProperty().bind(
	            Bindings.concat("-fx-background-color: #1c113c; ",
	            		"-fx-padding: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", // EDITED: heightProperty
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px; ", 
	                    "-fx-border-radius: ", mainScene.heightProperty().divide(100).asString("%.0f"), "px;")); 
	    
	    // Inner White Box Styling
	    whiteMonsters.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    whiteCells.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    whiteCards.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    whitegameRules.styleProperty().bind(
	            Bindings.concat(
	            		"-fx-padding: 0;",
	            		"-fx-background-color: #f4f1ec; ",
	                    "-fx-background-radius: ", mainScene.heightProperty().divide(50).asString("%.0f"), "px; ", 
	                    "-fx-alignment: center;"));
	    
	    // Text Styling 
	    monsterslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(22).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #6a1eb5;")); 
	    
	    cellslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(22).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #6a1eb5;")); 
	    
	    cardslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding: 0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(22).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #6a1eb5;")); 
	    
	    gameRuleslbl.styleProperty().bind(
	            Bindings.concat("-fx-padding:  0;",
	                    "-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(22).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #6a1eb5;")); 
	    
	    instructionsLabel.styleProperty().bind(
	            Bindings.concat("-fx-font-family: 'Lilita One';",
	                    " -fx-font-size: ", mainScene.heightProperty().divide(10).asString("%.0f"), "px;", // EDITED: heightProperty
	                    " -fx-text-fill: #1c113c;"));
	    
	    // Layout & Sizing
	    InstructionsScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05)); // EDITED: heightProperty
	    widgetsLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.05)); // EDITED: heightProperty
	    whiteMonsters.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); 
	    whiteCards.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); 
	    whiteCells.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); 
	    whitegameRules.spacingProperty().bind(mainScene.heightProperty().multiply(0.55).multiply(0.07)); 
	    
	    // EDITED: Used bindDynamicSize helper. Using 0.25 as width multiplier so all 4 fit on screen based on height!
	    bindDynamicSize(monstersButton, mainScene.heightProperty().multiply(0.28), mainScene.heightProperty().multiply(0.4));
	    bindDynamicSize(cardsButton, mainScene.heightProperty().multiply(0.28), mainScene.heightProperty().multiply(0.4));
	    bindDynamicSize(cellsButton, mainScene.heightProperty().multiply(0.28), mainScene.heightProperty().multiply(0.4));
	    bindDynamicSize(gameRulesButton, mainScene.heightProperty().multiply(0.28), mainScene.heightProperty().multiply(0.4));

	    bindDynamicSize(whiteMonsters, mainScene.heightProperty().multiply(0.28).multiply(0.9), mainScene.heightProperty().multiply(0.4).multiply(0.9));
	    bindDynamicSize(whiteCards, mainScene.heightProperty().multiply(0.28).multiply(0.9), mainScene.heightProperty().multiply(0.4).multiply(0.9));
	    bindDynamicSize(whiteCells, mainScene.heightProperty().multiply(0.28).multiply(0.9), mainScene.heightProperty().multiply(0.4).multiply(0.9));
	    bindDynamicSize(whitegameRules, mainScene.heightProperty().multiply(0.28).multiply(0.9), mainScene.heightProperty().multiply(0.4).multiply(0.9));
	    
	    monstersImgView.fitHeightProperty().bind(monstersButton.heightProperty().multiply(0.5));
	    cardsImgView.fitHeightProperty().bind(cardsButton.heightProperty().multiply(0.5));
	 // Bind the cell icon to be a perfect square, taking up 40% of the button's height
		bindDynamicSize(cellIcon, cellsButton.heightProperty().multiply(0.4), cellsButton.heightProperty().multiply(0.4));
//	    gameRulesImgView.fitHeightProperty().bind(gameRulesButton.heightProperty().multiply(0.5));
	    
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
	        
	        if (monstersScreen == null) {
	        	buildMonstersScreen();
	        }
	        switchToScreen(InstructionsScreen, monstersScreen);
	        
	    });
	    
	    cardsButton.setOnAction(e -> {
	    	if (CardsScreen == null) {
	        	buildCardsScreen();
	        }
	        switchToScreen(InstructionsScreen, CardsScreen);
	    });
	    
	    cellsButton.setOnAction(e -> {
	    	if (CellsScreen == null) {
	        	buildCellsScreen();
	        }
	        switchToScreen(InstructionsScreen, CellsScreen);
	        
	    });
	    
	    gameRulesButton.setOnAction(e -> {
	    	if (RulesScreen == null) {
	        	buildGameRulesScreen();
	        }
	        switchToScreen(InstructionsScreen, RulesScreen);
	    });
	    
	    String imageUrl = getClass().getResource("white background.png").toExternalForm();
	    String cssBackground = "-fx-background-image: url('" + imageUrl + "'); -fx-background-size: cover; -fx-background-position: center center; -fx-background-repeat: no-repeat;";
	    InstructionsScreen.setStyle(cssBackground);
	}
	
	public void buildMonstersScreen() {
		// 1. Data Extracted from Game PDF
		String[] names = {"James P. Sullivan", "Mike Wazowski", "Randall Boggs", "Celia Mae", "Roz", "Fungus", "Henry J. Waternoose", "Yeti"};
		String[] roles = {"SCARER (Dynamo)", "LAUGHER (Dasher)", "SCARER (Schemer)", "LAUGHER (MultiTasker)", "SCARER (MultiTasker)", "LAUGHER (Dasher)", "SCARER (Schemer)", "LAUGHER (Dynamo)"};
		
		// EDITED: Simplified the Start Energy text
		String[] energies = {"Start Energy: 300", "Start Energy: 100", "Start Energy: 20", "Start Energy: 50", "Start Energy: 100", "Start Energy: 50", "Start Energy: 70", "Start Energy: 100"};
		
		// NEW: Extracted and Summarized Passive Traits from the PDF
		String[] passives = {
				"⚡ Passive: 2x multiplier on all energy gains and losses.", 
				"⚡ Passive: Lightning Movement (Moves at 2x speed).", 
				"⚡ Passive: +10 bonus to all energy gains and reduced losses.", 
				"⚡ Passive: Moves at half speed, but gets +200 bonus to all energy changes.", 
				"⚡ Passive: Moves at half speed, but gets +200 bonus to all energy changes.", 
				"⚡ Passive: Lightning Movement (Moves at 2x speed).", 
				"⚡ Passive: +10 bonus to all energy gains and reduced losses.", 
				"⚡ Passive: 2x multiplier on all energy gains and losses."
		};
		
		// NEW: Extracted and Summarized Active Powerups from the PDF
		String[] actives = {
				"🔥 Active (Energy Freeze): Skips the opponent's next turn.", 
				"🔥 Active (Momentum Rush): Moves at 3x speed for 3 turns.", 
				"🔥 Active (Chain Attack): Steals 10 energy from all players on board.", 
				"🔥 Active (Focus Mode): Moves at normal speed for the next 2 turns.", 
				"🔥 Active (Focus Mode): Moves at normal speed for the next 2 turns.", 
				"🔥 Active (Momentum Rush): Moves at 3x speed for 3 turns.", 
				"🔥 Active (Chain Attack): Steals 10 energy from all players on board.", 
				"🔥 Active (Energy Freeze): Skips the opponent's next turn."
		};
		
		String[] descs = {"\"The top scarer-powerful and confident.\"", "\"Fast and funny-the comedy speedster.\"", "\"Sneaky and cunning-always has an angle.\"", "\"Organized receptionist-handles everything.\"", "\"Always watching-nothing escapes her notice.\"", "\"Timid assistant-quick but nervous.\"", "\"Witty and strategic CEO.\"", "\"Banished snow monster-surprisingly cheerful.\""};
		String[] images = {"james-removebg-preview.png", "mike-removebg-preview.png", "purp-removebg-preview.png", "celia-removebg-preview.png", "grump-removebg-preview.png", "3eyed-removebg-preview.png", "boss-removebg-preview.png", "yeti-removebg-preview.png"};
		
		int[] currentIndex = {0}; 

		// 2. UI Elements Setup
		Label titleLbl = new Label("MONSTERS ARCHIVE");
		
		Label nameLbl = new Label(names[0]);
		Label roleLbl = new Label(roles[0]);
		Label energyLbl = new Label(energies[0]);
		
		// NEW: Added the Passive and Active UI Labels
		Label passiveLbl = new Label(passives[0]);
		passiveLbl.setWrapText(true);
		Label activeLbl = new Label(actives[0]);
		activeLbl.setWrapText(true);
		
		Label descLbl = new Label(descs[0]);
		descLbl.setWrapText(true);
		
		// EDITED: Added the new passive and active labels to the VBox layout
		VBox textLayout = new VBox(nameLbl, roleLbl, energyLbl, passiveLbl, activeLbl, descLbl);
		textLayout.setAlignment(Pos.CENTER_LEFT);
		textLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.015)); // Custom spacing to fit the new text
		
		// Right Side: Image
		ImageView monsterImgView = new ImageView(new Image(getClass().getResource(images[0]).toExternalForm()));
		monsterImgView.setPreserveRatio(true);
		VBox imgLayout = new VBox(monsterImgView);
		imgLayout.setAlignment(Pos.CENTER);
		
		HBox contentLayout = new HBox(textLayout, imgLayout);
		contentLayout.setAlignment(Pos.CENTER);
		
		// 3. Navigation Buttons
		Button backBtn = new Button("⬅ BACK");
		Button nextBtn = new Button("NEXT ➡");
		
		HBox topBar = new HBox(backBtn);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10));
		
		HBox bottomBar = new HBox(nextBtn);
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setPadding(new javafx.geometry.Insets(0, 30, 20, 0));

		monstersScreen = new VBox(topBar, titleLbl, contentLayout, bottomBar); 
		
		// 4. Styling & Dynamic Resizing
		String bgStyle = "-fx-background-image: url('" + getClass().getResource("white background.png").toExternalForm() + "'); -fx-background-size: cover;";
		monstersScreen.setStyle(bgStyle);
		monstersScreen.setAlignment(Pos.TOP_CENTER);
		monstersScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05));
		
		contentLayout.styleProperty().bind(Bindings.concat("-fx-background-color: #f4f1ec; -fx-border-color: #1c113c; -fx-border-width: 5px; -fx-background-radius: 20px; -fx-border-radius: 15px;"));
		bindDynamicSize(contentLayout, mainScene.heightProperty().multiply(1.2), mainScene.heightProperty().multiply(0.6));
		contentLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.1));
		
		bindDynamicSize(textLayout, mainScene.heightProperty().multiply(0.65), mainScene.heightProperty().multiply(0.55)); // Slightly wider for the new text
		bindDynamicSize(imgLayout, mainScene.heightProperty().multiply(0.4), mainScene.heightProperty().multiply(0.5));
		monsterImgView.fitHeightProperty().bind(imgLayout.heightProperty().multiply(0.9));

		// Font bindings 
		titleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(12).asString("%.0f"), "px;"));
		nameLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #6a1eb5; -fx-font-size: ", mainScene.heightProperty().divide(16).asString("%.0f"), "px;"));
		roleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1faaae; -fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		energyLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(32).asString("%.0f"), "px;"));
		
		// NEW: Styling for the Passive and Active Labels
		passiveLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #ff8b94; -fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		activeLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #ff8b94; -fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		
		// EDITED: Slightly smaller and italicized the personality quote to distinguish it
		descLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-font-style: italic; -fx-text-fill: #888888; -fx-font-size: ", mainScene.heightProperty().divide(40).asString("%.0f"), "px;"));
		
		String btnStyle = "-fx-background-color: #1c113c; -fx-text-fill: white; -fx-font-family: 'Lilita One'; -fx-background-radius: 10px; -fx-cursor: hand;";
		backBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		nextBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		bindDynamicSize(backBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));
		bindDynamicSize(nextBtn, mainScene.heightProperty().multiply(0.25), mainScene.heightProperty().multiply(0.08));

		// 5. Actions 
		nextBtn.setOnAction(e -> {
			currentIndex[0] = (currentIndex[0] + 1) % names.length; 
			nameLbl.setText(names[currentIndex[0]]);
			roleLbl.setText(roles[currentIndex[0]]);
			energyLbl.setText(energies[currentIndex[0]]);
			
			// NEW: Update Passive and Active text on button click
			passiveLbl.setText(passives[currentIndex[0]]);
			activeLbl.setText(actives[currentIndex[0]]);
			
			descLbl.setText(descs[currentIndex[0]]);
			monsterImgView.setImage(new Image(getClass().getResource(images[currentIndex[0]]).toExternalForm()));
		});
		
		backBtn.setOnAction(e -> switchToScreen(monstersScreen, InstructionsScreen));
	}

	public void buildCardsScreen() {
		// 1. Data Extracted from Game PDF
		String[] names = {"Swapper Card", "Energy Steal Card", "Start Over Card", "Shield Card", "Confusion Card"};
		String[] counts = {"Quantity: 4 in deck", "Quantity: 6 in deck", "Quantity: 5 in deck", "Quantity: 5 in deck", "Quantity: 5 in deck"};
		String[] descs = {
				"Swaps place with the opponent if you are currently behind them on the board.", 
				"Steals energy from opponent (Small Snatcher: 50, Sneaky Thief: 100, Mega Drain: 150).", 
				"Can be lucky or not! Contamination Code sends YOU to the start, 2319 Alert sends OPPONENT to start.", 
				"Activates shield protection. Blocks the next negative energy loss effect for your entire team!", 
				"Swaps the roles (Scarer/Laugher) of both players for 2 to 3 turns! Chaos ensues."
		};
		// Placeholder images for cards - replace with actual card png names if you have them!
		String[] images = {"swapper.png", "energysteal.png", "startover.png", "shiels.png", "confusion.png",}; 
		
		int[] currentIndex = {0};

		// 2. UI Elements Setup
		Label titleLbl = new Label("CARDS ARCHIVE");
		
		Label nameLbl = new Label(names[0]);
		Label countLbl = new Label(counts[0]);
		Label descLbl = new Label(descs[0]);
		descLbl.setWrapText(true);
		
		VBox textLayout = new VBox(nameLbl, countLbl, descLbl);
		textLayout.setAlignment(Pos.CENTER_LEFT);
		textLayout.setSpacing(20);
		
		ImageView imgView = new ImageView(new Image(getClass().getResource(images[0]).toExternalForm()));
		imgView.setPreserveRatio(true);
		VBox imgLayout = new VBox(imgView);
		imgLayout.setAlignment(Pos.CENTER);
		
		HBox contentLayout = new HBox(textLayout, imgLayout);
		contentLayout.setAlignment(Pos.CENTER);
		
		// 3. Navigation Buttons
		Button backBtn = new Button("⬅ BACK");
		Button nextBtn = new Button("NEXT ➡");
		
		HBox topBar = new HBox(backBtn);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10));
		
		HBox bottomBar = new HBox(nextBtn);
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setPadding(new javafx.geometry.Insets(0, 30, 20, 0));

		CardsScreen = new VBox(topBar, titleLbl, contentLayout, bottomBar); 
		
		// 4. Styling & Dynamic Resizing
		String bgStyle = "-fx-background-image: url('" + getClass().getResource("white background.png").toExternalForm() + "'); -fx-background-size: cover;";
		CardsScreen.setStyle(bgStyle);
		CardsScreen.setAlignment(Pos.TOP_CENTER);
		CardsScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05));
		
		contentLayout.styleProperty().bind(Bindings.concat("-fx-background-color: #f4f1ec; -fx-border-color: #1c113c; -fx-border-width: 5px; -fx-background-radius: 20px; -fx-border-radius: 15px;"));
		bindDynamicSize(contentLayout, mainScene.heightProperty().multiply(1.2), mainScene.heightProperty().multiply(0.6));
		contentLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.1));
		
		bindDynamicSize(textLayout, mainScene.heightProperty().multiply(0.55), mainScene.heightProperty().multiply(0.5));
		bindDynamicSize(imgLayout, mainScene.heightProperty().multiply(0.5), mainScene.heightProperty().multiply(0.5));
		imgView.fitHeightProperty().bind(imgLayout.heightProperty().multiply(0.9));

		titleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(12).asString("%.0f"), "px;"));
		nameLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #6a1eb5; -fx-font-size: ", mainScene.heightProperty().divide(16).asString("%.0f"), "px;"));
		countLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1faaae; -fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		descLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(30).asString("%.0f"), "px;"));
		
		String btnStyle = "-fx-background-color: #1c113c; -fx-text-fill: white; -fx-font-family: 'Lilita One'; -fx-background-radius: 10px; -fx-cursor: hand;";
		backBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		nextBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		bindDynamicSize(backBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));
		bindDynamicSize(nextBtn, mainScene.heightProperty().multiply(0.25), mainScene.heightProperty().multiply(0.08));

		// 5. Actions (The Carousel Logic)
		nextBtn.setOnAction(e -> {
			currentIndex[0] = (currentIndex[0] + 1) % names.length; 
			nameLbl.setText(names[currentIndex[0]]);
			countLbl.setText(counts[currentIndex[0]]);
			descLbl.setText(descs[currentIndex[0]]);
			imgView.setImage(new Image(getClass().getResource(images[currentIndex[0]]).toExternalForm()));
		});
		
		backBtn.setOnAction(e -> switchToScreen(CardsScreen, InstructionsScreen));
	}

	public void buildCellsScreen() {
		// 1. Data Extracted from Game PDF
		String[] names = {"Door Cell", "Monster Cell", "Conveyor Belt", "Contamination\nSock", "Card Cell", "Normal Cell"};
		String[] descs = {
				"Collects Energy! Match your role to gain energy for your whole team. Mismatch costs you energy unless shielded. Exhausted after 1 use.", 
				"Contains a sidelined monster. Match role = free powerup! Mismatch = swaps energy with them if they have more.", 
				"Automated transport system! Landing here instantly transports you safely forward.", 
				"DANGER! CDA emergency protocols activated! Transports you backwards and drains 100 energy.", 
				"Mysterious cell. Draw a random card from the pile and face its consequences!", 
				"A regular corridor. You are safe here. Nothing happens."
		};
		
		// EDITED: Exactly 5 images. The 6th item (Normal Cell) will use the rectangle instead!
		String[] images = {"doorcell.png", "monstercell.png", "conveyorbelt.png", "contaminationsock.png", "cardcell.png"}; 
		
		int[] currentIndex = {0};

		// 2. UI Elements Setup
		Label titleLbl = new Label("THE FLOOR CELLS");
		
		Label nameLbl = new Label(names[0]);
		Label descLbl = new Label(descs[0]);
		descLbl.setWrapText(true);
		
		VBox textLayout = new VBox(nameLbl, descLbl);
		textLayout.setAlignment(Pos.CENTER_LEFT);
		textLayout.setSpacing(20);
		
		// Setup Image View for the first 5 cells
		ImageView imgView = new ImageView(new Image(getClass().getResource(images[0]).toExternalForm()));
		imgView.setPreserveRatio(true);
		
		// Setup the styled Region for the Normal Cell
		javafx.scene.layout.Region normalCellIcon = new javafx.scene.layout.Region();
		normalCellIcon.styleProperty().bind(Bindings.concat(
				"-fx-background-color: #f2efea; ", // Beige corridor color to match the board
				"-fx-border-color: #1c113c; ",     // Dark border
				"-fx-border-width: 8px; ",
				"-fx-background-radius: 20px; ",
				"-fx-border-radius: 15px;"
		));
		normalCellIcon.setVisible(false); // Hidden initially because we start on the Door Cell
		
		// StackPane to hold both the ImageView AND the Region on top of each other
		StackPane iconPane = new StackPane(imgView, normalCellIcon);
		iconPane.setAlignment(Pos.CENTER);
		
		VBox imgLayout = new VBox(iconPane); 
		imgLayout.setAlignment(Pos.CENTER);
		
		HBox contentLayout = new HBox(textLayout, imgLayout);
		contentLayout.setAlignment(Pos.CENTER);
		
		// 3. Navigation Buttons
		Button backBtn = new Button("⬅ BACK");
		Button nextBtn = new Button("NEXT ➡");
		
		HBox topBar = new HBox(backBtn);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10));
		
		HBox bottomBar = new HBox(nextBtn);
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setPadding(new javafx.geometry.Insets(0, 30, 20, 0));

		CellsScreen = new VBox(topBar, titleLbl, contentLayout, bottomBar); 
		
		// 4. Styling & Dynamic Resizing
		String bgStyle = "-fx-background-image: url('" + getClass().getResource("white background.png").toExternalForm() + "'); -fx-background-size: cover;";
		CellsScreen.setStyle(bgStyle);
		CellsScreen.setAlignment(Pos.TOP_CENTER);
		CellsScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05));
		
		contentLayout.styleProperty().bind(Bindings.concat("-fx-background-color: #f4f1ec; -fx-border-color: #1c113c; -fx-border-width: 5px; -fx-background-radius: 20px; -fx-border-radius: 15px;"));
		bindDynamicSize(contentLayout, mainScene.heightProperty().multiply(1.2), mainScene.heightProperty().multiply(0.6));
		contentLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.1));
		
		bindDynamicSize(textLayout, mainScene.heightProperty().multiply(0.55), mainScene.heightProperty().multiply(0.5));
		bindDynamicSize(imgLayout, mainScene.heightProperty().multiply(0.5), mainScene.heightProperty().multiply(0.5));
		
		imgView.fitHeightProperty().bind(imgLayout.heightProperty().multiply(0.9));
		
		// Bind the Normal Cell rectangle to be a perfect square that scales with the layout
		bindDynamicSize(normalCellIcon, imgLayout.heightProperty().multiply(0.7), imgLayout.heightProperty().multiply(0.7));

		titleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(12).asString("%.0f"), "px;"));
		nameLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1faaae; -fx-font-size: ", mainScene.heightProperty().divide(14).asString("%.0f"), "px;"));
		descLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		String btnStyle = "-fx-background-color: #1c113c; -fx-text-fill: white; -fx-font-family: 'Lilita One'; -fx-background-radius: 10px; -fx-cursor: hand;";
		backBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		nextBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		bindDynamicSize(backBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));
		bindDynamicSize(nextBtn, mainScene.heightProperty().multiply(0.25), mainScene.heightProperty().multiply(0.08));

		// 5. Actions (The Visibility Toggle Logic)
		nextBtn.setOnAction(e -> {
			currentIndex[0] = (currentIndex[0] + 1) % names.length; 
			nameLbl.setText(names[currentIndex[0]]);
			descLbl.setText(descs[currentIndex[0]]);
			
			// EDITED: Safely handles the switch. If it's "Normal Cell", show the rectangle and DO NOT touch the image array.
			if (names[currentIndex[0]].toLowerCase().contains("normal")) {
				imgView.setVisible(false);
				normalCellIcon.setVisible(true); // Show the rectangle!
			} else {
				normalCellIcon.setVisible(false);
				imgView.setVisible(true); // Show the image!
				imgView.setImage(new Image(getClass().getResource(images[currentIndex[0]]).toExternalForm()));
			}
		});
		
		backBtn.setOnAction(e -> switchToScreen(CellsScreen, InstructionsScreen));
	}

	public void buildGameRulesScreen() {
		Label titleLbl = new Label("HOW TO PLAY");
		
		// 1. Text Extracted from Rules PDF
		Label rule1 = new Label("1. Goal: Reach Boo's Door (Cell 99) with at least 1000 Energy!");
		Label rule2 = new Label("2. Sequence: Powerup Phase (cost 500) -> Roll Dice -> ");
		Label rule2c = new Label("     Move -> Trigger Cell Effect"); // Added indent
		Label rule3 = new Label("3. Grid: 10x10 Zig-Zag floor." );
		Label rule3c = new Label("     Move past cell 99 and you wrap around to the start."); // Added indent
		Label rule4 = new Label("4. Combat: If you land exactly on the opponent," );
		Label rule4c = new Label("     the move is invalid and you must re-roll." ); // Added indent
		Label rule5 = new Label("5. Team Play: Door energy affects your entire role team." );
		Label rule5c = new Label("     Strategy is key!" ); // Added indent
		
		// EDITED: Group each rule and its continuation into its own VBox with zero spacing!
		VBox box1 = new VBox(rule1);
		VBox box2 = new VBox(rule2, rule2c);
		VBox box3 = new VBox(rule3, rule3c);
		VBox box4 = new VBox(rule4, rule4c);
		VBox box5 = new VBox(rule5, rule5c);
		
		// Add the grouped boxes to the main layout
		VBox textLayout = new VBox(box1, box2, box3, box4, box5);
		textLayout.setAlignment(Pos.CENTER_LEFT);
		
		// 3. Navigation Buttons
		Button backBtn = new Button("⬅ BACK");
		HBox topBar = new HBox(backBtn);
		topBar.setAlignment(Pos.CENTER_LEFT);
		topBar.setPadding(new javafx.geometry.Insets(10, 0, 0, 10));
		
		RulesScreen = new VBox(topBar, titleLbl, textLayout); 
		
		// 4. Styling & Dynamic Resizing
		String bgStyle = "-fx-background-image: url('" + getClass().getResource("white background.png").toExternalForm() + "'); -fx-background-size: cover;";
		RulesScreen.setStyle(bgStyle);
		RulesScreen.setAlignment(Pos.TOP_CENTER);
		RulesScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05));
		
		textLayout.styleProperty().bind(Bindings.concat("-fx-background-color: #f4f1ec; -fx-border-color: #1c113c; -fx-border-width: 5px; -fx-background-radius: 20px; -fx-border-radius: 15px; -fx-padding: 30px;"));
		bindDynamicSize(textLayout, mainScene.heightProperty().multiply(1.2), mainScene.heightProperty().multiply(0.75));
		textLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.04)); // Spacing between the rule blocks

		titleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(12).asString("%.0f"), "px;"));
		
		// Style all rule labels
		String ruleStyle = "-fx-font-family: 'Lilita One'; -fx-text-fill: #6a1eb5;";
		
		// Bind original labels
		rule1.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule2.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule3.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule4.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule5.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		// EDITED: Bind the new 'c' labels so they match the rest of the text!
		rule2c.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule3c.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule4c.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		rule5c.styleProperty().bind(Bindings.concat(ruleStyle, "-fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
		
		String btnStyle = "-fx-background-color: #1c113c; -fx-text-fill: white; -fx-font-family: 'Lilita One'; -fx-background-radius: 10px; -fx-cursor: hand;";
		backBtn.styleProperty().bind(Bindings.concat(btnStyle, "-fx-font-size: ", mainScene.heightProperty().divide(35).asString("%.0f"), "px;"));
		bindDynamicSize(backBtn, mainScene.heightProperty().multiply(0.2), mainScene.heightProperty().multiply(0.06));

		// 5. Actions
		backBtn.setOnAction(e -> switchToScreen(RulesScreen, InstructionsScreen));
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
				playerc.radiusProperty().bind(mainScene.heightProperty().divide(60)); // EDITED: Used mainScene instead of BoardPane
				opponentc.radiusProperty().bind(mainScene.heightProperty().divide(60)); // EDITED: Used mainScene instead of BoardPane
				
				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						Button btn = new Button();
						
						// 1. NEW CELL INDEX BADGE (Clearer Index)
						Label textLabel = new Label(String.valueOf(c));
						textLabel.styleProperty().bind(Bindings.concat("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: ", mainScene.heightProperty().divide(65).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
						StackPane badge = new StackPane(textLabel);
						badge.setStyle("-fx-background-color: rgba(28, 17, 60, 0.7); -fx-background-radius: 5px;");
						badge.setPadding(new javafx.geometry.Insets(2, 5, 2, 5));
						badge.setMaxWidth(javafx.scene.layout.Region.USE_PREF_SIZE);
						badge.setMaxHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
						StackPane.setAlignment(badge, Pos.TOP_LEFT);
						StackPane.setMargin(badge, new javafx.geometry.Insets(3, 0, 0, 3));

						// 2. Cell Content Layout
						VBox cellContent = new VBox(2);
						cellContent.setAlignment(Pos.CENTER);
						
						Label iconLabel = new Label();
						iconLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.heightProperty().divide(30).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
						
						Label detailLabel = new Label();
						detailLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.heightProperty().divide(60).asString("%.0f"), "px; -fx-text-fill: #1c113c; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
						detailLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

						String bgColor = "#f2efea"; 
						
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
							String mName = "Unknown";
							String mIcon = "👾";
							bgColor = "#dcb0ff"; 
							
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
								
								if (stationed.getRole().toString().equals("SCARER")) {
									mIcon = "👿"; 
									bgColor = "#ff8b94"; 
								} else {
									mIcon = "🤡"; 
									bgColor = "#4dd0e1"; 
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
										"-fx-border-width: ", mainScene.heightProperty().divide(400).asString("%.0f"), "px; ", // EDITED: mainScene + %.0f
										"-fx-border-radius: ", mainScene.heightProperty().divide(400).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f

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

		BoardPane.prefWidthProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		BoardPane.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		BoardPane.minWidthProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		BoardPane.minHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		BoardPane.maxWidthProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		BoardPane.maxHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene

		RightLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		RightLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95))); // EDITED: Used mainScene

		upperlayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.7)); // EDITED: Used mainScene
		upperlayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95))); // EDITED: Used mainScene

		DownLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.2)); // EDITED: Used mainScene
		DownLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95))); // EDITED: Used mainScene
		
		cardsLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.2)); // EDITED: Used mainScene
		cardsLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.88))); // EDITED: Used mainScene
		
		actionCenter.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.2)); // EDITED: Used mainScene
		actionCenter.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.12))); // EDITED: Used mainScene

		playerLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		playerLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95)).multiply(0.4)); // EDITED: Used mainScene

		VSLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		VSLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95)).multiply(0.2)); // EDITED: Used mainScene

		opponentLayout.prefHeightProperty().bind(mainScene.heightProperty().multiply(0.9)); // EDITED: Used mainScene
		opponentLayout.prefWidthProperty().bind(mainScene.widthProperty().subtract(mainScene.heightProperty().multiply(0.95)).multiply(0.4)); // EDITED: Used mainScene

		GameScreen.spacingProperty().bind(mainScene.widthProperty().divide(125)); // EDITED: Used mainScene

		opponentTurnLabel.setVisible(false);
		rollDiceLabel.setVisible(false);
		rollDiceButton.setVisible(false);

		
		// ==========================================
				// DYNAMIC FONT SIZING FOR UPPER & DOWN LAYOUTS
				// ==========================================
				
				// --- PLAYER LAYOUT FONTS ---
				playerTurnLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(50).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				youLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(55).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				playerMonsterNameLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				playerMonsterOriginalRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterCurrentRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterTypeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(65).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterEnergyLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				playerMonsterPositionLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterShieldedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterFrozenLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerMonsterConfusedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				playerStatus.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f

				// --- OPPONENT LAYOUT FONTS ---
				opponentTurnLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(50).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				opponentLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(55).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				opponentMonsterNameLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				opponentMonsterOriginalRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterCurrentRoleLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterTypeLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(65).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterEnergyLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				opponentMonsterPositionLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterShieldedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterFrozenLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentMonsterConfusedLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				opponentStatus.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f

				// --- DOWN LAYOUT FONTS (Cards & Action Center) ---
				cardCounterLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(30).asString("%.0f"), "px; -fx-font-weight: bold; -fx-font-family: 'Forte';")); // EDITED: mainScene + %.0f
				
				usePowerup.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-font-weight: bold; -fx-text-fill: black;")); // EDITED: mainScene + %.0f
				yesButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				noButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(70).asString("%.0f"), "px;")); // EDITED: mainScene + %.0f
				
				rollDiceButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(55).asString("%.0f"), "px; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				rollDiceLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", mainScene.widthProperty().divide(60).asString("%.0f"), "px; -fx-text-fill: black; -fx-font-weight: bold;")); // EDITED: mainScene + %.0f
				
				
		yesButton.setOnAction(e -> GameControl.handleUsePowerUpYES());
		noButton.setOnAction(e -> GameControl.handleUsePowerUpNO());
		rollDiceButton.setOnAction(e -> GameControl.handleRollDice());
	}
	
	// EDITED: Added the strict size locking helper method
	public static void bindDynamicSize(javafx.scene.layout.Region node, javafx.beans.binding.DoubleBinding widthMath, javafx.beans.binding.DoubleBinding heightMath) {
	    node.minWidthProperty().bind(widthMath);
	    node.prefWidthProperty().bind(widthMath);
	    node.maxWidthProperty().bind(widthMath);
	    
	    node.minHeightProperty().bind(heightMath);
	    node.prefHeightProperty().bind(heightMath);
	    node.maxHeightProperty().bind(heightMath);
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
	public void buildGameOverScreen(String winnerName, String winnerRole, int playerEnergy, int opponentEnergy) {
	    // 1. UI Elements
	    Label titleLbl = new Label("GAME OVER");
	    Label winnerLbl = new Label(winnerName + " WINS!");
	    Label roleLbl = new Label("Role: " + winnerRole);
	    
	    roleLbl.styleProperty().bind(Bindings.concat(
	    	    "-fx-font-family: 'Lilita One'; ", 
	    	    "-fx-text-fill: #1faaae; ", // Using your teal color
	    	    "-fx-font-size: ", mainScene.heightProperty().divide(20).asString("%.0f"), "px;"
	    	));
	    // REQUIREMENT: Final energy of both monsters must be displayed
	    Label energyLbl = new Label("FINAL ENERGY\n" + 
	                                "Player: " + playerEnergy + " | Opponent: " + opponentEnergy);
	    energyLbl.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
	    
	    ImageView winnerImgView = new ImageView(new Image(getClass().getResource("PLAYMonster.png").toExternalForm()));
	    winnerImgView.setPreserveRatio(true);
	    
	    VBox contentLayout = new VBox(winnerLbl, roleLbl, energyLbl, winnerImgView);
	    contentLayout.setAlignment(Pos.CENTER);
	    
	    Button returnMenuBtn = new Button("RETURN TO MAIN MENU");
	    
	    GameOverScreen = new VBox(titleLbl, contentLayout, returnMenuBtn); 
	    
	    // 2. Styling (Using active mainScene height)
	    String bgStyle = "-fx-background-image: url('" + getClass().getResource("white background.png").toExternalForm() + "'); -fx-background-size: cover;";
	    GameOverScreen.setStyle(bgStyle);
	    GameOverScreen.setAlignment(Pos.CENTER);
	    GameOverScreen.spacingProperty().bind(mainScene.heightProperty().multiply(0.05));
	    
	    contentLayout.styleProperty().bind(Bindings.concat(
	            "-fx-background-color: #f4f1ec; -fx-border-color: #1c113c; -fx-border-width: 5px; ",
	            "-fx-background-radius: 20px; -fx-border-radius: 15px;"
	    ));
	    
	    bindDynamicSize(contentLayout, mainScene.heightProperty().multiply(0.7), mainScene.heightProperty().multiply(0.7));
	    contentLayout.spacingProperty().bind(mainScene.heightProperty().multiply(0.03));
	    winnerImgView.fitHeightProperty().bind(contentLayout.heightProperty().multiply(0.3));
	    
	    // Text Styling
	    titleLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(10).asString("%.0f"), "px;"));
	    winnerLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #6a1eb5; -fx-font-size: ", mainScene.heightProperty().divide(16).asString("%.0f"), "px;"));
	    energyLbl.styleProperty().bind(Bindings.concat("-fx-font-family: 'Lilita One'; -fx-text-fill: #1c113c; -fx-font-size: ", mainScene.heightProperty().divide(25).asString("%.0f"), "px;"));
	    
	    returnMenuBtn.styleProperty().bind(Bindings.concat(
	            "-fx-background-color: #1c113c; -fx-text-fill: #1faaae; -fx-font-family: 'Lilita One';",
	            "-fx-font-size: ", mainScene.heightProperty().divide(30).asString("%.0f"), "px;",
	            "-fx-background-radius: 10px; -fx-cursor: hand;"
	    ));
	    bindDynamicSize(returnMenuBtn, mainScene.heightProperty().multiply(0.4), mainScene.heightProperty().multiply(0.08));

	    // 3. Action: Return to main menu
	    returnMenuBtn.setOnAction(e -> {
	        MainScreen = null;
	        chooseRoleScreen = null;
	        GameScreen = null; 
	        GameOverScreen = null;
	        
	        buildMainScreen((Stage) mainScene.getWindow()); 
	        mainScene.setRoot(MainScreen);
	    });
	}
	
	
	
	
	// Helper 1: Smoothly fade to a new screen
	public void switchToScreen(Parent oldRoot, Parent newRoot) {
		
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