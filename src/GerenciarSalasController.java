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

public class GerenciarSalasController implements Initializable{

	@FXML
	private TextField inputNumSala, inputCapacidade;

	@FXML
	private Pane paneViewSalas, paneCreatingSala;
	@FXML
	private TableView<Sala> salasTable;
	@FXML
	private TableColumn<Sala, Integer> numCol;
	@FXML
	private TableColumn<Sala, Integer> capacidadeCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		numCol.setCellValueFactory(new PropertyValueFactory<>("numSala"));
		capacidadeCol.setCellValueFactory(new PropertyValueFactory<>("capacidade"));

		if(!CinemaUtil.getSalas().isEmpty()) {
			updateList();
		} else {
			salasTable.setPlaceholder(new Label("Nenhuma sala existente."));
		}
	}	

	private ObservableList<Sala> salasList() {
		return FXCollections.observableArrayList(CinemaUtil.getSalas());
	}

	@FXML
	private void newSala() {
		paneViewSalas.setVisible(false);
		paneCreatingSala.setVisible(true);
	}

	@FXML
	private void cancelCreatingSala() {
		inputNumSala.setText("");
		inputCapacidade.setText("");
		paneViewSalas.setVisible(true);
		paneCreatingSala.setVisible(false);
	}

	@FXML
	private void createSala() {
		int numSala, capacidade;
		
		try {
			numSala = Integer.parseInt(inputNumSala.getText());
			capacidade = Integer.parseInt(inputCapacidade.getText());
			Sala sala = new Sala(numSala, capacidade);
			CinemaUtil.saveData(sala);
			updateList();
			cancelCreatingSala();
		} catch(Exception e) {
			Alert a = new Alert(AlertType.INFORMATION, "Valores inválidos!");
			a.showAndWait();
		}	
	}

	private void updateList() {
		salasTable.setItems(salasList());
	}


}
