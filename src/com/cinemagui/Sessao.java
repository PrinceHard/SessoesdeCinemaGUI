package com.cinemagui;

import java.io.Serializable;
import java.time.LocalTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sessao implements Comparable<Sessao>, Serializable{
    private final SimpleObjectProperty<Filme> filme;
    private final SimpleObjectProperty<Sala> sala;
    private final SimpleObjectProperty<LocalTime> horarioInicial;
    private final SimpleObjectProperty<LocalTime> horarioFinal;
    private final SimpleDoubleProperty valorIngresso;
    private final SimpleObjectProperty<char[]> poltronas; //l = livre; m = meia; i = inteira
    private final SimpleBooleanProperty exibicao3D;
    private final SimpleStringProperty tipoAudio;
	private final SimpleDoubleProperty taxaOcupacao;
	private final SimpleBooleanProperty selected;

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

    public boolean ocuparPoltrona(int poltrona, char tipoIngresso) {

        if(poltronas.get()[poltrona] == 'l') {
            poltronas.get()[poltrona] = tipoIngresso;

			int ocupados=0;			
			for (char p : poltronas.get()) {
				if(p != 'l'){
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

			int ocupados=0;			
			for (char p : poltronas.get()) {
				if(p != 'l'){
				    ocupados++;
				}
            }         

			taxaOcupacao.set(ocupados / sala.get().getCapacidade());

            return true;
        } else {
            return false;
        }
    }
   
    public String poltronasLivres(){
        int quantidade = 0;
        String poltronasLivres = "|  ";

        for(int i = 0; i < poltronas.get().length; i++){

            if(poltronas.get()[i] == 'l'){
                quantidade++;

                if (i<9){
                    poltronasLivres += " " + (i+1) + "  |  ";    
                } else {
                    poltronasLivres += (i+1) + "  |  ";
                }

                if((i+1) % 10 == 0 && i != 0 && i != poltronas.get().length) { //Dividir em 10 colunas
                    poltronasLivres += "\n|  ";
                }
            }
        }
        return "Quantidade de poltronas livres: " + quantidade + "\n   > Poltronas <   \n" + poltronasLivres;
    }

    public String poltronasOcupadas(){
        int quantidade = 0;
        String poltronasOcupadas = "|  ";

        for(int i = 0; i < poltronas.get().length; i++){

            if(poltronas.get()[i] != 'l'){
                quantidade++;

                if (i<9){
                    poltronasOcupadas += " " + (i+1) + "  |  ";    
                } else {
                    poltronasOcupadas += (i+1) + "  |  ";    
                }

                if(i % 9 == 0 && i != 0 && i != poltronas.get().length) { //Dividir em 10 colunas
                    poltronasOcupadas += "\n|  ";
                }
            }
        }
        return "\n Quantidade de poltronas Ocupadas: " + quantidade + "\n\n    > Poltronas <   \n" + poltronasOcupadas;
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
