package com.example.appreceitas;

import java.io.Serializable;

public class Receita implements Serializable {

    private String titulo;
    private String ingredientes;
    private String recheio;
    private String preparo;
    private String imagemUri;

    public Receita(String titulo, String ingredientes, String recheio, String preparo, String imagemUri) {
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.recheio = recheio;
        this.preparo = preparo;
        this.imagemUri = imagemUri;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getRecheio() {
        return recheio;
    }

    public void setRecheio(String recheio) {
        this.recheio = recheio;
    }

    public String getPreparo() {
        return preparo;
    }

    public void setPreparo(String preparo) {
        this.preparo = preparo;
    }

    public String getImagemUri() {
        return imagemUri;
    }

    public void setImagemUri(String imagemUri) {
        this.imagemUri = imagemUri;
    }
}
