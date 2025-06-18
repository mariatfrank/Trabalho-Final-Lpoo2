package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        setTitle("Tela Principal");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(new Color(10, 25, 50));
        painelPrincipal.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);

        JButton btnAreaCliente = criarBotao("Área do Cliente");
        JButton btnFazerPedido = criarBotao("Fazer um Pedido");
        JButton btnVisualizarPedidos = criarBotao("Visualizar Pedidos");
        JButton btnAtualizarPreco = criarBotao("Atualizar Preços por Cm2");
        JButton btnCadastrarSabores = criarBotao("Cadastrar Sabores");

        gbc.gridy = 0;
        painelPrincipal.add(btnAreaCliente, gbc);
        gbc.gridy = 1;
        painelPrincipal.add(btnFazerPedido, gbc);
        gbc.gridy = 2;
        painelPrincipal.add(btnVisualizarPedidos, gbc);
        gbc.gridy = 3;
        painelPrincipal.add(btnCadastrarSabores, gbc);
        gbc.gridy = 4;
        painelPrincipal.add(btnAtualizarPreco, gbc);

        btnAreaCliente.addActionListener(e -> {
            ClienteView clienteView = new ClienteView(this);
            clienteView.setVisible(true);
        });

        btnFazerPedido.addActionListener(e -> {
            PedidoFormView pedidoForm = new PedidoFormView(this, null);
            pedidoForm.setVisible(true);
        });

        btnVisualizarPedidos.addActionListener(e -> {
            VisualizarPedidosView visualizarPedidosView = new VisualizarPedidosView();
            visualizarPedidosView.setVisible(true);
        });

        btnAtualizarPreco.addActionListener(e -> {
            AtualizarPrecoView precoView = new AtualizarPrecoView(this);
            precoView.setVisible(true);
        });

        btnCadastrarSabores.addActionListener(e -> {
            CadastrarSaborView cadastrarSaborView = new CadastrarSaborView(this);
            cadastrarSaborView.setVisible(true);
        });


        add(painelPrincipal);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setPreferredSize(new Dimension(250, 50));
        botao.setFont(new Font("Arial", Font.BOLD, 16));
        botao.setBackground(Color.LIGHT_GRAY);
        botao.setFocusPainted(false);
        return botao;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainView mainView = new MainView();
            mainView.setVisible(true);
        });
    }
}
