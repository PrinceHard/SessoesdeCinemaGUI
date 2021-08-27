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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.util.ResourceBundle;
import java.net.URL;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class ControllerGerenciarSalas implements Initializable{
	
	//Inputs para coletar o número da sala e a capacidade.
	@FXML private TextField inputNumSala, inputCapacity;
	
	//Botões para criar ou editar as salas.
	@FXML private Button buttonCreate, buttonEdit;
	
	//Painel para visualizar as salas e painel para criar/editar as salas.
	@FXML private Pane paneViewSalas, paneCreateSala;
	
	//Tabela para visualizar as salas.
	@FXML private TableView<Sala> tableSalas;
	
	//Colunas da tabela
	@FXML private TableColumn<Sala, Integer> colNumSala, colCapacity;
	@FXML private TableColumn<Sala, Boolean> colSelect;
	
	//Sala selecionada para edição.
	private Sala salaSelected=null;

	@Override

	public void initialize(URL location, ResourceBundle resources) {
		System.out.println(location);
		System.out.println(resources);

		//Inicializa as fábricas de células.
		factorys();

		//Define a lista de salas para aparecer na tabelas.
		tableSalas.setItems(Cinema.getSalas());

		//Caso a lista esteja vazia, a mensagem abaixo irá aparecer.
		tableSalas.setPlaceholder(new Label("Nenhuma sala existente."));

	}	

	@FXML
	private void openCreatePane() {

		//Para de renderizar o painel de visualização das salas.
		paneViewSalas.setVisible(false);

		//Renderiza o painel de criação de salas.
		paneCreateSala.setVisible(true);

	}

	@FXML
	private void openEditSala() {

		//Verificar se o usuário selecionou apenas 1 sala.
		int countSelect=0;
		Sala salaSelected=null;
		for(Sala sala : Cinema.getSalas()) {

			if (sala.isSelected()) {

				countSelect += 1;
				salaSelected = sala;

			}

		}
		
		if(countSelect == 1) {

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

		//Renderiza o painel de criação de salas.
		paneCreateSala.setVisible(false);
		

		//Para de renderizar o painel de visualização das salas.
		paneViewSalas.setVisible(true);

	}

	@FXML
	private void createSala() {

		//Variáveis temporárias
		int numSala, capacidade;

		//Flag de verificação.
		boolean salaExiste=false;

		if(verifyInputs()) {

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

		//Variáveis temporárias
		int numSala=0, capacidade=0;		

		if (verifyInputs()) {

			//Modifica os atributos da Sala selecionada.
			salaSelected.setNumSala(numSala);
			salaSelected.setCapacidade(capacidade);

			//Para de renderizar o botão de criar sala (botão do painel de criação).
			buttonCreate.setVisible(false);

			//Renderiza o botão de editar sala (botão do painel de criação).
			buttonEdit.setVisible(true);

			//Fecha o painel de criação
			closeCreatingSala();

		}
				
	}

	private boolean verifyInputs() {

		//Flag que será retornada.
		boolean valid = true;

		try {

			//Coleta o texto do input e converte em número.
			numSala = Integer.parseInt(inputNumSala.getText());
			capacidade = Integer.parseInt(inputCapacidade.getText());
			
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

			valid = false;

		}

		return valid;

	}
	
	@FXML
	private void deleteSalas() {

		//Lista temporária para armazenar as salas que serão removidas.
		ArrayList<Sala> oldSalas;
		ArrayList<Sessao> oldSessoes;

		//Flag para cancelar a exclusão.
		boolean cancel = false;

		for (Sala sala : Cinema.getSalas()) {
			if (sala.isSelected()) {

				//Verifica se existem sessões que acontecem nesta sala.
				for(Sessao sessao : Cinema.getSessoes()) {

					if (sessao.getSala() == sala) {
						Alert a = new Alert(AlertType.CONFIRMATION, "A sessão " + sessao.getFilme().getTitulo() + " (" + sessao.getHorarioInicial() + ")" +  
																	"acontece na sala " + sala.getNumSala() + "." +   
																   "Se continuar, a sessão será apagada.");

						//Verifica se o usuário ainda deseja excluir.
						a.showAndWait().ifPresent(response -> {
							if(!(response == ButtonType.OK)) {
								cancel = true;
								break;
							}
						});

						oldSessoes.add(sessao);

					}

				}
				
				if(cancel) {
					break;
				} 	

				oldSalas.add(sala);

			}

		}

		if(!cancel) {
			Cinema.removeSessoes(oldSessoes);
			Cinema.removeSalas(oldSalas);
		}

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
		selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));

		/*
		 * Renderiza uma CheckBox na célula. Quando o valor da propridedade for true, a
		 * CheckBox será renderizada como selecionada marcada.
		*/
		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
		
		//Chama o método "numSalaProperty()" para cada Sala na lista.
		numCol.setCellValueFactory(new PropertyValueFactory<>("numSala"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		numCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		numCol.setEditable(false);
	
		//Chama o método "capacidadeProperty()" para cada Sala na lista.
		capacidadeCol.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		capacidadeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		capacidadeCol.setEditable(false);

	}

}
