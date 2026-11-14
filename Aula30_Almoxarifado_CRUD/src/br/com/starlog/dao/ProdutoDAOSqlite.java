package br.com.starlog.dao;

import br.com.starlog.factory.ConexaoFactory;
import br.com.starlog.model.Produto;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ProdutoDAOSqlite implements ProdutoDAO {
    private final ConexaoFactory conexoes;

    public ProdutoDAOSqlite() {
        this(Path.of("almoxarifado.db"));
    }

    public ProdutoDAOSqlite(Path arquivo) {
        this.conexoes = new ConexaoFactory(arquivo);
        String sql = """
                CREATE TABLE IF NOT EXISTS produtos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL,
                    estoque INTEGER NOT NULL
                );
                """;
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao criar a tabela produtos.", e);
        }
    }

    @Override
    public void salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, estoque) VALUES (?, ?, ?);";
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.executeUpdate();
            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (!chaves.next()) {
                    throw new SQLException("O driver não retornou o ID gerado.");
                }
                produto.setId(chaves.getLong(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao salvar produto.", e);
        }
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        String sql = "SELECT id, nome, preco, estoque FROM produtos WHERE id = ?;";
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearLinha(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao consultar produto por ID.", e);
        }
    }

    @Override
    public List<Produto> listarTodos() {
        String sql = "SELECT id, nome, preco, estoque FROM produtos ORDER BY nome ASC;";
        List<Produto> lista = new ArrayList<>();
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearLinha(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao listar produtos.", e);
        }
        return lista;
    }

    @Override
    public boolean atualizar(Produto produto) {
        if (produto.getId() == null) {
            return false;
        }

        String sql = "UPDATE produtos SET nome = ?, preco = ?, estoque = ? WHERE id = ?;";
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getEstoque());
            stmt.setLong(4, produto.getId());
            int afetadas = stmt.executeUpdate();
            return afetadas > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao atualizar produto.", e);
        }
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }

        String sql = "DELETE FROM produtos WHERE id = ?;";
        try (Connection conn = conexoes.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            int afetadas = stmt.executeUpdate();
            return afetadas > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Erro ao excluir produto.", e);
        }
    }

    private Produto mapearLinha(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getDouble("preco"),
                rs.getInt("estoque")
        );
    }
}
