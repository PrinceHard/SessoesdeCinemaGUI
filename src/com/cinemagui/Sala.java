package com.cinemagui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;


public class Sala implements Comparable<Sala>, Serializable{
	private transient SimpleBooleanProperty selected;
	private transient SimpleIntegerProperty numSala;
	private transient SimpleIntegerProperty capacidade;
	
	public Sala(int numSala, int capacidade) {
		this.numSala = new SimpleIntegerProperty(numSala);
		this.capacidade = new SimpleIntegerProperty(capacidade);
		this.selected = new SimpleBooleanProperty(false);
	}

	//Ler objeto serializado.
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(numSala.get());
		out.writeObject(capacidade.get());
		out.writeObject(selected.get());
	}
	
	//Serializar objeto
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		numSala = new SimpleIntegerProperty( (int) in.readObject());
		capacidade = new SimpleIntegerProperty( (int) in.readObject());
		selected = new SimpleBooleanProperty( (boolean) in.readObject());
	}
	
	//Getters Properties
	public SimpleBooleanProperty selectedProperty() {
		return selected;
	}
	
	public SimpleIntegerProperty numSalaProperty() {
		return numSala;
	}
	
	public SimpleIntegerProperty capacidadeProperty() {
		return capacidade;
	}
	
	//Getters
	public boolean isSelected() {
		return selected.get();
	}
	
	public int getNumSala() {
		return numSala.get();
	}
	
	public int getCapacidade() {
		return capacidade.get();
	}
	
	//Setters
	public void setSelected(boolean selected){
		this.selected.set(selected);
	}

	public void setNumSala(int numSala) {
		this.numSala.set(numSala);	
	}	

	public void setCapacidade(int capacidade) {
		this.capacidade.set(capacidade);
    }

	@Override
	public int compareTo(Sala sala) {
		if(this.numSala.get() > sala.numSala.get()){
			return 1;
		}
		if(this.numSala.get() < sala.numSala.get()){
			return -1;
		}
		return 0;
	}	
}
