package model.forma;

public class Triangulo extends Forma {
    private double lado;

    public Triangulo(double lado) {
        super(lado);
        this.lado = lado;
    }

    @Override
    public double calcularArea() {
        return (Math.sqrt(3) / 4) * lado * lado;
    }

    @Override
    public String getDescricao() {
        return "Triângulo equilátero de lado " + lado;
    }

    @Override
    public String getTipo() {
        return "Triângulo";
    }

    public double getLado() {
        return lado;
    }
}
