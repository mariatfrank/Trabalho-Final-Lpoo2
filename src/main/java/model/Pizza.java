package model;

import model.forma.Forma;

import java.util.ArrayList;
import java.util.List;

public class Pizza {
    private int id;
    private Forma forma;
    private List<Sabor> sabores;
    private double preco;
    private String estado = "Pedido Realizado";


    public Pizza(int id, Forma forma, List<Sabor> sabores) {
        this.id = id;
        this.forma = forma;
        this.sabores = (sabores != null) ? sabores : new ArrayList<>();
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Forma getForma() {
        return forma;
    }

    public void setForma(Forma forma) {
        this.forma = forma;
    }

    public List<Sabor> getSabores() {
        return sabores;
    }

    public void setSabores(List<Sabor> sabores) {
        this.sabores = sabores;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }


    public double calcularPreco(double precoSimples, double precoEspecial, double precoPremium) {
        double area = forma.calcularArea();

        if (sabores == null || sabores.isEmpty()) return 0;

        double preco1 = getPrecoPorTipo(sabores.get(0).getTipo(), precoSimples, precoEspecial, precoPremium);
        double preco2 = sabores.size() == 2
                ? getPrecoPorTipo(sabores.get(1).getTipo(), precoSimples, precoEspecial, precoPremium)
                : preco1;

        double precoMedio = (preco1 + preco2) / 2.0;
        return area * precoMedio;
    }

    private double getPrecoPorTipo(String tipo, double ps, double pe, double pp) {
        return switch (tipo.toLowerCase()) {
            case "simples" -> ps;
            case "especial" -> pe;
            case "premium" -> pp;
            default -> ps;
        };
    }


}
