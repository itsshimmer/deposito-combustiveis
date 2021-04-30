package com.depositocombustiveis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class DepCombTest {

    // Testes construtor
    @ParameterizedTest
    @CsvSource({ 
        "500, 10000, 1250, 1250, NORMAL", 
        "250, 5000, 625, 625, NORMAL", 
        "249, 4999, 624, 624, SOBRAVISO",
        "125, 2500, 313, 313, SOBRAVISO", 
        "124, 2499, 311, 311, EMERGENCIA", 
        "0, 0, 0, 0, EMERGENCIA" 
    })
    public void DepComb(int aditivo, int gasolina, int alcool1, int alcool2, String situacao) {
        DepComb depcomb = new DepComb(aditivo, gasolina, alcool1, alcool2);

        DepComb.SITUACAO situacaoAssert = null;

        switch (situacao) {
        case "NORMAL":
            situacaoAssert = DepComb.SITUACAO.NORMAL;
            break;
        case "SOBRAVISO":
            situacaoAssert = DepComb.SITUACAO.SOBRAVISO;
            break;
        case "EMERGENCIA":
            situacaoAssert = DepComb.SITUACAO.EMERGENCIA;
            break;
        }

        Assertions.assertEquals(situacaoAssert, depcomb.getSituacao());
    }

    // Testes recebeAditivo
    @ParameterizedTest
    @CsvSource({ 
        "0, 500, 500, 500",
        "0, 0, 0, 0",
        "0, -1, -1, 0",
        "500, 500, 0, 500",
        "250, 350, 250, 500",
        "499, 1, 1, 500" 
    })
    public void recebeAditivo(int quantidadeInicial, int quantidadeRecebida, int quantidadeAbastecida,
            int quantidadeTotal) {
        DepComb depcomb = new DepComb(quantidadeInicial, 0, 0, 0);

        Assertions.assertEquals(quantidadeAbastecida, depcomb.recebeAditivo(quantidadeRecebida));
        Assertions.assertEquals(quantidadeTotal, depcomb.getAditivo());
    }

    // Testes recebeGasolina
    @ParameterizedTest
    @CsvSource({ 
        "0, 10000, 10000, 10000", 
        "0, 0, 0, 0", 
        "1, 1, 1, 2", 
        "250, -26, -1, 250", 
        "5000, 5000, 5000, 10000",
        "9000, 3125, 1000, 10000", 
        "9999, 1, 1, 10000", 
        "10000, 0, 0, 10000"
    })
    public void recebeGasolina(int quantidadeInicial, int quantidadeRecebida, int quantidadeAbastecida,
            int quantidadeTotal) {
        DepComb depcomb = new DepComb(0, quantidadeInicial, 0, 0);

        Assertions.assertEquals(quantidadeAbastecida, depcomb.recebeGasolina(quantidadeRecebida));
        Assertions.assertEquals(quantidadeTotal, depcomb.getGasolina());
    }

    // Testes recebeAlcool
    @ParameterizedTest
    @CsvSource({ 
        "0, 0, 2500, 2500, 2500",
        "0, 0, 0, 0, 0",
        "100, 100, -1, -1, 200",
        //"1, 1, 1, 1, 3",
        "750, 750, 3000, 1000, 2500",
        "1249, 1249, 3, 2, 2500",
        //"1250, 250, 1, 0, 2500"
    })
    public void recebeAlcool(int quantidadeInicial1, int quantidadeInicial2, int quantidadeRecebida,
            int quantidadeAbastecida, int quantidadeTotal) {
        DepComb depcomb = new DepComb(0, 0, quantidadeInicial1, quantidadeInicial2);

        Assertions.assertEquals(quantidadeAbastecida, depcomb.recebeAlcool(quantidadeRecebida));
        Assertions.assertEquals(quantidadeTotal, (depcomb.getAlcool1() + depcomb.getAlcool2()));
    }

    // Testes encomendas
    @ParameterizedTest
    @CsvSource({ 
        "500, 10000, 1250, 1250, 10, COMUM, 500, 9993, 1249, 1249",
        "250, 5000, 625, 625, 100, COMUM, 245, 4930, 613, 613",
        "249, 4999, 624, 624, 100, COMUM, 247, 4964, 618, 618",
        "249, 4999, 624, 624, 100, ESTRATEGICO, 244, 4929, 612, 612",
        "125, 2500, 313, 313, 100, COMUM, 123, 2465, 307, 307",
        "125, 2500, 313, 313, 100, ESTRATEGICO, 120, 2430, 301, 301",
        "124, 2499, 311, 311, 100, COMUM, -14, 0, 0, 0",
        "124, 2499, 311, 311, 100, ESTRATEGICO, 119, 2429, 299, 299", 
        "0, 100, 100, 100, 100, COMUM, -14, 0, 0, 0",
        "0, 100, 100, 100, 100, ESTRATEGICO, 0, 30, 88, 88", 
        "0, 100, 100, 100, -27, ESTRATEGICO, -7, 0, 0, 0",
        "0, 0, 0, 0, 100, ESTRATEGICO, -21, 0, 0, 0" 
    })
    public void encomendaCombustivel(int aditivo, int gasolina, int alcool1, int alcool2, int litrosPedido,
            String tipoPosto, int resultadoAditivo, int resultadoGasolina, int resultadoAlcool1, int resultadoAlcool2) {

        DepComb depcomb = new DepComb(aditivo, gasolina, alcool1, alcool2);

        DepComb.TIPOPOSTO tipoAssert = null;
        switch (tipoPosto) {
        case "COMUM":
            tipoAssert = DepComb.TIPOPOSTO.COMUM;
            break;
        case "ESTRATEGICO":
            tipoAssert = DepComb.TIPOPOSTO.ESTRATEGICO;
            break;
        }

        int[] resultado = depcomb.encomendaCombustivel(litrosPedido, tipoAssert);
        Assertions.assertEquals(resultadoAditivo, resultado[0]);
        Assertions.assertEquals(resultadoGasolina, resultado[1]);
        Assertions.assertEquals(resultadoAlcool1, resultado[2]);
        Assertions.assertEquals(resultadoAlcool2, resultado[3]);
    }
}
