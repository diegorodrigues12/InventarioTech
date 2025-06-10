package br.com.fatec.model;

import java.time.LocalDate;

/**
 *
 * @author Diego, Gustavo e Mastheus
 */
public class Manutencao {
    private int idManutencao;           // id_manutencao
    private int idEquipamento;          // id_equipamento FK
    private int idResponsavel;          // id_responsavel FK
    private LocalDate dataInicioManutencao; // data_inicio_manutencao
    private LocalDate dataFimManutencao;  // data_fim_manutencao
    private String descricaoServico;    // descricao_servico

    // Construtor vazio
    public Manutencao() {
    }

    // Construtor completo do banco
    public Manutencao(int idManutencao, int idEquipamento, int idResponsavel, LocalDate dataInicioManutencao, LocalDate dataFimManutencao, String descricaoServico) {
        this.idManutencao = idManutencao;
        this.idEquipamento = idEquipamento;
        this.idResponsavel = idResponsavel;
        this.dataInicioManutencao = dataInicioManutencao;
        this.dataFimManutencao = dataFimManutencao;
        this.descricaoServico = descricaoServico;
    }

    // Construtor para nova manutencao 
    public Manutencao(int idEquipamento, int idResponsavel, LocalDate dataInicioManutencao, LocalDate dataFimManutencao, String descricaoServico) {
        this.idEquipamento = idEquipamento;
        this.idResponsavel = idResponsavel;
        this.dataInicioManutencao = dataInicioManutencao;
        this.dataFimManutencao = dataFimManutencao;
        this.descricaoServico = descricaoServico;
    }

    //Getters e Setters 
    public int getIdManutencao() {
        return idManutencao;
    }

    public void setIdManutencao(int idManutencao) {
        this.idManutencao = idManutencao;
    }

    public int getIdEquipamento() {
        return idEquipamento;
    }

    public void setIdEquipamento(int idEquipamento) {
        this.idEquipamento = idEquipamento;
    }

    public int getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(int idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public LocalDate getDataInicioManutencao() {
        return dataInicioManutencao;
    }

    public void setDataInicioManutencao(LocalDate dataInicioManutencao) {
        this.dataInicioManutencao = dataInicioManutencao;
    }

    public LocalDate getDataFimManutencao() {
        return dataFimManutencao;
    }

    public void setDataFimManutencao(LocalDate dataFimManutencao) {
        this.dataFimManutencao = dataFimManutencao;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    @Override
    public String toString() {
        return "Manutencao{" +
                "idManutencao=" + idManutencao +
                ", idEquipamento=" + idEquipamento +
                ", idResponsavel=" + idResponsavel +
                ", dataInicioManutencao=" + dataInicioManutencao +
                ", dataFimManutencao=" + dataFimManutencao +
                ", descricaoServico='" + descricaoServico + '\'' +
                '}';
    }
}
