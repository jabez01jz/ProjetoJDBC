package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Connection conn = null;

        try {
            conn = ConnectionFactory.createConnection();
            int opcao = -1;

            while (opcao != 0) {
                System.out.println("\n===== MENU =====");
                System.out.println("1 - Inserir funcionário");
                System.out.println("2 - Listar funcionários");
                System.out.println("3 - Atualizar funcionário");
                System.out.println("4 - Deletar funcionário");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();

                switch (opcao) {
                    case 1:
                        inserir(conn);
                        break;
                    case 2:
                        listar(conn);
                        break;
                    case 3:
                        atualizar(conn);
                        break;
                    case 4:
                        deletar(conn);
                        break;
                    case 0:
                        System.out.println("Encerrando o programa...");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro: " + e);
        } finally {
            ConnectionFactory.closeConnection(conn);
        }
    }

    // --- INSERIR ---
    public static void inserir(Connection conn) throws SQLException {
        scanner.nextLine();
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        System.out.print("Salário: ");
        double salario = scanner.nextDouble();

        String sql = "INSERT INTO funcionario (nome, sobrenome, idade, salario) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nome);
        ps.setString(2, sobrenome);
        ps.setInt(3, idade);
        ps.setDouble(4, salario);
        ps.executeUpdate();
        System.out.println("Funcionário inserido com sucesso!");
    }

    // --- LISTAR ---
    public static void listar(Connection conn) throws SQLException {
        String sql = "SELECT codigo, nome, sobrenome, idade, salario FROM funcionario";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- Lista de Funcionários ---");
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
            System.out.println("Nenhum funcionário cadastrado.");
        }
    }

    // --- ATUALIZAR ---
    public static void atualizar(Connection conn) throws SQLException {
        listar(conn);
        System.out.print("\nDigite o código do funcionário que deseja atualizar: ");
        int codigo = scanner.nextInt();
        scanner.nextLine();

        System.out.println("O que deseja atualizar?");
        System.out.println("1 - Nome");
        System.out.println("2 - Sobrenome");
        System.out.println("3 - Idade");
        System.out.println("4 - Salário");
        System.out.print("Escolha: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        String sql = "";
        PreparedStatement ps = null;

        switch (opcao) {
            case 1:
                System.out.print("Novo nome: ");
                String novoNome = scanner.nextLine();
                sql = "UPDATE funcionario SET nome = ? WHERE codigo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, novoNome);
                ps.setInt(2, codigo);
                break;
            case 2:
                System.out.print("Novo sobrenome: ");
                String novoSobrenome = scanner.nextLine();
                sql = "UPDATE funcionario SET sobrenome = ? WHERE codigo = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, novoSobrenome);
                ps.setInt(2, codigo);
                break;
            case 3:
                System.out.print("Nova idade: ");
                int novaIdade = scanner.nextInt();
                sql = "UPDATE funcionario SET idade = ? WHERE codigo = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, novaIdade);
                ps.setInt(2, codigo);
                break;
            case 4:
                System.out.print("Novo salário: ");
                double novoSalario = scanner.nextDouble();
                sql = "UPDATE funcionario SET salario = ? WHERE codigo = ?";
                ps = conn.prepareStatement(sql);
                ps.setDouble(1, novoSalario);
                ps.setInt(2, codigo);
                break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        ps.executeUpdate();
        System.out.println("Funcionário atualizado com sucesso!");
    }

    // --- DELETAR ---
    public static void deletar(Connection conn) throws SQLException {
        listar(conn);
        System.out.print("\nDigite o código do funcionário que deseja deletar: ");
        int codigo = scanner.nextInt();

        String sql = "DELETE FROM funcionario WHERE codigo = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, codigo);
        ps.executeUpdate();
        System.out.println("Funcionário deletado com sucesso!");
    }
}