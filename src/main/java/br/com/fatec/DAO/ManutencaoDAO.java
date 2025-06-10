package br.com.fatec.dao;

import br.com.fatec.persistencia.Banco; // Para gerenciar a conexao com o banco de dados
import br.com.fatec.model.Manutencao;   // Para o modelo de Manutencao
import br.com.fatec.model.Equipamento; // Necessario para atualizar o status do equipamento

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; // Para converter LocalDate para java.sql.Date
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Diego, Gustavo e Matheus
 */
public class ManutencaoDAO {

    // DAOs auxiliares que serao necessarios para atualizar o status do equipamento
    private EquipamentoDAO equipamentoDAO;
    private StatusEquipamentoDAO statusEquipamentoDAO;

    public ManutencaoDAO() {
        this.equipamentoDAO = new EquipamentoDAO();
        this.statusEquipamentoDAO = new StatusEquipamentoDAO();
    }

    public boolean adicionarManutencao(Manutencao manutencao) throws SQLException {
        String sqlInsertManutencao = "INSERT INTO Manutencoes (id_equipamento, id_responsavel, data_inicio_manutencao, data_fim_manutencao, descricao_servico) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateEquipamentoStatus = "UPDATE Equipamentos SET id_status = ? WHERE id_equipamento = ?";
        Connection connection = null;
        PreparedStatement stmtInsertManutencao = null;
        PreparedStatement stmtUpdateEquipamento = null;

        try {
            connection = Banco.obterConexao();
            connection.setAutoCommit(false);

            // 1. Inserir a Manutencao
            stmtInsertManutencao = connection.prepareStatement(sqlInsertManutencao);
            stmtInsertManutencao.setInt(1, manutencao.getIdEquipamento());
            stmtInsertManutencao.setInt(2, manutencao.getIdResponsavel());
            stmtInsertManutencao.setDate(3, Date.valueOf(manutencao.getDataInicioManutencao()));
            stmtInsertManutencao.setDate(4, Date.valueOf(manutencao.getDataFimManutencao()));
            stmtInsertManutencao.setString(5, manutencao.getDescricaoServico());

            int rowsAffectedManutencao = stmtInsertManutencao.executeUpdate();

            if (rowsAffectedManutencao > 0) {
                int idStatusEmManutencao = 1; 

                stmtUpdateEquipamento = connection.prepareStatement(sqlUpdateEquipamentoStatus);
                stmtUpdateEquipamento.setInt(1, idStatusEmManutencao);
                stmtUpdateEquipamento.setInt(2, manutencao.getIdEquipamento());
                int rowsAffectedEquipamento = stmtUpdateEquipamento.executeUpdate();

                if (rowsAffectedEquipamento > 0) {
                    connection.commit(); 
                    return true;
                } else {
                    connection.rollback(); 
                    return false;
                }
            } else {
                connection.rollback(); 
                return false;
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Desfaz em caso de qualquer erro
                } catch (SQLException rbEx) {
                    System.err.println("Erro ao realizar rollback: " + rbEx.getMessage());
                }
            }
            throw e; // Relanca a excecao para o controlador
        } finally {
            if (stmtInsertManutencao != null) { try { stmtInsertManutencao.close(); } catch (SQLException e) { System.err.println("Erro ao fechar stmtInsertManutencao: " + e.getMessage()); }}
            if (stmtUpdateEquipamento != null) { try { stmtUpdateEquipamento.close(); } catch (SQLException e) { System.err.println("Erro ao fechar stmtUpdateEquipamento: " + e.getMessage()); }}
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restaura o auto-commit
                    connection.close();
                } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }
            }
        }
    }


    public Manutencao buscarManutencaoPorIdEquipamento(int idEquipamento) throws SQLException {
        String sql = "SELECT id_manutencao, id_equipamento, id_responsavel, data_inicio_manutencao, data_fim_manutencao, descricao_servico FROM Manutencoes WHERE id_equipamento = ? ORDER BY data_inicio_manutencao DESC LIMIT 1";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Manutencao manutencao = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idEquipamento);
            rs = stmt.executeQuery();

            if (rs.next()) {
                manutencao = new Manutencao();
                manutencao.setIdManutencao(rs.getInt("id_manutencao"));
                manutencao.setIdEquipamento(rs.getInt("id_equipamento"));
                manutencao.setIdResponsavel(rs.getInt("id_responsavel"));
                manutencao.setDataInicioManutencao(rs.getDate("data_inicio_manutencao").toLocalDate());
                
                Date dataFim = rs.getDate("data_fim_manutencao");
                manutencao.setDataFimManutencao(dataFim != null ? dataFim.toLocalDate() : null);
                
                manutencao.setDescricaoServico(rs.getString("descricao_servico"));
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return manutencao;
    }

    public boolean atualizarManutencao(Manutencao manutencao) throws SQLException {
        String sql = "UPDATE Manutencoes SET id_equipamento = ?, id_responsavel = ?, data_inicio_manutencao = ?, data_fim_manutencao = ?, descricao_servico = ? WHERE id_manutencao = ?";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, manutencao.getIdEquipamento());
            stmt.setInt(2, manutencao.getIdResponsavel());
            stmt.setDate(3, Date.valueOf(manutencao.getDataInicioManutencao()));
            stmt.setDate(4, Date.valueOf(manutencao.getDataFimManutencao()));
            stmt.setString(5, manutencao.getDescricaoServico());
            stmt.setInt(6, manutencao.getIdManutencao());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public boolean excluirManutencao(int idManutencao) throws SQLException {
        String sql = "DELETE FROM Manutencoes WHERE id_manutencao = ?";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idManutencao);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }
    
    public boolean atualizarStatusEquipamento(int idEquipamento, int idStatus) throws SQLException {
        String sqlUpdateEquipamentoStatus = "UPDATE Equipamentos SET id_status = ? WHERE id_equipamento = ?";
        Connection connection = null;
        PreparedStatement stmtUpdateEquipamento = null;
        try {
            connection = Banco.obterConexao();
            stmtUpdateEquipamento = connection.prepareStatement(sqlUpdateEquipamentoStatus);
            stmtUpdateEquipamento.setInt(1, idStatus);
            stmtUpdateEquipamento.setInt(2, idEquipamento);
            int rowsAffected = stmtUpdateEquipamento.executeUpdate();
            return rowsAffected > 0;
        } finally {
             if (stmtUpdateEquipamento != null) { try { stmtUpdateEquipamento.close(); } catch (SQLException e) { System.err.println("Erro ao fechar stmtUpdateEquipamento: " + e.getMessage()); }}
             if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }
}
