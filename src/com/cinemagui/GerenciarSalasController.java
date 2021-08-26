package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.collections.ListChangeListener;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;


public class GerenciarSalasController implements Initializable{

	@FXML
	private TextField inputNumSala, inputCapacidade;
	@FXML
	private Button buttonCreate, buttonEdit;

	@FXML
	private Pane paneViewSalas, paneCreatingSala;
	@FXML
	private TableView<Sala> salasTable;
	@FXML
	private TableColumn<Sala, Integer> numCol, capacidadeCol;
	@FXML
	private TableColumn<Sala, CheckBox> selectCol;
	
	private Sala salaSelected=null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {


		if(!CinemaUtil.getSalas().isEmpty()) {
			updateList();
		} else {
			salasTable.setPlaceholder(new Label("Nenhuma sala existente."));
		}
	}	
	
	private void factorys() {
		selectCol.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
		
		numCol.setCellValueFactory(new PropertyValueFactory<>("numSala"));
		numCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		numCol.setOnEditCommit(new EventHandler<CellEditEvent<Sala, Integer>>() {
				@Override
				public void handle(CellEditEvent<Sala, Integer> sala) {
					sala.getTableView().getItems().get(sala.getTablePosition().getRow()).setNumSala(sala.getNewValue());
				}
			});
		numCol.setEditable(false);
	
		capacidadeCol.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
		capacidadeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		capacidadeCol.setOnEditCommit(new EventHandler<CellEditEvent<Sala, Integer>>() {
				@Override
				public void handle(CellEditEvent<Sala, Integer> sala) {
					sala.getTableView().getItems().get(sala.getTablePosition().getRow()).setCapacidade(sala.getNewValue());
				}
			});
		capacidadeCol.setEditable(false);
	}

	private ObservableList<Sala> salasList() {
    	return FXCollections.observableArrayList(CinemaUtil.getSalas());
	}

	@FXML
	private void newSala() {
		paneViewSalas.setVisible(false);
		paneCreatingSala.setVisible(true);
	}

	@FXML
	private void openEditSala() {
		int countSelect=0;
		for(Sala sala : CinemaUtil.getSalas()){
			if(sala.getCheckBox().isSelected()){
				countSelect+=1;
				salaSelected = sala;
			}
		}
		
		if(countSelect == 1) {
			buttonCreate.setVisible(false);
			buttonEdit.setVisible(true);
			newSala();
			
			inputNumSala.setText(Integer.toString(salaSelected.getNumSala()));
			inputCapacidade.setText(Integer.toString(salaSelected.getCapacidade()));
			
		} else if(countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sala.");
			a.showAndWait();
		} else {
			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sala.");
			a.showAndWait();
		}
	}
	@FXML
	private void editSala()  {
		int numSala=0, capacidade=0;
		boolean salaExiste=false;
		

		try {
			numSala = Integer.parseInt(inputNumSala.getText());
			capacidade = Integer.parseInt(inputCapacidade.getText());
			
			for(Sala sala : CinemaUtil.getSalas()){
				if(sala.getNumSala() == numSala && sala != salaSelected){
					Alert a = new Alert(AlertType.INFORMATION, "Sala já existente, defina outra!");
					salaExiste=true;
					break;
				}
			}

			if(!salaExiste){
				salaSelected.setNumSala(numSala);
				salaSelected.setCapacidade(capacidade);
				updateList();
				cancelCreatingSala();
				buttonCreate.setVisible(false);
				buttonEdit.setVisible(true);
			}
			
			
			

		} catch(Exception e) {
			Alert a = new Alert(AlertType.INFORMATION, "Valores inválidos, tente novamente.");
			a.showAndWait();
		}	
	}
	@FXML
	private void cancelCreatingSala() {
		inputNumSala.setText("");
		inputCapacidade.setText("");
		paneViewSalas.setVisible(true);
		paneCreatingSala.setVisible(false);
	}

	@FXML
	private void createSala() {
		int numSala, capacidade;
		boolean salaExiste=false;
		
		try {
			numSala = Integer.parseInt(inputNumSala.getText());
			capacidade = Integer.parseInt(inputCapacidade.getText());
			
			for(Sala sala : CinemaUtil.getSalas()){
				if(sala.getNumSala() == numSala){
					Alert a = new Alert(AlertType.INFORMATION, "Sala já existente, defina outra!");
					salaExiste=true;
					break;
				}
			}

			if(!salaExiste){
				Sala sala = new Sala(numSala, capacidade);
				CinemaUtil.saveData(sala);
				updateList();
				cancelCreatingSala();
			}
			

		} catch(Exception e) {
			Alert a = new Alert(AlertType.INFORMATION, "Valores inválidos, tente novamente.");
			a.showAndWait();
		}	
	}
	
	@FXML
	private void deleteSala() {
		ObservableList<Sala> oldSalas = FXCollections.observableArrayList();
		for (Sala sala : CinemaUtil.getSalas()) {
			if(sala.getCheckBox().isSelected()) {
				oldSalas.add(sala);
			}
		}
		CinemaUtil.getSalas().removeAll(oldSalas);
		updateList();
	}

	private void updateList() {
		salasTable.setItems(salasList());
		factorys();
	}


}
