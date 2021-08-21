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
import javafx.util.StringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.collections.ListChangeListener;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.geometry.Side;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ChoiceDialog;
import java.util.Arrays;
import javafx.scene.control.Button;

public class GerenciarFilmesController implements Initializable{

	@FXML
	private TextField inputTitulo, inputDuracao;
	@FXML
	private CheckBox prodNac, prodEst, audioOriginal, audioLegendado, audioDublado, sim3D, nao3D;
	@FXML
	private Button buttonCreate, buttonEdit;

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
		
		if(!CinemaUtil.getFilmes().isEmpty()) {
			updateList();
		} else {
			tableFilmes.setPlaceholder(new Label("Nenhum filme existente."));
		}
	}	
	
	private void factorys() {
		selectCol.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
		
		titCol.setCellValueFactory(new PropertyValueFactory("titulo"));
		titCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
			
		durCOl.setCellValueFactory(new PropertyValueFactory<>("duracao"));
		durCOl.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		prodCol.setCellValueFactory(new PropertyValueFactory<>("tipoProducao"));
		prodCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

		audioCol.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
		audioCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String[]>(){
				@Override
				public String toString(String[] audios){
					return Arrays.toString(audios).replace("[", " ").replace("]", " ");
				}
				@Override
				public String[] fromString(String string){
					String[] a = {string};
					return a;
				}
		
			}));

		exib3DCol.setCellValueFactory(new PropertyValueFactory<>("permite3D"));
		exib3DCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Boolean>() {
				@Override
				public String toString(Boolean perm3d) {
					if(perm3d){
						return "SIM";
					} else {
						return "NÃO";
					}
				}
				@Override
				public Boolean fromString(String string){
					if(string.equals("SIM")){
						return true;
					} else {
						return false;
					}
				}
			
			}));
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
		prodNac.setSelected(false);
		prodEst.setSelected(false);
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
		String titulo="", tipoProducao="";
		ArrayList<String> tipoAudio= new ArrayList<>();;
		int duracao;
		boolean permite3D=false;
		
		Alert a;
		
		boolean filmeExiste=false;
		try {
			titulo = inputTitulo.getText();
			duracao = Integer.parseInt(inputDuracao.getText());
			
			if(prodNac.isSelected()){
				tipoProducao = prodNac.getText();
			} else if(prodEst.isSelected()){
				tipoProducao = prodEst.getText();
			} else {
				a = new Alert(AlertType.INFORMATION, "Por favor, selecione um tipo de produção.");
				a.showAndWait();
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
				a = new Alert(AlertType.INFORMATION, "Por favor, selecione um (ou mais) tipo(s) de áudio.");
				a.showAndWait();
			}

			if(sim3D.isSelected()){
				permite3D = true;
			} else if(!nao3D.isSelected()){
				a = new Alert(AlertType.INFORMATION, "Por favor, selecione se o filme permite 3D ou não");
				a.showAndWait();
			}

			for(Filme filme : CinemaUtil.getFilmes()){
				if(filme.getTitulo() == titulo){
					a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro!");
					filmeExiste=true;
					break;
				}
			}
			
			String[] audioFinal = new String[tipoAudio.size()];
			if(!filmeExiste){
				Filme filme = new Filme(titulo, duracao, tipoProducao, tipoAudio.toArray(audioFinal), permite3D);
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

	@FXML
	private void openEditFilme() {
		int countSelect=0;
		Filme filmeSelect=null;
		for(Filme filme : filmesList()){
			if(filme.getCheckBox().isSelected()){
				countSelect += 1;
				filmeSelect = filme;
			}
		}
		
		if(countSelect == 1) {
			buttonCreate.setVisible(false);
			buttonEdit.setVisible(true);
			newFilme();
			
			inputTitulo.setText(filmeSelect.getTitulo());
			inputDuracao.setText(Integer.toString(filmeSelect.getDuracao()));
			
			if(filmeSelect.getTipoProducao().equals("Nacional")){
				prodNac.setSelected(true);
			} else {
				prodEst.setSelected(true);
			}
			
			for(String string : filmeSelect.getTipoAudio()){
				if(string.equals("Dublado")) {
					audioDublado.setSelected(true);
				}
				if(string.equals("Legendado")) {
					audioLegendado.setSelected(true);
				}
				if(string.equals("Original")) {
					audioOriginal.setSelected(true);
				}
			}
			
			if(filmeSelect.getPermite3D()){
					sim3D.setSelected(true);
			} 
			
		} else if(countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhum filme.");
			a.showAndWait();
		} else {
			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 filme.");
			a.showAndWait();
		}
	}
	
	@FXML
	private void editFilme() {
		Filme filmeSelect=null;
		for(Filme filme : filmesList()){
			if(filme.getCheckBox().isSelected()){
				filmeSelect = filme;
				break;
			}
		}

		ArrayList<String> tipoAudio= new ArrayList<>();
		
		Alert a;
		
		boolean filmeExiste=false;
		try {
			for(Filme filme : CinemaUtil.getFilmes()){
				if(filme.getTitulo() == inputTitulo.getText() && filmeSelect != filme){
					a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro!");
					filmeExiste=true;
					break;
				}
			}
			
			if(!filmeExiste){
				
				filmeSelect.setTitulo(inputTitulo.getText());
				filmeSelect.setDuracao(Integer.parseInt(inputDuracao.getText()));
				
				if(prodNac.isSelected()){
					filmeSelect.setTipoProducao(prodNac.getText());
				} else if(prodEst.isSelected()){
					filmeSelect.setTipoProducao(prodEst.getText());
				} else {
					a = new Alert(AlertType.INFORMATION, "Por favor, selecione um tipo de produção.");
					a.showAndWait();
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
					a = new Alert(AlertType.INFORMATION, "Por favor, selecione um (ou mais) tipo(s) de áudio.");
					a.showAndWait();
				}
				
				String[] audioFinal = new String[tipoAudio.size()];
				filmeSelect.setTipoAudio(tipoAudio.toArray(audioFinal));

				if(sim3D.isSelected()){
					filmeSelect.setPermite3D(true);
				} else if(nao3D.isSelected()){
					filmeSelect.setPermite3D(false);
				} else {
					a = new Alert(AlertType.INFORMATION, "Por favor, selecione se o filme permite exibição em 3D ou não");
					a.showAndWait();
				}

				updateList();
				cancelCreatingFilme();
				buttonCreate.setVisible(true);
				buttonEdit.setVisible(false);
				
			} else {
				a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro título!");
				a.showAndWait();
			}
			
		} catch(Exception e) {
			a = new Alert(AlertType.INFORMATION, "Ops! Algo deu errado, verifique os campos e tente novamente.");
			a.showAndWait();
		}	
		
		

	}
	
	private void updateList() {
		factorys();
		tableFilmes.setItems(filmesList());
	}

	@FXML
	private void selectProdNac(){
		if(prodEst.isSelected()){
			prodEst.fire();
			
		}
	}

	@FXML
	private void selectProdEst(){
		if(prodNac.isSelected()){
			prodNac.fire();
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
