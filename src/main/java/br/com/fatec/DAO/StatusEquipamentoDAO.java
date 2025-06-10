package br.com.fatec.dao;

import br.com.fatec.persistencia.Banco; // Para gerenciar a conexao com o banco de dados
import br.com.fatec.model.StatusEquipamento; // Para o modelo de StatusEquipamento

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego, Gustavo Matheus
 */
public class StatusEquipamentoDAO {

    public StatusEquipamentoDAO() {
    }

    /**
     * Lista todos os status de equipamentos disponiveis no banco de dados.
     */
    public List<StatusEquipamento> listarStatus() throws SQLException {
        List<StatusEquipamento> statusList = new ArrayList<>();
        String sql = "SELECT id_status, nome_status FROM StatusEquipamento ORDER BY nome_status"; 
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_status");
                String nome = rs.getString("nome_status");
                statusList.add(new StatusEquipamento(id, nome));
            }
        } finally {
            // Garante que os recursos sejam fechados em qualquer cenario
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return statusList;
    }

    public int getIdStatusByNome(String nomeStatus) throws SQLException {
        String sql = "SELECT id_status FROM StatusEquipamento WHERE nome_status = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0; // Valor padrao para nao encontrado

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeStatus);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id_status");
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return id;
    }

    public String getNomeStatusById(int idStatus) throws SQLException {
        String sql = "SELECT nome_status FROM StatusEquipamento WHERE id_status = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String nome = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idStatus);
            rs = stmt.executeQuery();

            if (rs.next()) {
                nome = rs.getString("nome_status");
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return nome;
    }
}
