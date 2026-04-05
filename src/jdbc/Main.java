package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = ConnectionFactory.createConnection();
            conn.setAutoCommit(false);

            // --- INSERT ---
            System.out.println("\n--- Inserindo registro ---");
            String sqlInsert = "INSERT INTO funcionario (nome, sobrenome, idade, salario) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sqlInsert);
            ps.setString(1, "Mario");
            ps.setString(2, "Corleone");
            ps.setInt(3, 28);
            ps.setDouble(4, 2322.39);
            ps.executeUpdate();
            conn.commit();
            System.out.println("Registro inserido com sucesso!");

            // --- SELECT após INSERT ---
            System.out.println("\n--- Lista após INSERT ---");
            listarFuncionarios(conn);

            // --- UPDATE ---
            System.out.println("\n--- Atualizando salário ---");
            String sqlUpdate = "UPDATE funcionario SET salario = ? WHERE nome = ?";
            ps = conn.prepareStatement(sqlUpdate);
            ps.setDouble(1, 5000.00);
            ps.setString(2, "Mario");
            int registrosAtualizados = ps.executeUpdate();
            conn.commit();
            System.out.println("Registros atualizados: " + registrosAtualizados);

            // --- SELECT após UPDATE ---
            System.out.println("\n--- Lista após UPDATE ---");
            listarFuncionarios(conn);

            // --- DELETE ---
            System.out.println("\n--- Deletando registro ---");
            String sqlDelete = "DELETE FROM funcionario WHERE nome = ?";
            ps = conn.prepareStatement(sqlDelete);
            ps.setString(1, "Mario");
            int registrosDeletados = ps.executeUpdate();
            conn.commit();
            System.out.println("Registros deletados: " + registrosDeletados);

            // --- SELECT após DELETE ---
            System.out.println("\n--- Lista após DELETE ---");
            listarFuncionarios(conn);

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Rollback efetuado na transação!");
                }
            } catch (SQLException e2) {
                System.out.println("Erro no rollback: " + e2);
            }
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            ConnectionFactory.closeConnection(conn);
        }
    }

    // Método separado para listar funcionários
    public static void listarFuncionarios(Connection conn) throws SQLException {
        String sqlSelect = "SELECT codigo, nome, sobrenome, idade, salario FROM funcionario";
        PreparedStatement ps = conn.prepareStatement(sqlSelect);
        ResultSet rs = ps.executeQuery();

        boolean temRegistros = false;
        while (rs.next()) {
            temRegistros = true;
            int codigo       = rs.getInt("codigo");
            String nome      = rs.getString("nome");
            String sobrenome = rs.getString("sobrenome");
            int idade        = rs.getInt("idade");
            double salario   = rs.getDouble("salario");

            System.out.printf("Código %d: %s %s - %d anos | Salário: R$ %.2f%n",
                    codigo, nome, sobrenome, idade, salario);
        }

        if (!temRegistros) {
            System.out.println("Nenhum registro encontrado.");
        }
    }
}