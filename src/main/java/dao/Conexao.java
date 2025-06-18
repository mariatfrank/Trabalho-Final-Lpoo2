package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:sqlite:/Users/mariafrank/Downloads/ufpr/lpoo/lpoo2/PizzariaApp/src/main/database/DBPizzaria.db";

    public static Connection getConexao() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco: " + e.getMessage(), e);
        }
    }
}
