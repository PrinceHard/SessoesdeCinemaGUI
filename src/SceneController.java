package src;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToSceneHome(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("mainSceneGraph.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneGerenciamento(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Gerenciamento.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneVendas(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Vendas.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToSceneFaturamento(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Faturamento.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
