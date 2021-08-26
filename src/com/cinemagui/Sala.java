package src;

import java.io.Serializable;
import javafx.scene.control.CheckBox;

public class Sala implements Comparable<Sala>, Serializable{
	private int numSala;
	private int capacidade;
	private transient CheckBox checkBox;


	public Sala(int numSala, int capacidade) {
		this.numSala = numSala;
		this.capacidade = capacidade;
	}

	public CheckBox getCheckBox() {
		if(checkBox == null) {
			this.checkBox = new CheckBox();
		}
		return checkBox;
	}
	
	public int getNumSala() {
		return numSala;
	}

	public int getCapacidade() {
		return capacidade;
	}
	
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public void setNumSala(int numSala) {
		this.numSala = numSala;	
	}	

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
    }

	@Override
	public int compareTo(Sala sala) {
		if(this.numSala > sala.numSala){
			return 1;
		}
		if(this.numSala < sala.numSala){
			return -1;
		}
		return 0;
	}	
}
