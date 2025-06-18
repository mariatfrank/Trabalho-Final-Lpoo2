package view;

import controller.ClienteFormController;
import model.Cliente;

import javax.swing.*;
import java.awt.*;

public class ClienteFormDialog extends JDialog {
    private JTextField campoNome;
    private JTextField campoSobrenome;
    private JTextField campoTelefone;
    private boolean confirmado = false;

    private Cliente cliente;
    private ClienteFormController controller;

    public ClienteFormDialog(Cliente clienteExistente) {
        setTitle(clienteExistente == null ? "Adicionar Cliente" : "Atualizar Cliente");
        setModal(true);
        setLayout(new GridBagLayout());
        setSize(400, 250);
        setLocationRelativeTo(null);

        this.cliente = clienteExistente != null ? clienteExistente : new Cliente();
        this.controller = new ClienteFormController(this.cliente);

        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        campoNome.setText(cliente.getNome());

        JLabel lblSobrenome = new JLabel("Sobrenome:");
        campoSobrenome = new JTextField(20);
        campoSobrenome.setText(cliente.getSobrenome());

        JLabel lblTelefone = new JLabel("Telefone:");
        campoTelefone = new JTextField(20);
        campoTelefone.setText(cliente.getTelefone());

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblNome, gbc);
        gbc.gridx = 1;
        add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblSobrenome, gbc);
        gbc.gridx = 1;
        add(campoSobrenome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblTelefone, gbc);
        gbc.gridx = 1;
        add(campoTelefone, gbc);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(btnCancelar, gbc);
        gbc.gridx = 1;
        add(btnSalvar, gbc);

        btnCancelar.addActionListener(e -> dispose());

        btnSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            String sobrenome = campoSobrenome.getText().trim();
            String telefone = campoTelefone.getText().trim();

            if (controller.validarCampos(nome, sobrenome, telefone)) {
                cliente.setNome(nome);
                cliente.setSobrenome(sobrenome);
                cliente.setTelefone(controller.formatarTelefone(telefone));
                confirmado = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, controller.getMensagemErro(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
