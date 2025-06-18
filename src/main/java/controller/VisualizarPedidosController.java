package controller;

import dao.ClienteDAO;
import dao.PedidoDAO;
import dao.TipoPizzaDAO;
import model.Cliente;
import model.Pedido;
import model.Pizza;
import model.Sabor;
import model.TipoPizza;
import view.VisualizarPedidosView;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

public class VisualizarPedidosController {
    private VisualizarPedidosView view;
    private PedidoDAO pedidoDAO;
    private TipoPizzaDAO tipoPizzaDAO;
    private ClienteDAO clienteDAO;

    public VisualizarPedidosController(VisualizarPedidosView view) {
        this.view = view;
        this.pedidoDAO = new PedidoDAO();
        this.tipoPizzaDAO = new TipoPizzaDAO();
        this.clienteDAO = new ClienteDAO();
    }

    public void carregarPedidos() {
        view.modeloTabela.setRowCount(0);

        List<Pedido> pedidos = pedidoDAO.listarTodosComDetalhes();
        TipoPizza precos = tipoPizzaDAO.getPrecos();

        for (Pedido pedido : pedidos) {
            Cliente cliente = pedido.getCliente();

            for (Pizza pizza : pedido.getPizzas()) {
                double preco = pizza.calcularPreco(
                        precos.getPrecoSimples(),
                        precos.getPrecoEspecial(),
                        precos.getPrecoPremium()
                );

                view.modeloTabela.addRow(new Object[]{
                        pedido.getId(),
                        pizza.getId(),
                        cliente.getNome() + " " + cliente.getSobrenome(),
                        cliente.getTelefone(),
                        pizza.getForma().getClass().getSimpleName(),
                        pizza.getSabores().stream().map(Sabor::getNome).collect(Collectors.joining(", ")),
                        String.format("R$ %.2f", preco),
                        pizza.getEstado()
                });
            }
        }
    }

    public void salvarAlteracoes() {
        for (int i = 0; i < view.modeloTabela.getRowCount(); i++) {
            int pizzaId = (int) view.modeloTabela.getValueAt(i, 1);
            String novoEstado = (String) view.modeloTabela.getValueAt(i, 7);
            pedidoDAO.alterarEstadoPizza(pizzaId, novoEstado);
        }

        JOptionPane.showMessageDialog(view, "Estados atualizados com sucesso!");
    }
}
