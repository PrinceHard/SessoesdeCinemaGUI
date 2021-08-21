package src;

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
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.BooleanStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.collections.ListChangeListener;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;

public class GerenciarFilmesController implements Initializable{

	@FXML
	private TextField inputTitulo, inputDuracao;
	private CheckBox prodNacional, prodEstrangeira, audioOriginal, audioLegendado, audioDublado, sim3D, nao3D;

	@FXML
	private Pane paneViewFilmes, paneCreatingFilme;
	@FXML
	private TableView<Filme> tableFilmes;
	@FXML
	private TableColumn<Filme, CheckBox> selectCol;
	@FXML
	private TableColumn<Filme, String> titCol, prodCol;
	@FXML
	private TableColumn<Filme, Integer> durCOl;
	@FXML
	private TableColumn<Filme, String[]> audioCol;
	@FXML
	private TableColumn<Filme, Boolean> exib3DCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectCol.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
		
		titCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
		titCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		titCol.setOnEditCommit(new EventHandler<CellEditEvent<Filme, String>>() {
				@Override
				public void handle(CellEditEvent<Filme, String> index) {
					index.getTableView().getItems().get(index.getTablePosition().getRow()).setTitulo(index.getNewValue());
				}
			});
			
		durCOl.setCellValueFactory(new PropertyValueFactory<>("duracao"));
		durCOl.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		durCOl.setOnEditCommit(new EventHandler<CellEditEvent<Filme, Integer>>() {
				@Override
				public void handle(CellEditEvent<Filme, Integer> index) {
					index.getTableView().getItems().get(index.getTablePosition().getRow()).setDuracao(index.getNewValue());
				}
			});

		prodCol.setCellValueFactory(new PropertyValueFactory<>("tipoProducao"));
		prodCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		prodCol.setOnEditCommit(new EventHandler<CellEditEvent<Filme, String>>() {
				@Override
				public void handle(CellEditEvent<Filme, String> index) {
					index.getTableView().getItems().get(index.getTablePosition().getRow()).setTipoProducao(index.getNewValue());
				}
			});

		audioCol.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
		audioCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		audioCol.setOnEditCommit(new EventHandler<CellEditEvent<Filme, String>>() {
				@Override
				public void handle(CellEditEvent<Filme, String> index) {
					index.getTableView().getItems().get(index.getTablePosition().getRow()).setTipoAudio(index.getNewValue());
				}
			});

		exib3DCol.setCellValueFactory(new PropertyValueFactory<>("permite3D"));
		exib3DCol.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
		exib3DCol.setOnEditCommit(new EventHandler<CellEditEvent<Filme, Boolean>>() {
				@Override
				public void handle(CellEditEvent<Filme, Boolean> index) {
					index.getTableView().getItems().get(index.getTablePosition().getRow()).setPermite3D(index.getNewValue());
				}
			});

		if(!CinemaUtil.getFilmes().isEmpty()) {
			updateList();
		} else {
			tableFilmes.setPlaceholder(new Label("Nenhum filme existente."));
		}
	}	

	private ObservableList<Filme> filmesList() {
    	return FXCollections.observableArrayList(CinemaUtil.getFilmes());
	}

	@FXML
	private void newFilme() {
		paneViewFilmes.setVisible(false);
		paneCreatingFilme.setVisible(true);
	}

	@FXML
	private void cancelCreatingFilme() {
		inputTitulo.setText("");
		inputDuracao.setText("");
		prodNacional.setSelected(false);
		prodEstrangeira.setSelected(false);
		audioOriginal.setSelected(false);
		audioLegendado.setSelected(false);
		audioDublado.setSelected(false);
		sim3D.setSelected(false);
		nao3D.setSelected(false);

		paneViewFilmes.setVisible(true);
		paneCreatingFilme.setVisible(false);
	}

	@FXML
	private void createFilme() {
		String titulo="", tipoProducao;
		ArrayList<String> tipoAudio;
		int duracao;
		boolean permite3D;
		
		Alert a;
		
		boolean filmeExiste=false;
		try {
			duracao = Integer.parseInt(inputDuracao.getText());
			
			if(selecProdNac.isSelected()){
				tipoProducao = selecProdNac.getText();
			} else if(selecProdEst.isSelected()){
				tipoProducao = selecProdNac.getText();
			} else {
				a = new Alert(AlertType.INFORMATION, "Por favor, selecione um tipo de produção.");
				a.showAndWait();
				break;
			}
		
			if(audioOriginal.isSelected()){
				tipoAudio.add(audioOriginal.getText());
			}
			if(audioDublado.isSelected()){
				tipoAudio.add(audioDublado.getText());
			}
			if(audioLegendado.isSelected()){
				tipoAudio.add(audioLegendado.getText());
			}
			if(tipoAudio.isEmpty()){
				a = new Alert(Alertype.INFORMATION, "Por favor, selecione um (ou mais) tipo(s) de áudio.");
				a.showAndWait();
				break;
			}

			if(sim3D.isSelected()){
				permite3D = true;
			} else if(nao3D.isSelected()){
				permite3D = false;
			} else {
				a = new Alert(AlertType.INFORMATION, "Por favor, selecione se o filme permite 3D ou não");
				a.showAndWait();
				break;
			}

			for(Filme filme : CinemaUtil.getFilmes()){
				if(filme.getTitulo() == titulo){
					a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro!");
					filmeExiste=true;
					break;
				}
			}
			
			if(!filmeExiste){
				Filme filme = new Filme(titulo, duracao, tipoProducao, tipoAudio.toArray(new String(tipoAudio.size())), permite3D);
				CinemaUtil.saveData(filme);
				updateList();
				cancelCreatingFilme();
			}
			

		} catch(Exception e) {
			a = new Alert(AlertType.INFORMATION, "Ops! Algo deu errado, verifique os campos e tente novamente.");
			a.showAndWait();
		}	
	}
	
	@FXML
	private void deleteFilme() {
		ObservableList<Filme> oldFilmes = FXCollections.observableArrayList();
		for (Filme filme : CinemaUtil.getFilmes()) {
			if(filme.getCheckBox().isSelected()) {
				oldFilmes.add(filme);
			}
		}
		CinemaUtil.getFilmes().removeAll(oldFilmes);
		updateList();
	}

	private void updateList() {
		tableFilmes.setItems(filmesList());
	}

	@FXML
	private void selectProdNac(){
		if(prodEstrangeira.isSelected()){
			prodEstrangeira.fire();
		}
	}

	@FXML
	private void selectProdEst(){
		if(prodNacional.isSelected()){
			prodNacional.fire();
		}
	}

	@FXML
	private void selectSim3D(){
		if(nao3D.isSelected()){
			nao3D.fire();
		}
	}

	@FXML
	private void selectNao3D(){
		if(sim3D.isSelected()){
			sim3D.fire();
		}
	}

}
