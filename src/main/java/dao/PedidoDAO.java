package dao;

import model.*;
import model.forma.Circulo;
import model.forma.Quadrado;
import model.forma.Triangulo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void inserir(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (cliente_id, preco_total, estado) VALUES (?, ?, ?)";
        String sqlPizza = "INSERT INTO pizza (pedido_id, forma, dimensao, preco, estado) VALUES (?, ?, ?, ?, ?)";
        String sqlPizzaSabor = "INSERT INTO pizza_sabor (pizza_id, sabor_id) VALUES (?, ?)";

        try (Connection conn = Conexao.getConexao()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement stmtPizza = conn.prepareStatement(sqlPizza, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement stmtPizzaSabor = conn.prepareStatement(sqlPizzaSabor)
            ) {
                // Inserir o pedido
                stmtPedido.setInt(1, pedido.getCliente().getId());
                stmtPedido.setDouble(2, pedido.getPrecoTotal());
                stmtPedido.setString(3, pedido.getEstado());
                stmtPedido.executeUpdate();

                ResultSet rsPedido = stmtPedido.getGeneratedKeys();
                if (rsPedido.next()) {
                    pedido.setId(rsPedido.getInt(1));
                }

                // Inserir pizzas
                for (Pizza pizza : pedido.getPizzas()) {
                    stmtPizza.setInt(1, pedido.getId());
                    stmtPizza.setString(2, pizza.getForma().getClass().getSimpleName());
                    stmtPizza.setDouble(3, pizza.getForma().getDimensao());
                    stmtPizza.setDouble(4, pizza.getPreco());
                    stmtPizza.setString(5, pizza.getEstado());
                    stmtPizza.executeUpdate();

                    ResultSet rsPizza = stmtPizza.getGeneratedKeys();
                    if (rsPizza.next()) {
                        pizza.setId(rsPizza.getInt(1));
                    }

                    for (Sabor sabor : pizza.getSabores()) {
                        stmtPizzaSabor.setInt(1, pizza.getId());
                        stmtPizzaSabor.setInt(2, sabor.getId());
                        stmtPizzaSabor.executeUpdate();
                    }
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erro ao inserir pedido: ");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Erro de conexão ao inserir pedido:");
            e.printStackTrace();
        }
    }

    public void atualizar(Pedido pedido) {
        excluir(pedido.getId());
        inserir(pedido);
    }

    public Pedido buscarPorClienteId(int clienteId) {
        List<Pedido> pedidos = buscarPorCliente(clienteId);
        return pedidos.isEmpty() ? null : pedidos.get(0);
    }

    public List<Pedido> buscarPorCliente(int clienteId) {
        List<Pedido> pedidos = new ArrayList<>();
        String sqlPedido = "SELECT * FROM pedido WHERE cliente_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido)) {

            stmtPedido.setInt(1, clienteId);
            ResultSet rsPedido = stmtPedido.executeQuery();

            while (rsPedido.next()) {
                int pedidoId = rsPedido.getInt("id");
                Pedido pedido = new Pedido(pedidoId, new ClienteDAO().buscarPorId(clienteId));
                pedido.setPrecoTotal(rsPedido.getDouble("preco_total"));
                pedido.setEstado(rsPedido.getString("estado"));
                pedido.setPizzas(buscarPizzasPorPedidoId(pedidoId));
                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pedidos por cliente:");
            e.printStackTrace();
        }

        return pedidos;
    }

    public List<Pedido> listarTodosComDetalhes() {
        List<Pedido> pedidos = new ArrayList<>();
        String sqlPedido = "SELECT * FROM pedido";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido);
             ResultSet rsPedido = stmtPedido.executeQuery()) {

            while (rsPedido.next()) {
                int pedidoId = rsPedido.getInt("id");
                int clienteId = rsPedido.getInt("cliente_id");

                Pedido pedido = new Pedido(pedidoId, new ClienteDAO().buscarPorId(clienteId));
                pedido.setPrecoTotal(rsPedido.getDouble("preco_total"));
                pedido.setEstado(rsPedido.getString("estado"));
                pedido.setPizzas(buscarPizzasPorPedidoId(pedidoId));

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar pedidos:");
            e.printStackTrace();
        }

        return pedidos;
    }

    private List<Pizza> buscarPizzasPorPedidoId(int pedidoId) {
        List<Pizza> pizzas = new ArrayList<>();
        String sqlPizza = "SELECT * FROM pizza WHERE pedido_id = ?";
        String sqlSabores = "SELECT s.id, s.nome, s.tipo FROM pizza_sabor ps JOIN sabor s ON ps.sabor_id = s.id WHERE ps.pizza_id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPizza = conn.prepareStatement(sqlPizza);
             PreparedStatement stmtSabores = conn.prepareStatement(sqlSabores)) {

            stmtPizza.setInt(1, pedidoId);
            ResultSet rsPizza = stmtPizza.executeQuery();

            while (rsPizza.next()) {
                Pizza pizza = new Pizza(0, null, new ArrayList<>());
                pizza.setId(rsPizza.getInt("id"));
                pizza.setPreco(rsPizza.getDouble("preco"));

                String forma = rsPizza.getString("forma");
                double dimensao = rsPizza.getDouble("dimensao");

                switch (forma) {
                    case "Circulo" -> pizza.setForma(new Circulo(dimensao));
                    case "Quadrado" -> pizza.setForma(new Quadrado(dimensao));
                    case "Triangulo" -> pizza.setForma(new Triangulo(dimensao));
                    default -> pizza.setForma(null);
                }

                stmtSabores.setInt(1, pizza.getId());
                ResultSet rsSabores = stmtSabores.executeQuery();
                while (rsSabores.next()) {
                    Sabor sabor = new Sabor(
                            rsSabores.getInt("id"),
                            rsSabores.getString("nome"),
                            rsSabores.getString("tipo")
                    );
                    pizza.getSabores().add(sabor);
                }

                pizza.setEstado(rsPizza.getString("estado"));
                pizzas.add(pizza);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar pizzas:");
            e.printStackTrace();
        }

        return pizzas;
    }

    public void excluir(Integer id) {
        String sqlDeletePizzaSabor = "DELETE FROM pizza_sabor WHERE pizza_id IN (SELECT id FROM pizza WHERE pedido_id = ?)";
        String sqlDeletePizzas = "DELETE FROM pizza WHERE pedido_id = ?";
        String sqlDeletePedido = "DELETE FROM pedido WHERE id = ?";

        try (Connection conn = Conexao.getConexao()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement stmt1 = conn.prepareStatement(sqlDeletePizzaSabor);
                    PreparedStatement stmt2 = conn.prepareStatement(sqlDeletePizzas);
                    PreparedStatement stmt3 = conn.prepareStatement(sqlDeletePedido)
            ) {
                stmt1.setInt(1, id);
                stmt1.executeUpdate();

                stmt2.setInt(1, id);
                stmt2.executeUpdate();

                stmt3.setInt(1, id);
                stmt3.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erro ao excluir pedido:");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Erro de conexão ao excluir pedido:");
            e.printStackTrace();
        }
    }

    public void alterarEstadoPizza(int pizzaId, String novoEstado) {
        String sql = "UPDATE pizza SET estado = ? WHERE id = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoEstado);
            stmt.setInt(2, pizzaId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao alterar estado da pizza:");
            e.printStackTrace();
        }
    }
}
