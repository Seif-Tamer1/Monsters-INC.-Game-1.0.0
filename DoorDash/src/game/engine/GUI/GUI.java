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

public class GUI extends Application{

	private Scene mainScene;
	
	private VBox StartScreen;
	private VBox AbousaVideo;
	private VBox MainScreen;
	private static HBox GameScreen;
	private VBox GameOverScreen;
	
	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		buildMainScreen(primaryStage);
		
		
		primaryStage.setTitle("Monsters INC.");
		mainScene = new Scene(MainScreen, 640, 480);
		primaryStage.setScene(mainScene);
		
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public void buildMainScreen(Stage primaryStage){
		//Elements of mainScreen
		Label Role_Question_Label= new Label("Do you want to be a SCARER or a LAUGHER?");
		
		Button SCARER_Button= new Button("Switch");
		SCARER_Button.setOnAction(e -> {
			GameControl.handleChoosenRole();
			
		});
		
		Button PLAY_Button= new Button("PLAY!");
		PLAY_Button.setOnAction(e->{
			buildGameScreen();
			mainScene.setRoot(GameScreen);
		});
		
		Button INSTRUCTIONS_Button= new Button("RULES");
		
		
		MainScreen = new VBox(Role_Question_Label,SCARER_Button,PLAY_Button,INSTRUCTIONS_Button);
		
	
		
		// Bind Role_Question_Label button text size
		Role_Question_Label.styleProperty().bind(
			    Bindings.concat(
			        "-fx-font-family: 'Forte'; -fx-font-size: ", 
			        MainScreen.widthProperty().divide(30).asString(), 
			        "px;"
			    )
			);
		
		// Bind SCARER button text size
		SCARER_Button.styleProperty().bind(
		    Bindings.concat(
		        "-fx-font-size: ", 
		        MainScreen.widthProperty().divide(30.0).asString(), 
		        "px;"
		    )
		);

		
		// Bind PLAY button text size
				PLAY_Button.styleProperty().bind(
				    Bindings.concat(
				        "-fx-font-size: ", 
				        MainScreen.widthProperty().divide(30.0).asString(), 
				        "px;"
				    )
				);

		// Bind INSTRUCTIONS button text size
				INSTRUCTIONS_Button.styleProperty().bind(
				    Bindings.concat(
				        "-fx-font-size: ", 
				        MainScreen.widthProperty().divide(30.0).asString(), 
				        "px;"
				    )
				);
				
		
		//Elements Size of mainScreen
		Role_Question_Label.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		SCARER_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		SCARER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		PLAY_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		PLAY_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		INSTRUCTIONS_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		INSTRUCTIONS_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		
		MainScreen.spacingProperty().bind(MainScreen.heightProperty().divide(9));
		
		MainScreen.setAlignment(Pos.CENTER);
		
	}
	
	public void buildGameScreen(){
		GameControl.startGame();
		
		//LEFT LAYOUT
		StackPane BoardPane= new StackPane();
		GridPane Board=new GridPane();
		Pane tokenLayer = new Pane();
	    // Make it mouse transparent so you can still click the buttons underneath
	    tokenLayer.setMouseTransparent(true);
		
	    
	    //RIGHT LAYOUT
	    StackPane RightLayoutFrame= new StackPane();
	    	VBox RightLayout= new VBox();
				HBox upperlayout= new HBox();
					VBox playerLayout= new VBox();
					VBox VSLayout= new VBox();
					VBox opponentLayout=new VBox();
		
					Label usePowerup= new Label("USE POWERUP?");
					Button yesButton= new Button("YES!");
					Button noButton= new Button("NO!");
					HBox yesNoPowerup = new HBox(yesButton,noButton);
				VBox DownLayout = new VBox(usePowerup, yesNoPowerup);
		GameScreen =new HBox(BoardPane, RightLayoutFrame);
		
		
		
		
		//PlayerLayoutElements
		Label playerTurnLabel= new Label("YOUR TURN!");
		Label youLabel= new Label("YOU");
		StackPane playerWidget=new StackPane();
		Label playerMonsterNameLabel=new Label(GameControl.getGame().getPlayer().getName());
		Label playerMonsterOriginalRoleLabel=new Label(GameControl.getGame().getPlayer().getOriginalRole().toString() + "(Original)");
		Label playerMonsterCurrentRoleLabel=new Label(GameControl.getGame().getPlayer().getRole().toString() + "(Current)");
		String playerMonsterType= (GameControl.getGame().getPlayer() instanceof Dasher ? "Dasher" :
			GameControl.getGame().getPlayer() instanceof MultiTasker ? "MultiTasker":
				GameControl.getGame().getPlayer() instanceof Dynamo ? "Dynamo":
					"Schemer");
		Label playerMonsterTypeLabel=new Label(playerMonsterType);
		Label playerMonsterEnergyLabel= new Label(GameControl.getGame().getPlayer().getEnergy()+" energy");
		Label playerMonsterPositionLabel= new Label("Position: "+ GameControl.getGame().getPlayer().getPosition());
		Label playerMonsterShieldedLabel= new Label(GameControl.getGame().getPlayer().isShielded() ? "Shielded" : "Not Shielded");
		Label playerMonsterConfusedLabel= new Label("Confused for: " + GameControl.getGame().getPlayer().getConfusionTurns()+" turns");
		Label playerMonsterFrozenLabel= new Label(GameControl.getGame().getPlayer().isFrozen() ? "Frozen" : "Not Frozen");
		Label playerStatus= new Label(GameControl.getGame().getPlayer() instanceof Dasher ? "Momentum Rush for 0 turns" :
			GameControl.getGame().getPlayer() instanceof MultiTasker ? "Focus Mode for 0 turns":
				GameControl.getGame().getPlayer() instanceof Dynamo ? "Energy Freeze":
					"Chain Attack");
		
		
		//OpponentLayoutElements
		Label opponentTurnLabel= new Label("OPPONENT TURN!");
		Label opponentLabel= new Label("OPPONENT");
		StackPane opponentWidget=new StackPane();
		Label opponentMonsterNameLabel=new Label(GameControl.getGame().getOpponent().getName());
		Label opponentMonsterOriginalRoleLabel=new Label(GameControl.getGame().getOpponent().getOriginalRole().toString() +  "(Original)");
		Label opponentMonsterCurrentRoleLabel=new Label(GameControl.getGame().getOpponent().getRole().toString()+"(Current)");
		String opponentMonsterType= (GameControl.getGame().getOpponent() instanceof Dasher ? "Dasher" :
			GameControl.getGame().getOpponent() instanceof MultiTasker ? "MultiTasker":
				GameControl.getGame().getOpponent() instanceof Dynamo ? "Dynamo":
					"Schemer");
		Label opponentMonsterTypeLabel=new Label(opponentMonsterType);
		Label opponentMonsterEnergyLabel= new Label(GameControl.getGame().getOpponent().getEnergy()+" energy");
		Label opponentMonsterPositionLabel= new Label("Position: "+ GameControl.getGame().getOpponent().getPosition());
		Label opponentMonsterShieldedLabel= new Label(GameControl.getGame().getOpponent().isShielded() ? "Shielded" : "Not Shielded");
		Label opponentMonsterConfusedLabel= new Label("Confused for: " + GameControl.getGame().getOpponent().getConfusionTurns()+" turns");
		Label opponentMonsterFrozenLabel= new Label(GameControl.getGame().getOpponent().isFrozen() ? "Frozen" : "Not Frozen");
		Label opponentStatus= new Label(GameControl.getGame().getOpponent() instanceof Dasher ? "Momentum Rush for 0 turns" :
			GameControl.getGame().getOpponent() instanceof MultiTasker ? "Focus Mode for 0 turns":
				GameControl.getGame().getOpponent() instanceof Dynamo ? "Energy Freeze":
					"Chain Attack");
		
		//Board drawing
		int c=0;
		Circle playerc = new Circle();
		Circle opponentc = new Circle();
		playerc.setFill(Color.BLUE);
		opponentc.setFill(Color.RED);
		playerc.radiusProperty().bind(BoardPane.heightProperty().divide(60)); 
		opponentc.radiusProperty().bind(BoardPane.heightProperty().divide(60));
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				Button btn = new Button();
				Label textLabel = new Label(" " + c);
				StackPane customLayout = new StackPane();
				customLayout.getChildren().addAll( textLabel);
				StackPane.setAlignment(textLabel, Pos.TOP_LEFT);
		        btn.setGraphic(customLayout);
				

				btn.styleProperty().bind(
					    Bindings.concat(
					        "-fx-background-color: #f2efea; ",
					        "-fx-border-color: #1c113c; ",
					        "-fx-border-width: ", BoardPane.heightProperty().divide(400).asString(), "px; ",
					        "-fx-border-radius: ", BoardPane.heightProperty().divide(400).asString(), "px;"
					    )
					);
				textLabel.setStyle(
					    
			    	    "-fx-text-fill: #1c113c;"
			    	);

				if ((c/10) %2 ==0 && c%10!=9){
					c++;
					
				}else{
					if ((c/10) %2 ==0 && c%10==9){
						c=c+10;
					}else{
						if ((c/10) %2 !=0 && c%10!=0){
							c--;
						}else{
							c=c+10;
						}
					}
				}
		        
		        // 1. Tell the button to expand to its maximum possible limits
		        btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		        
		        // 2. Tell the GridPane that this node should always grow to fill space
		        GridPane.setHgrow(btn, Priority.ALWAYS);
		        GridPane.setVgrow(btn, Priority.ALWAYS);
		        
		        // i represents the column index, j represents the row index
		        Board.add(btn, j, i);
		        
		        
			}
			

		}
		
		//Circles Monsters
		// Blue (Player): Bottom-Left of Cell (0,0)
		playerc.centerXProperty().bind(
		    tokenLayer.widthProperty().divide(10).multiply(0)
		    .add(playerc.radiusProperty())
		    .add(tokenLayer.widthProperty().divide(100)) // Dynamic padding instead of 5
		);

		playerc.centerYProperty().bind(
		    tokenLayer.heightProperty().divide(10).multiply(1)
		    .subtract(playerc.radiusProperty())
		    .subtract(tokenLayer.heightProperty().divide(100)) // Dynamic padding instead of 5
		);

		// Red (Opponent): Bottom-Right of Cell (0,0)
		opponentc.centerXProperty().bind(
		    tokenLayer.widthProperty().divide(10).multiply(1)
		    .subtract(opponentc.radiusProperty())
		    .subtract(tokenLayer.widthProperty().divide(100)) // Dynamic padding instead of 5
		);

		opponentc.centerYProperty().bind(
		    tokenLayer.heightProperty().divide(10).multiply(1)
		    .subtract(opponentc.radiusProperty())
		    .subtract(tokenLayer.heightProperty().divide(100)) // Dynamic padding instead of 5
		);
	        
	        
		
		
		//Background cover
		// 1. Dynamically get the correct path to the image in this specific package
		String imageUrl = getClass().getResource("white background.png").toExternalForm();

		// 2. Inject that path into your CSS (Make sure to remove 'file:')
		String cssBackground = "-fx-background-image: url('" + imageUrl + "');" +
		                       "-fx-background-size: cover;" +
		                       "-fx-background-position: center center;" +
		                       "-fx-background-repeat: no-repeat;";

		GameScreen.setStyle(cssBackground);
	
		
		//Layout Colors
		RightLayoutFrame.setStyle("-fx-background-color: blue;"); 
		upperlayout.setStyle("-fx-background-color: #ff0000;");
		playerLayout.setStyle("-fx-background-color: orange;");
		VSLayout.setStyle("-fx-background-color: blue;");
		opponentLayout.setStyle("-fx-background-color: green;");
		DownLayout.setStyle("-fx-background-color: black;"); 
		
		//Layout children
		RightLayoutFrame.getChildren().add(RightLayout);
		upperlayout.getChildren().addAll(playerLayout, VSLayout, opponentLayout);
		playerLayout.getChildren().addAll(playerTurnLabel, youLabel, playerWidget, playerMonsterNameLabel, playerMonsterOriginalRoleLabel, playerMonsterCurrentRoleLabel, playerMonsterTypeLabel, playerMonsterEnergyLabel, playerMonsterPositionLabel, playerMonsterShieldedLabel, playerMonsterFrozenLabel, playerMonsterConfusedLabel, playerStatus);
		opponentLayout.getChildren().addAll(opponentTurnLabel, opponentLabel, opponentWidget, opponentMonsterNameLabel,opponentMonsterOriginalRoleLabel, opponentMonsterCurrentRoleLabel, opponentMonsterTypeLabel, opponentMonsterEnergyLabel, opponentMonsterPositionLabel, opponentMonsterShieldedLabel, opponentMonsterFrozenLabel, opponentMonsterConfusedLabel, opponentStatus);
		tokenLayer.getChildren().addAll(playerc, opponentc);
	    BoardPane.getChildren().addAll(Board, tokenLayer);
	    RightLayout.getChildren().addAll(upperlayout,DownLayout);
	    
	    
	    //Layout alignment
		GameScreen.setAlignment(Pos.CENTER);
		RightLayout.setAlignment(Pos.CENTER);
		opponentLayout.setAlignment(Pos.CENTER);
		playerLayout.setAlignment(Pos.CENTER);
		DownLayout.setAlignment(Pos.CENTER);
		yesNoPowerup.setAlignment(Pos.CENTER);
		RightLayoutFrame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		//Board size
		BoardPane.prefWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		BoardPane.minWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.minHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		BoardPane.maxWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.maxHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		//RightLayout size
		
		RightLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		RightLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));
		
		upperlayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.7));
		upperlayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));
		
		DownLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.2));
		DownLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)));
		
		
		playerLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		playerLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.4));
		
		VSLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		VSLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.2));
		
		opponentLayout.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		opponentLayout.prefWidthProperty().bind(GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.95)).multiply(0.4));
		
		GameScreen.spacingProperty().bind(GameScreen.widthProperty().divide(125));
		
		opponentTurnLabel.setVisible(false);
		
		
	}
	
	public static void updateLabel(Label label, String newText){
		label.setText(newText);
	}
	
	public static double getScreenHeight(){
		return Screen.getPrimary().getBounds().getHeight();
	}

}
