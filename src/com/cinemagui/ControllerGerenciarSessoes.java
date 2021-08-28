package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
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

import java.util.ResourceBundle;

import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class GerenciarSessoesController implements Initializable{
	
	//Inputs para coletar a hora e o valor do inresso.
	@FXML private TextField inputHora, inputMinuto, inputValorIngresso;	

	//ChoiceBoxes para coletar o filme e a sala.
	@FXML private ChoiceBox<Filme> choiceFilmes;
	@FXML private ChoiceBox<Sala> choiceSalas;

	//CheckBoxes para coletar o tipo de áudio e o tipo de exibição.
	@FXML private CheckBox checkAudioLegendado, checkAudioOriginal, checkAudioDublado, 
	@FXML private CheckBox checkExibicao3D, checkExibicao2D;

	//Botões para criar ou editar os filmes.
	@FXML private Button buttonEdit, buttonCreate;
	
	//Painel para visualizar as sessões e painel para criar/editar as sessões.
	@FXML private Pane paneViewSessoes, paneCreateSessao;

	//Tabela para visualizar as sessões.
	@FXML private TableView<Sessao> sessoesTable;

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
		System.out.println(location);
		System.out.println(resources);

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de filmes para aparecer na tabelas.
		sessoesTable.setItems(Cinema.getSessoes());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		sessoesTable.setPlaceholder(new Label("Nenhuma sessão existente."));

	}	
	
	@FXML
	private void openCreatePane() {

		//Para de renderizar o painel de visualização dos filmes.
		paneViewSessoes.setVisible(false);

		//Renderiza o painel de criação de filmes.
		paneCreateSessao.setVisible(true);

	}
	
	@FXML
	private void openEditSessao(){

		//Verificar se o usuário selecionou apenas 1 sessao.
		int countSelect=0;
		Sessao sessaoSelected=null;
		for(Sessao sessao : Cinema.getSessoes()){

			if(sessao.isSelected()){
				countSelect += 1;
				sessaoSelected = sessao;
			}
		}
		
		if(countSelect == 1) {

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
			switch(sessaoSelected.getTipoAudio()) {
				case "Legendado":
					checkAudioLegendado.setSelected(true);
				case "Dublado":
					checkAudioDublado.setSelected(true);
				case "Original":
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
			
		} else if(countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sala.");
			a.showAndWait();
		} else {
			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sala.");
			a.showAndWait();
		}
		
	}
	
	@FXML
	private void closeCreatingSessao(){

		//Limpa os inputs e os CheckBoxes		
		inputHora.setText("");
		inputMinuto.setText("");
		inputValorIngresso.setText("");
		checkAudioLegendado.setSelected(false);
		checkAudioDublado.setSelected(false);
		checkAudioOriginal.setSelected(false);
		checkExibicao3D.setSelected(false);
		checkExibicao2D.setSelected(false);

		//Para de renderizar o painel de visualização das salas.
		paneCreateSessao.setVisible(false);

		//Renderiza o painel de criação de salas.
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
			filme = choiceBoxFilmes.getValue();
			
			sala = choiceBoxSalas.getValue();
			
			horarioInicial = LocalTime.of(Integer.parseInt(inputHora.getText()), 
										  Integer.parseInt(inputMinuto.getText()));			  
										  
			horarioFinal = LocalTime.ofSecondOfDay(horarioInicial.toSecondOfDay() + 
												   choiceBoxFilmes.getValue().getDuracao() * 60);	
												   
			valorIngresso = Double.parseDouble(inputValorIngresso.getText());
			
			if (audioLegendado.isSelected()) {
				tipoAudio = "Legendado";
			} else if (audioDublado.isSelected()) {
				tipoAudio = "Dublado";
			}
			
			if(exib3D.isSelected()) {
				exibicao3D = true;
			}
			
			//Cria o sessão
			Sessao sessao = new Sessao(filme, sala, horarioInicial, horarioFinal, 
									   valorIngresso, exibicao3D, tipoAudio);
			
			//Adiciona o sessão na lista.
			Cinema.addSessao(sessao);

			//Fecha o painel de criação
			closeCreateSessao();
			
		}

	}

	@FXML
	private void editSessao() {
		
		if(verifyInputs() && verifyTime) {
			
			//Modifica os atributos da Sala selecionada.
			sessaoSelected.setFilme(choiceFilmes.getValue());
			
			sessaoSelected.setSala(choiceSalas.getValue());
			
			
			sessaoSelected.setHorarioInicial(LocalTime.of(Integer.parseInt(inputHora.getText()), 
														  Integer.parseInt(inputMinuto.getText())));			  
										  
			sessaoSelected.setHorarioFinal(LocalTime.ofSecondOfDay(sessaoSelected.getHorarioInicial().toSecondOfDay() + 
												                   choiceFilmes.getValue().getDuracao() * 60));	
												   
			sessaoSelected.setValorIngresso(Double.parseDouble(inputValorIngresso.getText()));
			
			if (audioLegendado.isSelected()) {
				sessaoSelected.setTipoAudio("Legendado");
			} else if (audioDublado.isSelected()) {
				sessaoSelected.setTipoAudio("Dublado");
			} else if (audioOriginal.isSelected()) {
				sessaoSelected.setTipoAudio("Original");
			}
			
			if(exib3D.isSelected()) {
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
		ArrayList<Sessao> oldSessoes;

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
		}
		
		//Verifica se os inputs estão vazios.
		if(inputHora.getText().isEmpty() || inputMinuto.getText().isEmpty() || 
			inputValorIngresso.getText().isEmpty() && valid) {
			valid = false;
		}

		//Verifica se os inputs estão vazios
		if(inputHora.getText().isEmpty() || inputMinuto.getText().isEmpty() || 
			inputValorIngresso.getText().isEmpty() && valid) {
			valid = false;
		}
		
		//Verifica se a hora pode ser convertida para inteiro e se o valor pode ser convertido para double.
		if (valid) {
			try {

				int hora = Integer.parseInt(inputHora.getText());
				int minuto = Integer.parseInt(inputMinuto.getText());
				double valor = Double.parseDouble(inputValorIngresso.getText());

			} catch (NumberFormatException e) {
				valid = false;
			}
		}
		
		/*
		 * Primeiro verifica se os checkboxes estão visiveis.
		 * Depois verifica se o usuário selecionou pelo menos 1.
		*/
		if((checkAudioLegendado.isVisible() || checkAudioDublado.isVisible() || checkAudioOriginal.isVisible()) &&
		  !(checkAudioLegendado.isSelected() || checkAudioDublado.isSelected() || checkAudioOriginal.isSelected()) && valid) {
			valid = false;
		}
		
		/*
		 * Primeiro verifica se os checkboxes estão visiveis.
		 * Depois verifica se o usuário selecionou pelo menos 1.
		*/
		if((checkExibicao3D.isVisible()) && !(checkExibicao3D.isSelected() || checkExibicao2D.isSelected()) && valid) {
			valid = false;
		}

		if(valid)
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
		if((horarioInicial.toSecondOfDay() + choiceBoxFilmes.getValue().getDuracao() * 60) > 86399){
			a = new Alert(AlertType.INFORMATION, "Não é possível criar sessões que só serão finalizadas depois das 23:59.");
			valid = false;
		} else {
			horarioFinal = LocalTime.ofSecondOfDay(horarioInicial.toSecondOfDay() + choiceBoxFilmes.getValue().getDuracao() * 60);
		}
		
		if(horarioInicial.toSecondOfDay() < LocalTime.now().toSecondOfDay()) {
            a = new Alert(AlertType.INFORMATION, "\nAinda não podemos voltar no tempo. Por favor, defina um horário depois das " + 
												 LocalTime.now());
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
				if(sessaoExistente.getSala() == sessao.getSala()) {
					cancel=true;
				}
			}
			if(sessaoExistente.getSala() == choiceBoxSalas.getValue() && !cancel) { 
			
				//Horário encontrado é igual ao horário que está sendo definido.
				if(inicioExistente - horarioInicial.toSecondOfDay() == 0){ 
					valid = false;
					break;
				//O horário que está sendo definido vai acontecer em um horário que já estará ocorrendo uma sessão.
				} else if(horarioInicial.toSecondOfDay() > inicioExistente && 
						  horarioInicial.toSecondOfDay() < finalExistente) { 
					valid = false;
					break;
				//O intervalo entre cada sessão precisa ser de 20 minutos.
				} else if(Math.abs(finalExistente - horarioInicial.toSecondOfDay()) < 1200 || 
				          Math.abs(inicioExistente - horarioFinal.toSecondOfDay()) < 1200) {
					valid = false;
					break;
				//O horário inicial da sessão que está sendo criada + o tempo do filme ultrapassam o horário inicial da próxima sessão.
				} else if (inicioExistente > horarioInicial.toSecondOfDay() && 
					       inicioExistente < horarioFinal.toSecondOfDay()){
					valid = false;
					break;
				}
			}
		}
		
		return valid;
	}
	
	
	private void factorys() {
		choiceBoxFilmes.setConverter(new StringConverter<Filme>() {
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
		
		ChangeListener<Filme> filmeListener = new ChangeListener<>() {
			@Override
			public void changed(ObservableValue<? extends Filme> observable, Filme oldValue, Filme newValue) {
				if(newValue.getPermite3D()) {
					exib3D.setVisible(true);
					exib2D.setVisible(true);
				} else {
					exib3D.setVisible(false);
					exib2D.setVisible(false);
					exib3D.setSelected(false);
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
		choiceBoxFilmes.getSelectionModel().selectedItemProperty().addListener(filmeListener);
		choiceBoxFilmes.getItems().addAll(Cinema.getFilmes());
		
		choiceBoxSalas.getItems().addAll(Cinema.getSalas());
		choiceBoxSalas.setConverter(new StringConverter<Sala>() {
			public String toString(Sala sala) {
				if(!(sala==null)){
					return Integer.toString(sala.getNumSala());
				} else {
					return "Clique aqui para selecionar uma sala...";
				}
			}
			@Override
			public Sala fromString(String string){
				Sala sala=null;
				for(Sala salaFound : Cinema.getSalas()) {
					if(salaFound.getNumSala() == sala.getNumSala())  {
						sala = salaFound;
						break;
					}
				}
				return sala;
			}
		});
		
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
					for(Filme filmeFound : Cinema.getFilmes()) {
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
					for(Sala salaFound : Cinema.getSalas()) {
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
	
	@FXML
	private void selectAudioDublado() {
		if(audioOriginal.isSelected()){
			audioOriginal.fire();
		}
		if(audioLegendado.isSelected()){
			audioLegendado.fire();
		}
	}
	
	@FXML
	private void selectAudioLegendado() {
		if(audioDublado.isSelected()){
			audioDublado.fire();
		}
		if(audioOriginal.isSelected()){
			audioOriginal.fire();
		}
	}
	
	@FXML
	private void selectAudioOriginal() {
		if(audioDublado.isSelected()){
			audioDublado.fire();
		}
		if(audioLegendado.isSelected()){
			audioLegendado.fire();
		}
	}

}
