package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

public class Warranty {
    private double valor;
    private String estado;
    private String id;


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setId(String warrantyId) {
        this.id = warrantyId;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getId() {
        return this.id;
    }

    public boolean isValid() {
        switch (this.estado) {
            case "PR":
            case "SC":
            case "RS":
                return false;
            default:
                return true;
        }
    }
}
