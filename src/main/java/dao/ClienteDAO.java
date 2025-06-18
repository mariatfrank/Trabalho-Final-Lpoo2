package dao;

import model.Cliente;
import dao.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDAO {

    public void inserir(Cliente cliente) {
        String sql = "INSERT INTO cliente (nome, sobrenome, telefone) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, sobrenome = ?, telefone = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sqlPedidos = "DELETE FROM pedido WHERE cliente_id = ?";
        String sqlCliente = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmtPedidos = conn.prepareStatement(sqlPedidos);
             PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {

            conn.setAutoCommit(false);

            stmtPedidos.setInt(1, id);
            stmtPedidos.executeUpdate();

            stmtCliente.setInt(1, id);
            stmtCliente.executeUpdate();

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("sobrenome"),
                        rs.getString("telefone")
                );
                lista.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public List<Cliente> buscarTodosPorTelefone(String telefone) {
        String telefoneLimpo = telefone.replaceAll("[^\\d]", "");
        List<Cliente> todos = listarTodos();

        return todos.stream()
                .filter(c -> c.getTelefone().replaceAll("[^\\d]", "").contains(telefoneLimpo))
                .collect(Collectors.toList());
    }

    public List<Cliente> buscarPorFiltro(String telefone, String sobrenome) {
        List<Cliente> todos = listarTodos();
        String telefoneLimpo = telefone.replaceAll("[^\\d]", "").toLowerCase();
        String sobrenomeFiltro = sobrenome.toLowerCase();

        return todos.stream()
                .filter(c -> telefoneLimpo.isEmpty() || c.getTelefone().replaceAll("[^\\d]", "").contains(telefoneLimpo))
                .filter(c -> sobrenomeFiltro.isEmpty() || c.getSobrenome().toLowerCase().contains(sobrenomeFiltro))
                .collect(Collectors.toList());
    }

    public Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("sobrenome"),
                        rs.getString("telefone")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
