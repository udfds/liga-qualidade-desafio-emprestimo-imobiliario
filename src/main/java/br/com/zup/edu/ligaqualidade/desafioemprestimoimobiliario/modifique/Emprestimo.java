package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Emprestimo {

    private String id;
    private Double proposal;
    private int prazo;
    private List<Proponent> proponents;
    private List<Warranty> warranties;

    public Emprestimo() {
        this.proponents = new ArrayList<>();
        this.warranties = new ArrayList<>();
    }

    public Double getProposal() {
        return proposal;
    }

    public void setProposal(Double proposal) {
        this.proposal = proposal;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public List<Proponent> getProponents() {
        return proponents;
    }

    public void setProponents(List<Proponent> proponents) {
        this.proponents = proponents;
    }

    public List<Warranty> getWarranties() {
        return warranties;
    }

    public void setWarranties(List<Warranty> warranties) {
        this.warranties = warranties;
    }

    public boolean avaliar() {
        // O valor do empréstimo deve estar entre R$ 30.000,00 e R$ 3.000.000,00
        if (this.proposal < 30000 || this.proposal > 3000000) {
            return false;
        }

        // O empréstimo deve ser pago em no mínimo 2 anos e no máximo 15 anos
        if (this.prazo < 24 || this.prazo > 180) {
            return false;
        }

        // Deve haver no mínimo 2 proponentes por proposta
        if (this.proponents.size() < 2) {
            return false;
        }

        // Deve haver exatamente 1 proponente principal por proposta
        if (this.proponents.stream().filter(Proponent::isPrincipal).count() != 1) {
            return false;
        }

        // Todos os proponentes devem ser maiores de 18 anos
        if (this.proponents.stream().anyMatch(it -> it.getIdade() < 18)) {
            return false;
        }

        // Deve haver no mínimo 1 garantia de imóvel por proposta
        if (this.warranties.size() == 0) {
            return false;
        }

        // As garantias de imóvel dos estados PR, SC e RS não são aceitas
        // A soma do valor das garantias deve ser maior ou igual ao dobro do valor do empréstimo
        double value = this.warranties.stream().filter(Warranty::isValid).mapToDouble(Warranty::getValor).sum();
        if (value < (2*this.proposal)) {
            return false;
        }

        double parcela = this.proposal / this.prazo;
        AtomicBoolean isValid = new AtomicBoolean(false);
        this.proponents.stream().filter(Proponent::isPrincipal).findFirst().ifPresent(it -> {
            if (it.getIdade() <24) {
                isValid.set(it.getMonthlyIncome() > (4 * parcela));
            } else if (it.getIdade() < 50) {
                isValid.set(it.getMonthlyIncome() > (3 * parcela));
            } else {
                isValid.set(it.getMonthlyIncome() > (2 * parcela));
            }
        });

        return isValid.get();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

