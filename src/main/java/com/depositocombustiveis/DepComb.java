package com.depositocombustiveis;

/**
 * DepComb
 */
public class DepComb {

    public enum SITUACAO {
        NORMAL, SOBRAVISO, EMERGENCIA
    }

    public enum TIPOPOSTO {
        COMUM, ESTRATEGICO
    }

    public static final int MAX_ADITIVO = 500;
    public static final int MAX_ALCOOL = 2500;
    public static final int MAX_GASOLINA = 10000;

    private SITUACAO situacao;
    private int gasolina;
    private int aditivo;
    private int alcool1;
    private int alcool2;

    public DepComb(int tAditivo, int tGasolina, int tAlcool1, int tAlcool2) {
        aditivo = tAditivo;
        gasolina = tGasolina;
        alcool1 = tAlcool1;
        alcool2 = tAlcool2;
        defineSituacao();
    }

    private void defineSituacao() {
        if (gasolina >= (MAX_GASOLINA * 0.5) && aditivo >= (MAX_ADITIVO * 0.5)
                && (alcool1 + alcool2) >= (MAX_ALCOOL * 0.5)) {
            situacao = SITUACAO.NORMAL;
            return;
        }
        if (gasolina >= (MAX_GASOLINA * 0.25) && aditivo >= (MAX_ADITIVO * 0.25)
                && (alcool1 + alcool2) >= (MAX_ALCOOL * 0.25)) {
            situacao = SITUACAO.SOBRAVISO;
            return;
        }
        situacao = SITUACAO.EMERGENCIA;
    }

    public SITUACAO getSituacao() {
        return situacao;
    }

    public int getGasolina() {
        return gasolina;
    }

    public int getAditivo() {
        return aditivo;
    }

    public int getAlcool1() {
        return alcool1;
    }

    public int getAlcool2() {
        return alcool2;
    }

    public int recebeAditivo(int quantidade) {
        if (quantidade < 0)
            return -1; // se valor invalido, retorna -1

        // se tanque nao esta lotado
        if (aditivo < MAX_ADITIVO) {
            int diff = MAX_ADITIVO - aditivo;
            // se valor a ser abastecido é menor que esta vazio no tanque
            if (quantidade <= diff) {
                // abastece o solicitado e retorna
                aditivo = aditivo + quantidade;
                return quantidade;
            }
            // abastece o maximo possivel, e retorna
            aditivo = MAX_ADITIVO;
            return diff;
        }
        return 0; // se tanque esta lotado retorna 0
    }

    public int recebeGasolina(int quantidade) {
        if (quantidade < 0)
            return -1; // se valor invalido, retorna -1

        // se tanque nao esta lotado
        if (gasolina < MAX_GASOLINA) {
            int diff = MAX_GASOLINA - gasolina;
            // se valor a ser abastecido é menor que esta vazio no tanque
            if (quantidade <= diff) {
                // abastece o solicitado e retorna
                gasolina = gasolina + quantidade;
                return quantidade;
            }
            // abastece o maximo possivel, e retorna
            gasolina = MAX_GASOLINA;
            return diff;
        }
        return 0; // se tanque esta lotado retorna 0
    }

    public int recebeAlcool(int quantidade) {
        if (quantidade < 0)
            return -1; // se valor invalido, retorna -1

        int totalAlcool = alcool1 + alcool2;

        // se tanque nao esta lotado
        if (totalAlcool < MAX_ALCOOL) {
            int diff = MAX_ALCOOL - totalAlcool;
            // se valor a ser abastecido é menor que esta vazio no tanque
            if (quantidade <= diff) {
                // abastece o solicitado e retorna
                alcool1 = alcool1 + (quantidade / 2);
                alcool2 = alcool2 + (quantidade / 2);
                return quantidade;
            }
            // abastece o maximo possivel, e retorna
            alcool1 = MAX_ALCOOL / 2;
            alcool2 = MAX_ALCOOL / 2;
            return diff;
        }
        return 0; // se tanque esta lotado retorna 0
    }

    private int[] efetuarEntrega(int gasolinaNecessaria, int alcoolNecessario, int aditivoNecessario) {
        int[] resultado = new int[4];
        resultado[0] = -21;
        if (gasolinaNecessaria <= gasolina && alcoolNecessario <= (alcool1 + alcool2) && aditivoNecessario <= aditivo) {
            gasolina = gasolina - gasolinaNecessaria;
            alcool1 = alcool1 - alcoolNecessario / 2;
            alcool2 = alcool2 - alcoolNecessario / 2;
            aditivo = aditivo - aditivoNecessario;
            resultado[0] = aditivo;
            resultado[1] = gasolina;
            resultado[2] = alcool1;
            resultado[3] = alcool2;
        }
        return resultado;
    }

    public int[] encomendaCombustivel(int quantidade, TIPOPOSTO tipoPosto) {
        int[] resultado = new int[4];
        // se invalido, retorna -7 no primeiro valor
        if (quantidade < 0) {
            resultado[0] = -7;
            return resultado;
        }
        // 5% de aditivo, 25% de álcool e 70% de gasolina pura AdAlGa
        int gasolinaNecessaria = (int) ((quantidade * 100) * 0.7) / 100;
        int alcoolNecessario = (int) ((quantidade * 100) * 0.25) / 100;
        int aditivoNecessario = (int) ((quantidade * 100) * 0.05) / 100;

        switch (situacao) {
        case NORMAL:
            // se a situacao é normal, atendemos todos os postos, por isso nao testamos
            return efetuarEntrega(gasolinaNecessaria, alcoolNecessario, aditivoNecessario);

        case SOBRAVISO:
            switch (tipoPosto) {
            case COMUM:
                gasolinaNecessaria = gasolinaNecessaria / 2;
                alcoolNecessario = alcoolNecessario / 2;
                aditivoNecessario = aditivoNecessario / 2;
                return efetuarEntrega(gasolinaNecessaria, alcoolNecessario, aditivoNecessario);

            case ESTRATEGICO:
                return efetuarEntrega(gasolinaNecessaria, alcoolNecessario, aditivoNecessario);
            }

        case EMERGENCIA:
            switch (tipoPosto) {
            case COMUM:
                resultado[0] = -14;
                return resultado;

            case ESTRATEGICO:
                if (gasolinaNecessaria <= gasolina && alcoolNecessario <= (alcool1 + alcool2)
                        && aditivoNecessario <= aditivo) {
                    gasolina = gasolina - gasolinaNecessaria;
                    alcool1 = alcool1 - alcoolNecessario / 2;
                    alcool2 = alcool2 - alcoolNecessario / 2;
                    aditivo = aditivo - aditivoNecessario;
                    resultado[0] = aditivo;
                    resultado[1] = gasolina;
                    resultado[2] = alcool1;
                    resultado[3] = alcool2;
                    return resultado;
                }

                if (gasolinaNecessaria <= gasolina && alcoolNecessario <= (alcool1 + alcool2)) {
                    gasolina = gasolina - gasolinaNecessaria;
                    alcool1 = alcool1 - alcoolNecessario / 2;
                    alcool2 = alcool2 - alcoolNecessario / 2;
                    resultado[0] = aditivo;
                    resultado[1] = gasolina;
                    resultado[2] = alcool1;
                    resultado[3] = alcool2;
                    return resultado;
                }

                resultado[0] = -21;
                return resultado;
            }

        }
        resultado[0] = 99; // erro acabou o sonho
        return resultado; // return obrigatorio do java
    }
}
