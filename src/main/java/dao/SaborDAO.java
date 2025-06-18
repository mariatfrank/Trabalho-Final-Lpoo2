package dao;

import model.Sabor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaborDAO {

        public boolean inserir(Sabor sabor) {
            String sql = "INSERT INTO sabor (nome, tipo) VALUES (?, ?)";

            try (Connection conn = Conexao.getConexao();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, sabor.getNome());
                stmt.setString(2, sabor.getTipo());
                stmt.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }




    public void atualizar(Sabor sabor) {
        String sql = "UPDATE sabor SET nome = ?, tipo = ? WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipo());
            stmt.setInt(3, sabor.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM sabor WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Sabor> listarTodos() {
        List<Sabor> lista = new ArrayList<>();
        String sql = "SELECT id, nome, tipo FROM sabor"; // tipo precisa estar presente

        try (Connection conn = Conexao.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Sabor sabor = new Sabor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo")
                );
                lista.add(sabor);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Sabor buscarPorId(int id) {
        String sql = "SELECT * FROM sabor WHERE id = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Sabor(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
