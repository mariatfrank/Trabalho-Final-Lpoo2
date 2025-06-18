package controller;

import dao.ClienteDAO;
import dao.PedidoDAO;
import dao.TipoPizzaDAO;
import model.*;
import view.AdicionarPizzaDialog;
import view.PedidoFormView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoController {
    private PedidoFormView view;
    private Pedido pedido;
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();
    private TipoPizzaDAO tipoPizzaDAO = new TipoPizzaDAO();

    private JComboBox<Cliente> comboClientes;
    private JFormattedTextField campoTelefone;
    private JTable tabelaPizzas;
    private DefaultTableModel modeloTabela;
    private JLabel lblPrecoTotal;
    private double precoSimples, precoEspecial, precoPremium;

    public PedidoController(PedidoFormView view, Pedido pedido) {
        this.view = view;
        this.pedido = (pedido != null) ? pedido : new Pedido(0, null);
        carregarPrecos();
    }

    private void carregarPrecos() {
        TipoPizza tp = tipoPizzaDAO.getPrecos();
        if (tp != null) {
            precoSimples = tp.getPrecoSimples();
            precoEspecial = tp.getPrecoEspecial();
            precoPremium = tp.getPrecoPremium();
        }
    }

    public void initComponents() {
        Color azulEscuro = new Color(10, 25, 50);
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(azulEscuro);

        JPanel painelCliente = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painelCliente.setBackground(azulEscuro);

        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setForeground(Color.WHITE);
        lblTelefone.setFont(new Font("Arial", Font.BOLD, 16));
        painelCliente.add(lblTelefone);

        try {
            MaskFormatter mask = new MaskFormatter("(##) #####-####");
            mask.setPlaceholderCharacter('_');
            campoTelefone = new JFormattedTextField(mask);
        } catch (Exception e) {
            campoTelefone = new JFormattedTextField();
        }
        campoTelefone.setColumns(10);
        campoTelefone.setFont(new Font("Arial", Font.PLAIN, 16));
        painelCliente.add(campoTelefone);

        JButton btnBuscarCliente = new JButton("Buscar");
        btnBuscarCliente.setBackground(new Color(200, 200, 255));
        btnBuscarCliente.setForeground(azulEscuro);
        btnBuscarCliente.addActionListener(e -> buscarClientePorTelefone());
        painelCliente.add(btnBuscarCliente);

        comboClientes = new JComboBox<>();
        comboClientes.setPreferredSize(new Dimension(200, 30));
        comboClientes.setFont(new Font("Arial", Font.PLAIN, 16));
        comboClientes.addActionListener(e -> carregarPedidoDoCliente((Cliente) comboClientes.getSelectedItem()));
        painelCliente.add(comboClientes);

        lblPrecoTotal = new JLabel("Preço Total: R$ 0,00");
        lblPrecoTotal.setForeground(Color.WHITE);
        lblPrecoTotal.setFont(new Font("Arial", Font.BOLD, 16));
        painelCliente.add(lblPrecoTotal);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Forma", "Sabores", "Preço", "Estado"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaPizzas = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaPizzas);

        JButton btnAddPizza = new JButton("Adicionar Pizza");
        btnAddPizza.addActionListener(e -> abrirDialogAdicionarPizza(null));

        JButton btnRemoverPizza = new JButton("Remover Pizza");
        btnRemoverPizza.addActionListener(e -> removerPizzaSelecionada());

        JButton btnAtualizarPizza = new JButton("Atualizar Pizza");
        btnAtualizarPizza.addActionListener(e -> atualizarPizzaSelecionada());

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarPedido());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> view.disposeView());

        JPanel painelBotoes = new JPanel(new FlowLayout());
        painelBotoes.setBackground(azulEscuro);
        painelBotoes.add(btnAddPizza);
        painelBotoes.add(btnRemoverPizza);
        painelBotoes.add(btnAtualizarPizza);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        painelPrincipal.add(painelCliente, BorderLayout.NORTH);
        painelPrincipal.add(scrollTabela, BorderLayout.CENTER);
        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        view.add(painelPrincipal);
        carregarPizzasNaTabela();
        atualizarPrecoTotal();
    }

    private void buscarClientePorTelefone() {
        String telefone = campoTelefone.getText().trim().replaceAll("[^\\d]", "");
        List<Cliente> clientes = clienteDAO.buscarTodosPorTelefone(telefone);
        comboClientes.removeAllItems();
        for (Cliente c : clientes) comboClientes.addItem(c);
        if (!clientes.isEmpty()) comboClientes.setSelectedIndex(0);
    }

    private void carregarPedidoDoCliente(Cliente cliente) {
        if (cliente == null) return;
        Pedido p = pedidoDAO.buscarPorClienteId(cliente.getId());
        pedido = (p != null) ? p : new Pedido(0, cliente);
        pedido.setCliente(cliente);
        carregarPizzasNaTabela();
        atualizarPrecoTotal();
    }

    private void abrirDialogAdicionarPizza(Pizza editarPizza) {
        AdicionarPizzaDialog dialog = (editarPizza == null)
                ? new AdicionarPizzaDialog(view, precoSimples, precoEspecial, precoPremium)
                : new AdicionarPizzaDialog(view, editarPizza, precoSimples, precoEspecial, precoPremium);

        dialog.setVisible(true);
        Pizza resultado = dialog.getPizzaCriada();

        if (resultado != null) {
            if (editarPizza == null) {
                resultado.setId(pedido.gerarNovoIdPizza());
                pedido.getPizzas().add(resultado);
            } else {
                int row = tabelaPizzas.getSelectedRow();
                resultado.setId(editarPizza.getId());
                pedido.getPizzas().set(row, resultado);
            }
            carregarPizzasNaTabela();
            atualizarPrecoTotal();
        }
    }

    private void removerPizzaSelecionada() {
        int row = tabelaPizzas.getSelectedRow();
        if (row >= 0) {
            pedido.getPizzas().remove(row);
            carregarPizzasNaTabela();
            atualizarPrecoTotal();
        }
    }

    private void atualizarPizzaSelecionada() {
        int row = tabelaPizzas.getSelectedRow();
        if (row >= 0) {
            Pizza pizza = pedido.getPizzas().get(row);
            abrirDialogAdicionarPizza(pizza);
        }
    }

    private void carregarPizzasNaTabela() {
        modeloTabela.setRowCount(0);
        for (Pizza pizza : pedido.getPizzas()) {
            double preco = pizza.calcularPreco(precoSimples, precoEspecial, precoPremium);
            modeloTabela.addRow(new Object[]{
                    pizza.getId(),
                    pizza.getForma().getClass().getSimpleName(),
                    pizza.getSabores().stream().map(Sabor::getNome).collect(Collectors.joining(", ")),
                    String.format("R$ %.2f", preco),
                    pizza.getEstado()
            });
        }
    }

    private void atualizarPrecoTotal() {
        double total = pedido.calcularTotal(precoSimples, precoEspecial, precoPremium);
        lblPrecoTotal.setText(String.format("Preço Total: R$ %.2f", total));
    }

    private void salvarPedido() {
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        if (cliente == null) {
            view.showMessage("Selecione um cliente.");
            return;
        }

        pedido.setCliente(cliente);
        pedido.setPrecoTotal(pedido.calcularTotal(precoSimples, precoEspecial, precoPremium));
        pedido.setEstado("Pedido Realizado");

        if (pedido.getId() == 0) {
            pedidoDAO.inserir(pedido);
        } else {
            pedidoDAO.atualizar(pedido);
        }

        view.showMessage("Pedido salvo com sucesso!");
        view.disposeView();
    }
}