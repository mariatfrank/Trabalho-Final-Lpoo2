package view;

import controller.AtualizarPrecoController;

import javax.swing.*;
import java.awt.*;

public class AtualizarPrecoView extends JFrame {
    public JTextField txtSimples;
    public JTextField txtEspecial;
    public JTextField txtPremium;
    public JButton btnSalvar;
    public JButton btnCancelar;

    private AtualizarPrecoController controller;

    public AtualizarPrecoView(JFrame parent) {
        setTitle("Atualizar Preços por cm²");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        controller = new AtualizarPrecoController(this);  // ← Aqui chamamos o controller

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Preço Simples (R$):"));
        txtSimples = new JTextField();
        panel.add(txtSimples);

        panel.add(new JLabel("Preço Especial (R$):"));
        txtEspecial = new JTextField();
        panel.add(txtEspecial);

        panel.add(new JLabel("Preço Premium (R$):"));
        txtPremium = new JTextField();
        panel.add(txtPremium);

        btnSalvar = new JButton("Salvar");
        panel.add(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        panel.add(btnCancelar);

        add(panel);

        controller.carregarPrecos(); // ← Carrega os preços iniciais do banco

        btnSalvar.addActionListener(e -> controller.salvarPrecos());
        btnCancelar.addActionListener(e -> dispose());
    }
}
