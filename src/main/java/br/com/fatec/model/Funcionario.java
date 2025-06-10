package br.com.fatec.model;

import java.time.LocalDateTime; 

/**
 * 
 * @author Diego, Gustavo e Matheus
 */
public class Funcionario {
    private int idFuncionario;
    private String nome;
    private String matricula;
    private String email;
    private String senhaHash; 
    private String funcao;
    private String telefone;
    private LocalDateTime dataCadastro;

    // Construtor padrao
    public Funcionario() {
    }

    // Construtor completo
    public Funcionario(int idFuncionario, String nome, String matricula, String email, String senhaHash, String funcao, String telefone, LocalDateTime dataCadastro) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.matricula = matricula;
        this.email = email;
        this.senhaHash = senhaHash;
        this.funcao = funcao;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
    }

    //Getters e Setters
    public int getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(int idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenhaHash() { // Retorna o hash da senha
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) { // Recebe a senha 
        this.senhaHash = senhaHash;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString() {
        // ComboBox de responsaveis exibe o nome do funcionario
        return nome;
    }
}
