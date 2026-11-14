package br.com.starlog.factory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public final class ConexaoFactory {
    private final String url;

    public ConexaoFactory(Path arquivo) {
        this.url = "jdbc:sqlite:" + Objects.requireNonNull(arquivo).toAbsolutePath();
    }

    public Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
