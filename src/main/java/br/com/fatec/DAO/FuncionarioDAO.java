package br.com.fatec.dao;

import br.com.fatec.persistencia.Banco;
import br.com.fatec.model.Funcionario;   
import org.mindrot.jbcrypt.BCrypt; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;         
import java.util.ArrayList;           
import java.util.List;                 

/**
 * @author Diego, Gustavo e Matheus
 */
public class FuncionarioDAO {

    private String hashSenha(String senhaPlana) {
        return BCrypt.hashpw(senhaPlana, BCrypt.gensalt());
    }


    public boolean adicionarFuncionario(Funcionario funcionario) throws SQLException {
        if (funcionario == null) {
            System.err.println("Erro: Objeto Funcionario e nulo.");
            return false;
        }

        // 1. Verificar se matricula ou e-mail ja existem (assumindo que sao UNIQUE no banco)
        if (existeFuncionarioPorMatriculaOuEmail(funcionario.getMatricula(), funcionario.getEmail())) {
            System.out.println("Erro: Funcionario com a mesma matricula ou e-mail ja existe.");
            return false; // Funcionario duplicado
        }

        String sql = "INSERT INTO funcionarios (nome, matricula, email, senha_hash, funcao, telefone, data_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = Banco.obterConexao(); // Obtem a conexao com o banco de dados

            String senhaPlana = funcionario.getSenhaHash(); 
            String senhaHashed = hashSenha(senhaPlana);    
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getMatricula());
            stmt.setString(3, funcionario.getEmail());
            stmt.setString(4, senhaHashed); 
            stmt.setString(5, funcionario.getFuncao());
            stmt.setString(6, funcionario.getTelefone());
            stmt.setTimestamp(7, funcionario.getDataCadastro() != null ?
                                   Timestamp.valueOf(funcionario.getDataCadastro()) :
                                   Timestamp.valueOf(java.time.LocalDateTime.now()));

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage());
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar Connection: " + e.getMessage());
                }
            }
        }
    }

    public Funcionario autenticar(String usuario, String senhaPlana) {
        String sql = "SELECT id_funcionario, nome, matricula, email, senha_hash, funcao, telefone, data_cadastro FROM funcionarios WHERE email = ? OR matricula = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Funcionario funcionario = null;

        try {
            connection = Banco.obterConexao(); 
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario); 
            stmt.setString(2, usuario); 
            rs = stmt.executeQuery();

            if (rs.next()) { 
                String senhaHashNoBanco = rs.getString("senha_hash");

                if (BCrypt.checkpw(senhaPlana, senhaHashNoBanco)) {
                    funcionario = new Funcionario();
                    funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
                    funcionario.setNome(rs.getString("nome"));
                    funcionario.setMatricula(rs.getString("matricula"));
                    funcionario.setEmail(rs.getString("email"));
                    funcionario.setFuncao(rs.getString("funcao"));
                    funcionario.setTelefone(rs.getString("telefone"));
                    
                    Timestamp dataCadastroTimestamp = rs.getTimestamp("data_cadastro");
                    if (dataCadastroTimestamp != null) {
                        funcionario.setDataCadastro(dataCadastroTimestamp.toLocalDateTime());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL na autenticacao: " + e.getMessage());
            e.printStackTrace(); 
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar Connection: " + e.getMessage());
                }
            }
        }
        return funcionario; 
    }

    public boolean existeFuncionarioPorMatriculaOuEmail(String matricula, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM funcionarios WHERE matricula = ? OR email = ?";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao(); 
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, matricula); 
            stmt.setString(2, email);    
            rs = stmt.executeQuery();    
            
            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
            return false; // Nao existe
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) { System.err.println("Erro ao fechar ResultSet: " + e.getMessage()); }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { System.err.println("Erro ao fechar PreparedStatement: " + e.getMessage()); }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erro ao fechar Connection: " + e.getMessage());
                }
            }
        }
    }

    //Lista todos os funcionarios
    public List<Funcionario> listarTodosFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT id_funcionario, nome, matricula, email, funcao, telefone, data_cadastro FROM funcionarios ORDER BY nome";
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = Banco.obterConexao();
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("id_funcionario"));
                funcionario.setNome(rs.getString("nome"));
                funcionario.setMatricula(rs.getString("matricula"));
                funcionario.setEmail(rs.getString("email"));
                funcionario.setFuncao(rs.getString("funcao"));
                funcionario.setTelefone(rs.getString("telefone"));
                
                Timestamp dataCadastroTimestamp = rs.getTimestamp("data_cadastro");
                if (dataCadastroTimestamp != null) {
                    funcionario.setDataCadastro(dataCadastroTimestamp.toLocalDateTime());
                }
                funcionarios.add(funcionario);
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
        return funcionarios;
    }
}
