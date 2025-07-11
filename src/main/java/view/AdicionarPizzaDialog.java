package view;

import controller.AdicionarPizzaController;
import model.Pizza;
import model.Sabor;

import javax.swing.*;
import java.awt.*;

public class AdicionarPizzaDialog extends JDialog {
    private JComboBox<String> comboForma;
    private JTextField campoDimensao;
    private JTextField campoArea;
    private JComboBox<Sabor> comboSabor1;
    private JComboBox<Sabor> comboSabor2;
    private JLabel lblPrecoPizza;
    private JButton btnConfirmar;

    private Pizza pizzaCriada;
    private AdicionarPizzaController controller;

    public AdicionarPizzaDialog(PedidoFormView parent, double precoSimples, double precoEspecial, double precoPremium) {
        this(parent, null, precoSimples, precoEspecial, precoPremium);
    }

    public AdicionarPizzaDialog(PedidoFormView parent, Pizza pizzaExistente, double precoSimples, double precoEspecial, double precoPremium) {
        super(parent, "Adicionar/Editar Pizza", true);
        setSize(400, 450);
        setLocationRelativeTo(parent);
        initComponents();
        controller = new AdicionarPizzaController(this, precoSimples, precoEspecial, precoPremium, pizzaExistente);
        registrarEventos();
    }

    private void initComponents() {
        JPanel painel = new JPanel(new GridLayout(10, 2, 10, 10));

        comboForma = new JComboBox<>(new String[]{"Circulo", "Quadrado", "Triangulo"});
        campoDimensao = new JTextField();
        campoArea = new JTextField();
        comboSabor1 = new JComboBox<>();
        comboSabor2 = new JComboBox<>();
        campoArea.setEditable(true);

        lblPrecoPizza = new JLabel("Preço: R$ 0.00", SwingConstants.CENTER);
        lblPrecoPizza.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar = new JButton("Confirmar");

        painel.add(new JLabel("Forma:"));
        painel.add(comboForma);
        painel.add(new JLabel("Raio/Lado (cm):"));
        painel.add(campoDimensao);
        painel.add(new JLabel("Área (cm²):"));
        painel.add(campoArea);
        painel.add(new JLabel("Sabor 1:"));
        painel.add(comboSabor1);
        painel.add(new JLabel("Sabor 2:"));
        painel.add(comboSabor2);
        painel.add(lblPrecoPizza);
        painel.add(btnConfirmar);

        add(painel);
    }

    private void registrarEventos() {
        comboForma.addActionListener(e -> controller.calcularPrecoAutomatico());
        comboSabor1.addActionListener(e -> controller.calcularPrecoAutomatico());
        comboSabor2.addActionListener(e -> controller.calcularPrecoAutomatico());

        campoDimensao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                controller.calcularAreaPorDimensao();
            }
        });

        campoArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                controller.calcularDimensaoPorArea();
            }
        });

        btnConfirmar.addActionListener(e -> controller.confirmarPizza());
    }
    public JComboBox<String> getComboForma() {
        return comboForma;
    }

    public JTextField getCampoDimensao() {
        return campoDimensao;
    }

    public JTextField getCampoArea() {
        return campoArea;
    }

    public JComboBox<Sabor> getComboSabor1() {
        return comboSabor1;
    }

    public JComboBox<Sabor> getComboSabor2() {
        return comboSabor2;
    }

    public JLabel getLblPrecoPizza() {
        return lblPrecoPizza;
    }

    public void setPizzaCriada(Pizza pizza) {
        this.pizzaCriada = pizza;
    }

    public Pizza getPizzaCriada() {
        return pizzaCriada;
    }
}
