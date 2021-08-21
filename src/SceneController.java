package src;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.stage.StageStyle;

public class SceneController implements Initializable{
	
	private static Stage stageGerenciarSessoes = new Stage();
	private static Stage stageGerenciarSalas = new Stage();
	private static Stage stageGerenciarFilmes = new Stage();
	
	@FXML
	private Label cinemaLabel;

	@FXML
	private Pane paneHome, paneGerenciamento;
	@FXML
	private Pane paneVendas, paneFaturamento;
	
	@Override	
	public void initialize(URL location, ResourceBundle resources) {
		stageGerenciarSessoes.initModality(Modality.APPLICATION_MODAL);
		stageGerenciarSalas.initModality(Modality.APPLICATION_MODAL);
		stageGerenciarFilmes.initModality(Modality.APPLICATION_MODAL);

		FileReader readerFlagFirstOpen = null;
		try {
			readerFlagFirstOpen = new FileReader("flagFirstOpen.txt");
			if((char) readerFlagFirstOpen.read() == '1') {
				int character;
				while((character = readerFlagFirstOpen.read()) != -1) {
					cinemaLabel.setText(cinemaLabel.getText() + (char) character);
				}
				readerFlagFirstOpen.close();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}	

	@FXML
	private void switchToHome() {
		paneHome.setVisible(true);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(false);
	}
	
	@FXML
	private void switchToGerenciamento() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(true);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(false);
	}

	@FXML
	private void switchToVendas() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(true);
		paneFaturamento.setVisible(false);
	}

	@FXML
	private void switchToFaturamento() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(true);
	}
	
	@FXML
	private void openGerenciarSessoes() throws Exception{
		Parent nodeRootGerenciarSessoes = FXMLLoader.load(getClass().getResource("gerenciarSessoesSceneGraph.fxml"));
		Scene sceneGerenciarSessoes = new Scene(nodeRootGerenciarSessoes);
		stageGerenciarSessoes.setScene(sceneGerenciarSessoes);
		stageGerenciarSessoes.showAndWait();
	}

	@FXML
	private void openGerenciarSalas() throws Exception{
		Parent nodeRootGerenciarSalas = FXMLLoader.load(getClass().getResource("gerenciarSalasSceneGraph.fxml"));
		Scene sceneGerenciarSalas = new Scene(nodeRootGerenciarSalas);
		stageGerenciarFilmes.setScene(sceneGerenciarSalas);
		stageGerenciarFilmes.showAndWait();
	}

	@FXML
	private void openGerenciarFilmes() throws Exception{
		Parent nodeRootGerenciarFilmes = FXMLLoader.load(getClass().getResource("gerenciarFilmesSceneGraph.fxml"));
		Scene sceneGerenciarFilmes = new Scene(nodeRootGerenciarFilmes);
		stageGerenciarSalas.setScene(sceneGerenciarFilmes);
		stageGerenciarSalas.showAndWait();
	}

	public void changeCinemaName(String name) {
		cinemaLabel.setText(name);
	}

}
