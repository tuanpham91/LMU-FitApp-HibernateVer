package main;

import controller.Controller;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import logging.MyLogger;

public class Main extends Application {

	private Pane splashLayout; 
	private ProgressBar loadProgress; 
	private Label progressText; 
	private static final int SPLASH_WIDTH = 476; 
	private static final int SPLASH_HEIGHT = 227; 
	
	@Override 
	public void init() {
		ImageView splash = new ImageView(new Image("resources/fit-app-logo.jpg")); 
		loadProgress = new ProgressBar(); 
		loadProgress.setPrefWidth(SPLASH_WIDTH - 20);
		Label infos = new Label("Version 1.10 \n 10.04.2017 \n Datenbank: " + Controller.url.substring(18));
		progressText = new Label("Loading...");
		splashLayout = new HBox(); 
		VBox progressContent = new VBox(); 
		progressContent.getChildren().addAll(infos, progressText, loadProgress); 
		VBox.setMargin(progressText, new Insets(80, 0, 0, 0));
		splashLayout.getChildren().addAll(splash, progressContent); 
		progressText.setAlignment(Pos.CENTER);
		splashLayout.setStyle("-fx-padding: 5; -fx-background-color: #EEEEEE;");
		splashLayout.setEffect(new DropShadow());
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		MyLogger.setLogging(false, false);
		MyLogger.setDirectory();

		showSplash(primaryStage);
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1)); 
		pause.setOnFinished(event -> {
			showMainStage(primaryStage);
		});
		pause.play();
		
	}
	
	private void showMainStage(Stage primaryStage) {
		
		Controller controller = new Controller(getHostServices()); 
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				controller.init(primaryStage);				
			}
		});		
	}
	
	private void showSplash(Stage primaryStage) {
		Scene splashScene = new Scene(splashLayout); 
		primaryStage.initStyle(StageStyle.UNDECORATED);
		final Rectangle2D bounds = Screen.getPrimary().getBounds(); 
		primaryStage.setScene(splashScene);
		primaryStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
		primaryStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args); 
	}
	
}
