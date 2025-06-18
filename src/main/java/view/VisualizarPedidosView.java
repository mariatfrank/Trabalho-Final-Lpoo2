package view;

import controller.VisualizarPedidosController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultCellEditor;
import java.awt.*;

public class VisualizarPedidosView extends JFrame {
    public JTable tabela;
    public DefaultTableModel modeloTabela;
    public JButton btnSalvar;
    public JButton btnCancelar;
    private VisualizarPedidosController controller;

    public VisualizarPedidosView() {
        setTitle("Visualizar Pedidos");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(new String[]{
                "ID Pedido", "ID Pizza", "Cliente", "Telefone", "Forma", "Sabores", "Preço", "Estado"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        String[] estados = {"Pedido Realizado", "A Caminho", "Entregue"};
        JComboBox<String> comboEstado = new JComboBox<>(estados);
        tabela.getColumnModel().getColumn(7).setCellEditor(new DefaultCellEditor(comboEstado));

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancelar = new JButton("Cancelar");
        btnSalvar = new JButton("Salvar e Fechar");

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);
        add(painelBotoes, BorderLayout.SOUTH);

        controller = new VisualizarPedidosController(this);
        controller.carregarPedidos();

        btnSalvar.addActionListener(e -> {
            controller.salvarAlteracoes();
            dispose();
            new MainView().setVisible(true);
        });

        btnCancelar.addActionListener(e -> dispose());
    }
}
