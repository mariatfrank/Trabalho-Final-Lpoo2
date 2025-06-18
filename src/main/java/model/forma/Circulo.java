package model.forma;

public class Circulo extends Forma {
    private double raio;

    public Circulo(double raio) {
        super(raio);
        this.raio = raio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * raio * raio;
    }

    @Override
    public String getDescricao() {
        return "Círculo de raio " + raio;
    }

    @Override
    public String getTipo() {
        return "Círculo";
    }

    public double getRaio() {
        return raio;
    }
}
