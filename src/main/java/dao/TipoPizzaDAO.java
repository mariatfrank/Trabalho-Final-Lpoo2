package dao;

import model.TipoPizza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoPizzaDAO {

    private static final int ID_UNICO = 1;

    public TipoPizzaDAO() {
        criarTabelaSeNaoExistir();
        inicializarPrecosPadraoSeNaoExistir();
    }

    // Cria a tabela tipo_pizza se não existir
    private void criarTabelaSeNaoExistir() {
        final String sql = """
            CREATE TABLE IF NOT EXISTS tipo_pizza (
                id INTEGER PRIMARY KEY CHECK (id = 1),
                preco_simples REAL,
                preco_especial REAL,
                preco_premium REAL
            )
            """;

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela tipo_pizza:");
            e.printStackTrace();
        }
    }

    // Insere preços padrão caso ainda não existam
    private void inicializarPrecosPadraoSeNaoExistir() {
        final String sqlVerifica = "SELECT COUNT(*) FROM tipo_pizza WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica)) {

            stmtVerifica.setInt(1, ID_UNICO);
            ResultSet rs = stmtVerifica.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                final String sqlInsere = "INSERT INTO tipo_pizza (id, preco_simples, preco_especial, preco_premium) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmtInsere = conn.prepareStatement(sqlInsere)) {
                    stmtInsere.setInt(1, ID_UNICO);
                    stmtInsere.setDouble(2, 0.05);
                    stmtInsere.setDouble(3, 0.08);
                    stmtInsere.setDouble(4, 0.12);
                    stmtInsere.executeUpdate();
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao inicializar os preços padrão:");
            e.printStackTrace();
        }
    }

    // Retorna os preços atuais (sempre retorna 1 registro)
    public TipoPizza getPrecos() {
        final String sql = "SELECT * FROM tipo_pizza WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ID_UNICO);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new TipoPizza(
                        rs.getDouble("preco_simples"),
                        rs.getDouble("preco_especial"),
                        rs.getDouble("preco_premium")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erro ao obter preços da tabela tipo_pizza:");
            e.printStackTrace();
        }

        return new TipoPizza(0.05, 0.08, 0.12);
    }

    public void atualizar(TipoPizza tipoPizza) {
        final String sql = "UPDATE tipo_pizza SET preco_simples = ?, preco_especial = ?, preco_premium = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, tipoPizza.getPrecoSimples());
            stmt.setDouble(2, tipoPizza.getPrecoEspecial());
            stmt.setDouble(3, tipoPizza.getPrecoPremium());
            stmt.setInt(4, ID_UNICO);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar preços da tabela tipo_pizza:");
            e.printStackTrace();
        }
    }


}
