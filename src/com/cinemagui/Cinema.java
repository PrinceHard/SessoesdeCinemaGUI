package com.cinemagui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Collections;

import java.time.LocalTime;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.lang.ClassNotFoundException;

public class Cinema {

    /*
     * Não existe a necessidade de instanciar objetos desta classe, então todas as variáveis e quase todos os métodos
     * serão estáticos.
    */

    private static SimpleObjectProperty<Calendar> today = new SimpleObjectProperty<>();
    private static SimpleDoubleProperty faturamentoTotal = new SimpleDoubleProperty();
    private static SimpleIntegerProperty ingressosTotal = new SimpleIntegerProperty();
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
                    ingressosInteiras3D.set(ingressosInteiras3D.get() + 1);
                    faturamentoInteiras3D.set(faturamentoInteiras3D.get() + sessao.getValorIngresso());
                    faturamentoTotal.set(faturamentoTotal.get()  + sessao.getValorIngresso());
                } else {
                    ingressosMeias3D.set(ingressosMeias3D.get() + 1);
                    faturamentoMeias3D.set(faturamentoMeias3D.get() + sessao.getValorIngresso() / 2);               
                    faturamentoTotal.set(faturamentoTotal.get()  + sessao.getValorIngresso() / 2);
                }

                ingressosTotal.set(ingressosTotal.get() + 1);
                

            } else {                       

                if(tipoIngresso == 'i') {
                    ingressosInteiras.set(ingressosInteiras.get() + 1);
                    faturamentoInteiras.set(faturamentoInteiras.get() + sessao.getValorIngresso());
                    faturamentoTotal.set(faturamentoTotal.get()  + sessao.getValorIngresso());
                } else {
                    ingressosMeias.set(ingressosMeias.get() + 1);
                    faturamentoMeias.set(faturamentoMeias.get() + sessao.getValorIngresso() / 2);
                    faturamentoTotal.set(faturamentoTotal.get()  + sessao.getValorIngresso() / 2);
                }

                ingressosTotal.set(ingressosTotal.get() + 1);
                
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
                    faturamentoTotal.set(faturamentoTotal.get() - sessao.getValorIngresso());
                } else {
                    ingressosMeias3D.set(ingressosMeias3D.get()-1);
                    faturamentoMeias3D.set(faturamentoMeias3D.get() - sessao.getValorIngresso() / 2);
                    faturamentoTotal.set(faturamentoTotal.get() - sessao.getValorIngresso() / 2);
                }

                ingressosTotal.set(ingressosTotal.get() - 1);
                

            } else {                       

                if(tipoIngresso == 'i') {
                    ingressosInteiras.set(ingressosInteiras.get()-1);
                    faturamentoInteiras.set(faturamentoInteiras.get() - sessao.getValorIngresso());
                } else {
                    ingressosMeias.set(ingressosMeias.get()-1);
                    faturamentoMeias.set(faturamentoMeias.get() - sessao.getValorIngresso() / 2);
                }

                ingressosTotal.set(ingressosTotal.get() - 1);
                faturamentoTotal.set(faturamentoTotal.get()  - sessao.getValorIngresso());
            }
        } else {

            //Não foi possível cancelar a venda.
            return false;
        }

        //A venda foi cancelada com sucesso.
        return true;
    }

    //Método para remover sessões que já acabaram.
    public static void updateSessaoList() {

        ArrayList<Sessao> oldSessoes = new ArrayList<>();

        for (Sessao sessao : sessoes.get()) {
            if(sessao.getHorarioFinal().toSecondOfDay() < LocalTime.now().toSecondOfDay()) {
                oldSessoes.add(sessao);
            }
        }

        removeSessoes(oldSessoes);
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

        out.writeObject(Calendar.getInstance());

        ArrayList<Integer> ingressosVendidos = new ArrayList<>();
        Collections.addAll(ingressosVendidos, ingressosInteiras.get(), ingressosMeias.get(),
                           ingressosInteiras3D.get(), ingressosMeias3D.get());
        out.writeObject(ingressosVendidos);


        ArrayList<Double> faturamento = new ArrayList<>();
        Collections.addAll(faturamento, faturamentoInteiras.get(), faturamentoMeias.get(), 
                           faturamentoInteiras3D.get(), faturamentoMeias3D.get());
        out.writeObject(faturamento);

        out.writeObject(faturamentoTotal.get());
        out.writeObject(ingressosTotal.get());

    }

    //Método para deserializar os dados.
    public static void unserializeData() throws ClassNotFoundException {
        try {

            ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.ser"));
        
            sessoes.set(FXCollections.observableArrayList( (ArrayList<Sessao>) in.readObject() ));
            filmes.set(FXCollections.observableArrayList( (ArrayList<Filme>) in.readObject() ));
            salas.set(FXCollections.observableArrayList( (ArrayList<Sala>) in.readObject()) );

            today.set( (Calendar) in.readObject());
            Calendar newToday = Calendar.getInstance();

            if(today.get().get(today.get().DAY_OF_YEAR) == newToday.get(newToday.DAY_OF_YEAR)) {

                ArrayList<Integer> ingressosVendidos = (ArrayList<Integer>) in.readObject();
                ArrayList<Double> faturamento = (ArrayList<Double>) in.readObject();

                ingressosInteiras.set(ingressosVendidos.get(0));
                ingressosMeias.set(ingressosVendidos.get(1));
                ingressosInteiras3D.set(ingressosVendidos.get(2));
                ingressosMeias3D.set(ingressosVendidos.get(3));

                faturamentoInteiras.set(faturamento.get(0));
                faturamentoMeias.set(faturamento.get(1));
                faturamentoInteiras3D.set(faturamento.get(2));
                faturamentoMeias3D.set(faturamento.get(3));

                faturamentoTotal.set( (double) in.readObject());
                ingressosTotal.set( (int) in.readObject());

            } else {
                today.set(newToday);
            }

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
            today.set(Calendar.getInstance());


        }
    }

    //Getters das propriedas
    public static SimpleDoubleProperty faturamentoTotalProperty() {
        return faturamentoTotal;
    }

    public static SimpleIntegerProperty ingressosTotalProperty() {
        return ingressosTotal;
    }

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

    //Getters dos valores
    public static Calendar getToday() {
        return today.get();
    }

    public static double getFaturamentoTotal() {
        return faturamentoTotal.get();
    }
    
    public static int getIngressosTotal() {
        return ingressosTotal.get();
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

}
