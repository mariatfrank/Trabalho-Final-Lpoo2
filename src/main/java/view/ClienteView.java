
package view;

import controller.ClienteController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClienteView extends JDialog {
    private ClienteController controller;

    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    private JComboBox<String> filtroTipoComboBox;
    private JTextField campoFiltro;
    private JButton botaoAplicarFiltro;

    private JButton btnAdicionar;
    private JButton btnAtualizar;
    private JButton btnRemover;

    private final Color fundoJanela = new Color(0, 31, 63);
    private final Color fundoPainel = new Color(0, 43, 86);
    private final Color corBotao = new Color(10, 88, 165);
    private final Color corBotaoHover = new Color(32, 116, 220);
    private final Color corTextoBotao = Color.WHITE;
    private final Color corTextoCampo = Color.WHITE;
    private final Color corTabelaLinhaPar = new Color(0, 31, 63);
    private final Color corTabelaLinhaImpar = new Color(0, 38, 75);
    private final Color corTabelaSelecao = new Color(32, 116, 220);

    public ClienteView(Frame parent) {
        super(parent, "Cadastro de Clientes", true);
        this.controller = new ClienteController(this);

        initComponents();
        controller.configurarEventos();
        controller.carregarClientes();

        setSize(640, 420);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        getContentPane().setBackground(fundoJanela);
        setLayout(new BorderLayout());

        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.setBackground(fundoPainel);

        filtroTipoComboBox = new JComboBox<>(new String[]{"Sem filtro", "Por sobrenome", "Por telefone"});
        campoFiltro = new JTextField(15);
        campoFiltro.setBackground(new Color(20, 43, 77));
        campoFiltro.setForeground(corTextoCampo);
        campoFiltro.setCaretColor(Color.WHITE);

        botaoAplicarFiltro = criarBotao("Aplicar filtro");

        painelFiltro.add(filtroTipoComboBox);
        painelFiltro.add(campoFiltro);
        painelFiltro.add(botaoAplicarFiltro);

        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Sobrenome", "Telefone"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaClientes = new JTable(modeloTabela);
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaClientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(corTabelaSelecao);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(row % 2 == 0 ? corTabelaLinhaPar : corTabelaLinhaImpar);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaClientes);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        painelBotoes.setBackground(fundoPainel);

        btnAdicionar = criarBotao("Adicionar");
        btnAtualizar = criarBotao("Atualizar");
        btnRemover = criarBotao("Remover");

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);

        JButton btnVoltar = criarBotao("Voltar");
        btnVoltar.addActionListener(e -> dispose());
        painelBotoes.add(btnVoltar);

        add(painelFiltro, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(corBotao);
        botao.setForeground(corTextoBotao);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(corBotaoHover);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(corBotao);
            }
        });
        return botao;
    }

    public JTable getTabelaClientes() { return tabelaClientes; }
    public DefaultTableModel getModeloTabela() { return modeloTabela; }
    public JTextField getCampoFiltro() { return campoFiltro; }
    public JComboBox<String> getFiltroTipoComboBox() { return filtroTipoComboBox; }
    public JButton getBotaoAplicarFiltro() { return botaoAplicarFiltro; }
    public JButton getBtnAdicionar() { return btnAdicionar; }
    public JButton getBtnAtualizar() { return btnAtualizar; }
    public JButton getBtnRemover() { return btnRemover; }
}
