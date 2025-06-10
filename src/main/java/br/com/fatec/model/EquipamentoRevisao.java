package br.com.fatec.model;

public class EquipamentoRevisao {
    private String nomeEquipamento;
    private String observacao;

    public EquipamentoRevisao(String nomeEquipamento, String observacao) {
        this.nomeEquipamento = nomeEquipamento;
        this.observacao = observacao;
    }

    public String getNomeEquipamento() {
        return nomeEquipamento;
    }

    public void setNomeEquipamento(String nomeEquipamento) {
        this.nomeEquipamento = nomeEquipamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
