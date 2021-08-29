package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ResourceBundle;
import java.util.ArrayList;

import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class ControllerGerenciarSessoes implements Initializable{
	
	//Inputs para coletar a hora e o valor do inresso.
	@FXML private TextField inputHora, inputMinuto, inputValorIngresso;	

	//ChoiceBoxes para coletar o filme e a sala.
	@FXML private ChoiceBox<Filme> choiceFilmes;
	@FXML private ChoiceBox<Sala> choiceSalas;

	//CheckBoxes para coletar o tipo de áudio e o tipo de exibição.
	@FXML private CheckBox checkAudioLegendado, checkAudioOriginal, checkAudioDublado;
	@FXML private CheckBox checkExibicao3D, checkExibicao2D;

	//Botões para criar ou editar os filmes.
	@FXML private Button buttonEdit, buttonCreate;
	
	//Painel para visualizar as sessões e painel para criar/editar as sessões.
	@FXML private Pane paneViewSessoes, paneCreateSessao;

	//Tabela para visualizar as sessões.
	@FXML private TableView<Sessao> tableSessoes;

	//Colunas da tabela
	@FXML private TableColumn<Sessao, Boolean> colSelect;
	@FXML private TableColumn<Sessao, Filme> colFilme;
	@FXML private TableColumn<Sessao, Sala> colSala;
	@FXML private TableColumn<Sessao, LocalTime> colHorarioInicial, colHorarioFinal;
	@FXML private TableColumn<Sessao, String> colTipoAudio;
	@FXML private TableColumn<Sessao, Double> colValorIngresso;
	@FXML private TableColumn<Sessao, Boolean> colTipoExibicao;

	//Variável para armazenar a sessão que será editado
	private Sessao sessaoSelected = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de sessões para aparecer na tabelas.
		tableSessoes.setItems(Cinema.getSessoes());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		tableSessoes.setPlaceholder(new Label("Nenhuma sessão existente."));

		//Atualiza as sessões.
		Cinema.updateSessaoList();

	}	
	
	@FXML
	private void openCreatePane() {

		//Para de renderizar o painel de visualização das sessões.
		paneViewSessoes.setVisible(false);

		//Renderiza o painel de criação das sessões.
		paneCreateSessao.setVisible(true);

	}
	
	@FXML
	private void openEditPane(){

		//Verificar se o usuário selecionou apenas 1 sessao.
		int countSelect=0;
		for(Sessao sessao : Cinema.getSessoes()){

			if(sessao.isSelected()){
				countSelect += 1;
				sessaoSelected = sessao;
			}
		}
		
		if(countSelect == 1 && sessaoSelected.getTaxaOcupacao() == 0.0) {

			//Para de renderizar o botão de criar sessão (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sessão (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Abre o painel de criar filme.
			openCreatePane();

			//Preenche os inputs com as informações da sessão que o usuário escolheu.
			String[] horario = sessaoSelected.getHorarioInicial().toString().split(":");
			inputHora.setText(horario[0]);
			inputMinuto.setText(horario[1]);
			inputValorIngresso.setText(Double.toString(sessaoSelected.getValorIngresso()));

			//Preenche os ChoiceBoxes com as informações da sessão que o usuário escolheu.
			choiceFilmes.setValue(sessaoSelected.getFilme());
			choiceSalas.setValue(sessaoSelected.getSala());
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			if(sessaoSelected.getTipoAudio().equals("Dublado")) {
				checkAudioDublado.setSelected(true);
			} else if(sessaoSelected.getTipoAudio().equals("Legendado")) {
				checkAudioLegendado.setSelected(true);
			} else {
				checkAudioOriginal.setSelected(true);				
			}
			
			//Preenche os CheckBoxes com as informações do filme que o usuário escolheu.
			if(sessaoSelected.getFilme().getPermite3D()){
				checkExibicao3D.setVisible(true);
				checkExibicao2D.setVisible(true);

				if(sessaoSelected.getExibicao3D()) {
					checkExibicao3D.setSelected(true);
					checkExibicao2D.setSelected(false);
				} else {
					checkExibicao3D.setSelected(true);
					checkExibicao2D.setSelected(false);
				}
			}
			
		} else if (countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sala.");
			a.showAndWait();
		} else if (countSelect > 1){
			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sala.");
			a.showAndWait();
		} else {
			Alert a = new Alert(AlertType.INFORMATION, "Você já vendeu ingressos para essa sessão, então\nnão pode modificá-la.");
			a.showAndWait();
		}
		
	}
	
	@FXML
	private void closeCreatePane(){

		//Limpa os inputs e os CheckBoxes
		inputHora.setText("");
		inputMinuto.setText("");
		inputValorIngresso.setText("");
		checkAudioLegendado.setSelected(false);
		checkAudioDublado.setSelected(false);
		checkAudioOriginal.setSelected(false);
		checkExibicao3D.setSelected(false);
		checkExibicao2D.setSelected(false);

		//Para de renderizar o painel de criação das sessões.
		paneCreateSessao.setVisible(false);

		//Renderiza o painel de visualização das sessões.		
		paneViewSessoes.setVisible(true);
		
	}

	@FXML
	private void createSessao() {

		//Variáveis temporárias
		Filme filme;
		Sala sala;
		LocalTime horarioInicial;
		LocalTime horarioFinal;
		double valorIngresso;
		boolean exibicao3D = false;
		String tipoAudio = "Original";
		
		
		if(verifyInputs() && verifyTime(true, null)) {
			
			//Coleta os dados e armazena em variáveis temporárias
			filme = choiceFilmes.getValue();
			
			sala = choiceSalas.getValue();
			
			horarioInicial = LocalTime.of(Integer.parseInt(inputHora.getText()), 
										  Integer.parseInt(inputMinuto.getText()));			  
										  
			horarioFinal = LocalTime.ofSecondOfDay(horarioInicial.toSecondOfDay() + 
												   choiceFilmes.getValue().getDuracao() * 60);	
												   
			valorIngresso = Double.parseDouble(inputValorIngresso.getText());
			
			if (checkAudioLegendado.isSelected()) {
				tipoAudio = "Legendado";
			} else if (checkAudioDublado.isSelected()) {
				tipoAudio = "Dublado";
			}
			
			if(checkExibicao3D.isSelected()) {
				exibicao3D = true;
				valorIngresso *= 1.25;
			}
			
			//Cria o sessão
			Sessao sessao = new Sessao(filme, sala, horarioInicial, horarioFinal, 
									   valorIngresso, exibicao3D, tipoAudio);
			
			//Adiciona o sessão na lista.
			Cinema.addSessao(sessao);

			//Fecha o painel de criação
			closeCreatePane();
			
		}

	}

	@FXML
	private void editSessao() {
		
		if(verifyInputs() && verifyTime(false, sessaoSelected)) {
			
			//Modifica os atributos da Sala selecionada.
			sessaoSelected.setFilme(choiceFilmes.getValue());
			
			sessaoSelected.setSala(choiceSalas.getValue());
			
			
			sessaoSelected.setHorarioInicial(LocalTime.of(Integer.parseInt(inputHora.getText()), 
														  Integer.parseInt(inputMinuto.getText())));			  
										  
			sessaoSelected.setHorarioFinal(LocalTime.ofSecondOfDay(sessaoSelected.getHorarioInicial().toSecondOfDay() + 
												                   choiceFilmes.getValue().getDuracao() * 60));	
												   
			sessaoSelected.setValorIngresso(Double.parseDouble(inputValorIngresso.getText()));
			
			if (checkAudioLegendado.isSelected()) {
				sessaoSelected.setTipoAudio("Legendado");

			} else if (checkAudioDublado.isSelected()) {
				sessaoSelected.setTipoAudio("Dublado");

			} else if (checkAudioOriginal.isSelected()) {
				sessaoSelected.setTipoAudio("Original");
			}
			
			if(checkExibicao3D.isSelected()) {
				sessaoSelected.setExibicao3D(true);
			} else {
				sessaoSelected.setExibicao3D(false);
			}
			
			//Para de renderizar o botão de criar sessão (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sessão (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Fecha o painel de criação
			closeCreatePane();
			
		}
	}
	
	@FXML
	private void deleteSessao(){

		//Lista temporária para armazenar as sessões que serão removidas.
		ArrayList<Sessao> oldSessoes = new ArrayList<>();

		for (Sessao sessao : Cinema.getSessoes()) {

			if(sessao.isSelected()) {
				oldSessoes.add(sessao);
			}
		}

		Cinema.removeSessoes(oldSessoes);
	}

	private boolean verifyInputs() {

		/*
		 * Flag que será retornada.
		 * Essa flag também será usada para evitar de fazer uma verificação quando outra já a invalidou.
		*/
		boolean valid = true;
		
		//Verifica se o usuário selecionou um filme e uma sala.
		if(choiceFilmes.getValue() == null || choiceSalas.getValue() == null) {
			valid = false;

			Alert a = new Alert(AlertType.INFORMATION, "Selecione um filme e uma sala.");
			a.showAndWait();
		}
		
		//Verifica se os inputs estão vazios.
		if((inputHora.getText().isEmpty() || inputMinuto.getText().isEmpty() || 
			inputValorIngresso.getText().isEmpty()) && valid) {
			valid = false;

			Alert a = new Alert(AlertType.INFORMATION, "Digite o horário e o valor do ingresso.");
			a.showAndWait();
		}
		
		//Verifica se a hora pode ser convertida para inteiro e se o valor pode ser convertido para double.
		if (valid) {
			try {

				int hora = Integer.parseInt(inputHora.getText());
				int minuto = Integer.parseInt(inputMinuto.getText());
				double valor = Double.parseDouble(inputValorIngresso.getText());

			} catch (NumberFormatException e) {
				valid = false;

				Alert a = new Alert(AlertType.INFORMATION, "O horário ou o valor do ingresso é inválido.");
				a.showAndWait();
			}
		}
		
		/*
		 * Primeiro verifica se os checkboxes estão visiveis.
		 * Depois verifica se o usuário selecionou pelo menos 1.
		*/
		if(((checkAudioLegendado.isVisible() || checkAudioDublado.isVisible() || checkAudioOriginal.isVisible()) &&
		  !(checkAudioLegendado.isSelected() || checkAudioDublado.isSelected() || checkAudioOriginal.isSelected())) && valid) {
			valid = false;

			Alert a = new Alert(AlertType.INFORMATION, "Selecione o tipo de áudio.");
			a.showAndWait();
		}
		
		/*
		 * Primeiro verifica se os checkboxes estão visiveis.
		 * Depois verifica se o usuário selecionou pelo menos 1.
		*/
		if(((checkExibicao3D.isVisible()) && !(checkExibicao3D.isSelected() || checkExibicao2D.isSelected())) && valid) {
			valid = false;
			Alert a = new Alert(AlertType.INFORMATION, "Selecione o tipo de exibição.");
			a.showAndWait();
		}

		
		return valid;
	}	
	
	private boolean verifyTime(boolean criando, Sessao sessao) {

		//Flag que será retornada.
		//Essa flag também será usada para evitar de fazer uma verificação quando outra já a invalidou.
		boolean valid=true;
		
		//Esse recurso também é utilizado para modificar sessão.
		boolean modificando = false;
		if(!criando) {
			modificando = true;
		}
		
		int hora = Integer.parseInt(inputHora.getText());
		int minuto = Integer.parseInt(inputMinuto.getText());		
		
		LocalTime horarioInicial = LocalTime.of(hora, minuto, 0, 0);
		LocalTime horarioFinal = null;
		if((horarioInicial.toSecondOfDay() + choiceFilmes.getValue().getDuracao() * 60) > 86399){
			Alert a = new Alert(AlertType.INFORMATION, "Não é possível criar sessões que só serão\nfinalizadas depois das 23:59.");
			a.showAndWait();
			valid = false;
		} else {
			horarioFinal = LocalTime.ofSecondOfDay(horarioInicial.toSecondOfDay() + choiceFilmes.getValue().getDuracao() * 60);
		}
		
		if(horarioInicial.toSecondOfDay() < LocalTime.now().toSecondOfDay()) {
            Alert a = new Alert(AlertType.INFORMATION, "Ainda não podemos voltar no tempo. Por favor, \ndefina um horário depois das " + 
												 LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
            a.showAndWait();
            valid = false;
        }

		//Verifica o horário que está sendo criado com todos os horários já definidos.
		for (Sessao sessaoExistente : Cinema.getSessoes()) {
			//Se as verificações anteriores acharem um problema, o loop é desnecessário.
			if(!valid) {
				break;
			}
			//Horário que a sessão encontrada começa.
			int inicioExistente = sessaoExistente.getHorarioInicial().toSecondOfDay();
			//Horário que a sessão encontrada acaba.
			int finalExistente = sessaoExistente.getHorarioFinal().toSecondOfDay();
			
			
			/*
			   1 - Verifica se a sessão encontrada ocorre na mesma sala da sessão que está 
			       sendo criada.
			   2 - Esse recurso também é usado para editar sessões. Por isso, é necessário a 
			       verificação do cancel, ela garente que o programa não vai verificar a sala
			       que está sendo modificada com ela mesma.
			*/
			boolean cancel=false;
			if(modificando){
				if(sessaoExistente.getSala() == sessaoSelected.getSala()) {
					cancel=true;
				}
			}
			if(sessaoExistente.getSala() == choiceSalas.getValue() && !cancel) { 
			
				//Horário encontrado é igual ao horário que está sendo definido.
				if(inicioExistente - horarioInicial.toSecondOfDay() == 0){ 
					valid = false;
					Alert a = new Alert(AlertType.INFORMATION, "Já existe uma sessão que ocorre nesse horário.");
					a.showAndWait();
					break;
				//O horário que está sendo definido vai acontecer em um horário que já estará ocorrendo uma sessão.
				} else if(horarioInicial.toSecondOfDay() > inicioExistente && 
						  horarioInicial.toSecondOfDay() < finalExistente) { 
					valid = false;
					Alert a = new Alert(AlertType.INFORMATION, "Uma sessão irá ocorrer nesse horário.");
					a.showAndWait();
					break;
				//O intervalo entre cada sessão precisa ser de 20 minutos.
				} else if(Math.abs(finalExistente - horarioInicial.toSecondOfDay()) < 1200 || 
				          Math.abs(inicioExistente - horarioFinal.toSecondOfDay()) < 1200) {
					valid = false;
					Alert a = new Alert(AlertType.INFORMATION, "O intervalo entre cada sessão precisa ser de 20 minutos.");
					a.showAndWait();
					break;
				//O horário inicial da sessão que está sendo criada + o tempo do filme ultrapassam o horário inicial da próxima sessão.
				} else if (inicioExistente > horarioInicial.toSecondOfDay() && 
					       inicioExistente < horarioFinal.toSecondOfDay()){
					valid = false;
					Alert a = new Alert(AlertType.INFORMATION, "Essa sessão não vai acabar antes do início da próxima.");
					a.showAndWait();
					break;
				}
			}
		}
		
		return valid;
	}
	
	
	private void factorys() {

		/*
		 * O "CellValueFactory" é a fabrica que coleta as propriedades dos objetos da lista
		 * que foi definida para "tableSessoes".
		 *
		 * O "CellFactory" é a fábrica que define como que os valores coletados na fábrica
		 * anterior serão renderizados na tabela.
		*/

		//Adiciona um conversor de Filme para String na ChoiceBox de filmes.
		choiceFilmes.setConverter(new StringConverter<Filme>() {
			
			@Override
			public String toString(Filme filme) {

				if(!(filme==null)){
					return filme.getTitulo();
				} else {
					return "Clique aqui para selecionar um filme...";
				}

			}

			@Override
			public Filme fromString(String string){
				Filme filme=null;
				for(Filme filmeFound : Cinema.getFilmes()) {

					if(filmeFound.getTitulo() == filme.getTitulo())  {
						filme = filmeFound;
						break;
					}

				}

				return filme;
			}
		});
		
		//Cria um "ouvinte de mudanças" para um lista de filmes.
		ChangeListener<Filme> filmeListener = new ChangeListener<>() {
			
			@Override
			public void changed(ObservableValue<? extends Filme> observable, Filme oldValue, Filme newValue) {

				//Verifica se o filme permite exibição 3D.
				if(newValue.getPermite3D()) {
					checkExibicao3D.setVisible(true);
					checkExibicao2D.setVisible(true);
					checkExibicao3D.setSelected(false);
					checkExibicao2D.setSelected(false);
				} else {
					checkExibicao3D.setVisible(false);
					checkExibicao2D.setVisible(false);
					checkExibicao3D.setSelected(false);
					checkExibicao2D.setSelected(true);
				}
				
				//Verifica quais tipos de áudio estão disponíveis para o filme.
				for (String string : newValue.getTipoAudio()) {

					if(string.equals("Dublado")) {
						checkAudioDublado.setVisible(true);
					}
					if(string.equals("Legendado")) {
						checkAudioLegendado.setVisible(true);
					}
					if(string.equals("Original")) {
						checkAudioOriginal.setVisible(true);
					}

				}

			}

		};

		//Adiciona um "ouvinte de mudanças" na ChoiceBox de filmes.
		choiceFilmes.getSelectionModel().selectedItemProperty().addListener(filmeListener);

		//Adiciona a lista de filmes na ChoiceBox de filmes.
		choiceFilmes.setItems(Cinema.getFilmes());

		//Adiciona um conversor de Sala para String na ChoiceBox de salas.
		choiceSalas.setConverter(new StringConverter<Sala>() {

			@Override
			public String toString(Sala sala) {

				if (sala != null) {
					return Integer.toString(sala.getNumSala());
				} else {
					return "Clique aqui para selecionar uma sala...";
				}
			}
			
			@Override
			public Sala fromString(String string){
				Sala sala=null;
				for (Sala salaFound : Cinema.getSalas()) {

					if (salaFound.getNumSala() == Integer.parseInt(string)) {
						sala = salaFound;
						break;
					}
				}

				return sala;
			}
		});

		//Adiciona a lista de salas na ChoiceBox de salas.
		choiceSalas.getItems().addAll(Cinema.getSalas());
		
		//Chama o método "selectedProperty()" para cada Sessao na lista.
		colSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));

		/*
		 * Renderiza uma CheckBox na célula. Quando o valor da propridedade for true, a
		 * CheckBox será renderizada como selecionada marcada.
		*/
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
		
		//Chama o método "filmeProperty()" para cada Sessao na lista.
		colFilme.setCellValueFactory(new PropertyValueFactory<>("filme"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colFilme.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Filme>() {

				@Override
				public String toString(Filme filme) {
					return filme.getTitulo();
				}
				
				@Override
				//Esse método não será utilizado.
				public Filme fromString(String string) {
					Filme filme=null;
					for(Filme filmeFound : Cinema.getFilmes()) {

						if(filmeFound.getTitulo() == string){
							filme = filmeFound;
							break;
						}
					}

					return filme;
				}
			}));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colFilme.setEditable(false);
	
		//Chama o método "salaProperty()" para cada Sessao na lista.
		colSala.setCellValueFactory(new PropertyValueFactory<>("sala"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colSala.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Sala>() {

				@Override
				public String toString(Sala sala) {
					return Integer.toString(sala.getNumSala());
				}

				
				@Override
				//Esse método não será utilizado.
				public Sala fromString(String string) {
					Sala sala=null;
					for(Sala salaFound : Cinema.getSalas()) {

						if(Integer.toString(salaFound.getNumSala()) == string){
							sala = salaFound;
							break;
						}
					}

					return sala;
				}
			}));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colSala.setEditable(false);
	
		//Chama o método "horarioInicialProperty()" para cada Sessao na lista.
		colHorarioInicial.setCellValueFactory(new PropertyValueFactory<>("horarioInicial"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colHorarioInicial.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colHorarioInicial.setEditable(false);

		//Chama o método "horarioFinalProperty()" para cada Sessao na lista.
		colHorarioFinal.setCellValueFactory(new PropertyValueFactory<>("horarioFinal"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colHorarioFinal.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colHorarioFinal.setEditable(false);

		//Chama o método "tipoAudioProperty()" para cada Sessao na lista.
		colTipoAudio.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTipoAudio.setEditable(false);

		//Chama o método "exibicao3DProperty()" para cada Sessao na lista.
		colTipoExibicao.setCellValueFactory(new PropertyValueFactory<>("exibicao3D"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colTipoExibicao.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Boolean>() {

				@Override
				public String toString(Boolean perm3d) {
					if(perm3d){
						return "3D";
					} else {
						return "2D";
					}
				}

				
				@Override
				////Esse método não será utilizado.
				public Boolean fromString(String string){
					if(string.equals("3D")){
						return true;
					} else {
						return false;
					}
				}
			
			}));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTipoExibicao.setEditable(false);
	
		//Chama o método "valorIngressoProperty()" para cada Sessao na lista.
		colValorIngresso.setCellValueFactory(new PropertyValueFactory<>("valorIngresso"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colValorIngresso.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colValorIngresso.setEditable(false);
	}
	
	/*
	 * Para o tipo de exibição e o tipo de produção o usuário só pode selecionar
	 * uma das CheckBoxes disponíveis. Os métodos abaixo servem para garantir isso.
	*/

	@FXML
	private void selectExibicao3D(){
		
		if(checkExibicao2D.isSelected()){
			checkExibicao2D.fire();
		}
	}

	@FXML
	private void selectExibicao2D(){

		if(checkExibicao3D.isSelected()){
			checkExibicao3D.fire();
		}
	}
	
	@FXML
	private void selectAudioDublado() {

		if(checkAudioOriginal.isSelected()){
			checkAudioOriginal.fire();
		}

		if(checkAudioLegendado.isSelected()){
			checkAudioLegendado.fire();
		}
	}
	
	@FXML
	private void selectAudioLegendado() {

		if(checkAudioDublado.isSelected()){
			checkAudioDublado.fire();
		}

		if(checkAudioOriginal.isSelected()){
			checkAudioOriginal.fire();
		}
	}
	
	@FXML
	private void selectAudioOriginal() {

		if(checkAudioDublado.isSelected()){
			checkAudioDublado.fire();
		}

		if(checkAudioLegendado.isSelected()){
			checkAudioLegendado.fire();
		}
	}

}
