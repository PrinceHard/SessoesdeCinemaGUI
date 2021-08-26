package src;

import java.io.Serializable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Filme implements Comparable<Filme>, Serializable{ //implements Serializable
	private final SimpleStringProperty titulo, tipoProducao;
	private final SimpleIntegerProperty duracao;
	private final SimpleObjectProperty<String[]> tipoAudio;
	private final SimpleBooleanProperty permite3D;
	private final SimpleBooleanProperty selected;

	public Filme(String titulo, int duracao, String tipoProducao, String[] tipoAudio, boolean permite3D) {
		this.titulo = new SimpleStringProperty(titulo);
		this.duracao = new SimpleIntegerProperty(duracao);
		this.tipoProducao = new SimpleStringProperty(tipoProducao);
		this.tipoAudio = new SimpleObjectProperty<String[]>(tipoAudio);
		this.permite3D = new SimpleBooleanProperty(permite3D);
		this.selected = new SimpleBooleanProperty(false);
	}
	
	public SimpleStringProperty tituloProperty() {
		return titulo;
	}
	
	public SimpleIntegerProperty duracaoProperty() {
		return duracao;
	}
	
	public SimpleStringProperty tipoProducaoProperty() {
		return tipoProducao;
	}
	
	public SimpleObjectProperty<String[]> tipoAudioProperty() {
		return tipoAudio;
	}
	
	public SimpleBooleanProperty permite3DProperty() {
		return permite3D;
	}
	
	public SimpleBooleanProperty selectedProperty() {
		return selected;
	}

	public boolean isSelected() {
		return selected.get();
	}

	public String getTitulo() {
		return titulo.get();
	}

	public int getDuracao() {
		return duracao.get();
	}

	public String getTipoProducao() {
		return tipoProducao.get();
	}	
	
	public String[] getTipoAudio() {
		return tipoAudio.get();
	}
	
	public boolean getPermite3D()  {
		return permite3D.get();
	}

	public void setTitulo(String titulo) {
		this.titulo.set(titulo);
	}

	public void setDuracao(int duracao) {
		this.duracao.set(duracao);
	}

	public void setTipoProducao(String tipoProducao) {
		this.tipoProducao.set(tipoProducao);
	}

	public void setTipoAudio(String[] tipoAudio) {
		this.tipoAudio.set(tipoAudio);
	}

	public void setPermite3D(boolean permite3D) {
		this.permite3D.set(permite3D);
	}

	@Override
	public int compareTo(Filme filme) {
		return this.titulo.get().compareTo(filme.titulo.get());
	}
}
