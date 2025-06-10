package com.example.appreceitas;

import java.io.Serializable;

public class Receita implements Serializable {

    private long id; // Adicionado para representar o ID no banco de dados
    private String titulo;
    private String ingredientes;
    private String recheio;
    private String preparo;
    private String imagemUri;

    // Construtor para quando a receita é carregada do banco de dados (com ID)
    public Receita(long id, String titulo, String ingredientes, String recheio, String preparo, String imagemUri) {
        this.id = id;
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.recheio = recheio;
        this.preparo = preparo;
        this.imagemUri = imagemUri;
    }

    // Construtor para quando uma nova receita é criada (sem ID, será gerado pelo banco)
    public Receita(String titulo, String ingredientes, String recheio, String preparo, String imagemUri) {
        this.titulo = titulo;
        this.ingredientes = ingredientes;
        this.recheio = recheio;
        this.preparo = preparo;
        this.imagemUri = imagemUri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


