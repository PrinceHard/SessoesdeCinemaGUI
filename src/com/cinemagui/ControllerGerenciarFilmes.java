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
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.StringConverter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class ControllerGerenciarFilmes implements Initializable{

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
	@FXML private TableColumn<Filme, String> colTitulo;
	@FXML private TableColumn<Filme, Integer> colDuracao;
	@FXML private TableColumn<Filme, String> colTipoProducao;
	@FXML private TableColumn<Filme, String[]> colTipoAudio;
	@FXML private TableColumn<Filme, Boolean> colTipoExibicao;

	//Variável para armazenar o filme que será editado
	private Filme filmeSelected = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de filmes para aparecer na tabelas.
		tableFilmes.setItems(Cinema.getFilmes());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		tableFilmes.setPlaceholder(new Label("Nenhum filmes existente."));

		//Atualiza as sessões.
		Cinema.updateSessaoList();

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
		int countSelect = 0;
		for(Filme filme : Cinema.getFilmes()){

			if(filme.isSelected()){
				countSelect += 1;
				filmeSelected = filme;
			}

		}

		//Flag pra indicar se o usuário pode editar o filme.
		boolean valid = true;
		for(Sessao sessao : Cinema.getSessoes()) {
			if(sessao.getFilme() == filmeSelected) {
				valid = false;
				break;
			}
		}

		if(countSelect == 1 && valid) {

			//Para de renderizar o botão de criar filme (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar filme (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Abre o painel de criar filme.
			openCreatePane();

			//Preenche os inputs com as informações do filme que o usuário escolheu.
			inputTitulo.setText(filmeSelected.getTitulo());
			inputDuracao.setText(Integer.toString(filmeSelected.getDuracao()));
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			if(filmeSelected.getTipoProducao().equals("Nacional")){
				checkProdNacional.setSelected(true);
			} else {
				checkProdEstrangeira.setSelected(true);
			}
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			for (String string : filmeSelected.getTipoAudio()) {
				if(string.equals("Dublado")) {
					checkAudioDublado.setSelected(true);
				}
				if(string.equals("Legendado")) {
					checkAudioLegendado.setSelected(true);
				}
				if(string.equals("Original")) {
					checkAudioOriginal.setSelected(true);
				}
			}
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			if(filmeSelected.getPermite3D()){
				checkPermite3D.setSelected(true);
			} else {
				checkNaoPermite3D.setSelected(true);
			}
			
		} else if(countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhum filme.");
			a.showAndWait();

		} else if (!valid) {
			Alert a = new Alert(AlertType.INFORMATION, "Uma sessão irá reproduzir esse filme, você não\npode modificá-lo.");
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
		checkProdEstrangeira.setSelected(false);
		checkProdNacional.setSelected(false);
		checkAudioDublado.setSelected(false);
		checkAudioLegendado.setSelected(false);
		checkAudioOriginal.setSelected(false);
		checkPermite3D.setSelected(false);
		checkNaoPermite3D.setSelected(false);

		//Para de renderizar o painel de visualização das salas.
		paneCreateFilme.setVisible(false);

		//Renderiza o painel de criação de salas.
		paneViewFilmes.setVisible(true);
		
	}

	@FXML
	private void createFilme() {

		//Variáveis temporárias
		String titulo = "";
		String tipoProducao = "Nacional";
		ArrayList<String> tipoAudioTemporario = new ArrayList<>();;
		int duracao;
		boolean permite3D = false;

		//Filme selected só deve ser usada para edição.
		filmeSelected = null;

		if(verifyInputs()) {

			//Coleta os dados e armazena em variáveis temporárias
			titulo = inputTitulo.getText();
			duracao = Integer.parseInt(inputDuracao.getText());

			if(checkProdEstrangeira.isSelected()){
				tipoProducao = "Estrangeira";
			}

			if(checkAudioOriginal.isSelected() ){
				tipoAudioTemporario.add("Original");
			}
			if(checkAudioDublado.isSelected()){
				tipoAudioTemporario.add("Dublado");
			}
			if(checkAudioLegendado.isSelected()){
				tipoAudioTemporario.add("Legendado");
			}

			String[] tipoAudioFinal = new String[tipoAudioTemporario.size()];
			tipoAudioTemporario.toArray(tipoAudioFinal);

			if(checkPermite3D.isSelected()){
				permite3D = true;
			}

			//Cria o filme
			Filme filme = new Filme(titulo, duracao, tipoProducao, tipoAudioFinal, permite3D);

			//Adiciona o filme na lista.
			Cinema.addFilme(filme);

			//Fecha o painel de criação
			closeCreatePane();
		}

	}

	@FXML
	private void editFilme() {

		//Variáveis temporárias
		ArrayList<String> tipoAudioTemporario = new ArrayList<>();
		
		if(verifyInputs()) {

			//Modifica os atributos da Sala selecionada.
			filmeSelected.setTitulo(inputTitulo.getText());
			filmeSelected.setDuracao(Integer.parseInt(inputDuracao.getText()));

			if(checkProdEstrangeira.isSelected()){
				filmeSelected.setTipoProducao("Estrangeira");
			} else {
				filmeSelected.setTipoProducao("Nacional");
			}

			if(checkAudioOriginal.isSelected()){
				tipoAudioTemporario.add("Original");
			}
			if(checkAudioDublado.isSelected()){
				tipoAudioTemporario.add("Dublado");
			}
			if(checkAudioLegendado.isSelected()){
				tipoAudioTemporario.add("Legendado");
			}

			String[] tipoAudioFinal = new String[tipoAudioTemporario.size()];
			filmeSelected.setTipoAudio(tipoAudioTemporario.toArray(tipoAudioFinal));

			if(checkPermite3D.isSelected()){
				filmeSelected.setPermite3D(true);
			} else {
				filmeSelected.setPermite3D(false);
			}

			//Para de renderizar o botão de criar sala (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sala (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Fecha o painel de criação
			closeCreatePane();
		}

	}
	
	@FXML
	private void deleteFilme() {

		//Lista temporária para armazenar os filmes e as sessões que serão removidas.
		ArrayList<Filme> oldFilmes = new ArrayList<>();
		ArrayList<Sessao> oldSessoes = new ArrayList<>();

		//Flag para cancelar a exclusão.
		final SimpleBooleanProperty cancel = new SimpleBooleanProperty(false);

		for (Filme filme : Cinema.getFilmes()) {
			if(filme.isSelected()) {

				//Verifica se existem sessões que reproduzem este filme.
				for(Sessao sessao : Cinema.getSessoes()) {

					if (sessao.getFilme() == filme) {
						Alert a = new Alert(AlertType.CONFIRMATION);
						Text t = new Text("A sessão " + sessao.getFilme().getTitulo() + 
										  " (" + sessao.getHorarioInicial() + ") " +  
										  "reproduz este filme. Se continuar, a sessão será apagada.");

						a.getDialogPane().setContent(t); 

						//Verifica se o usuário ainda deseja excluir.
						a.showAndWait().ifPresent(response -> {
							if(response != ButtonType.OK) {
								cancel.set(true);
							}
						});

						if (cancel.get())  {
							break;
						}

						oldSessoes.add(sessao);

					}

				}

				if (cancel.get()) {
					break;
				}

				oldFilmes.add(filme);
			}
		}

		if(!cancel.get()) {
			Cinema.removeSessoes(oldSessoes);
			Cinema.removeFilmes(oldFilmes);			
		}
	}

	private boolean verifyInputs() {

		/*
		 * Flag que será retornada.
		 * Essa flag também será usada para evitar de fazer uma verificação quando outra já a invalidou.
		*/
		boolean valid = true;

		//Verifica se o input de título está vazio.
		if(inputTitulo.getText().isEmpty()) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "O título não pode ser vazio.");
			a.showAndWait();
		}

		//Verifica se a duração passada pode ser transformada em inteiro.
		if(valid) {
			try {
				Integer.parseInt(inputDuracao.getText());
			} catch (NumberFormatException e) {
				valid = false;
				Alert a = new Alert(AlertType.INFORMATION, "A duração é inválida.");
				a.showAndWait();
			}
		}

		//Verifica se o usuário selecionou um tipo de produção.
		if(!(checkProdNacional.isSelected() || checkProdEstrangeira.isSelected()) && valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Selecione um tipo de produção.");
			a.showAndWait();
		}

		//Verifica se o usuário selecionou um tipo de áudio.
		if(!(checkAudioDublado.isSelected() || checkAudioOriginal.isSelected() || checkAudioLegendado.isSelected()) &&
		   valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Selecione um tipo de áudio.");
			a.showAndWait();
		}

		//Verifica se o usuário selecionou um tipo de exibição.
		if(!(checkPermite3D.isSelected() || checkNaoPermite3D.isSelected()) && valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Defina se o filme permite exibição 3D ou não.");
			a.showAndWait();
		}

		//Verifica se o filme já foi definido.
		for(Filme filme : Cinema.getFilmes()){
			if(filme.getTitulo().equals(inputTitulo.getText()) && filme != filmeSelected){
				Alert a = new Alert(AlertType.INFORMATION, "Filme já existente, defina outro!");
				a.showAndWait();
				valid = false;
				break;
			}
		}

		return valid;

	}

	private void factorys() {

		/*
		 * O "CellValueFactory" é a fabrica que coleta as propriedades dos objetos da lista
		 * que foi definida para "tableFilmes".
		 *
		 * O "CellFactory" é a fábrica que define como que os valores coletados na fábrica
		 * anterior serão renderizados na tabela.
		*/

		//Chama o método "selectedProperty()" para cada Filme na lista.
		colSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));

		/*
		 * Renderiza uma CheckBox na célula. Quando o valor da propridedade for true, a
		 * CheckBox será renderizada como selecionada.
		*/
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
		
		//Chama o método "tituloProperty()" para cada Sala na lista.
		colTitulo.setCellValueFactory(new PropertyValueFactory("titulo"));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTitulo.setEditable(false);

		//Chama o método "duracaoProperty()" para cada Sala na lista.
		colDuracao.setCellValueFactory(new PropertyValueFactory<>("duracao"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colDuracao.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colDuracao.setEditable(false);

		//Chama o método "tipoProducaoProperty()" para cada Sala na lista.
		colTipoProducao.setCellValueFactory(new PropertyValueFactory<>("tipoProducao"));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTipoProducao.setEditable(false);

		//Chama o método "tipoAudioProperty()" para cada Sala na lista.
		colTipoAudio.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colTipoAudio.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<String[]>(){

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

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTipoAudio.setEditable(false);

		//Chama o método "tipoExibicaoProperty()" para cada Sala na lista.
		colTipoExibicao.setCellValueFactory(new PropertyValueFactory<>("permite3D"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colTipoExibicao.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Boolean>() {

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

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTipoExibicao.setEditable(false);

	}

	/*
	 * Para o tipo de exibição e o tipo de produção o usuário só pode selecionar
	 * uma das CheckBoxes disponíveis. Os métodos abaixo servem para garantir isso.
	*/
	
	@FXML
	private void selectProdNacional(){
		if(checkProdEstrangeira.isSelected()){
			checkProdEstrangeira.fire();
			
		}
	}

	@FXML
	private void selectProdEstrangeira(){
		if(checkProdNacional.isSelected()){
			checkProdNacional.fire();
		}
	}

	@FXML
	private void selectPermite3D(){
		if(checkNaoPermite3D.isSelected()){
			checkNaoPermite3D.fire();
		}
	}

	@FXML
	private void selectNaoPermite3D(){
		if(checkPermite3D.isSelected()){
			checkPermite3D.fire();
		}
	}

}
