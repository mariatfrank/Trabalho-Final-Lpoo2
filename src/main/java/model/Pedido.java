package model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private List<Pizza> pizzas;
    private double precoTotal;
    private String estado; // aberto, a caminho, entregue

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.pizzas = new ArrayList<>();
        this.estado = "aberto";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }


    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double calcularTotal(double precoSimples, double precoEspecial, double precoPremium) {
        return pizzas.stream()
                .mapToDouble(p -> p.calcularPreco(precoSimples, precoEspecial, precoPremium))
                .sum();
    }


    public int gerarNovoIdPizza() {
        return pizzas.stream()
                .mapToInt(Pizza::getId)
                .max()
                .orElse(0) + 1;
    }

}
