package src;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CinemaUtil extends Application{

	public static String cinemaName;
	private static Stage stageFirstOpen = new Stage();
		
	@FXML
	private TextField userInput;

	@Override
	public void start(Stage mainStage) throws Exception {
		Parent rootNodeSceneMain = FXMLLoader.load(getClass().getResource("mainSceneGraph.fxml"));
		Parent rootNodeSceneFirstOpen = FXMLLoader.load(getClass().getResource("firstSceneGraph.fxml"));
		
		Scene mainScene = new Scene(rootNodeSceneMain);
		Scene firstOpenScene = new Scene(rootNodeSceneFirstOpen);
		
		mainStage.setScene(mainScene);
		stageFirstOpen.setScene(firstOpenScene);

		FileReader readerFlagFirstOpen = null;
		try {
			readerFlagFirstOpen = new FileReader("flagFirstOpen.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		if((char) readerFlagFirstOpen.read() == '0') {
			stageFirstOpen.showAndWait();
			System.out.println(cinemaName);
		}

		FileWriter writerFlagFirstOpen;
		try {
			writerFlagFirstOpen = new FileWriter("flagFirstOpen.txt");
			writerFlagFirstOpen.write("1");
			writerFlagFirstOpen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mainStage.show();
	}

	@FXML
	private void enterCinemaName() {
		if(!userInput.getText().isEmpty()){
			cinemaName = userInput.getText();
			stageFirstOpen.close();
		} else {
			Alert a = new Alert(Alert.AlertType.INFORMATION, "É necessário definir um nome para o cinema!");
			a.showAndWait();
		}
	}	
	
	public static void main(String[] args) {
		launch(args);
	}

}
