package com.clientehm.model;

public class AdministradorRegistroDTO {
    private String nome;
    private String email;
    private String senha;
    private String palavraChave;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getPalavraChave() { return palavraChave; }
    public void setPalavraChave(String palavraChave) { this.palavraChave = palavraChave; }
}