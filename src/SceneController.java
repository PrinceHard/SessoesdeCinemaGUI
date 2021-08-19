package src;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class SceneController {
	
	@FXML
	private Pane paneHome, paneGerenciamento, paneVendas, paneFaturamento;

	@FXML
	private void switchToHome() {
		paneHome.setVisible(true);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(false);
	}
	
	@FXML
	private void switchToGerenciamento() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(true);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(false);
	}

	@FXML
	private void switchToVendas() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(true);
		paneFaturamento.setVisible(false);
	}

	@FXML
	private void switchToFaturamento() {
		paneHome.setVisible(false);
		paneGerenciamento.setVisible(false);
		paneVendas.setVisible(false);
		paneFaturamento.setVisible(true);
	}
	
}
