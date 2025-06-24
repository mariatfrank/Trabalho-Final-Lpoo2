package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL;

    static {
        String currentDir = System.getProperty("user.dir");
        String relativePath = "/src/main/database/DBPizzaria.db";
        URL = "jdbc:sqlite:" + currentDir + relativePath;
    }

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
