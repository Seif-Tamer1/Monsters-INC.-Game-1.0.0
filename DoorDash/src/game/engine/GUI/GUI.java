package game.engine.GUI;

import java.io.IOException;

import game.engine.Role;
import game.engine.GameControl.GameControl;
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
		buildGameScreen();
		
		primaryStage.setTitle("Monsters INC.");
		mainScene = new Scene(MainScreen, 640, 480);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public void buildMainScreen(Stage primaryStage){
		Label Role_Question_Label= new Label("Do you want to be a SCARER or a LAUGHER?");
		
		
		Button SCARER_Button= new Button("Switch");
		
		
		SCARER_Button.setOnAction(e -> {
			GameControl.handleChoosenRole();
			
		});
		

		
		Button PLAY_Button= new Button("PLAY!");
		Button INSTRUCTIONS_Button= new Button("RULES");
		MainScreen = new VBox(Role_Question_Label,SCARER_Button,PLAY_Button,INSTRUCTIONS_Button);
		
		
		PLAY_Button.setOnAction(e->{
			mainScene.setRoot(GameScreen);
		});
		
		
		
		SCARER_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		SCARER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(5));
		
		
		
		
		
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
				
		PLAY_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		INSTRUCTIONS_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		
		Role_Question_Label.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		SCARER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		PLAY_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		INSTRUCTIONS_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		MainScreen.spacingProperty().bind(MainScreen.heightProperty().divide(9));
		
		MainScreen.setAlignment(Pos.CENTER);
		
	}
	
	public void buildGameScreen() throws IOException{
		GameControl.startGame();
		StackPane BoardPane= new StackPane();
		GridPane Board=new GridPane();
		
		Pane tokenLayer = new Pane();
	    // Make it mouse transparent so you can still click the buttons underneath
	    tokenLayer.setMouseTransparent(true);
		
		Button btn1=new Button();
		
		
		int c=0;
		Image imageOk = new Image(getClass().getResourceAsStream("15.png"));
		Circle playerc = new Circle();
		Circle opponentc = new Circle();
		playerc.setFill(Color.BLUE);
		opponentc.setFill(Color.RED);
		playerc.setRadius(5.0); 
		opponentc.setRadius(5.0); 
		
		// 2. Add the circles to the token layer, NOT the button
	    tokenLayer.getChildren().addAll(playerc, opponentc);
	    
	    // 3. Stack the tokenLayer directly on top of the Board
	    BoardPane.getChildren().addAll(Board, tokenLayer);
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				
				
				
				Button btn = new Button();

				// 1. Wrap your raw String into a Label (a Node)
				Label textLabel = new Label(" " + c);
				
				
				
				// 2. Wrap your Image into an ImageView (a Node)
				// Note: You need a NEW ImageView for every button, but they can share the same 'imageOk' data
				ImageView iconView = new ImageView(imageOk);

				StackPane customLayout = new StackPane();
				
				
					customLayout.getChildren().addAll( textLabel);
					
					StackPane.setAlignment(textLabel, Pos.TOP_LEFT);
		        
				// 3. Add the Nodes (ImageView and Label) to the layout, NOT the raw String/Image
			
				// 4. Align the Nodes
//				StackPane.setAlignment(iconView, Pos.CENTER);
			
		        
				
				
				
				
				
				// Optional: Add a little padding so the text isn't touching the exact pixel edge
//				StackPane.setMargin(textLabel, new Insets(5, 0, 0, 5));

				// 5. Set the layout as the button's graphic
				btn.setGraphic(customLayout);
				
//				if (c %2 ==0){
					btn.setStyle(
						    "-fx-background-color: #f2efea; " +
						    "-fx-border-color: #1c113c; " +
						    "-fx-border-width: 1px; " +
						    "-fx-border-radius: 1px;"
						);
					textLabel.setStyle(
						    
				    	    "-fx-text-fill: #1c113c;"           // The text color (e.g., Gold)
				    	);
//				}else{
//					if (c %7==0){
//						btn.setStyle(
//							    "-fx-background-color: #0d7d88; " +
//							    "-fx-border-color: #1c113c; " +
//							    "-fx-border-width: 1px; " +
//							    "-fx-border-radius: 1px;"
//							);
//						textLabel.setStyle(
//							    
//					    	    "-fx-text-fill: #f7e6a0;"           // The text color (e.g., Gold)
//					    	);
//					}else{
//						if (c %3==0){
//							btn.setStyle(
//								    "-fx-background-color: #b22222; " +
//								    "-fx-border-color: #1c113c; " +
//								    "-fx-border-width: 1px; " +
//								    "-fx-border-radius: 1px;"
//								);
//							textLabel.setStyle(
//								    
//						    	    "-fx-text-fill: #f7e6a0;"           // The text color (e.g., Gold)
//						    	);
//						}else{
//						btn.setStyle(
//							    "-fx-background-color: #742c76; " +
//							    "-fx-border-color: #1c113c; " +
//							    "-fx-border-width: 1px; " +
//							    "-fx-border-radius: 1px;"
//							);
//						textLabel.setStyle(
//							    
//					    	    "-fx-text-fill: #f7e6a0;"           // The text color (e.g., Gold)
//					    	);
//						}
//					}
//				}
				
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
		
		playerc.centerXProperty().bind(
	            tokenLayer.widthProperty().divide(10).multiply(0) // Col 0 Left Edge
	            .add(playerc.radiusProperty()).add(5)             // Add padding right
	        );
	        playerc.centerYProperty().bind(
	            tokenLayer.heightProperty().divide(10).multiply(1) // Row 0 Bottom Edge
	            .subtract(playerc.radiusProperty()).subtract(5)    // Subtract padding up
	        );

	        // Red (Opponent): Bottom-Right of Cell (0,0)
	        opponentc.centerXProperty().bind(
	            tokenLayer.widthProperty().divide(10).multiply(1)  // Col 0 Right Edge
	            .subtract(opponentc.radiusProperty()).subtract(5)  // Subtract padding left
	        );
	        opponentc.centerYProperty().bind(
	            tokenLayer.heightProperty().divide(10).multiply(1) // Row 0 Bottom Edge
	            .subtract(opponentc.radiusProperty()).subtract(5)  // Subtract padding up
	        );
		
	        
	        
		GameScreen =new HBox(BoardPane, btn1);
		
		BoardPane.prefWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		BoardPane.minWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.minHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		BoardPane.maxWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		BoardPane.maxHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		
		GameScreen.setAlignment(Pos.CENTER);
		GameScreen.setSpacing(10);
		
		btn1.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		btn1.prefWidthProperty().bind((GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.9))).multiply(0.9));
		
		// 1. Dynamically get the correct path to the image in this specific package
		String imageUrl = getClass().getResource("white background.png").toExternalForm();

		// 2. Inject that path into your CSS (Make sure to remove 'file:')
		String cssBackground = "-fx-background-image: url('" + imageUrl + "');" +
		                       "-fx-background-size: cover;" +
		                       "-fx-background-position: center center;" +
		                       "-fx-background-repeat: no-repeat;";

		GameScreen.setStyle(cssBackground);
		
//		GameScreen.setStyle("-fx-background-color: #f7e6a0;");
		
		btn1.setVisible(true);
		
		btn1.setOnAction(e ->{
			GameControl.handleMoveMonsterOnBoard(20, playerc);
		});
		
		
	}
	
	public static double getScreenHeight(){
		return Screen.getPrimary().getBounds().getHeight();
	}

}
