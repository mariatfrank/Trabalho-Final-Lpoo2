package view;

import controller.CadastrarSaborController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CadastrarSaborView extends JFrame {
    public JTextField txtNome;
    public JComboBox<String> cbTipo;
    public JButton btnSalvar;
    public JButton btnCancelar;

    private CadastrarSaborController controller;

    public CadastrarSaborView(JFrame parent) {
        setTitle("Cadastrar Sabor");
        setSize(350, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        controller = new CadastrarSaborController(this);  // Instancia o controller

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Nome do Sabor:"));
        txtNome = new JTextField();
        panel.add(txtNome);

        // Impede números no nome
        txtNome.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        panel.add(new JLabel("Tipo:"));
        cbTipo = new JComboBox<>(new String[]{"Simples", "Especial", "Premium"});
        panel.add(cbTipo);

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> controller.salvarSabor());
        panel.add(btnSalvar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panel.add(btnCancelar);

        add(panel);
    }
}
