package game.engine.GUI;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application{

	Scene mainScene;
	
	VBox StartScreen;
	VBox AbousaVideo;
	VBox MainScreen;
	HBox GameScreen;
	VBox GameOverScreen;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		buildMainScreen(primaryStage);
		
		mainScene = new Scene(MainScreen, 640, 480);
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch();
	}
	
	public void buildMainScreen(Stage primaryStage){
		Label Role_Question_Label= new Label("Do you want to be a SCARER or a LAUGHER?");
		
		
		Button SCARER_Button= new Button("SCARER");
		
		
		Button LAUGHER_Button= new Button("LAUGHER");
		
		
		HBox SCARER_LAUGHER_HBox= new HBox(SCARER_Button,LAUGHER_Button);
		SCARER_LAUGHER_HBox.setAlignment(Pos.CENTER);
		
		
		
		Button PLAY_Button= new Button("PLAY!");
		Button INSTRUCTIONS_Button= new Button("RULES");
		MainScreen = new VBox(Role_Question_Label,SCARER_LAUGHER_HBox,PLAY_Button,INSTRUCTIONS_Button);
		
		SCARER_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		SCARER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(5));
		
		LAUGHER_Button.prefWidthProperty().bind(MainScreen.widthProperty().divide(5));
		LAUGHER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(5));
		
		SCARER_LAUGHER_HBox.spacingProperty().bind(MainScreen.widthProperty().divide(5));
		
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

		// Bind LAUGHER button text size
		LAUGHER_Button.styleProperty().bind(
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
		LAUGHER_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		PLAY_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		INSTRUCTIONS_Button.prefHeightProperty().bind(MainScreen.heightProperty().divide(9));
		
		MainScreen.spacingProperty().bind(MainScreen.heightProperty().divide(9));
		
		MainScreen.setAlignment(Pos.CENTER);
		
	}

}
