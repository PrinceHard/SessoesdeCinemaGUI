package com.cinemagui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class Cinema {

    /*
     * Não existe a necessidade de instanciar objetos desta classe, 
     * então todas as variáveis e quase todos os métodos serão estáticos.
    */

    private static SimpleStringProperty name = new SimpleStringProperty();
    private static SimpleDoubleProperty faturamentoInteiras = new SimpleDoubleProperty();
    private static SimpleDoubleProperty faturamentoInteiras3D = new SimpleDoubleProperty();
    private static SimpleDoubleProperty faturamentoMeias = new SimpleDoubleProperty();
    private static SimpleDoubleProperty faturamentoMeias3D = new SimpleDoubleProperty();
    private static SimpleIntegerProperty ingressosInteiras = new SimpleIntegerProperty();
    private static SimpleIntegerProperty ingressosInteiras3D = new SimpleIntegerProperty();
    private static SimpleIntegerProperty ingressosMeias = new SimpleIntegerProperty();
    private static SimpleIntegerProperty ingressosMeias3D = new SimpleIntegerProperty();
    private static SimpleObjectProperty<ObservableList<Sala>> salas = new SimpleObjectProperty<ObservableList<Sala>>();
    private static SimpleObjectProperty<ObservableList<Filme>> filmes = new SimpleObjectProperty<ObservableList<Filme>>();
    private static SimpleObjectProperty<ObservableList<Sessao>> sessoes = new SimpleObjectProperty<ObservableList<Sessao>>();

    //Métodos para modificar as ObservableLists:
    public static void addSala(Sala sala) {
        salas.get().add(sala);
    }

    public static void removeSalas(List<Sala> oldSalas) {
        salas.get().removeAll(oldSalas);
    }

    public static void addFilme(Filme filme) {
        filmes.get().add(filme);
    }

    public static void removeFilmes(List<Filme> oldFilmes) {
        filmes.get().removeAll(oldFilmes);
    }

    public static void addSessao(Sessao sessao) {
        sessoes.get().add(sessao);
    }

    public static void removeSessoes(List<Sessao> oldSessoes) {
        sessoes.get().removeAll(oldSessoes);
    }

    //Métodos de interação com os objetos do tipo Sessao:
    public static boolean venderIngresso(Sessao sessao, char tipoIngresso, int poltrona){               

        //Poltrona ocupada com sucesso.
        if(sessao.ocuparPoltrona(poltrona, tipoIngresso)) { 

            //A sessão é 3D.
            if(sessao.getExibicao3D()) { 

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

            //Não foi possível vender o ingresso.
            return false;
        }

        //O ingresso foi vendido com sucesso.
        return true;
    }

    public static boolean cancelarVenda(Sessao sessao, int poltrona){

        //Salva o tipo de ingresso que será sobrescrito.
        char tipoIngresso = sessao.getPoltronas()[poltrona];

        //Poltrona liberada com sucesso.
        if(sessao.liberarPoltrona(poltrona)) { 

            //A sessão é 3D.
            if(sessao.getExibicao3D()) { 

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

            //Não foi possível cancelar a venda.
            return false;
        }

        //A venda foi cancelada com sucesso.
        return true;
    }

    //Método para serializar os dados.
    public static void serializeData() throws IOException{

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.ser"));
    
        /*
         * É necessário converter a ObservableList para ArrayList, pois a primeira não é
         * serializável.
         *
         */

        out.writeObject(new ArrayList<Sessao>(sessoes.get()));
        out.writeObject(new ArrayList<Filme>(filmes.get()));
        out.writeObject(new ArrayList<Sala>(salas.get()));
    
    }

    //Método para deserializar os dados.
    public static void unserializeData() throws ClassNotFoundException {
        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.ser"));
        
            sessoes.set(FXCollections.observableArrayList( (ArrayList<Sessao>) in.readObject() ));
            filmes.set(FXCollections.observableArrayList( (ArrayList<Filme>) in.readObject() ));
            salas.set(FXCollections.observableArrayList( (ArrayList<Sala>) in.readObject()) );

        } catch (IOException e) {

            System.out.println("Serialized file not found, aborting unserialization.");

            /*
             * Caso o programa não ache dados serializados, 
             * ele instancia as variáveis com listas vazias.
             *
            */

            sessoes.set(FXCollections.observableArrayList());
            filmes.set(FXCollections.observableArrayList());
            salas.set(FXCollections.observableArrayList());
        }
    }

    //Getters das propriedas
    public static SimpleDoubleProperty faturamentoInteirasProperty() {
        return faturamentoInteiras;
    }

    public static SimpleDoubleProperty faturamentoInteiras3DProperty() {
        return faturamentoInteiras3D;
    }

    public static SimpleDoubleProperty faturamentoMeiasProperty() {
        return faturamentoMeias;
    }

    public static SimpleDoubleProperty faturamentoMeias3DProperty() {
        return faturamentoMeias3D;
    }

    public static SimpleIntegerProperty ingressosInteirasProperty() {
        return ingressosInteiras;
    }

    public static SimpleIntegerProperty ingressosInteiras3DProperty() {
        return ingressosInteiras3D;
    }

    public static SimpleIntegerProperty ingressosMeiasProperty() {
        return ingressosMeias;
    }

    public static SimpleIntegerProperty ingressosMeias3DProperty() {
        return ingressosMeias3D;
    }
	
	public static SimpleStringProperty nameProperty() {
		return name;
	}

    //Getters dos valores
    public static String getName() {
        return name.get();
    }
    
    public static double getFaturamentoInteiras(){
        return faturamentoInteiras.get();
    }

    public static double getFaturamentoInteiras3D(){
        return faturamentoInteiras3D.get();
    }

    public static double getFaturamentoMeias(){
        return faturamentoMeias.get();
    }

    public static double getFaturamentoMeias3D(){
        return faturamentoMeias3D.get();
    }

    public static int getIngressosInteiras(){
        return ingressosInteiras.get();
    }

    public static int getIngressosInteiras3D(){
        return ingressosInteiras3D.get();
    }

    public static int getIngressosMeias(){
        return ingressosMeias.get();
    }

    public static int getIngressosMeias3D(){
        return ingressosMeias3D.get();
    }

    public static ObservableList<Sala> getSalas(){
        return salas.get();
    }

    public static ObservableList<Filme> getFilmes(){
        return filmes.get();
    }

    public static ObservableList<Sessao> getSessoes(){
        return sessoes.get();
    }
	
	//Setters dos valores
	public static void setName(String nameCinema) {
		name.set(nameCinema);
	}

}
