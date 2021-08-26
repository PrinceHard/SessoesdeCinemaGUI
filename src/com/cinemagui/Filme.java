package com.cinemagui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Filme implements Comparable<Filme>, Serializable{ //implements Serializable
	private SimpleStringProperty titulo, tipoProducao;
	private SimpleIntegerProperty duracao;
	private SimpleObjectProperty<String[]> tipoAudio;
	private SimpleBooleanProperty permite3D;
	private SimpleBooleanProperty selected;

	public Filme(String titulo, int duracao, String tipoProducao, String[] tipoAudio, boolean permite3D) {
		this.titulo = new SimpleStringProperty(titulo);
		this.duracao = new SimpleIntegerProperty(duracao);
		this.tipoProducao = new SimpleStringProperty(tipoProducao);
		this.tipoAudio = new SimpleObjectProperty<String[]>(tipoAudio);
		this.permite3D = new SimpleBooleanProperty(permite3D);
		this.selected = new SimpleBooleanProperty(false);
	}

	//Ler objeto serializado.
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.titulo = new SimpleStringProperty( (String) in.readObject());
		this.duracao = new SimpleIntegerProperty( (int) in.readObject());
		this.tipoProducao = new SimpleStringProperty( (String) in.readObject());
		this.tipoAudio = new SimpleObjectProperty<String[]>( (String[]) in.readObject());
		this.permite3D = new SimpleBooleanProperty( (boolean) in.readObject());
		this.selected = new SimpleBooleanProperty( (boolean) in.readObject());
	}

	//Serializar objeto
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(titulo.get());
		out.writeObject(duracao.get());
		out.writeObject(tipoProducao.get());
		out.writeObject(tipoAudio.get());
		out.writeObject(permite3D.get());
		out.writeObject(selected.get());
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
