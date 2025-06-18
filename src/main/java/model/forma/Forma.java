package model.forma;

public abstract class Forma {
    protected double dimensao;

    public Forma(double dimensao) {
        this.dimensao = dimensao;
    }

    public double getDimensao() {
        return dimensao;
    }

    public void setDimensao(double dimensao) {
        this.dimensao = dimensao;
    }

    public abstract double calcularArea();

    public abstract String getDescricao();

    public abstract String getTipo();

    public double getMedida() {
        return this.dimensao;
    }
}
