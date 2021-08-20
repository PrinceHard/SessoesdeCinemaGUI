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
import javafx.stage.Modality;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class CinemaUtil extends Application{

	private static ArrayList<Object> data = null;
	private static ArrayList<Sessao> sessoes = null;
	private static ArrayList<Filme> filmes = null;
	private static ArrayList<Sala> salas = null;

	public static String cinemaName = "";
	private static Stage stageFirstOpen = new Stage();
		
	@FXML
	private TextField userInput;

	@Override
	public void start(Stage mainStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("mainSceneGraph.fxml"));
		Parent rootNodeSceneMain = (Parent) loader.load();
		SceneController controller = loader.getController();

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
			controller.changeCinemaName(cinemaName);
		}
		
		mainStage.show();
	}

	@FXML
	private void enterCinemaName() {
		if(!userInput.getText().isEmpty()){
			cinemaName = userInput.getText();
			stageFirstOpen.close();

			FileWriter writerFlagFirstOpen;
			try {
				writerFlagFirstOpen = new FileWriter("flagFirstOpen.txt");
				writerFlagFirstOpen.write("1");
				writerFlagFirstOpen.write(cinemaName);
				writerFlagFirstOpen.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			Alert a = new Alert(Alert.AlertType.INFORMATION, "É necessário definir um nome para o cinema!");
			a.showAndWait();
		}
	}	

	public static void saveData(Object items, String type) {
		if(type.equals("sessoes")) {
			data.add(0, items);
		} else if(type.equals("salas")) {
			data.add(1, items);
		} else {
			data.add(2, items);
		}
	}

	private static void serializeData() throws Exception{
		FileOutputStream fileOut = new FileOutputStream("data.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);

		out.writeObject(data);

		fileOut.close();
		out.close();
	}

	private static void unserializeData() throws Exception {
		FileInputStream fileIn = new FileInputStream("data.ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		
		try {
			data = (ArrayList<Object>) in.readObject();

			sessoes = (ArrayList<Sessao>) data.get(0);
			salas = (ArrayList<Sala>) data.get(1);
			filmes = (ArrayList<Filme>) data.get(2);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static ArrayList<Sessao> getSessoes() {
		return sessoes;
	}

	public static ArrayList<Filme> getFilmes() {
		return filmes;
	}

	public static ArrayList<Sala> getSalas() {
		return salas;
	}
	
	public static void main(String[] args) throws Exception{
		Font.loadFont("file:fonts/Montserrat-Light.ttf", 300);
		Font.loadFont("file:fonts/Montserrat-Medium.ttf", 500);
		Font.loadFont("file:fonts/Montserrat-MediumItalic.ttf", 500);
		Font.loadFont("file:fonts/Montserrat-SemiBold.ttf", 600); 
		Font.loadFont("file:fonts/Montserrat-Bold.ttf", 700); 
		Font.loadFont("file:fonts/Montserrat-Black.ttf", 900);
		try {
			unserializeData();
		} catch (java.io.FileNotFoundException e) {
			System.out.println("Serialized data not found!");
		}
		launch(args);
	}

}
