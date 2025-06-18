
package controller;

import dao.ClienteDAO;
import model.Cliente;
import view.ClienteFormDialog;
import view.ClienteView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ClienteController {
    private ClienteView view;
    private ClienteDAO clienteDAO;

    public ClienteController(ClienteView view) {
        this.view = view;
        this.clienteDAO = new ClienteDAO();
    }

    public void configurarEventos() {
        view.getTabelaClientes().getSelectionModel().addListSelectionListener(e -> {
            boolean selecionado = view.getTabelaClientes().getSelectedRow() >= 0;
            view.getBtnAtualizar().setEnabled(selecionado);
            view.getBtnRemover().setEnabled(selecionado);
        });

        view.getBotaoAplicarFiltro().addActionListener(e -> aplicarFiltro());

        view.getBtnAdicionar().addActionListener(e -> {
            ClienteFormDialog dialog = new ClienteFormDialog(null);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                clienteDAO.inserir(dialog.getCliente());
                carregarClientes();
            }
        });

        view.getBtnAtualizar().addActionListener(e -> atualizarCliente());

        view.getBtnRemover().addActionListener(e -> removerCliente());
    }

    private void aplicarFiltro() {
        String filtro = view.getCampoFiltro().getText().trim();
        String tipo = (String) view.getFiltroTipoComboBox().getSelectedItem();

        if (tipo.equals("Sem filtro") || filtro.isEmpty()) {
            carregarClientes();
        } else if (tipo.equals("Por sobrenome")) {
            atualizarTabela(clienteDAO.buscarPorFiltro("", filtro));
        } else if (tipo.equals("Por telefone")) {
            atualizarTabela(clienteDAO.buscarPorFiltro(filtro, ""));
        }
    }

    public void carregarClientes() {
        atualizarTabela(clienteDAO.listarTodos());
    }

    private void atualizarTabela(List<Cliente> lista) {
        DefaultTableModel modelo = view.getModeloTabela();
        modelo.setRowCount(0);
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNome(), c.getSobrenome(), c.getTelefone()});
        }
    }

    private void atualizarCliente() {
        int linha = view.getTabelaClientes().getSelectedRow();
        if (linha >= 0) {
            DefaultTableModel modelo = view.getModeloTabela();
            Cliente c = new Cliente(
                    (int) modelo.getValueAt(linha, 0),
                    (String) modelo.getValueAt(linha, 1),
                    (String) modelo.getValueAt(linha, 2),
                    (String) modelo.getValueAt(linha, 3)
            );

            ClienteFormDialog dialog = new ClienteFormDialog(c);
            dialog.setVisible(true);
            if (dialog.isConfirmado()) {
                clienteDAO.atualizar(dialog.getCliente());
                carregarClientes();
            }
        }
    }

    private void removerCliente() {
        int linha = view.getTabelaClientes().getSelectedRow();
        if (linha >= 0) {
            int id = (int) view.getModeloTabela().getValueAt(linha, 0);
            int resposta = JOptionPane.showConfirmDialog(view,
                    "Deseja remover este cliente?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);

            if (resposta == JOptionPane.YES_OPTION) {
                clienteDAO.excluir(id);
                carregarClientes();
            }
        }
    }
}
