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
import java.io.File;
import java.io.IOException;
import javafx.stage.Modality;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class CinemaUtil extends Application{

	private static Cinema cinema = new Cinema();
	
	private static Gson gson = new Gson();

	private static ArrayList<Object> data = new ArrayList<>();
	private static ObservableList<Sessao> sessoes = FXCollections.observableArrayList(); 
	private static ObservableList<Filme> filmes = FXCollections.observableArrayList();
	private static ObservableList<Sala> salas = FXCollections.observableArrayList();

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

		readerFlagFirstOpen.close();
		
		mainStage.show();
			mainStage.setOnCloseRequest(event -> {
				try {
					serializeData();
				} catch(Exception e) {
					System.out.println(e.getMessage());
				}
			});
	}

	public static Cinema getCinema() {
		return cinema;
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
			Alert a = new Alert(AlertType.INFORMATION, "É necessário definir um nome para o cinema!");
			a.showAndWait();
		}
	}	

	public static void saveData(Object item) {
		if(item instanceof Sessao) {
			sessoes.add( (Sessao) item);
			System.out.println("Sessao add");
		} else if(item instanceof Filme) {
			filmes.add( (Filme) item);
			System.out.println("Filme add");
		} else {
			salas.add( (Sala) item);
			System.out.println("Sala add");
		}
	}

	private static void serializeData() throws Exception{/*
		FileWriter writerSessoes = new FileWriter("dataSessoes.json");
		FileWriter writerFilmes = new FileWriter("dataFilmes.json");
		FileWriter writerSalas = new FileWriter("dataSalas.json");
		
		writerSessoes.write(gson.toJson(sessoes, new TypeToken<ObservableList<Sessao>>(){}.getType()));
		writerFilmes.write(gson.toJson(filmes, new TypeToken<ObservableList<Filme>>(){}.getType()));
		writerSalas.write(gson.toJson(salas, new TypeToken<ObservableList<Sala>>(){}.getType()));

		writerSessoes.close();
		writerFilmes.close();
		writerSalas.close();*/
	}

	private static void unserializeData() throws Exception {
		/*
		try {
			FileReader readerSessoes = new FileReader("dataSessoes.json");
			FileReader readerFilmes = new FileReader("dataFilmes.json");
			FileReader readerSalas = new FileReader("dataSalas.json");
			
			String sessoesExtracted="";
			while(readerFilmes.ready()) {
				sessoesExtracted+=(char)readerFilmes.read();
			}
			ArrayList<Sessao> sessoesArrayList = gson.fromJson(sessoesExtracted, new TypeToken<ArrayList<Sessao>>(){}.getType());
			sessoes = FXCollections.observableArrayList(sessoesArrayList);
			
			String filmesExtracted="";
			while(readerFilmes.ready()) {
				sessoesExtracted+=(char)readerFilmes.read();
			}
			ArrayList<Filme> filmessArrayList = gson.fromJson(filmesExtracted, new TypeToken<ArrayList<Filme>>(){}.getType());
			filmes = FXCollections.observableArrayList(filmessArrayList);
			
			String salasExtracted="";
			while(readerSalas.ready()) {
				salasExtracted+=(char)readerSalas.read();
			}
			ArrayList<Sala> salasArrayList = gson.fromJson(salasExtracted, new TypeToken<ArrayList<Sala>>(){}.getType());
			salas = FXCollections.observableArrayList(salasArrayList);
			
			readerSessoes.close();
			readerFilmes.close();
			readerSalas.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}*/
	}

	public static ObservableList<Sessao> getSessoes() {
		return sessoes;
	}

	public static ObservableList<Filme> getFilmes() {
		return filmes;
	}

	public static ObservableList<Sala> getSalas() {
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
