package br.com.zup.edu.ligaqualidade.desafioemprestimoimobiliario.modifique;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Solucao {

    private static String CREATED = "created";

    private static double VALOR_MAXIMO_EMPRESTIMO = 3000000;

    private static double VALOR_MINIMO_EMPRESTIMO = 30000;

    private static int IDADE_MINIMA = 18;

    private static int MAXIMO_MES = 180;

    private static int MINIMO_MES = 24;

    public static String processMessages(List<String> messages) {

      List<String> resposta = messages.stream().map(event -> {
        String[] proposta = event.split(",");

        switch (proposta[1]) {
            case "proposal" :
                return proposal(proposta);

            case "warranty" :
               return warranty(proposta);

            case "proponent" :
                return proponent(proposta);
        }
          return null;
      }).collect(Collectors.toList());
        resposta.removeIf(Objects::isNull);
        return String.join(",", resposta);
  }


    static String proposal(String[] proposta) {
        double valorEmprestimo =  Double.parseDouble(proposta[5]);

        //regra:  O valor do empréstimo deve estar entre R$ 30.000,00 e R$ 3.000.000,00
        if (valorEmprestimo < VALOR_MINIMO_EMPRESTIMO || valorEmprestimo > VALOR_MAXIMO_EMPRESTIMO){
            return null;
        }
        //regra: O empréstimo deve ser pago em no mínimo 2 anos e no máximo 15 anos
        if (Integer.parseInt(proposta[6]) < MINIMO_MES || Integer.parseInt(proposta[6]) > MAXIMO_MES) {
            return null;
        }

        return proposta[4];
    }

    static String warranty(String[] proposta) {
        // if (proposta[])
        return null;
    }

    static String proponent(String[] proposta) {
        //regra: Todos os proponentes devem ser maiores de 18 anos
        if (Integer.parseInt(proposta[7]) < IDADE_MINIMA){
            return null;
        }
        return null;
    }

}