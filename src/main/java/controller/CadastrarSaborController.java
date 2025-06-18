package controller;

import dao.SaborDAO;
import model.Sabor;
import view.CadastrarSaborView;

import javax.swing.*;

public class CadastrarSaborController {
    private CadastrarSaborView view;
    private SaborDAO saborDAO;

    public CadastrarSaborController(CadastrarSaborView view) {
        this.view = view;
        this.saborDAO = new SaborDAO();
    }

    public void salvarSabor() {
        String nome = view.txtNome.getText().trim();
        String tipo = (String) view.cbTipo.getSelectedItem();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(view, "O nome do sabor é obrigatório!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Sabor novo = new Sabor();
        novo.setNome(nome);
        novo.setTipo(tipo);

        try {
            if (saborDAO.inserir(novo)) {
                JOptionPane.showMessageDialog(view, "Sabor cadastrado com sucesso!");
                view.dispose();
            } else {
                JOptionPane.showMessageDialog(view, "Erro ao cadastrar sabor.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Erro ao salvar sabor: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
