package model;

public class TipoPizza {
    private double precoSimples;
    private double precoEspecial;
    private double precoPremium;

    public TipoPizza(double precoSimples, double precoEspecial, double precoPremium) {
        this.precoSimples = precoSimples;
        this.precoEspecial = precoEspecial;
        this.precoPremium = precoPremium;
    }

    public double getPrecoSimples() {
        return precoSimples;
    }

    public void setPrecoSimples(double precoSimples) {
        this.precoSimples = precoSimples;
    }

    public double getPrecoEspecial() {
        return precoEspecial;
    }

    public void setPrecoEspecial(double precoEspecial) {
        this.precoEspecial = precoEspecial;
    }

    public double getPrecoPremium() {
        return precoPremium;
    }

    public void setPrecoPremium(double precoPremium) {
        this.precoPremium = precoPremium;
    }
    @Override
    public String toString() {
        return String.format("Simples: R$ %.2f, Especial: R$ %.2f, Premium: R$ %.2f",
                precoSimples, precoEspecial, precoPremium);
    }

}
