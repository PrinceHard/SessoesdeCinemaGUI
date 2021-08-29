package com.cinemagui;

import java.time.LocalTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Sessao implements Comparable<Sessao>, Serializable{
    private SimpleObjectProperty<Filme> filme;
    private SimpleObjectProperty<Sala> sala;
    private SimpleObjectProperty<LocalTime> horarioInicial;
    private SimpleObjectProperty<LocalTime> horarioFinal;
    private SimpleDoubleProperty valorIngresso;
    private SimpleObjectProperty<char[]> poltronas; //l = livre; m = meia; i = inteira
    private SimpleBooleanProperty exibicao3D;
    private SimpleStringProperty tipoAudio;
	private SimpleDoubleProperty taxaOcupacao;
	private SimpleBooleanProperty selected;

    public Sessao(Filme filme, Sala sala, LocalTime horarioInicial, LocalTime horarioFinal, double valorIngresso, boolean exibicao3D, String tipoAudio){
        this.filme = new SimpleObjectProperty(filme);
        this.sala = new SimpleObjectProperty(sala);
        this.horarioInicial = new SimpleObjectProperty(horarioInicial);
        this.horarioFinal = new SimpleObjectProperty(horarioFinal);
        this.valorIngresso = new SimpleDoubleProperty(valorIngresso);
		this.poltronas = new SimpleObjectProperty(new char[sala.getCapacidade()]);
        this.exibicao3D = new SimpleBooleanProperty(exibicao3D);
        this.tipoAudio = new SimpleStringProperty(tipoAudio);
		this.taxaOcupacao = new SimpleDoubleProperty(0.0);
		this.selected = new SimpleBooleanProperty(false);

        for(int i=0; i < poltronas.get().length; i++) { //Inicializando todas as poltronas como livres.
            poltronas.get()[i] = 'l';
        }
    }

    //Ler objeto serializado.
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.filme = new SimpleObjectProperty( (Filme) in.readObject());
        this.sala = new SimpleObjectProperty( (Sala) in.readObject());
        this.horarioInicial = new SimpleObjectProperty( (LocalTime) in.readObject());
        this.horarioFinal = new SimpleObjectProperty( (LocalTime) in.readObject());
        this.valorIngresso = new SimpleDoubleProperty( (double) in.readObject());
        this.poltronas = new SimpleObjectProperty( (char[]) in.readObject());
        this.exibicao3D = new SimpleBooleanProperty( (boolean) in.readObject());
        this.tipoAudio = new SimpleStringProperty( (String) in.readObject());
        this.taxaOcupacao = new SimpleDoubleProperty( (double) in.readObject());
        this.selected = new SimpleBooleanProperty( (boolean) in.readObject());
    }

    //Serializar objeto
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.writeObject(filme.get());
        out.writeObject(sala.get());
        out.writeObject(horarioInicial.get());
        out.writeObject(horarioFinal.get());
        out.writeObject(valorIngresso.get());
        out.writeObject(poltronas.get());
        out.writeObject(exibicao3D.get());
        out.writeObject(tipoAudio.get());
        out.writeObject(taxaOcupacao.get());
        out.writeObject(selected.get());

    }

    public boolean ocuparPoltrona(int poltrona, char tipoIngresso) {

        if(poltronas.get()[poltrona] == 'l') {
            poltronas.get()[poltrona] = tipoIngresso;

			double ocupados=0;			
			for (char p : poltronas.get()) {
				if(p != 'l'){
                    System.out.println("Achou ocupado");
				    ocupados++;
				}
            }         

			taxaOcupacao.set(ocupados / sala.get().getCapacidade());

			return true;
            
        } else {
            return false;
        }

    }

    public boolean liberarPoltrona(int poltrona) {

        if(poltronas.get()[poltrona] != 'l') {
            poltronas.get()[poltrona] = 'l';

			double ocupados=0;			
			for (char p : poltronas.get()) {
				if(p != 'l'){
                    System.out.println("Achou");
				    ocupados++;
				}
            }         

			taxaOcupacao.set(ocupados / sala.get().getCapacidade());

            return true;
        } else {
            return false;
        }
    }
	
	//Propertys getters
	public SimpleObjectProperty<Filme> filmeProperty() {
		return filme;
	}
	
	public SimpleObjectProperty<Sala> salaProperty() {
		return sala;
	}	
	
	public SimpleObjectProperty<LocalTime> horarioInicialProperty() {
		return horarioInicial;
	}	
	
	public SimpleObjectProperty<LocalTime> horarioFinalProperty() {
		return horarioFinal;
	}	
	
	public SimpleDoubleProperty valorIngressoProperty() {
		return valorIngresso;
	}	
	
	public SimpleObjectProperty<char[]> poltronasProperty() {
		return poltronas;
	}	
	
	public SimpleBooleanProperty exibicao3DProperty() {
		return exibicao3D;
	}		
	
	public SimpleStringProperty tipoAudioProperty() {
		return tipoAudio;
	}		

	public SimpleBooleanProperty selectedProperty() {
		return selected;
	}		

	public SimpleDoubleProperty taxaOcupacaoProperty() {
		return taxaOcupacao;
	}
	
	//Values getters
    public Filme getFilme(){
        return filme.get();
    }
	
    public Sala getSala(){
        return sala.get();
    }

    public LocalTime getHorarioInicial(){
        return horarioInicial.get();
    }

    public LocalTime getHorarioFinal(){
        return horarioFinal.get();
    }

    public double getValorIngresso(){
        return valorIngresso.get();
    }

    public char[] getPoltronas(){
        return poltronas.get();
    }

    public boolean getExibicao3D(){
        return exibicao3D.get();
    }

    public String getTipoAudio(){
        return tipoAudio.get();
    }
	
	public boolean isSelected() {
		return selected.get();
	}

	public double getTaxaOcupacao() {
		return taxaOcupacao.get();
	}
	
	//Values Setters
    public void setFilme(Filme filme){
        this.filme.set(filme); 
    }
	
	public void setSala(Sala sala){
        this.sala.set(sala); 
    }
	
	public void setHorarioInicial(LocalTime horarioInicial){
        this.horarioInicial.set(horarioInicial);
    }

    public void setHorarioFinal(LocalTime horarioFinal){
        this.horarioFinal.set(horarioFinal);
    }

    public void setValorIngresso(double valorIngresso){
        this.valorIngresso.set(valorIngresso); 
    }
	
    public void setTipoAudio(String tipoAudio){
        this.tipoAudio.set(tipoAudio);
    }
	
    public void setExibicao3D(boolean exibicao3D){
        this.exibicao3D.set(exibicao3D); 
    }

	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
	
    @Override
    public int compareTo(Sessao sessao) {
        if(this.horarioInicial.get().toSecondOfDay() > sessao.horarioInicial.get().toSecondOfDay()) {
            return 1;
        }
        if(this.horarioInicial.get().toSecondOfDay() < sessao.horarioInicial.get().toSecondOfDay()) {
            return -1;
        }
        return 0;
    }
}
