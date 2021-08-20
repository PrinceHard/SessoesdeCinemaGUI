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
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.cell.CheckBoxTableCell;


public class GerenciarSalasController implements Initializable{

	@FXML
	private TextField inputNumSala, inputCapacidade;

	@FXML
	private Pane paneViewSalas, paneCreatingSala;
	@FXML
	private TableView<Sala> salasTable;
	@FXML
	private TableColumn<Sala, Integer> numCol, capacidadeCol;
	@FXML
	private TableColumn<Sala, Boolean> selectCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
		selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
		
		numCol.setCellValueFactory(new PropertyValueFactory<>("numSala"));
		numCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		numCol.setOnEditCommit(new EventHandler<CellEditEvent<Sala, Integer>>() {
				@Override
				public void handle(CellEditEvent<Sala, Integer> sala) {
					sala.getTableView().getItems().get(sala.getTablePosition().getRow()).setNumSala(sala.getNewValue());
				}
			});
			
		capacidadeCol.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
		capacidadeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		capacidadeCol.setOnEditCommit(new EventHandler<CellEditEvent<Sala, Integer>>() {
				@Override
				public void handle(CellEditEvent<Sala, Integer> sala) {
					sala.getTableView().getItems().get(sala.getTablePosition().getRow()).setCapacidade(sala.getNewValue());
				}
			});

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
		boolean salaExiste=false;
		
		try {
			numSala = Integer.parseInt(inputNumSala.getText());
			capacidade = Integer.parseInt(inputCapacidade.getText());
			
			for(Sala sala : CinemaUtil.getSalas()){
				if(sala.getNumSala() == numSala){
					Alert a = new Alert(AlertType.INFORMATION, "Sala já existente, defina outra!");
					salaExiste=true;
					break;
				}
			}
			
			if(!salaExiste){
				Sala sala = new Sala(numSala, capacidade);
				CinemaUtil.saveData(sala);
				updateList();
				cancelCreatingSala();
			}
			

		} catch(Exception e) {
			Alert a = new Alert(AlertType.INFORMATION, "Valores inválidos, tente novamente.");
			a.showAndWait();
		}	
	}
	
	@FXML
	private void deleteSala() {
		System.out.println("Enter");
		ArrayList<Sala> oldSalas = new ArrayList<>();
		for (Sala sala : CinemaUtil.getSalas()) {
			if(sala.isSelected()) {
				System.out.println("Achou");
				oldSalas.add(sala);
			}
		}
		CinemaUtil.getSalas().remove(oldSalas);
		updateList();
	}

	private void updateList() {
		salasTable.setItems(salasList());
	}


}
