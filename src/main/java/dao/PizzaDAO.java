package dao;

import model.Pizza;
import model.forma.Forma;
import model.Sabor;
import model.forma.Circulo;
import model.forma.Quadrado;
import model.forma.Triangulo;
import dao.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PizzaDAO {

    // Atualizar pizza (ex: preço ou forma/dimensão)
    public void atualizar(Pizza pizza) {
        String sql = "UPDATE pizza SET forma = ?, dimensao = ?, preco = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pizza.getForma().getClass().getSimpleName());
            stmt.setDouble(2, pizza.getForma().getDimensao());
            stmt.setDouble(3, pizza.getPreco());
            stmt.setInt(4, pizza.getId());

            stmt.executeUpdate();

            // Atualizar sabores: para simplificar, exclui os antigos e insere os novos
            String delSabores = "DELETE FROM pizza_sabor WHERE pizza_id = ?";
            try (PreparedStatement stmtDel = conn.prepareStatement(delSabores)) {
                stmtDel.setInt(1, pizza.getId());
                stmtDel.executeUpdate();
            }

            String insSabores = "INSERT INTO pizza_sabor (pizza_id, sabor_id) VALUES (?, ?)";
            try (PreparedStatement stmtIns = conn.prepareStatement(insSabores)) {
                for (Sabor sabor : pizza.getSabores()) {
                    stmtIns.setInt(1, pizza.getId());
                    stmtIns.setInt(2, sabor.getId());
                    stmtIns.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Buscar pizza por ID (com sabores)
    public Pizza buscarPorId(int id) {
        String sqlPizza = "SELECT * FROM pizza WHERE id = ?";
        String sqlSabores = "SELECT s.id, s.nome, s.tipo FROM pizza_sabor ps JOIN sabor s ON ps.sabor_id = s.id WHERE ps.pizza_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPizza = conn.prepareStatement(sqlPizza);
             PreparedStatement stmtSabores = conn.prepareStatement(sqlSabores)) {

            stmtPizza.setInt(1, id);
            ResultSet rsPizza = stmtPizza.executeQuery();

            if (rsPizza.next()) {
                Forma forma = null;
                List<Sabor> sabores = new ArrayList<>();
                Pizza pizza = new Pizza(id,forma,sabores);
                pizza.setId(id);

                String formaStr = rsPizza.getString("forma");
                double dimensao = rsPizza.getDouble("dimensao");
                double preco = rsPizza.getDouble("preco");

                // Criar forma correta
                switch (formaStr) {
                    case "Quadrado":
                        pizza.setForma(new Quadrado(dimensao));
                        break;
                    case "Triangulo":
                        pizza.setForma(new Triangulo(dimensao));
                        break;
                    case "Circulo":
                        pizza.setForma(new Circulo(dimensao));
                        break;
                    default:
                        pizza.setForma(null);
                }

                pizza.setPreco(preco);

                // Buscar sabores
                stmtSabores.setInt(1, id);
                ResultSet rsSabores = stmtSabores.executeQuery();

                //List<Sabor> sabores = new ArrayList<>();
                while (rsSabores.next()) {
                    sabores.add(new Sabor(
                            rsSabores.getInt("id"),
                            rsSabores.getString("nome"),
                            rsSabores.getString("tipo")
                    ));
                }
                pizza.setSabores(sabores);

                return pizza;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Excluir pizza (e sabores relacionados)
    public void excluir(int id) {
        String sqlPizzaSabor = "DELETE FROM pizza_sabor WHERE pizza_id = ?";
        String sqlPizza = "DELETE FROM pizza WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPizzaSabor = conn.prepareStatement(sqlPizzaSabor);
             PreparedStatement stmtPizza = conn.prepareStatement(sqlPizza)) {

            stmtPizzaSabor.setInt(1, id);
            stmtPizzaSabor.executeUpdate();

            stmtPizza.setInt(1, id);
            stmtPizza.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
