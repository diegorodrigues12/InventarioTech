package br.com.fatec.model;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class StatusEquipamento {
    private int idStatus;    
    private String nomeStatus; 

    // Construtor vazio
    public StatusEquipamento() {
    }

    // Construtor completo
    public StatusEquipamento(int idStatus, String nomeStatus) {
        this.idStatus = idStatus;
        this.nomeStatus = nomeStatus;
    }

    // Getters e Setters 

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getNomeStatus() {
        return nomeStatus;
    }

    public void setNomeStatus(String nomeStatus) {
        this.nomeStatus = nomeStatus;
    }

    @Override
    public String toString() {
        // Exibe o nome do status corretamente
        return nomeStatus;
    }
}
