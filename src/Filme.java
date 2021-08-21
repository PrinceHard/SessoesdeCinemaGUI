package src;

import java.io.Serializable;
import javafx.scene.control.CheckBox;

public class Filme implements Comparable<Filme>, Serializable{ //implements Serializable
	private String titulo, tipoProducao;
	private int duracao;
	private String[] tipoAudio;
	private boolean permite3D;
	private transient CheckBox checkBox;

	public Filme(String titulo, int duracao, String tipoProducao, String[] tipoAudio, boolean permite3D) {
		this.titulo = titulo;
		this.duracao = duracao;
		this.tipoProducao = tipoProducao;
		this.tipoAudio = tipoAudio;
		this.permite3D = permite3D;
	}

	public CheckBox getCheckBox() {
		if(checkBox == null) {
			this.checkBox = new CheckBox();
		}
		return checkBox;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getDuracao() {
		return duracao;
	}

	public String getTipoProducao() {
		return tipoProducao;
	}	
	
	public String[] getTipoAudio() {
		return tipoAudio;
	}
	
	public boolean getPermite3D()  {
		return permite3D;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDuracao(int duracao) {
		this.duracao = duracao;
	}

	public void setTipoProducao(String tipoProducao) {
		this.tipoProducao = tipoProducao;
	}

	public void setTipoAudio(String[] tipoAudio) {
		this.tipoAudio = tipoAudio;
	}

	public void setPermite3D(boolean permite3D) {
		this.permite3D = permite3D;
	}

	@Override
	public int compareTo(Filme filme) {
		return this.titulo.compareTo(filme.titulo);
	}
}
