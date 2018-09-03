package com.example.dany.stockhome;

/**
 * Created by dany on 06/05/2016.
 */
public class Receita {
    int id;
    String nome,preparaçao;

    public Receita(int id, String nome, String preparaçao) {
        this.id = id;
        this.nome = nome;
        this.preparaçao = preparaçao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPreparaçao() {
        return preparaçao;
    }

    public void setPreparaçao(String preparaçao) {
        this.preparaçao = preparaçao;
    }
}
