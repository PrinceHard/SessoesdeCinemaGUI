package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

import java.time.LocalTime;
import java.net.URL;

import java.util.ResourceBundle;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando
 * o arquivo FXML (que possui essa classe como controlador) for carregado.
*/

public class ControllerVendas implements Initializable {

	//Botões para abris os painéis de venda e de cancelamento.
	@FXML private Button buttonVender, buttonCancelar;

	//Text para informar as poltronas.
	@FXML private Text textListPoltronas;
	
	//Inputs para coletar o número da poltrona.
	@FXML private TextField inputPoltrona;

	//Painel para visualizar as salas e painel para criar/editar as salas.
	@FXML private Pane paneViewSessoes, paneTicket;

	//CheckBoxes para coletar o tipo de ingresso.
	@FXML private CheckBox selectInteiro, selectMeio;

	//Label para mostrar para qual sessão o usuário está vendendo o ingresso.
	@FXML private Label showSessao;
	
	//Tabela para visualizar as sessões.
	@FXML private TableView<Sessao> tableSessoes;

	//Colunas da tabela
	@FXML private TableColumn<Sessao, Boolean> colSelect;
	@FXML private TableColumn<Sessao, Filme> colFilme;
	@FXML private TableColumn<Sessao, Sala> colSala;
	@FXML private TableColumn<Sessao, LocalTime> colHorarioInicial, colHorarioFinal;
	@FXML private TableColumn<Sessao, String> colTipoAudio;
	@FXML private TableColumn<Sessao, Boolean> colTipoExibicao;	
	@FXML private TableColumn<Sessao, Double> colValorIngresso;
	@FXML private TableColumn<Sessao, Double> colTaxaOcupacao;

	//Variável para armazenar a sessão que o usuário estará vendendo o ingresso.
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
	private void openIngressoPane() {

		//Verificar se o usuário selecionou apenas 1 sessao.
		int countSelect=0;
		for (Sessao sessao : Cinema.getSessoes()) {

			if(sessao.isSelected()) {
				countSelect += 1;
				sessaoSelected = sessao;
			}
		}

		if(countSelect == 1) {

			//Para de renderizar o painel de visualização das sessões.
			paneViewSessoes.setVisible(false);

			//Renderiza o painel de venda ou cancelamento de venda de ingressos.
			paneTicket.setVisible(true);
			
			//Coloca o a sessão na label para mostrar ao usuário para qual sessão o ingresso será vendido.
			showSessao.setText(sessaoSelected.getFilme().getTitulo());

			//Se estiver vendendo ingresso, esse bloco é executado.
			if(buttonVender.isVisible()) {
				//Mostra as poltronas disponíveis.
				for (int i = 0; i < sessaoSelected.getPoltronas().length; i++) {

					if(sessaoSelected.getPoltronas()[i] == 'l') {
						textListPoltronas.setText(textListPoltronas.getText() + (i+1) + "   ");
					}
				}

			//Se estiver cancelando a venda de um ingresso, esse bloco é executado.
			} else {

				//Mostra as poltronas disponíveis.
				for (int i = 0; i < sessaoSelected.getPoltronas().length; i++) {

					if(sessaoSelected.getPoltronas()[i] != 'l') {
						textListPoltronas.setText(textListPoltronas.getText() + (i+1) + "   ");
					}
				}
			}
			
		} else if(countSelect == 0){
			Alert a = new Alert(AlertType.INFORMATION, "Você não selecionou nenhuma sessão.");
			a.showAndWait();
		} else {
			Alert a = new Alert(AlertType.INFORMATION, "Selecione apenas 1 sessão.");
			a.showAndWait();
		}
		
	}

	@FXML
	private void closeIngressoPane() {

		//Limpa os inputs e os CheckBoxes
		inputPoltrona.setText("");
		selectInteiro.setSelected(false);
		selectMeio.setSelected(false);
		textListPoltronas.setText("");

		//Para de renderizar o painel de vendas.
		paneTicket.setVisible(false);

		//Renderiza o painel de criação de salas.
		paneViewSessoes.setVisible(true);

	}

	@FXML
	private void vender() {

		//Variáveis temporárias.
		char tipoIngresso = 'i';

		if (verifyInputs(true)) {

			//Coleta os dados e armazena em variáveis temporárias.
			if(selectInteiro.isSelected()) {
				tipoIngresso = 'm';
			}

			//Tenta ocuapr a poltrona.
			if (Cinema.venderIngresso(sessaoSelected, tipoIngresso, Integer.parseInt(inputPoltrona.getText()))) {
				Alert a = new Alert(AlertType.INFORMATION, "Ingresso vendido com sucesso!");
				a.showAndWait();

				//Fecha o painel de vendas.
				closeIngressoPane();
			
			} else {
				Alert a = new Alert(AlertType.INFORMATION, "Essa poltrona já foi vendida!");
				a.showAndWait();
			}

		}

	}

	@FXML
	private void cancelarVenda() {

		if (verifyInputs(false)) {

			//Tenta desocupar a poltrona.
			if (Cinema.cancelarVenda(sessaoSelected, Integer.parseInt(inputPoltrona.getText()))) {
				Alert a = new Alert(AlertType.INFORMATION, "Venda cancelada com sucesso!");
				a.showAndWait();

				//Fecha o painel de vendas.
				closeIngressoPane();
			
			} else {
				Alert a = new Alert(AlertType.INFORMATION, "Essa poltrona não foi vendida!");
				a.showAndWait();
			}

		}

	}

	private boolean verifyInputs(boolean vendendo) {

		/*
		 * Esse método pode ser usado tanto na venda ou no cancelamento da venda do ingresso. Isso é definido através 
		 * do parâmetro "vendendo"
		*/

		/*
		 * Flag que será retornada.
		 * Essa flag também será usada para evitar de fazer uma verificação quando outra já a invalidou.
		*/
		boolean valid = true;

		//Verifica se o input está vazio.
		if(inputPoltrona.getText().isEmpty()) {
			valid = false;
		}

		//Verifica se a poltrona pode ser convertida para inteiro.
		if(valid) {

			try {
				int i = Integer.parseInt(inputPoltrona.getText());

				if(!(i > 0 && i <= sessaoSelected.getSala().getCapacidade())) {
					valid = false;

					Alert a = new Alert(AlertType.INFORMATION, "Essa sessão só possui" + sessaoSelected.getSala().getCapacidade());
				}

			} catch (Exception e) {
				valid = false;
			}

		}

		//Verifica se o usuário selecionou um tipo de ingresso.
		if (vendendo && !(selectInteiro.isSelected() || selectMeio.isSelected())) {
				valid = false;
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

		//Chama o método "taxaOcupacaoProperty()" para cada Sessao na lista.
		colTaxaOcupacao.setCellValueFactory(new PropertyValueFactory<>("taxaOcupacao"));

		//Renderiza uma TextField na célula e converte o valor da propriedade para String.
		colTaxaOcupacao.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

		//Não permite que o usuário edite o valor diretamente na tabela.
		colTaxaOcupacao.setEditable(false);
	}

	//O usuário só pode selecionar um tipo de ingresso. Os métodos abaixo servem para garantir isso.

	@FXML
	private void inteiroSelected(){
		
		if(selectMeio.isSelected()){
			selectMeio.fire();
		}
	}

	@FXML
	private void meioSelected(){

		if(selectInteiro.isSelected()){
			selectInteiro.fire();
		}
	}
}	
