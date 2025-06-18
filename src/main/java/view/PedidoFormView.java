package view;
import controller.PedidoController;
import model.Pedido;
import javax.swing.*;
import java.awt.*;

public class PedidoFormView extends JDialog {
    private PedidoController controller;

    public PedidoFormView(Frame parent, Pedido pedido) {
        super(parent, true);
        controller = new PedidoController(this, pedido);
        setTitle(pedido == null ? "Novo Pedido" : "Editar Pedido");
        setSize(800, 550);
        setLocationRelativeTo(parent);
        controller.initComponents();
    }

    public void disposeView() {
        dispose();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PedidoFormView(null, null).setVisible(true));
    }
}