package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.util.HashMap;
import java.util.List;

public class Solucao {

    private static final int event_id = 0;
    private static final int event_schema = 1;
    private static final int event_action = 2;
    private static final int event_timestamp = 3;
    private static final int proposal_id = 4;

    private static final int proposal_loan_value = 5;
    private static final int proposal_number_of_monthly_installments = 6;

    private static final int warranty_id = 5;
    private static final int warranty_value = 6;
    private static final int warranty_province = 7;

    private static final int proponent_id = 5;
    private static final int proponent_name = 6;
    private static final int proponent_age = 7;
    private static final int proponent_monthly_income = 8;
    private static final int proponent_is_main = 9;

    private static final HashMap<String, Emprestimo> map = new HashMap<>();

    public static String processMessages(List<String> messages) {
        map.clear();

        messages.forEach(event -> {
            String[] proposta = event.split(",");

            String proposalId = proposta[proposal_id];
            if (!map.containsKey(proposalId)) {
                map.put(proposalId, new Emprestimo());
            }

            switch (proposta[event_schema]) {
                case "proposal":
                    proposal(proposta);
                    break;

                case "warranty":
                    warranty(proposta);
                    break;

                case "proponent":
                    proponent(proposta);
                    break;
            }

        });

        StringBuilder result = new StringBuilder();

        map.forEach((key, value) -> {
            if (value.avaliar()) {
                result.append(key).append(",");
            }
        });

        result.setLength(result.length() - 1);

        return result.toString();
    }


    public static void proposal(String[] proposta) {
        String proposalId = proposta[proposal_id];
        String eventAction = proposta[event_action];
        String proposalLoanValue = proposta[proposal_loan_value];
        String proposalNumberOfMonthly = proposta[proposal_number_of_monthly_installments];

        Emprestimo emprestimo = map.get(proposalId);

        switch (eventAction) {
            case "created":
            case "updated":
                emprestimo.setProposal(Double.parseDouble(proposalLoanValue));
                emprestimo.setPrazo(Integer.parseInt(proposalNumberOfMonthly));
                break;

            case "deleted":
                map.remove(proposalId);
                break;
        }
    }

    public static void warranty(String[] proposta) {
        String proposalId = proposta[proposal_id];
        String eventAction = proposta[event_action];
        String warrantyId = proposta[warranty_id];
        String warrantyValue = proposta[warranty_value];
        String warrantyProvince = proposta[warranty_province];

        Emprestimo emprestimo = map.get(proposalId);

        switch (eventAction) {
            case "added":
                Warranty warranty = new Warranty();
                warranty.setValor(Double.parseDouble(warrantyValue));
                warranty.setEstado(warrantyProvince);
                warranty.setId(warrantyId);
                emprestimo.getWarranties().add(warranty);
                break;

            case "updated":
                emprestimo.getWarranties().stream()
                        .filter(it -> it.getId().equalsIgnoreCase(warrantyId))
                        .findFirst().ifPresent(it -> {
                    it.setEstado(warrantyProvince);
                    it.setValor(Double.parseDouble(warrantyValue));
                });
                break;

            case "removed":
                emprestimo.getWarranties().removeIf(it -> it.getId().equalsIgnoreCase(warrantyId));
                break;
        }
    }

    public static void proponent(String[] proposta) {
        String proposalId = proposta[proposal_id];
        String proponentId = proposta[proponent_id];
        String proponentName = proposta[proponent_name];
        String proponentAge = proposta[proponent_age];
        String proponentMonthlyIncome = proposta[proponent_monthly_income];
        String proponentIsMain = proposta[proponent_is_main];

        Proponent proponent = new Proponent();
        proponent.setIdade(Integer.parseInt(proponentAge));
        proponent.setPrincipal(Boolean.parseBoolean(proponentIsMain));
        proponent.setMonthlyIncome(Double.parseDouble(proponentMonthlyIncome));
        proponent.setName(proponentName);
        proponent.setId(proponentId);

        Emprestimo emprestimo = map.get(proposalId);
        emprestimo.getProponents().add(proponent);
    }

}
