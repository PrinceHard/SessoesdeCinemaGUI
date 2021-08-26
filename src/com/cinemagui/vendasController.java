package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import java.time.LocalTime;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.CheckBox;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.scene.layout.Pane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.CheckBox;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class vendasController implements Initializable {

	@FXML
	private Pane viewTableSessoes, paneSellTicket;

	@FXML
	private CheckBox selectInteiro, selectMeio;

	@FXML
	private TextField inputPoltrona;

	@FXML
	private Label showSessao;
	
	@FXML
	private TableView<Sessao> sessoesTable;
	@FXML
	private TableColumn<Sessao, Filme> filmeCol;
	@FXML
	private TableColumn<Sessao, Sala> salaCol;
	@FXML
	private TableColumn<Sessao, LocalTime> horaIniCol, horaFinCol;
	@FXML
	private TableColumn<Sessao, String> audioCol;
	@FXML
	private TableColumn<Sessao, Double> valorCol, taxaOcupacao;
	@FXML
	private TableColumn<Sessao, Boolean> exibicaoCol, selectCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		factorys();
		sessoesTable.setItems(CinemaUtil.getSessoes());
		sessoesTable.setPlaceholder(new Label("Nenhuma sessão existente."));
	}
	
	@FXML
	private void openVenderIngresso() {
		Alert a;
		int countSelect=0;
		Sessao sessaoSelected = null;
		for (Sessao sessao : CinemaUtil.getSessoes()) {
			if(sessao.isSelected()) {
				countSelect += 1;
				sessaoSelected = sessao;
			}
		}

		if(countSelect == 1) {
			paneSellTicket.setVisible(true);
			viewTableSessoes.setVisible(false);
			
			showSessao.setText(sessaoSelected.getFilme().getTitulo());
			
		} else if(countSelect == 0){
			a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sessão.");
			a.showAndWait();
		} else {
			a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sessão.");
			a.showAndWait();
		}
		
	}

	@FXML
	private void cancelVenda() {
		paneSellTicket.setVisible(false);
		viewTableSessoes.setVisible(true);
		inputPoltrona.setText("");
		selectInteiro.setSelected(false);
		selectMeio.setSelected(false);
	}

	@FXML
	private void vender() {
		Alert a;
		Sessao sessaoSelected = null;
		for (Sessao sessao : CinemaUtil.getSessoes()) {
			if(sessao.isSelected()) {
				sessaoSelected = sessao;
				break;
			}
		}

		char tipoIngresso;
		if(selectInteiro.isSelected()) {
			tipoIngresso = 'i';
		} else {
			tipoIngresso = 'm';
		}

		if (verifyInputs()) {

			if (CinemaUtil.getCinema().venderIngresso(sessaoSelected, tipoIngresso, Integer.parseInt(inputPoltrona.getText()))) {
				a = new Alert(AlertType.INFORMATION, "Ingresso vendido com sucesso!");
				a.showAndWait();

				cancelVenda();
				
			} else {
				a = new Alert(AlertType.INFORMATION, "Essa poltrona já foi vendida!");
				a.showAndWait();
			}

		} else {
			a = new Alert(AlertType.INFORMATION, "Verifique se você preencheu todos os campos.");
		}
	}

	private boolean verifyInputs() {
		boolean valid = true;

		if(inputPoltrona.getText().isEmpty()) {
			valid = false;
		}

		try {
			int i = Integer.parseInt(inputPoltrona.getText());
		} catch (Exception e) {
			valid = false;
		}

			//Duas caixas selecionadas
		if (selectInteiro.isSelected() && selectMeio.isSelected() ||
			//Nenhuma caixa selecionada
			!(selectInteiro.isSelected() || selectMeio.isSelected())) {
				valid = false;
		} 

		return valid;
	}


	private void factorys() {
		
		selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
		
		filmeCol.setCellValueFactory(new PropertyValueFactory<>("filme"));
		filmeCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Filme>() {
				@Override
				public String toString(Filme filme) {
					return filme.getTitulo();
				}
				@Override
				public Filme fromString(String string) {
					Filme filme=null;
					for(Filme filmeFound : CinemaUtil.getFilmes()) {
						if(filmeFound.getTitulo() == string){
							filme = filmeFound;
							break;
						}
					}
					return filme;
				}
			}));
		filmeCol.setEditable(false);

		salaCol.setCellValueFactory(new PropertyValueFactory<>("sala"));
		salaCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Sala>() {
				@Override
				public String toString(Sala sala) {
					return Integer.toString(sala.getNumSala());
				}
				@Override
				public Sala fromString(String string) {
					Sala sala=null;
					for(Sala salaFound : CinemaUtil.getSalas()) {
						if(Integer.toString(salaFound.getNumSala()) == string){
							sala = salaFound;
							break;
						}
					}
					return sala;
				}
			}));
		salaCol.setEditable(false);
	
		horaIniCol.setCellValueFactory(new PropertyValueFactory<>("horarioInicial"));
		horaIniCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
		horaIniCol.setEditable(false);

		horaFinCol.setCellValueFactory(new PropertyValueFactory<>("horarioFinal"));
		horaIniCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
		horaIniCol.setEditable(false);

		audioCol.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
		audioCol.setEditable(false);

		exibicaoCol.setCellValueFactory(new PropertyValueFactory<>("exibicao3D"));
		exibicaoCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Boolean>() {
				@Override
				public String toString(Boolean perm3d) {
					if(perm3d){
						return "3D";
					} else {
						return "2D";
					}
				}
				@Override
				public Boolean fromString(String string){
					if(string.equals("3D")){
						return true;
					} else {
						return false;
					}
				}
			
			}));
		exibicaoCol.setEditable(false);
	
		valorCol.setCellValueFactory(new PropertyValueFactory<>("valorIngresso"));
		valorCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		valorCol.setEditable(false);

		taxaOcupacao.setCellValueFactory(new PropertyValueFactory<>("taxaOcupacao"));
		taxaOcupacao.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));	
		taxaOcupacao.setEditable(false);
	}
}	
