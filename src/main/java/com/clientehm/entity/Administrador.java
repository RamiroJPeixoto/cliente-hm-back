package com.clientehm.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
public class Administrador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String palavraChave;

    public Administrador() {
    }

    public Administrador(String nome, String email, String senha, String palavraChave) {
        this.nome = nome;
        this.email = email;
        this.senha = senha; // Lembre-se: idealmente, a senha deve ser codificada aqui antes de salvar
        this.palavraChave = palavraChave;
    }

    // Este construtor pode ser útil ou pode ser removido se não for mais usado após a introdução dos DTOs.
    public Administrador(String email, String senha) {
        this.email = email;
        this.senha = senha; // Lembre-se: idealmente, a senha deve ser codificada aqui
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha; // Lembre-se: idealmente, a senha deve ser codificada aqui se estiver sendo definida
    }

    public String getPalavraChave() {
        return palavraChave;
    }

    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }
}