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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class GerenciarSessoesController implements Initializable{
	
	@FXML
	private TextField inputHora, inputMinuto, inputValorIngresso;	
	
	@FXML
	private ChoiceBox<Filme> choiceBoxFilmes;
	@FXML
	private ChoiceBox<Sala> choiceBoxSalas;
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
		viewTableSessoes.setVisible(false);
		paneCreatingSessao.setVisible(true);
		buttonCreate.setVisible(true);
		buttonEdit.setVisible(false);
	}
	
	@FXML
	private void closeCreatingSessao(){
		viewTableSessoes.setVisible(true);
		paneCreatingSessao.setVisible(false);
		inputHora.setText("");
		inputMinuto.setText("");
		inputValorIngresso.setText("");
		audioLegendado.setSelected(false);
		audioOriginal.setSelected(false);
		audioDublado.setSelected(false);
		exib3D.setSelected(false);
		exib2D.setSelected(false);
	}
	
	@FXML
	private void deleteSessao(){
		ObservableList<Sessao> oldSessoes = FXCollections.observableArrayList();
		for (Sessao sessao : CinemaUtil.getSessoes()) {
			if(sessao.getCheckBox().isSelected()) {
				oldSessoes.add(sessao);
			}
		}
		CinemaUtil.getSessoes().removeAll(oldSessoes);
		updateTable();
	}
	
	@FXML
	private void editSessao(){
		
	}

	@FXML
	private void createSessao() {
		Alert a;
		Filme filme;
		Sala sala;
		LocalTime horarioInicial;
		LocalTime horarioFinal;
		double valorIngresso;
		boolean exibicao3D;
		String tipoAudio="";
		
		
		if(verifyInputs() && verifyTime(true, null)) {
			
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
			} else if (audioOriginal.isSelected()) {
				tipoAudio = "Original";
			}
			
			if(exib3D.isSelected()) {
				exibicao3D = true;
			} else {
				exibicao3D = false;
			}
			
			Sessao sessao = new Sessao(filme, sala, horarioInicial, horarioFinal, 
									   valorIngresso, exibicao3D, tipoAudio);
			
			CinemaUtil.saveData(sessao);
			updateTable();
			closeCreatingSessao();
			
		} else {
			String verif1 = "Por favor, verifique se: \n" +
							"1 - Nenhum campo ficou vazio ou desmarcado; \n" +
							"2 - Você digitou apenas números nos campos 'valor do ingresso', 'hora', 'minuto'.";
			a = new Alert(AlertType.INFORMATION, verif1);
			a.showAndWait();
		}
	}
	
	private boolean verifyTime(boolean criando, Sessao sessao) {
		Alert a;
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
		/*
		if(horarioInicial.toSecondOfDay() < LocalTime.now().toSecondOfDay()) {
            a = new Alert(AlertType.INFORMATION, "\nAinda não podemos voltar no tempo. Por favor, defina um horário depois das " + 
												 LocalTime.now());
            valid = false;
        }*/

		//Verifica o horário que está sendo criado com todos os horários já definidos.
		for (Sessao sessaoExistente : CinemaUtil.getSessoes()) {
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
	
	private boolean verifyInputs() {
		boolean validInputs=true;
		
		//Nenhum filme ou sala foi escolhido
		if(choiceBoxFilmes.getValue() == null || choiceBoxSalas.getValue() == null) {
			validInputs = false;
		}
		
		//inputs estão vazios
		if(inputHora.getText().isEmpty() || inputMinuto.getText().isEmpty() || inputValorIngresso.getText().isEmpty()) {
			validInputs = false;
		}
		
		//Verifica se os inputs são, de fato, int ou double.
		try {
			int hora = Integer.parseInt(inputHora.getText());
			int minuto = Integer.parseInt(inputMinuto.getText());
			double valor = Double.parseDouble(inputValorIngresso.getText());
		} catch (NumberFormatException e) {
			validInputs = false;
		}
		
		//Checkboxes disponíveis.
		if((audioLegendado.isVisible() || audioDublado.isVisible() || audioOriginal.isVisible()) &&
		  //Uusário não selecionou nenhuma opção.
		  !(audioLegendado.isSelected() || audioDublado.isSelected() || audioOriginal.isSelected())) {
			validInputs = false;
		}
		
		//Checkboxes disponíveis.
		if((exib3D.isVisible() || exib3D.isVisible()) && 
		  //Uusário não selecionou nenhuma opção.
		  !(exib3D.isSelected() || exib3D.isSelected())) {
			validInputs = false;
		}
		
		//inputs estão vazios
		if(inputHora.getText().isEmpty() || inputMinuto.getText().isEmpty() || inputValorIngresso.getText().isEmpty()) {
			validInputs = false;
		}
		
		return validInputs;
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
		choiceBoxFilmes.getSelectionModel().selectedItemProperty().addListener(filmeListener);
		choiceBoxFilmes.getItems().addAll(CinemaUtil.getFilmes());
		
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
