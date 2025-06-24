package controller;

import dao.SaborDAO;
import dao.TipoPizzaDAO;
import model.Pizza;
import model.Sabor;
import model.forma.Circulo;
import model.forma.Quadrado;
import model.forma.Triangulo;
import view.AdicionarPizzaDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AdicionarPizzaController {

    private AdicionarPizzaDialog view;
    private SaborDAO saborDAO = new SaborDAO();

    private double precoSimples;
    private double precoEspecial;
    private double precoPremium;

    public AdicionarPizzaController(AdicionarPizzaDialog view, double precoSimples, double precoEspecial, double precoPremium, Pizza pizzaEditando) {
        this.view = view;
        this.precoSimples = precoSimples;
        this.precoEspecial = precoEspecial;
        this.precoPremium = precoPremium;
        carregarSabores();
        if (pizzaEditando != null) preencherCamposEdicao(pizzaEditando);
    }

    private void carregarSabores() {
        JComboBox<Sabor> combo1 = view.getComboSabor1();
        JComboBox<Sabor> combo2 = view.getComboSabor2();
        combo1.removeAllItems();
        combo2.removeAllItems();
        for (Sabor s : saborDAO.listarTodos()) {
            combo1.addItem(s);
            combo2.addItem(s);
        }
    }

    public void calcularAreaPorDimensao() {
        try {
            String forma = (String) view.getComboForma().getSelectedItem();
            String textoDim = view.getCampoDimensao().getText().trim();
            if (textoDim.isEmpty()) return;

            double valor = Double.parseDouble(textoDim);
            double area = calcularArea(forma, valor);
            view.getCampoArea().setText(String.format("%.2f", area));
            calcularPrecoAutomatico();
        } catch (Exception e) {
            view.getCampoArea().setText("");
            view.getLblPrecoPizza().setText("Preço: R$ 0.00");
        }
    }

    public void calcularDimensaoPorArea() {
        try {
            String forma = (String) view.getComboForma().getSelectedItem();
            String textoArea = view.getCampoArea().getText().trim();
            if (textoArea.isEmpty()) return;

            double area = Double.parseDouble(textoArea);
            double valor = calcularDimensao(forma, area);
            view.getCampoDimensao().setText(String.format("%.2f", valor));
            calcularPrecoAutomatico();
        } catch (Exception e) {
            view.getCampoDimensao().setText("");
            view.getLblPrecoPizza().setText("Preço: R$ 0.00");
        }
    }

    private double calcularArea(String forma, double valor) throws Exception {
        return switch (forma) {
            case "Circulo" -> {
                if (valor < 7 || valor > 23) throw new Exception("Raio fora do intervalo (7-23 cm)");
                yield Math.PI * valor * valor;
            }
            case "Quadrado" -> {
                if (valor < 10 || valor > 40) throw new Exception("Lado fora do intervalo (10-40 cm)");
                yield valor * valor;
            }
            case "Triangulo" -> {
                if (valor < 20 || valor > 60) throw new Exception("Lado fora do intervalo (20-60 cm)");
                yield (Math.sqrt(3) / 4) * valor * valor;
            }
            default -> throw new Exception("Forma inválida.");
        };
    }

    private double calcularDimensao(String forma, double area) throws Exception {
        return switch (forma) {
            case "Circulo" -> Math.sqrt(area / Math.PI);
            case "Quadrado" -> Math.sqrt(area);
            case "Triangulo" -> Math.sqrt(area * 4 / Math.sqrt(3));
            default -> throw new Exception("Forma inválida.");
        };
    }

    public void calcularPrecoAutomatico() {
        try {
            double area = Double.parseDouble(view.getCampoArea().getText());
            Sabor sabor1 = (Sabor) view.getComboSabor1().getSelectedItem();
            Sabor sabor2 = (Sabor) view.getComboSabor2().getSelectedItem();
            if (sabor1 != null && sabor2 != null) {
                double preco1 = getPrecoPorTipo(sabor1.getTipo());
                double preco2 = getPrecoPorTipo(sabor2.getTipo());
                double precoMedio = (preco1 + preco2) / 2.0;
                double precoFinal = precoMedio * area;
                view.getLblPrecoPizza().setText(String.format("Preço: R$ %.2f", precoFinal));
            }
        } catch (Exception e) {
            view.getLblPrecoPizza().setText("Preço: R$ 0.00");
        }
    }

    public void confirmarPizza() {
        try {
            String forma = (String) view.getComboForma().getSelectedItem();
            double valor = Double.parseDouble(view.getCampoDimensao().getText());
            double area = calcularArea(forma, valor); // valida o valor!

            Sabor sabor1 = (Sabor) view.getComboSabor1().getSelectedItem();
            Sabor sabor2 = (Sabor) view.getComboSabor2().getSelectedItem();

            if (sabor1 == null || sabor2 == null)
                throw new Exception("Selecione dois sabores.");

            List<Sabor> sabores = new ArrayList<>();
            sabores.add(sabor1);
            if (!sabor1.equals(sabor2)) sabores.add(sabor2);

            double preco1 = getPrecoPorTipo(sabor1.getTipo());
            double preco2 = getPrecoPorTipo(sabor2.getTipo());
            double precoMedio = (preco1 + preco2) / 2.0;

            Pizza pizza = new Pizza(0, null, sabores);
            switch (forma) {
                case "Circulo" -> pizza.setForma(new Circulo(valor));
                case "Quadrado" -> pizza.setForma(new Quadrado(valor));
                case "Triangulo" -> pizza.setForma(new Triangulo(valor));
            }

            pizza.setPreco(precoMedio * area);
            view.setPizzaCriada(pizza);
            view.dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Erro ao confirmar pizza: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double getPrecoPorTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "simples" -> precoSimples;
            case "especial" -> precoEspecial;
            case "premium" -> precoPremium;
            default -> precoSimples;
        };
    }

    private void preencherCamposEdicao(Pizza pizza) {
        String tipoForma = pizza.getForma().getClass().getSimpleName().toLowerCase();
        view.getComboForma().setSelectedItem(Character.toUpperCase(tipoForma.charAt(0)) + tipoForma.substring(1));
        view.getCampoDimensao().setText(String.format("%.2f", pizza.getForma().getDimensao()));
        view.getCampoArea().setText(String.format("%.2f", pizza.getForma().calcularArea()));
        view.getLblPrecoPizza().setText(String.format("Preço: R$ %.2f", pizza.getPreco()));

        List<Sabor> sabores = pizza.getSabores();
        if (!sabores.isEmpty()) view.getComboSabor1().setSelectedItem(sabores.get(0));
        if (sabores.size() > 1) view.getComboSabor2().setSelectedItem(sabores.get(1));
    }
}
