package br.com.starlog.main;

import br.com.starlog.dao.ProdutoDAO;
import br.com.starlog.dao.ProdutoDAOSqlite;
import br.com.starlog.model.Produto;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class Main {
    public static void main(String[] args) throws IOException {
        Path banco = Files.createTempDirectory("almoxarifado-").resolve("almoxarifado.db");
        ProdutoDAO dao = new ProdutoDAOSqlite(banco);
        System.out.println("Banco da demonstração: " + banco);

        Produto teclado = new Produto("Teclado Mecânico RGB", 259.90, 10);
        Produto mousepad = new Produto("Mousepad Speed XL", 69.90, 20);
        dao.salvar(teclado);
        dao.salvar(mousepad);
        System.out.printf("Cadastrados: teclado (ID %d) e mousepad (ID %d).%n",
                teclado.getId(), mousepad.getId());

        listar("Produtos cadastrados", dao.listarTodos());

        teclado.setEstoque(15);
        teclado.setPreco(289.90);
        if (dao.atualizar(teclado)) {
            System.out.println("\nTeclado atualizado:");
            dao.buscarPorId(teclado.getId()).ifPresent(System.out::println);
        } else {
            System.out.println("Teclado não encontrado para atualização.");
        }

        boolean removido = dao.excluir(mousepad.getId());
        System.out.println(removido ? "\nMousepad removido." : "\nMousepad não encontrado.");

        boolean removido888 = dao.excluir(888L);
        System.out.println(removido888 ? "Produto 888 removido." : "Produto 888 não encontrado.");

        listar("Estoque final", dao.listarTodos());
    }

    private static void listar(String titulo, List<Produto> produtos) {
        System.out.printf("%n%s%n", titulo);
        System.out.printf("%-5s %-27s %-11s %8s%n", "ID", "Nome", "Preço", "Estoque");
        produtos.forEach(System.out::println);
        System.out.printf("Total de produtos: %d%n", produtos.size());
    }
}
