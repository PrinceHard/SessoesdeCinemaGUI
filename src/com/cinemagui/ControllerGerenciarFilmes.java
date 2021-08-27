package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class GerenciarFilmesController implements Initializable{

	//Inputs para coletar o título e a duração.
	@FXML private TextField inputTitulo, inputDuracao;

	//CheckBoxes para coletar o tipo de produção, os tipos de áudio e os tipos de exibição.
	@FXML private CheckBox checkProdNacional, checkProdEstrangeira, checkAudioOriginal, checkAudioLegendado;
	@FXML private CheckBox checkAudioDublado, checkPermite3D, checkNaoPermite3D;

	//Botões para criar ou editar os filmes.
	@FXML private Button buttonCreate, buttonEdit;

	//Painel para visualizar os filmes e painel para criar/editar os filmes.
	@FXML private Pane paneViewFilmes, paneCreateFilme;

	//Tabela para visualizar os filmes.
	@FXML private TableView<Filme> tableFilmes;

	//Colunas da tabela
	@FXML private TableColumn<Filme, Boolean> colSelect;
	@FXML private TableColumn<Filme, String> colTitulo, colTipoProducao;
	@FXML private TableColumn<Filme, Integer> colDuracao;
	@FXML private TableColumn<Filme, String[]> colTipoAudio;
	@FXML private TableColumn<Filme, Boolean> colTipoExibicao;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(location);
		System.out.println(resources);

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de filmes para aparecer na tabelas.
		tableFilmes.setItems(Cinema.getFilmes());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		tableFilmes.setPlaceholder(new Label("Nenhum filmes existente."));

	}	

	@FXML
	private void openCreatePane() {

		//Para de renderizar o painel de visualização dos filmes.
		paneViewFilmes.setVisible(false);

		//Renderiza o painel de criação de filmes.
		paneCreateFilme.setVisible(true);
	}

	@FXML
	private void openEditFilme() {

		//Verificar se o usuário selecionou apenas 1 filme.
		int countSelect=0;
		Filme filmeSelect=null;
		for(Filme filme : filmesList()){

			if(filme.isSelected()){
				countSelect += 1;
				filmeSelected = filme;
			}

		}
		
		if(countSelect == 1) {

			//Para de renderizar o botão de criar filme (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar filme (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Abre o painel de criar filme.
			openCreatePane();

			//Preenche os inputs com as informações do filme que o usuário escolheu.
			inputTitulo.setText(filmeSelect.getTitulo());
			inputDuracao.setText(Integer.toString(filmeSelect.getDuracao()));
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			if(filmeSelect.getTipoProducao().equals("Nacional")){
				prodNac.setSelected(true);
			} else {
				prodEst.setSelected(true);
			}
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
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
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
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
	private void closeCreatePane() {

		//Limpa os inputs e os CheckBoxes
		inputTitulo.setText("");
		inputDuracao.setText("");
		prodNac.setSelected(false);
		prodEst.setSelected(false);
		audioOriginal.setSelected(false);
		audioLegendado.setSelected(false);
		audioDublado.setSelected(false);
		sim3D.setSelected(false);
		nao3D.setSelected(false);

		//Renderiza o painel de criação de salas.
		paneViewFilmes.setVisible(true);

		//Para de renderizar o painel de visualização das salas.
		paneCreatingFilme.setVisible(false);
	}

	private boolean verifyInputs() {

		//Flag que será retornada.
		//Essa flag também será usada para evitar de fazer uma verificação quando outra já a invalidou.
		valid = true;

		//Verifica se o input de título está vazio.
		if(inputTitulo.getText().isEmpty()) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "O título não pode ser vazio.");
		}

		//Verifica se a duração passada pode ser transformada em inteiro.
		if(valid) {
			try {
				Integer.parseInt(inputDuracao.getText());
			} catch (NumberFormatException e) {
				valid = false;
				Alert a = new Alert(AlertType.INFORMATION, "A duração é inválida.");
			}
		}

		//Verifica se o usuário selecionou um tipo de produção.
		if(!(checkProdNacional.isSelected || checkProdEstrangeira) && valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Selecione um tipo de produção.");
		}

		//Verifica se o usuário selecionou um tipo de áudio.
		if(!(checkAudioDublado.isSelected() || checkAudioOriginal.isSelected() || checkAudioLegendado.isSelected()) &&
		   valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Selecione um tipo de áudio.");
		}

		//Verifica se o usuário selecionou um tipo de exibição.
		if(!(checkPermite3D.isSelected() || checkNaoPermite3D.isSelected()) && valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Defina se o filme permite exibição 3D ou não.");
		}

		//Verifica se o filme já foi definido.
		for(Filme filme : Cinema.getFilmes()){
			if(filme.getTitulo().equals(inputTitulo.getText())){
				Alert a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro!");
				valid = false;
				break;
			}
		}

	}

	@FXML
	private void createFilme() {

		//Variáveis temporárias
		String titulo="", tipoProducao="";
		ArrayList<String> tipoAudioTemporario = new ArrayList<>();;
		int duracao;
		boolean permite3D = false;

		if(verifyInputs()) {
			titulo = inputTitulo.getText();
			duracao = Integer.parseInt(inputDuracao.getText());

			if(prodNac.isSelected()){
				tipoProducao = prodNac.getText();
			} else if(prodEst.isSelected()){
				tipoProducao = prodEst.getText();
			}

			if(audioOriginal.isSelected() ){
				tipoAudioTemporario.add(audioOriginal.getText());
			}
			if(audioDublado.isSelected()){
				tipoAudioTemporario.add(audioDublado.getText());
			}
			if(audioLegendado.isSelected()){
				tipoAudioTemporario.add(audioLegendado.getText());
			}

			String[] tipoAudioFinal = new String[tipoAudioTemporario.size()];
			tipoAudioTemporario.toArray(tipoAudioFinal);

			if(sim3D.isSelected()){
				permite3D = true;
			}

			Filme filme = new Filme(titulo, duracao, tipoProducao, audioFinal, permite3D);
			Cinema.addFilme(filme);
			closeCreatePane();
		}

	}
	
	@FXML
	private void deleteFilme() {
		ObservableList<Filme> oldFilmes = FXCollections.observableArrayList();
		for (Filme filme : Cinema.getFilmes()) {
			if(filme.isSelected()) {
				oldFilmes.add(filme);
			}
		}
		Cinema.removeFilmes(oldFilmes);
		updateList();
	}

	
	
	@FXML
	private void editFilme() {
		Filme filmeSelect=null;
		for(Filme filme : filmesList()){
			if(filme.isSelected()){
				filmeSelect = filme;
				break;
			}
		}

		ArrayList<String> tipoAudio= new ArrayList<>();
		
		Alert a;
		
		boolean filmeExiste=false;
		try {
			for(Filme filme : Cinema.getFilmes()){
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

	private void factorys() {
		selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
		
		
		titCol.setCellValueFactory(new PropertyValueFactory("titulo"));
		titCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		titCol.setEditable(false);

		durCOl.setCellValueFactory(new PropertyValueFactory<>("duracao"));
		durCOl.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		durCOl.setEditable(false);

		prodCol.setCellValueFactory(new PropertyValueFactory<>("tipoProducao"));
		prodCol.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
		prodCol.setEditable(false);

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
		audioCol.setEditable(false);

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
		exib3DCol.setEditable(false);
	}

}
