package src;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

public class SceneController {
	
	@FXML
	private Pane paneHome, paneGerenciamento;

	@FXML
	private void switchToHome() {
		paneHome.setVisible(true);
		paneGerenciamento.setVisible(false);
	}
	
	@FXML
	private void switchToGerenciamento() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(true);
		
	}
	
}
