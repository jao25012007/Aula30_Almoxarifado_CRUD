package br.com.starlog.model;

public final class Produto {
    private Long id;
    private final String nome;
    private double preco;
    private int estoque;

    public Produto(Long id, String nome, double preco, int estoque) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }
        this.id = id;
        this.nome = nome.trim();
        setPreco(preco);
        setEstoque(estoque);
    }

    public Produto(String nome, double preco, int estoque) {
        this(null, nome, preco, estoque);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        if (!Double.isFinite(preco) || preco <= 0.0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode ser negativo.");
        }
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return String.format("%-5s %-27s R$ %8.2f %8d", id, nome, preco, estoque);
    }
}
