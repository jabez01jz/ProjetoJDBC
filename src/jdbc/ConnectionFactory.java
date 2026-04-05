package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public static Connection createConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/loja";
        String user = "postgres";
        String password = "root";

        Connection conexao = DriverManager.getConnection(url, user, password);
        System.out.println("Conexão estabelecida com sucesso!");
        return conexao;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexão encerrada.");
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e);
            }
        }
    }
}