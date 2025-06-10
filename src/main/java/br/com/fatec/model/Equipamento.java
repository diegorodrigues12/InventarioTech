package br.com.fatec.model;

import java.time.LocalDate; 

/**
 * 
 * @author Diego, Gustavo e Matheus
 */
public class Equipamento {
    private int idEquipamento;     //id_equipamento
    private String nome;           //nome_equipamento
    private String categoria;      // Nome da categoria
    private String numeroDeSerie;  // numero_serie
    private LocalDate dataAquisicao; // data_aquisicao
    private String descricao;      //descricao_servico
    private int idStatus;          //id_status
    private Integer idResponsavel; // id_responsavel
    private String nomeStatus;     //Nome do status


    public Equipamento() {
    }

    public Equipamento(String nome, String categoria, String numeroDeSerie, LocalDate dataAquisicao, String descricao, int idStatus) {
        this.nome = nome;
        this.categoria = categoria;
        this.numeroDeSerie = numeroDeSerie;
        this.dataAquisicao = dataAquisicao;
        this.descricao = descricao;
        this.idStatus = idStatus;
        this.idResponsavel = null; // Default para null, pois nao e um campo obrigatorio direto no formulario
    }
    
    public Equipamento(String nome, String categoria, String numeroDeSerie, LocalDate dataAquisicao, String descricao, int idStatus, Integer idResponsavel) {
        this.nome = nome;
        this.categoria = categoria;
        this.numeroDeSerie = numeroDeSerie;
        this.dataAquisicao = dataAquisicao;
        this.descricao = descricao;
        this.idStatus = idStatus;
        this.idResponsavel = idResponsavel;
    }

    public Equipamento(int idEquipamento, String nome, String categoria, String numeroDeSerie, LocalDate dataAquisicao, String descricao, int idStatus, Integer idResponsavel) {
        this.idEquipamento = idEquipamento;
        this.nome = nome;
        this.categoria = categoria;
        this.numeroDeSerie = numeroDeSerie;
        this.dataAquisicao = dataAquisicao;
        this.descricao = descricao;
        this.idStatus = idStatus;
        this.idResponsavel = idResponsavel;
    }

    // Getters e Setter 

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(int idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public void setNumeroDeSerie(String numeroDeSerie) {
        this.numeroDeSerie = numeroDeSerie;
    }

    public LocalDate getDataAquisicao() {
        return dataAquisicao;
    }

    public void setDataAquisicao(LocalDate dataAquisicao) {
        this.dataAquisicao = dataAquisicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public Integer getIdResponsavel() { 
        return idResponsavel;
    }

    public void setIdResponsavel(Integer idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getNomeStatus() {
        return nomeStatus;
    }

    public void setNomeStatus(String nomeStatus) { 
        this.nomeStatus = nomeStatus;
    }

    @Override
    public String toString() {
        return nome;
    }
}