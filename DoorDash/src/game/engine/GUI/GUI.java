package game.engine.GUI;

import game.engine.Role;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

	private Scene mainScene;
	
	private VBox StartScreen;
	private VBox AbousaVideo;
	private VBox MainScreen;
	private HBox GameScreen;
	private VBox GameOverScreen;
	
	private Role choosen_role=Role.SCARER;
	
	
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
			if (choosen_role==Role.SCARER)
				choosen_role=Role.LAUGHER;
			else
				choosen_role=Role.SCARER;
			System.out.println(choosen_role.toString());
			
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
	
	public void buildGameScreen(){
		GridPane Board=new GridPane();
		Button btn1=new Button();
		int c=0;
		Image imageOk = new Image(getClass().getResourceAsStream("15.png"));
		for(int i=0; i<10; i++){
			for(int j=0; j<10; j++){
				
				
				
				Button btn = new Button();

				// 1. Wrap your raw String into a Label (a Node)
				Label textLabel = new Label(" " + c);
				
				textLabel.setStyle(
					    
					    	    "-fx-text-fill: #000000;"           // The text color (e.g., Gold)
					    	);
				// 2. Wrap your Image into an ImageView (a Node)
				// Note: You need a NEW ImageView for every button, but they can share the same 'imageOk' data
				ImageView iconView = new ImageView(imageOk);

				StackPane customLayout = new StackPane();

				// 3. Add the Nodes (ImageView and Label) to the layout, NOT the raw String/Image
				customLayout.getChildren().addAll(iconView, textLabel);

				// 4. Align the Nodes
				StackPane.setAlignment(iconView, Pos.CENTER);
				StackPane.setAlignment(textLabel, Pos.TOP_LEFT);

				// Optional: Add a little padding so the text isn't touching the exact pixel edge
//				StackPane.setMargin(textLabel, new Insets(5, 0, 0, 5));

				// 5. Set the layout as the button's graphic
				btn.setGraphic(customLayout);
				
				btn.setStyle(
					    "-fx-background-color: #ffffff; " +
					    "-fx-border-color: #000000; " +
					    "-fx-border-width: 1px; " +
					    "-fx-border-radius: 1px;"
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
		
		
		GameScreen =new HBox(Board,btn1);
		
		Board.prefWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		Board.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		Board.minWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		Board.minHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		Board.maxWidthProperty().bind(GameScreen.heightProperty().multiply(0.9));
		Board.maxHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		
		
		GameScreen.setAlignment(Pos.CENTER);
		GameScreen.setSpacing(10);
		
		btn1.prefHeightProperty().bind(GameScreen.heightProperty().multiply(0.9));
		btn1.prefWidthProperty().bind((GameScreen.widthProperty().subtract(GameScreen.heightProperty().multiply(0.9))).multiply(0.9));
		
		// 1. Dynamically get the correct path to the image in this specific package
		String imageUrl = getClass().getResource("Background.png").toExternalForm();

		// 2. Inject that path into your CSS (Make sure to remove 'file:')
//		String cssBackground = "-fx-background-image: url('" + imageUrl + "');" +
//		                       "-fx-background-size: cover;" +
//		                       "-fx-background-position: center center;" +
//		                       "-fx-background-repeat: no-repeat;";
//
//		GameScreen.setStyle(cssBackground);
		
		GameScreen.setStyle("-fx-background-color: #ffffff;");
		
	}
	
	public Role get_choosen_role(){
		return this.choosen_role;
	}

}
