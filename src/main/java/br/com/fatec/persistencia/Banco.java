package br.com.fatec.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {
    // Informacoes de conexao (AJUSTE CONFORME SEU AMBIENTE MARIADB/XAMPP)
    private static final String URL = "jdbc:mariadb://localhost:3306/sistema_manutencao_equipamentos";
    private static final String USUARIO = "root"; // Usuario padrao do XAMPP MariaDB
    private static final String SENHA = "";       // Senha padrao do XAMPP MariaDB (geralmente vazia)

    /**
     * Obtem uma NOVA conexao com o banco de dados.
     *
     * @return Uma nova instancia de Connection.
     * @throws SQLException Se ocorrer um erro ao conectar ao banco de dados.
     */
    public static Connection obterConexao() throws SQLException {
        try {
            // Carrega o driver JDBC do MariaDB (pode ser omitido no JDBC 4.0+ mas e boa pratica para clareza)
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Driver JDBC do MariaDB nao encontrado. Verifique suas dependencias (pom.xml). " + e.getMessage());
            throw new SQLException("Driver JDBC do MariaDB nao encontrado.", e);
        }
        // Retorna uma nova conexao a cada chamada
        Connection connection = DriverManager.getConnection(URL, USUARIO, SENHA);
        System.out.println("Conexao com o banco de dados estabelecida."); // Log para depuracao
        return connection;
    }

    /**
     * Fecha uma conexao com o banco de dados.
     * Este metodo deve ser chamado no bloco finally de qualquer metodo que abra uma conexao.
     *
     * @param connection A conexao a ser fechada.
     */
    public static void desconectar(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexao com o banco de dados fechada."); // Log para depuracao
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexao com o banco de dados: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // public static Connection conectar() throws SQLException { /* ... */ }
    // public static void desconectar() { /* ... */ }
}
