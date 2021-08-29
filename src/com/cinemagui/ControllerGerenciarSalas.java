package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.ResourceBundle;

import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class ControllerGerenciarSalas implements Initializable{
	
	//Inputs para coletar o número da sala e a capacidade.
	@FXML private TextField inputNumSala, inputCapacidade;
	
	//Botões para criar ou editar as salas.
	@FXML private Button buttonCreate, buttonEdit;
	
	//Painel para visualizar as salas e painel para criar/editar as salas.
	@FXML private Pane paneViewSalas, paneCreateSala;
	
	//Tabela para visualizar as salas.
	@FXML private TableView<Sala> tableSalas;
	
	//Colunas da tabela
	@FXML private TableColumn<Sala, Boolean> colSelect;
	@FXML private TableColumn<Sala, Integer> colNumSala, colCapacidade;	
	
	//Sala selecionada para edição.
	private Sala salaSelected = null;

	@Override
	//Location retorna o path para o arquivo fxml
	public void initialize(URL location, ResourceBundle resources) {

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de salas para aparecer na tabelas.
		tableSalas.setItems(Cinema.getSalas());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		tableSalas.setPlaceholder(new Label("Nenhuma sala existente."));

		//Atualiza as sessões.
		Cinema.updateSessaoList();

	}	

	@FXML
	private void openCreatePane() {

		//Para de renderizar o painel de visualização das salas.
		paneViewSalas.setVisible(false);

		//Renderiza o painel de criação de salas.
		paneCreateSala.setVisible(true);

	}

	@FXML
	private void openEditPane() {

		//Verificar se o usuário selecionou apenas 1 sala.
		int countSelect=0;
		for(Sala sala : Cinema.getSalas()) {

			if (sala.isSelected()) {
				countSelect += 1;
				salaSelected = sala;
			}
		}

		//Flag pra indicar se o usuário pode editar o filme.
		boolean valid = true;
		for(Sessao sessao : Cinema.getSessoes()) {
			if(sessao.getSala() == salaSelected) {
				valid = false;
				break;
			}
		}
		
		if(countSelect == 1 && valid) {

			//Para de renderizar o botão de criar sala (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sala (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Abre o painel de criar sala.
			openCreatePane();
			
			//Preenche os inputs com as informações da sala que o usuário escolheu.
			inputNumSala.setText(Integer.toString(salaSelected.getNumSala()));
			inputCapacidade.setText(Integer.toString(salaSelected.getCapacidade()));
			
		} else if(countSelect == 0){

			//Cria um pop-up
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sala.");
			a.showAndWait();

		} else if(!valid) {

			Alert a = new Alert(AlertType.INFORMATION, "Uma sessão será reproduzida nesta sala, você não\npode modificá-la.");
			a.showAndWait();

		} else {

			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sala.");
			a.showAndWait();

		}
	}

	@FXML
	private void closeCreatePane() {

		//Limpa os inputs
		inputNumSala.setText("");
		inputCapacidade.setText("");

		//Para de renderizar o painel de criação de salas.
		paneCreateSala.setVisible(false);
		

		//Renderiza o painel de visualização das salas.
		paneViewSalas.setVisible(true);

	}

	@FXML
	private void createSala() {

		//Variáveis temporárias
		int numSala, capacidade;

		//Flag de verificação.
		boolean salaExiste=false;

		//Sala selected só deve ser usada para edição.
		salaSelected = null;

		if(verifyInputs()) {

			//Cria a sala
			Sala sala = new Sala(Integer.parseInt(inputNumSala.getText()),
						 		 Integer.parseInt(inputCapacidade.getText()));

			//Adiciona a sala na lista.
			Cinema.addSala(sala);

			//Fecha o painel de criação
			closeCreatePane();
		}

	}

	@FXML
	private void editSala()  {		

		if (verifyInputs()) {

			//Modifica os atributos da Sala selecionada.
			salaSelected.setNumSala(Integer.parseInt(inputNumSala.getText()));
			salaSelected.setCapacidade(Integer.parseInt(inputCapacidade.getText()));

			//Para de renderizar o botão de criar sala (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sala (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Fecha o painel de criação
			closeCreatePane();

		}
				
	}

	@FXML
	private void deleteSalas() {

		//Lista temporária para armazenar as salas e as sessões que serão removidas.
		ArrayList<Sala> oldSalas = new ArrayList<>();
		ArrayList<Sessao> oldSessoes = new ArrayList<>();

		//Flag para cancelar a exclusão.
		final SimpleBooleanProperty cancel = new SimpleBooleanProperty(false);

		for (Sala sala : Cinema.getSalas()) {
			if (sala.isSelected()) {

				//Verifica se existem sessões que acontecem nesta sala.
				for(Sessao sessao : Cinema.getSessoes()) {

					if (sessao.getSala() == sala) {
						Alert a = new Alert(AlertType.CONFIRMATION);
						Text t = new Text("A sessão " + sessao.getFilme().getTitulo() + " (" + sessao.getHorarioInicial() + 
								 		  ")" + " acontece na sala " + sala.getNumSala() + "." + " Se continuar, a sessão será apagada.");
						
						a.getDialogPane().setContent(t); 

						//Verifica se o usuário ainda deseja excluir.
						a.showAndWait().ifPresent(response -> {

							if(!(response == ButtonType.OK)) {
								cancel.set(true);
							}
						});

						if (cancel.get()) {
							break;
						}

						oldSessoes.add(sessao);

					}

				}
				
				if (cancel.get()) {
					break;
				} 	

				oldSalas.add(sala);

			}

		}

		if(!cancel.get()) {
			Cinema.removeSessoes(oldSessoes);
			Cinema.removeSalas(oldSalas);
		}

	}

	private boolean verifyInputs() {

		//Flag que será retornada.
		boolean valid = true;

		try {

			//Coleta o texto do input e converte em número.
			int numSala = Integer.parseInt(inputNumSala.getText());
			int capacidade = Integer.parseInt(inputCapacidade.getText());
			
			//Verifica se já existe uma sala com esse número.
			for (Sala sala : Cinema.getSalas()) {

				//A segunda verificação impede que a comparação seja feita com a própria sala.
				if (sala.getNumSala() == numSala && sala != salaSelected) {

					Alert a = new Alert(AlertType.INFORMATION, "Sala já existente, defina outra!");
					a.showAndWait();

					valid = false;
					break;

				}

			}

		} catch (Exception e) {

			Alert a = new Alert(AlertType.INFORMATION, "Verifique se você preencheu todos os campos \ncom valores numéricos.");
			a.showAndWait();

			valid = false;

		}

		return valid;

	}

	private void factorys() {

		/*
		 * O "CellValueFactory" é a fabrica que coleta as propriedades dos objetos da lista
		 * que foi definida para "tableSalas".
		 *
		 * O "CellFactory" é a fábrica que define como que os valores coletados na fábrica
		 * anterior serão renderizados na tabela.
		*/

		//Chama o método "selectedProperty()" para cada Sala na lista.
		colSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));

		/*
		 * Renderiza uma CheckBox na célula. Quando o valor da propridedade for true, a
		 * CheckBox será renderizada como selecionada marcada.
		*/
		colSelect.setCellFactory(CheckBoxTableCell.forTableColumn(colSelect));
		
		//Chama o método "numSalaProperty()" para cada Sala na lista.
		colNumSala.setCellValueFactory(new PropertyValueFactory<>("numSala"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colNumSala.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colNumSala.setEditable(false);
	
		//Chama o método "capacidadeProperty()" para cada Sala na lista.
		colCapacidade.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colCapacidade.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colCapacidade.setEditable(false);

	}

}
