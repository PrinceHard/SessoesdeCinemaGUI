package com.cinemagui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.net.URL;

import java.util.ResourceBundle;

/*
 * A interface Initializable possui o método "initialize()" que é chamado quando o arquivo FXML (que possui essa classe
 * como controlador) for carregado.
*/

public class ControllerMain implements Initializable{
	
	//Stages para as funcionalidades do programa.
	private Stage stageGerenciarSessoes = new Stage();
	private Stage stageGerenciarSalas = new Stage();
	private Stage stageGerenciarFilmes = new Stage();
	private Stage stageVendas = new Stage();
	private Stage stageCancelarVendas = new Stage();
	
	//Label que indica o nome do cinema.
	@FXML private static Label labelCinemaName;

	//Carrega as labels do faturamento.
	@FXML private Label labelSalesDayMoney, labelSalesDay, labelIntTicketsMoney, labelInt3DTicketsMoney,
						labelMeioTicketsMoney, labelMeio3DTickets, labelIntTickets, labelInt3DTickets,
						labelMeioTickets, labelMeio3DTicketsMoney, labelDay;

	//Painel para visualizar a página inicial e painel para visualizar a página de gerenciamento.
	@FXML private Pane paneHome, paneGerenciamento;

	//Painel para visualizar as página de vendas e painel para visualizar a página de faturamento.
	@FXML private Pane paneVendas, paneFaturamento;
	
	@Override	
	public void initialize(URL location, ResourceBundle resources) {

		/*
         * Definine o "Modality" das Stages como "APPLICATION_MODAL". Isso faz que, quando essa stage estiver aberta, 
         * o usuário não consiguirá usar nenhuma outra stage que esteja aberta (A stage principal, por exemplo).
        */

		stageGerenciarSessoes.initModality(Modality.APPLICATION_MODAL);
		stageGerenciarSessoes.setResizable(false);
		stageGerenciarSalas.initModality(Modality.APPLICATION_MODAL);
		stageGerenciarSalas.setResizable(false);
		stageGerenciarFilmes.initModality(Modality.APPLICATION_MODAL);
		stageGerenciarFilmes.setResizable(false);
		stageVendas.initModality(Modality.APPLICATION_MODAL);
		stageVendas.setResizable(false);
		stageCancelarVendas.initModality(Modality.APPLICATION_MODAL);
		stageCancelarVendas.setResizable(false);

	}

	public static void setName(String name) {
		//Define o nome do cinema na label especificada.
		labelCinemaName.setText(name);
	}	

	@FXML
	private void switchToHome() {

		//Renderiza o painel inicial.
		paneHome.setVisible(true);

		//Para de renderizar o painel de gerenciamento.
		paneGerenciamento.setVisible(false);

		//Para de renderizar o painel de vendas.
		paneVendas.setVisible(false);

		//Para de renderizar o painel de faturamento.
		paneFaturamento.setVisible(false);

	}
	
	@FXML
	private void switchToGerenciamento() {

		//Para de renderizar o painel inicial.
		paneHome.setVisible(false);

		//Renderiza o painel de gerenciamento.
		paneGerenciamento.setVisible(true);

		//Para de renderizar o painel de vendas.
		paneVendas.setVisible(false);

		//Para de renderizar o painel de faturamento.
		paneFaturamento.setVisible(false);

	}

	@FXML
	private void switchToVendas() {

		///Para de renderizar o painel inicial.
		paneHome.setVisible(false);

		//Para de renderizar o painel de gerenciamento.
		paneGerenciamento.setVisible(false);

		//Renderizar o painel de vendas.
		paneVendas.setVisible(true);

		//Para de renderizar o painel de faturamento.
		paneFaturamento.setVisible(false);;

	}

	@FXML
	private void switchToFaturamento() {

		///Para de renderizar o painel inicial.
		paneHome.setVisible(false);

		//Para de renderizar o painel de gerenciamento.
		paneGerenciamento.setVisible(false);

		//Para de renderizar o painel de vendas.
		paneVendas.setVisible(false);

		//Renderizar o painel de faturamento.
		paneFaturamento.setVisible(true);

		//Atualiza o faturamento.
		updateFaturamento();

	}
	
	@FXML
	private void openGerenciarSessoes() throws Exception{

		//Carrega a SceneGraph de gerenciamento de sessões e coloca o nó raiz em uma variável do controlador.
		Parent nodeRootGerenciarSessoes = FXMLLoader.load(getClass().getResource("SceneGraphGerenciarSessoes.fxml"));

		//Cria as Scenes utilizando o nó raiz.
		Scene sceneGerenciarSessoes = new Scene(nodeRootGerenciarSessoes);

		//Coloca a Scene dentro da Stage.
		stageGerenciarSessoes.setScene(sceneGerenciarSessoes);

		//Abre a Stage e aguarda sua finalização.
		stageGerenciarSessoes.show();

	}

	@FXML
	private void openGerenciarSalas() throws Exception{

		//Carrega a SceneGraph de gerenciamento de salas e coloca o nó raiz em uma variável do controlador.
		Parent nodeRootGerenciarSalas = FXMLLoader.load(getClass().getResource("SceneGraphGerenciarSalas.fxml"));

		//Cria as Scenes utilizando o nó raiz.
		Scene sceneGerenciarSalas = new Scene(nodeRootGerenciarSalas);

		//Coloca a Scene dentro da Stage.
		stageGerenciarFilmes.setScene(sceneGerenciarSalas);

		//Abre a Stage e aguarda sua finalização.
		stageGerenciarFilmes.show();

	}

	@FXML
	private void openGerenciarFilmes() throws Exception{

		//Carrega a SceneGraph de gerenciamento de filmes e coloca o nó raiz em uma variável do controlador.
		Parent nodeRootGerenciarFilmes = FXMLLoader.load(getClass().getResource("SceneGraphGerenciarFilmes.fxml"));

		//Cria as Scenes utilizando o nó raiz.
		Scene sceneGerenciarFilmes = new Scene(nodeRootGerenciarFilmes);

		//Coloca a Scene dentro da Stage.
		stageGerenciarSalas.setScene(sceneGerenciarFilmes);

		//Abre a Stage e aguarda sua finalização.
		stageGerenciarSalas.show();

	}

	@FXML
	private void openVendas() throws Exception {

		//Carrega a SceneGraph de vendas e coloca o nó raiz em uma variável do controlador.
		Parent nodeRootVendas = FXMLLoader.load(getClass().getResource("SceneGraphVendas.fxml"));

		//Cria as Scenes utilizando o nó raiz.
		Scene sceneVendas = new Scene(nodeRootVendas);

		//Coloca a Scene dentro da Stage.
		stageVendas.setScene(sceneVendas);

		//Abre a Stage e aguarda sua finalização.
		stageVendas.show();

	}

	@FXML
	private void openCancelarVendas() throws Exception {

		//Carrega a SceneGraph de cancelamento de vendas e coloca o nó raiz em uma variável do controlador.
		Parent nodeRootCancelarVendas = FXMLLoader.load(getClass().getResource("SceneGraphCancelarVendas.fxml"));

		//Cria as Scenes utilizando o nó raiz.
		Scene sceneCancelarVendas = new Scene(nodeRootCancelarVendas);

		//Coloca a Scene dentro da Stage.
		stageCancelarVendas.setScene(sceneCancelarVendas);

		//Abre a Stage e aguarda sua finalização.
		stageCancelarVendas.show();
		
	}

	public void updateFaturamento() {

		//Esse métodos puxam o faturamento da classe Cinema e adicionam nas labels.
		labelSalesDayMoney.setText("R$" + Double.toString(Cinema.getFaturamentoTotal()));
		labelSalesDay.setText(Integer.toString(Cinema.getIngressosTotal()));
		labelIntTicketsMoney.setText("R$" + Double.toString(Cinema.getFaturamentoInteiras()));
		labelInt3DTicketsMoney.setText("R$" + Double.toString(Cinema.getFaturamentoInteiras3D()));
		labelMeioTicketsMoney.setText("R$" + Double.toString(Cinema.getFaturamentoMeias()));
		labelMeio3DTicketsMoney.setText("R$" + Double.toString(Cinema.getFaturamentoMeias3D()));
		labelIntTickets.setText(Integer.toString(Cinema.getIngressosInteiras()));
		labelInt3DTickets.setText(Integer.toString(Cinema.getIngressosInteiras3D()));
		labelMeioTickets.setText(Integer.toString(Cinema.getIngressosMeias()));
		labelMeio3DTickets.setText(Integer.toString(Cinema.getIngressosMeias3D()));
		labelDay.setText(Cinema.getToday().get(Cinema.getToday().DAY_OF_MONTH) + " - " + Cinema.getToday().get(Cinema.getToday().MONTH) +
						 " - " + (Cinema.getToday().get(Cinema.getToday().YEAR)));

	}

}
