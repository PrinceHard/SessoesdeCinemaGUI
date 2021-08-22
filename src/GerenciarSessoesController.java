package src;

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

public class GerenciarSessoesController implements Initializable{
	
	
	@FXML
	private ChoiceBox choiceBoxFilmes, choiceBoxSalas;
	@FXML
	private Button buttonEdit, buttonCreate;
	@FXML
	private CheckBox audioLegendado, audioOriginal, audioDublado, exib3D, exib2D;
	
	@FXML
	private Pane viewTableSessoes, paneCreatingSessao;

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
	private TableColumn<Sessao, Double> valorCol;
	@FXML
	private TableColumn<Sessao, Boolean> exibicaoCol;
	@FXML
	private TableColumn<Sessao, CheckBox> selectCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

		if(CinemaUtil.getSessoes() != null) {
			updateTable();
		} else {
			sessoesTable.setPlaceholder(new Label("Nenhuma sessão existente."));
		}
	}	
	
	private ObservableList<Sessao> sessaoList() {
		return FXCollections.observableArrayList(CinemaUtil.getSessoes());
	}
	
	private void updateTable() {
		factorys();
		sessoesTable.setItems(sessaoList());
	}
	
	@FXML
	private void openNewSessao() {
		sessoesTable.setVisible(false);
		paneCreatingSessao.setVisible(true);
		buttonCreate.setVisible(true);
		buttonEdit.setVisible(false);
	}
	
	@FXML
	private void deleteSessao(){
		
	}
	
	@FXML
	private void editSessao(){
		
	}
	@FXML
	private void cancelCreatingFilme(){
		
	}
	@FXML
	private void createSessao() {
		Alert a;
		
		try {
			
		} catch(Exception e) {
			a = new Alert(AlertType.INFORMATION, "Ops! Algo deu errado, verifique os campos e tente novamente.");
		}
	}
	
	
	private void factorys() {
		choiceBoxFilmes.setConverter(new StringConverter<Filme>() {
			@Override
			public String toString(Filme filme) {
				return filme.getTitulo();
			}
			@Override
			public Filme fromString(String string){
				Filme filme=null;
				for(Filme filmeFound : CinemaUtil.getFilmes()) {
					if(filmeFound.getTitulo() == filme.getTitulo())  {
						filme = filmeFound;
						break;
					}
				}
				return filme;
			}
		});
		
		ChangeListener<Filme> filmeListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Filme> observable, Filme oldValue, Filme newValue) {
				if(newValue.getPermite3D()) {
					exib3D.setVisible(true);
					exib2D.setVisible(true);
				} else {
					exib3D.setVisible(true);
					exib2D.setVisible(true);
					exib2D.setSelected(false);
					exib2D.setSelected(true);
				}
				
				for(String string : newValue.getTipoAudio()) {
					if(string.equals("Dublado")) {
					audioDublado.setVisible(true);
					}
					if(string.equals("Legendado")) {
						audioLegendado.setVisible(true);
					}
					if(string.equals("Original")) {
						audioOriginal.setVisible(true);
					}
				}
			}
		};
		
		choiceBoxFilmes.getItems().addAll(CinemaUtil.getFilmes());
		
		choiceBoxFilmes.getSelectionModel().selectedItemProperty().addListener(filmeListener);
		
		choiceBoxSalas.getItems().addAll(CinemaUtil.getSalas());
		
		choiceBoxSalas.setConverter(new StringConverter<Sala>() {
			@Override
			public String toString(Sala sala) {
				return Integer.toString(sala.getNumSala());
			}
			@Override
			public Sala fromString(String string){
				Sala sala=null;
				for(Sala salaFound : CinemaUtil.getSalas()) {
					if(salaFound.getNumSala() == sala.getNumSala())  {
						sala = salaFound;
						break;
					}
				}
				return sala;
			}
		});
		
		selectCol.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
		
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
			
		horaIniCol.setCellValueFactory(new PropertyValueFactory<>("horarioInicial"));
		horaIniCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
		
		horaFinCol.setCellValueFactory(new PropertyValueFactory<>("horarioFinal"));
		horaIniCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
		
		audioCol.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
		
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
			
		valorCol.setCellValueFactory(new PropertyValueFactory<>("valorIngresso"));
		valorCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	}
	

	@FXML
	private void select3D(){
		if(exib2D.isSelected()){
			exib2D.fire();
		}
	}

	@FXML
	private void select2D(){
		if(exib3D.isSelected()){
			exib3D.fire();
		}
	}

}
