package org.example;

/**
 * Interface que define o contrato para estratégias de ordenação da lista de empresas.
 * <p>
 * O uso do padrão de projeto Strategy permite alternar facilmente entre o
 * Bubble Sort clássico tradicional e o Bubble Sort otimizado em paralelo (Odd-Even Transposition),
 * garantindo flexibilidade e facilidade de adaptação perante o professor.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public interface Ordenador {

    /**
     * Ordena o vetor de registros pelo valor de mercado em ordem DECRESCENTE
     * (do maior valor para o menor).
     *
     * @param registros Vetor de empresas a ser ordenado in-place
     */
    void ordenarDecrescente(Registro[] registros);

    /**
     * Retorna o nome descritivo do algoritmo para exibição de logs e testes.
     *
     * @return Nome descritivo da estratégia de ordenação
     */
    String getNomeEstrategia();
}
