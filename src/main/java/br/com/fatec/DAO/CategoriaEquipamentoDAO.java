package br.com.fatec.dao;

import br.com.fatec.persistencia.Banco;
import br.com.fatec.model.CategoriaEquipamento; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class CategoriaEquipamentoDAO {

    public CategoriaEquipamentoDAO() {

    }

    //Lista todas as categorias de equipamentos disponíveis no banco de dados.

    public List<CategoriaEquipamento> listarCategorias() throws SQLException {
        List<CategoriaEquipamento> categorias = new ArrayList<>();
        String sql = "SELECT id_categoria, nome_categoria FROM CategoriasEquipamento ORDER BY nome_categoria";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao(); // Obtém a conexão com o banco de dados
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_categoria");
                String nome = rs.getString("nome_categoria");
                categorias.add(new CategoriaEquipamento(id, nome));
            }
        } finally {
            // Garante que os recursos sejam fechados em qualquer cenário
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return categorias;
    }

    // Busca ID
    public int getIdCategoriaByNome(String nomeCategoria) throws SQLException {
        String sql = "SELECT id_categoria FROM CategoriasEquipamento WHERE nome_categoria = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0; // Valor padrão para não encontrado

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeCategoria);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_categoria");
            }
        } finally {
            if (rs != null) { 
                try { 
                    rs.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); 
                }
            }
            if (stmt != null) { 
                try { stmt.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }
            }
            if (connection != null) { 
                try { connection.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar Connection: " + e.getMessage()); }
            }
        }
        return id;
    }

    public String getNomeCategoriaById(int idCategoria) throws SQLException {
        // CORRIGIDO: Nome da tabela agora corresponde ao seu schema (CategoriasEquipamento)
        String sql = "SELECT nome_categoria FROM CategoriasEquipamento WHERE id_categoria = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String nome = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idCategoria);
            rs = stmt.executeQuery();

            if (rs.next()) {
                nome = rs.getString("nome_categoria");
            }
        } finally {
            if (rs != null) { 
                try { 
                    rs.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); 
                }
            }
            if (stmt != null) { 
                try { stmt.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }
            }
            if (connection != null) { 
                try { connection.close(); 
                } catch (SQLException e) { 
                    System.err.println("Erro ao fechar Connection: " + e.getMessage()); }
            }
        }
        return nome;
    }
}
