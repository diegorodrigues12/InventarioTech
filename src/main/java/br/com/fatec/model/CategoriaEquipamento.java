package br.com.fatec.model;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class CategoriaEquipamento {
    private int idCategoria;       // id_categoria
    private String nomeCategoria;  // nome_categoria

    public CategoriaEquipamento() {
    }

    public CategoriaEquipamento(int idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    //Getters e Setters

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    /**
     * 
     * @return O nome da categoria.
     */
    @Override
    public String toString() {
        return nomeCategoria;
    }
}
