package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.stage.StageStyle;

public class SceneController implements Initializable{
	
	private static Stage stageGerenciarSessoes = new Stage();
	private static Stage stageGerenciarSalas = new Stage();
	private static Stage stageGerenciarFilmes = new Stage();
	private static Stage stageVender = new Stage();
	private static Stage stageCancelarVendas = new Stage();
	
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
		stageVender.initModality(Modality.APPLICATION_MODAL);
		stageCancelarVendas.initModality(Modality.APPLICATION_MODAL);;

		cinemaLabel.set(Cinema.getName());
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

	@FXML
	private void openVendas() throws Exception {
		Parent nodeRootVendas = FXMLLoader.load(getClass().getResource("vendasSceneGraph.fxml"));
		Scene sceneVendas = new Scene(nodeRootVendas);
		stageVender.setScene(sceneVendas);
		stageVender.showAndWait();
	}

	@FXML
	private void openCancelarVendas() throws Exception {
		Parent nodeRootCancelarVendas = FXMLLoader.load(getClass().getResource("cancelarVendasSceneGraph.fxml"));
		Scene sceneCancelarVendas = new Scene(nodeRootCancelarVendas);
		stageCancelarVendas.setScene(sceneCancelarVendas);
		stageCancelarVendas.showAndWait();
	}

}
