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
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.IOException;
import javafx.stage.StageStyle;

public class SceneController implements Initializable{
	
	
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
		stageGerenciarVendas.initModality(Modality.APPLICATION_MODAL);;

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

}
