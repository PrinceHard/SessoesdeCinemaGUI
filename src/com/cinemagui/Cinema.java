package com.cinemagui;

import java.util.ArrayList;
import java.util.Collections;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Cinema implements Serializable{
    private SimpleDoubleProperty faturamentoInteiras;
    private SimpleDoubleProperty faturamentoInteiras3D;
    private SimpleDoubleProperty faturamentoMeias;
    private SimpleDoubleProperty faturamentoMeias3D;
    private SimpleIntegerProperty ingressosInteiras;
    private SimpleIntegerProperty ingressosInteiras3D;
    private SimpleIntegerProperty ingressosMeias; 
    private SimpleIntegerProperty ingressosMeias3D;
    private SimpleObjectProperty<ArrayList<Sala>> salas;
    private SimpleObjectProperty<ArrayList<Filme>> filmes;
    private SimpleObjectProperty<ArrayList<Sessao>> sessoes;

    public Cinema(){
        this.faturamentoInteiras = new SimpleDoubleProperty();
        this.faturamentoInteiras3D = new SimpleDoubleProperty();
        this.faturamentoMeias = new SimpleDoubleProperty();
        this.faturamentoMeias3D = new SimpleDoubleProperty();
        this.ingressosInteiras = new SimpleIntegerProperty();
        this.ingressosInteiras3D = new SimpleIntegerProperty();
        this.ingressosMeias = new SimpleIntegerProperty();
        this.ingressosMeias3D = new SimpleIntegerProperty();
        this.salas = new SimpleObjectProperty<ArrayList<Sala>>();
        this.filmes = new SimpleObjectProperty<ArrayList<Filme>>();
        this.sessoes = new SimpleObjectProperty<ArrayList<Sessao>>();
    }

    //Ler objeto serializado.
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.faturamentoInteiras = new SimpleDoubleProperty( (double) in.readObject());
        this.faturamentoInteiras3D = new SimpleDoubleProperty( (double) in.readObject());
        this.faturamentoMeias = new SimpleDoubleProperty( (double) in.readObject());
        this.faturamentoMeias3D = new SimpleDoubleProperty( (double) in.readObject());
        this.ingressosInteiras = new SimpleIntegerProperty( (int) in.readObject());
        this.ingressosInteiras3D = new SimpleIntegerProperty( (int) in.readObject());
        this.ingressosMeias = new SimpleIntegerProperty( (int) in.readObject());
        this.ingressosMeias3D = new SimpleIntegerProperty( (int) in.readObject());
        this.salas = new SimpleObjectProperty<ArrayList<Sala>>( (ArrayList<Sala>) in.readObject());
        this.filmes = new SimpleObjectProperty<ArrayList<Filme>>( (ArrayList<Filme>) in.readObject());
        this.sessoes = new SimpleObjectProperty<ArrayList<Sessao>>( (ArrayList<Sessao>) in.readObject());
    }

    //Serializar objeto
    private void writeObject(ObjectOutputStream out) throws IOException{
        out.writeObject(faturamentoInteiras.get());
        out.writeObject(faturamentoInteiras3D.get());
        out.writeObject(faturamentoMeias.get());
        out.writeObject(faturamentoMeias3D.get());
        out.writeObject(ingressosInteiras.get());
        out.writeObject(ingressosInteiras3D.get());
        out.writeObject(ingressosMeias.get());
        out.writeObject(ingressosMeias3D.get());
        out.writeObject(salas.get());
        out.writeObject(filmes.get());
        out.writeObject(sessoes.get());
    }

    public void novaSala(Sala sala) {
        salas.get().add(sala);
        Collections.sort(salas.get());
    }

    public void removerSala(Sala sala) {
        salas.get().remove(sala);
    }

    public void novoFilme(Filme filme) {
        filmes.get().add(filme);
        Collections.sort(filmes.get());
    }

    public void removerFilme(Filme filme) {
        filmes.get().remove(filme);
    }

    public void novaSessao(Sessao sessao) {
        sessoes.get().add(sessao);
        Collections.sort(sessoes.get());
    }

    public void removerSessao(Sessao sessao) {
        sessoes.get().remove(sessao);
    }

    public boolean venderIngresso(Sessao sessao, char tipoIngresso, int poltrona){               

        if(sessao.ocuparPoltrona(poltrona, tipoIngresso)) { //Poltrona ocupada com sucesso.

            if(sessao.getExibicao3D()) { //A sessão é 3D.

                if(tipoIngresso == 'i') {
                    ingressosInteiras3D.set(ingressosInteiras3D.get()+1);
                    faturamentoInteiras.set(faturamentoInteiras3D.get() + sessao.getValorIngresso());
                } else {
                    ingressosMeias3D.set(ingressosMeias3D.get()+1);
                    faturamentoMeias3D.set(faturamentoMeias3D.get() + sessao.getValorIngresso() / 2);
                }

            } else {                       

                if(tipoIngresso == 'i') {
                    ingressosInteiras.set(ingressosInteiras.get()+1);
                    faturamentoInteiras.set(faturamentoInteiras.get() + sessao.getValorIngresso());
                } else {
                    ingressosMeias.set(ingressosMeias.get()+1);
                    faturamentoMeias.set(faturamentoMeias.get() + sessao.getValorIngresso() / 2);
                }
            }

        } else {
            return false;
        }
        
        return true;
    }

    public boolean cancelarVenda(Sessao sessao, int poltrona){
        char tipoIngresso = sessao.getPoltronas()[poltrona]; //Salva o tipo de ingresso que será sobrescrito.

        if(sessao.liberarPoltrona(poltrona)) { //Poltrona liberada com sucesso.

            if(sessao.getExibicao3D()) { //A sessão é 3D.

                if(tipoIngresso == 'i') {
                    ingressosInteiras3D.set(ingressosInteiras3D.get()-1);
                    faturamentoInteiras.set(faturamentoInteiras3D.get() - sessao.getValorIngresso());
                } else {
                    ingressosMeias3D.set(ingressosMeias3D.get()-1);
                    faturamentoMeias3D.set(faturamentoMeias3D.get() - sessao.getValorIngresso() / 2);
                }

            } else {                       

                if(tipoIngresso == 'i') {
                    ingressosInteiras.set(ingressosInteiras.get()-1);
                    faturamentoInteiras.set(faturamentoInteiras.get() - sessao.getValorIngresso());
                } else {
                    ingressosMeias.set(ingressosMeias.get()-1);
                    faturamentoMeias.set(faturamentoMeias.get() - sessao.getValorIngresso() / 2);
                }
            }

        } else {
            return false;
        }

        return true;
    }

    //getters properties
    public SimpleDoubleProperty faturamentoInteirasProperty() {
        return faturamentoInteiras;
    }

    public SimpleDoubleProperty faturamentoInteiras3DProperty() {
        return faturamentoInteiras3D;
    }

    public SimpleDoubleProperty faturamentoMeiasProperty() {
        return faturamentoMeias;
    }

    public SimpleDoubleProperty faturamentoMeias3DProperty() {
        return faturamentoMeias3D;
    }

    public SimpleIntegerProperty ingressosInteirasProperty() {
        return ingressosInteiras;
    }

    public SimpleIntegerProperty ingressosInteiras3DProperty() {
        return ingressosInteiras3D;
    }

    public SimpleIntegerProperty ingressosMeiasProperty() {
        return ingressosMeias;
    }

    public SimpleIntegerProperty ingressosMeias3DProperty() {
        return ingressosMeias3D;
    }

    //getters values
    public double getFaturamentoInteiras(){
        return faturamentoInteiras.get();
    }

    public double getFaturamentoInteiras3D(){
        return faturamentoInteiras3D.get();
    }

    public double getFaturamentoMeias(){
        return faturamentoMeias.get();
    }

    public double getFaturamentoMeias3D(){
        return faturamentoMeias3D.get();
    }

    public int getIngressosInteiras(){
        return ingressosInteiras.get();
    }

    public int getIngressosInteiras3D(){
        return ingressosInteiras3D.get();
    }

    public int getIngressosMeias(){
        return ingressosMeias.get();
    }

    public int getIngressosMeias3D(){
        return ingressosMeias3D.get();
    }

    public ArrayList<Sala> getSalas(){
        return salas.get();
    }

    public ArrayList<Filme> getFilmes(){
        return filmes.get();
    }

    public ArrayList<Sessao> getSessoes(){
        return sessoes.get();
    }

}
