package com.cinemagui;

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
import java.io.ObjectInputStream;
import javafx.stage.Modality;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;
import java.lang.ClassNotFoundException;

/* CICLO DE VIDA DE UMA APLICAÇÃO JAVAFX
 * 
 * 1 - Faz uma instância da classe herdeira da classe Application. 
 * 1 - Chama o método init().
 * 3 - Chama o método start(javafx.stage.Stage).
 * 4 - Espera a aplicação finalizar, isso pode acontecer de duas formas:
       * A aplicação chama Platform.exit().
	   * A última janela é fechada e o atributo implicitExit da Platform é true.
 * 5 - Chama o método stop().
*/


	
//Toda aplicação JavaFX precisa estender a classe Application.
public class CinemaUtil extends Application{

	//Instância da classe Cinema.
	private static Cinema cinema = new Cinema();

	//Stage para perguntar o nome do cinema.
	private static Stage stageGetCinemaName = new Stage();
		
	@FXML
	//Input para coletar o nome do cinema.
	private TextField userInput;
	
	/*O método main é opcional em uma aplicação JavaFx.
	 *
	 *	public static void main(String[] args) throws Exception{
	 *		Application.launch(args);
	 *	}
	 *
	*/
	
	@Override
	public void init() throws Exception {
		//Carregando as fontes necessárias.
		Font.loadFont("resources/fonts/Montserrat-Light.ttf", 300);
		Font.loadFont("resources/fonts/Montserrat-Medium.ttf", 500);
		Font.loadFont("resources/fonts/Montserrat-MediumItalic.ttf", 500);
		Font.loadFont("resources/fonts/Montserrat-SemiBold.ttf", 600); 
		Font.loadFont("resources/fonts/Montserrat-Bold.ttf", 700); 
		Font.loadFont("resources/fonts/Montserrat-Black.ttf", 900);
		//Chama o método para carregar dados para o programa.
		unserializeData();
	}

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
		} else if(item instanceof Filme) {
			filmes.add( (Filme) item);
		} else {
			salas.add( (Sala) item);
		}
	}

	private static void serializeData() throws IOException {
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.ser"));
		
		out.writeObject(new ArrayList<Sessao>(sessoes));
		out.writeObject(new ArrayList<Filme>(filmes));
		out.writeObject(new ArrayList<Sala>(salas));
	
	}

	private static void unserializeData() throws IOException, ClassNotFoundException{
		
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.ser"));
		
		sessoes = FXCollections.observableArrayList( (ArrayList<Sessao>) in.readObject() );
		filmes = FXCollections.observableArrayList( (ArrayList<Filme>) in.readObject() );
		salas = FXCollections.observableArrayList( (ArrayList<Sala>) in.readObject()) ;
			
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
	
	

}
