package org.example;

/**
 * Representa o registro individual de uma empresa contendo seus dados cadastrais
 * e valor de mercado.
 * <p>
 * Implementa ordenação natural decrescente por valor de mercado para atender
 * aos requisitos de classificação da organização Estrutura de Dados S.A.
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class Registro {
    private final String empresa;
    private final String inscricao;
    private final double valor;

    /**
     * Constrói uma nova instância de Registro.
     *
     * @param empresa   Nome fantasia da empresa
     * @param inscricao Inscrição estadual da empresa
     * @param valor     Valor de mercado da empresa em reais
     */
    public Registro(String empresa, String inscricao, double valor) {
        this.empresa = empresa;
        this.inscricao = inscricao;
        this.valor = valor;
    }

    /**
     * Obtém o nome fantasia da empresa.
     *
     * @return nome da empresa
     */
    public String getEmpresa() {
        return empresa;
    }

    /**
     * Obtém a inscrição estadual da empresa.
     *
     * @return inscrição estadual
     */
    public String getInscricao() {
        return inscricao;
    }

    /**
     * Obtém o valor de mercado da empresa.
     *
     * @return valor de mercado em double
     */
    public double getValor() {
        return valor;
    }

    /**
     * Retorna a representação do registro no formato de linha exigido pelo TXT:
     * {@code Nome Fantasia | Inscrição Estadual | Valor de Mercado}.
     *
     * @return string formatada com os dados do registro
     */
    @Override
    public String toString() {
        // Formata mantendo o padrão especificado no enunciado: Nome | Inscrição | Valor
        // Se o valor tiver casas decimais, exibe formatado; caso seja inteiro, formata diretamente.
        if (valor == (long) valor) {
            return String.format("%s | %s | %d", empresa, inscricao, (long) valor);
        }
        return String.format("%s | %s | %.2f", empresa, inscricao, valor);
    }
}
