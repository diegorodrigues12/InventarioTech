package br.com.fatec.dao;

import br.com.fatec.persistencia.Banco; 
import br.com.fatec.model.Equipamento;   

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date; 
import java.time.LocalDate; 
import java.util.ArrayList;
import java.util.List;  

/**
 *
 * @author Diego, Gustavo Matheus
 */
public class EquipamentoDAO {

    private CategoriaEquipamentoDAO categoriaEquipamentoDAO; 
    private StatusEquipamentoDAO statusEquipamentoDAO;     

    public EquipamentoDAO() {
        this.categoriaEquipamentoDAO = new CategoriaEquipamentoDAO(); 
        this.statusEquipamentoDAO = new StatusEquipamentoDAO();     
    }

    public boolean adicionarEquipamento(Equipamento equipamento) throws SQLException {
        String sql = "INSERT INTO Equipamentos (nome_equipamento, id_categoria, numero_serie, data_aquisicao, descricao_servico, id_status, id_responsavel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao();

            int idCategoria = categoriaEquipamentoDAO.getIdCategoriaByNome(equipamento.getCategoria());
            if (idCategoria == 0) {
                System.err.println("Erro: Categoria '" + equipamento.getCategoria() + "' nao encontrada no banco de dados.");
                return false; 
            }

            int idStatus = equipamento.getIdStatus(); 

            // 3. Prepara a instrucao SQL
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, equipamento.getNome());         
            stmt.setInt(2, idCategoria);                          
            stmt.setString(3, equipamento.getNumeroDeSerie());
            stmt.setDate(4, Date.valueOf(equipamento.getDataAquisicao()));
            stmt.setString(5, equipamento.getDescricao());         
            stmt.setInt(6, idStatus);                              

            if (equipamento.getIdResponsavel() != null) {
                stmt.setInt(7, equipamento.getIdResponsavel());   
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);         
            }

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public boolean numeroDeSerieExiste(String numeroDeSerie) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Equipamentos WHERE numero_serie = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, numeroDeSerie);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public Equipamento buscarEquipamentoPorNumeroSerie(String numeroDeSerie) throws SQLException {
        String sql = "SELECT id_equipamento, nome_equipamento, id_categoria, numero_serie, data_aquisicao, descricao_servico, id_status, id_responsavel FROM Equipamentos WHERE numero_serie = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Equipamento equipamento = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, numeroDeSerie);
            rs = stmt.executeQuery();

            if (rs.next()) {
                equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                
                int idCategoria = rs.getInt("id_categoria");
                String nomeCategoria = categoriaEquipamentoDAO.getNomeCategoriaById(idCategoria);
                equipamento.setCategoria(nomeCategoria);

                equipamento.setNumeroDeSerie(rs.getString("numero_serie"));
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                
                equipamento.setIdStatus(rs.getInt("id_status")); 
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) { // Verifica se o valor era NULL no banco
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamento;
    }

    public Equipamento buscarEquipamentoPorId(int idEquipamento) throws SQLException {
        String sql = "SELECT id_equipamento, nome_equipamento, id_categoria, numero_serie, data_aquisicao, descricao_servico, id_status, id_responsavel FROM Equipamentos WHERE id_equipamento = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Equipamento equipamento = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idEquipamento);
            rs = stmt.executeQuery();

            if (rs.next()) {
                equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                
                int idCategoria = rs.getInt("id_categoria");
                String nomeCategoria = categoriaEquipamentoDAO.getNomeCategoriaById(idCategoria);
                equipamento.setCategoria(nomeCategoria);

                equipamento.setNumeroDeSerie(rs.getString("numero_serie"));
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                
                equipamento.setIdStatus(rs.getInt("id_status")); 
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) { // Verifica se o valor era NULL no banco
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamento;
    }

    public boolean atualizarEquipamento(Equipamento equipamento) throws SQLException {
        String sql = "UPDATE Equipamentos SET nome_equipamento = ?, id_categoria = ?, numero_serie = ?, data_aquisicao = ?, descricao_servico = ?, id_status = ?, id_responsavel = ? WHERE id_equipamento = ?";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao();

            int idCategoria = categoriaEquipamentoDAO.getIdCategoriaByNome(equipamento.getCategoria());
            if (idCategoria == 0) {
                System.err.println("Erro: Categoria '" + equipamento.getCategoria() + "' nao encontrada no banco de dados para atualizacao.");
                return false;
            }

            int idStatus = equipamento.getIdStatus();

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, equipamento.getNome());
            stmt.setInt(2, idCategoria);
            stmt.setString(3, equipamento.getNumeroDeSerie());
            stmt.setDate(4, Date.valueOf(equipamento.getDataAquisicao()));
            stmt.setString(5, equipamento.getDescricao());
            stmt.setInt(6, idStatus);

            // Lida com id_responsavel: se for nulo no objeto, seta NULL no banco.
            if (equipamento.getIdResponsavel() != null) {
                stmt.setInt(7, equipamento.getIdResponsavel());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }
            
            stmt.setInt(8, equipamento.getIdEquipamento());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public boolean excluirEquipamento(String numeroDeSerie) throws SQLException {
        String sql = "DELETE FROM Equipamentos WHERE numero_serie = ?";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, numeroDeSerie);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public List<Equipamento> listarTodosEquipamentos() throws SQLException {
        List<Equipamento> equipamentos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            
            int idStatusAtivo = 2; 

            // SQL para selecionar equipamentos com status "Ativo" (id_status = 2)
            String sql = "SELECT e.id_equipamento, e.nome_equipamento, ce.nome_categoria, e.numero_serie, e.data_aquisicao, e.descricao_servico, e.id_status, e.id_responsavel " +
                         "FROM Equipamentos e " +
                         "JOIN CategoriasEquipamento ce ON e.id_categoria = ce.id_categoria " +
                         "WHERE e.id_status = ? " + // Filtra por status Ativo (agora id=2)
                         "ORDER BY e.nome_equipamento";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, idStatusAtivo); // Define o ID do status Ativo como 2
            rs = stmt.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                equipamento.setCategoria(rs.getString("nome_categoria")); // Seta o nome da categoria
                equipamento.setNumeroDeSerie(rs.getString("numero_serie")); 
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                equipamento.setIdStatus(rs.getInt("id_status")); // Seta o ID do status
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) {
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
                equipamentos.add(equipamento);
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamentos;
    }

    public List<Equipamento> listarEquipamentosPorStatus(int statusId) throws SQLException {
        List<Equipamento> equipamentos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            String sql = "SELECT e.id_equipamento, e.nome_equipamento, ce.nome_categoria, e.numero_serie, e.data_aquisicao, e.descricao_servico, e.id_status, e.id_responsavel " +
                         "FROM Equipamentos e " +
                         "JOIN CategoriasEquipamento ce ON e.id_categoria = ce.id_categoria " +
                         "WHERE e.id_status = ? " + // Filtra pelo status desejado
                         "ORDER BY e.nome_equipamento";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, statusId); 
            rs = stmt.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                equipamento.setCategoria(rs.getString("nome_categoria"));
                equipamento.setNumeroDeSerie(rs.getString("numero_serie")); 
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                equipamento.setIdStatus(rs.getInt("id_status"));
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) {
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
                equipamentos.add(equipamento);
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamentos;
    }

    public boolean atualizarStatusEquipamento(int idEquipamento, int novoIdStatus) throws SQLException {
        String sql = "UPDATE Equipamentos SET id_status = ? WHERE id_equipamento = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, novoIdStatus);
            stmt.setInt(2, idEquipamento);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
    }

    public List<Equipamento> listarTodosEquipamentosSemFiltroDeStatus() throws SQLException {
        List<Equipamento> equipamentos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            String sql = "SELECT e.id_equipamento, e.nome_equipamento, ce.nome_categoria, e.numero_serie, e.data_aquisicao, e.descricao_servico, e.id_status, se.nome_status, e.id_responsavel " +
                         "FROM Equipamentos e " +
                         "JOIN CategoriasEquipamento ce ON e.id_categoria = ce.id_categoria " +
                         "JOIN StatusEquipamento se ON e.id_status = se.id_status " +
                         "ORDER BY e.nome_equipamento";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                equipamento.setCategoria(rs.getString("nome_categoria"));
                equipamento.setNumeroDeSerie(rs.getString("numero_serie")); 
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                equipamento.setIdStatus(rs.getInt("id_status"));
                equipamento.setNomeStatus(rs.getString("nome_status")); // Adicionado para a tabela de consulta
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) {
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
                equipamentos.add(equipamento);
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamentos;
    }


    public List<Equipamento> consultarEquipamentos(Integer idCategoria, String termoEquipamento, LocalDate dataDe, LocalDate dataAte) throws SQLException {
        List<Equipamento> equipamentos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT e.id_equipamento, e.nome_equipamento, ce.nome_categoria, e.numero_serie, e.data_aquisicao, e.descricao_servico, e.id_status, se.nome_status, e.id_responsavel ");
            sqlBuilder.append("FROM Equipamentos e ");
            sqlBuilder.append("JOIN CategoriasEquipamento ce ON e.id_categoria = ce.id_categoria ");
            sqlBuilder.append("JOIN StatusEquipamento se ON e.id_status = se.id_status ");
            sqlBuilder.append("WHERE 1=1 "); // Cláusula sempre verdadeira para facilitar a adição de ANDs

            List<Object> parametros = new ArrayList<>();

            if (idCategoria != null && idCategoria > 0) { // Considera 0 como "selecione categoria" ou nulo
                sqlBuilder.append("AND e.id_categoria = ? ");
                parametros.add(idCategoria);
            }

            if (termoEquipamento != null && !termoEquipamento.isEmpty()) {
                // Tenta converter para int para buscar por ID, caso contrario busca por nome/numero de serie
                try {
                    int equipamentoId = Integer.parseInt(termoEquipamento);
                    sqlBuilder.append("AND e.id_equipamento = ? ");
                    parametros.add(equipamentoId);
                } catch (NumberFormatException e) {
                    sqlBuilder.append("AND (e.nome_equipamento LIKE ? OR e.numero_serie LIKE ?) ");
                    parametros.add("%" + termoEquipamento + "%");
                    parametros.add("%" + termoEquipamento + "%");
                }
            }

            if (dataDe != null) {
                sqlBuilder.append("AND e.data_aquisicao >= ? ");
                parametros.add(Date.valueOf(dataDe));
            }
            if (dataAte != null) {
                sqlBuilder.append("AND e.data_aquisicao <= ? ");
                parametros.add(Date.valueOf(dataAte));
            }

            sqlBuilder.append("ORDER BY e.nome_equipamento");
            
            stmt = connection.prepareStatement(sqlBuilder.toString());

            // Define os parametros na PreparedStatement
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                Equipamento equipamento = new Equipamento();
                equipamento.setIdEquipamento(rs.getInt("id_equipamento"));
                equipamento.setNome(rs.getString("nome_equipamento"));
                equipamento.setCategoria(rs.getString("nome_categoria"));
                equipamento.setNumeroDeSerie(rs.getString("numero_serie")); 
                equipamento.setDataAquisicao(rs.getDate("data_aquisicao").toLocalDate());
                equipamento.setDescricao(rs.getString("descricao_servico"));
                equipamento.setIdStatus(rs.getInt("id_status"));
                equipamento.setNomeStatus(rs.getString("nome_status"));
                
                int idResponsavel = rs.getInt("id_responsavel");
                if (rs.wasNull()) {
                    equipamento.setIdResponsavel(null);
                } else {
                    equipamento.setIdResponsavel(idResponsavel);
                }
                equipamentos.add(equipamento);
            }
        } finally {
            if (rs != null) { try { rs.close(); } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }}
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }}
            if (connection != null) { try { connection.close(); } catch (SQLException e) { System.err.println("Erro ao fechar Connection: " + e.getMessage()); }}
        }
        return equipamentos;
    }
}
