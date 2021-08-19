package src;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.time.LocalTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;

public class GerenciarSessoesController implements Initializable{

	@FXML
	private TableView<Sessao> sessoesTable;
	@FXML
	private TableColumn<Sessao, String> filmeCol;
	@FXML
	private TableColumn<Sessao, Sala> salaCol;
	@FXML
	private TableColumn<Sessao, LocalTime> horaIniCol, horaFinCol;
	@FXML
	private TableColumn<Sessao, String> audioCol, exibicaoCol;
	@FXML
	private TableColumn<Sessao, Double> valorCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		filmeCol.setCellValueFactory(new PropertyValueFactory<>("filme"));
		salaCol.setCellValueFactory(new PropertyValueFactory<>("sala"));
		horaIniCol.setCellValueFactory(new PropertyValueFactory<>("horarioInicial"));
		horaFinCol.setCellValueFactory(new PropertyValueFactory<>("horarioFinal"));
		audioCol.setCellValueFactory(new PropertyValueFactory<>("tipoAudio"));
		exibicaoCol.setCellValueFactory(new PropertyValueFactory<>("exibicao3D"));
		valorCol.setCellValueFactory(new PropertyValueFactory<>("valorIngresso"));

		sessoesTable.setItems(sessaoList());
	}	

	private ObservableList<Sessao> sessaoList() {
		return FXCollections.observableArrayList(CinemaUtil.getSessoes());
	}


}
