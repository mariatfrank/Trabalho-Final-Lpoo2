package controller;

import dao.TipoPizzaDAO;
import model.TipoPizza;
import view.AtualizarPrecoView;

import javax.swing.*;

public class AtualizarPrecoController {
    private AtualizarPrecoView view;
    private TipoPizzaDAO tipoPizzaDAO;

    public AtualizarPrecoController(AtualizarPrecoView view) {
        this.view = view;
        this.tipoPizzaDAO = new TipoPizzaDAO();
    }

    public void carregarPrecos() {
        TipoPizza precoAtual = tipoPizzaDAO.getPrecos();
        if (precoAtual != null) {
            view.txtSimples.setText(String.valueOf(precoAtual.getPrecoSimples()));
            view.txtEspecial.setText(String.valueOf(precoAtual.getPrecoEspecial()));
            view.txtPremium.setText(String.valueOf(precoAtual.getPrecoPremium()));
        } else {
            JOptionPane.showMessageDialog(view, "Erro ao carregar os preços. Verifique o banco de dados.");
        }
    }

    public void salvarPrecos() {
        try {
            double simples = Double.parseDouble(view.txtSimples.getText());
            double especial = Double.parseDouble(view.txtEspecial.getText());
            double premium = Double.parseDouble(view.txtPremium.getText());

            TipoPizza nova = new TipoPizza(simples, especial, premium);
            tipoPizzaDAO.atualizar(nova);

            JOptionPane.showMessageDialog(view, "Preços atualizados com sucesso!");
            view.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(view, "Digite valores numéricos válidos!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
