package model.forma;

public class Quadrado extends Forma {
    private double lado;

    public Quadrado(double lado) {
        super(lado);
        this.lado = lado;
    }

    @Override
    public double calcularArea() {
        return lado * lado;
    }

    @Override
    public String getDescricao() {
        return "Quadrado de lado " + lado;
    }

    @Override
    public String getTipo() {
        return "Quadrado";
    }

    public double getLado() {
        return lado;
    }
}
