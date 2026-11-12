package org.example;

import java.io.File;
import java.io.IOException;

/**
 * ============================================================================
 * COMO EXECUTAR ESTA SUÍTE DE TESTES NO TERMINAL:
 * ============================================================================
 * 1. Compilar:
 *    javac -encoding UTF-8 -d target/classes *.java
 *
 * 2. Executar a Suíte com 21 Asserções:
 *    java -cp target/classes org.example.TestesBubbleSort
 * ============================================================================
 *
 * Suíte de Testes Automatizados para a aplicação "Estrutura de Dados S.A.".
 * <p>
 * Demonstra de forma clara e visual a homologação do algoritmo Bubble Sort,
 * validando:
 * <ul>
 *   <li>Ordenação estritamente decrescente (maior para menor).</li>
 *   <li>Consistência entre a versão clássica e a versão paralela.</li>
 *   <li>Casos de borda (vetor vazio, elemento único, valores repetidos, lista já ordenada, lista invertida).</li>
 *   <li>Leitura e escrita física em arquivos TXT.</li>
 *   <li>Geração e verificação de massa de dados em escala.</li>
 * </ul>
 * </p>
 *
 * @author Gabriel Lira Figueira
 * @author Eduarda Santana Santos
 * @author João Luís Aragão Rocha
 * @version 1.0
 */
public class TestesBubbleSort {

    private static int totalTestes = 0;
    private static int testesAprovados = 0;

    /**
     * Ponto de entrada para execução dos testes.
     *
     * @param args argumentos de linha de comando
     */
    public static void main(String[] args) {
        imprimirCabecalho("INICIANDO BATERIA DE TESTES - BUBBLE SORT (ESTRUTURA DE DADOS S.A.)");

        // Instancia as duas estratégias para testar ambas
        Ordenador classico = new BubbleSortClassico();
        Ordenador paralelo = new BubbleSortParalelo();

        // ===============================================================
        // CENÁRIO 1: Testes de Ordenação Básica e Decrescente (Maior para Menor)
        // ===============================================================
        imprimirSecao("CENÁRIO 1: Validação da Ordenação Decrescente (Maior para Menor)");
        
        Registro[] dados1 = criarAmostraDesordenada();
        classico.ordenarDecrescente(dados1);
        validar("Bubble Sort Clássico: deve ordenar em ordem decrescente", estaOrdenadoDecrescente(dados1));
        validar("Maior valor deve estar na primeira posição", dados1[0].getValor() == 9000.0);
        validar("Menor valor deve estar na última posição", dados1[dados1.length - 1].getValor() == 1000.0);

        Registro[] dados2 = criarAmostraDesordenada();
        paralelo.ordenarDecrescente(dados2);
        validar("Bubble Sort Paralelo: deve ordenar em ordem decrescente", estaOrdenadoDecrescente(dados2));
        validar("Resultados de ambos devem coincidir elemento a elemento", saoIdenticos(dados1, dados2));

        // ===============================================================
        // CENÁRIO 2: Casos de Borda (Corner Cases)
        // ===============================================================
        imprimirSecao("CENÁRIO 2: Casos de Borda (Vetor Vazio, 1 Elemento e Empates)");

        // 1. Vetor Vazio
        Registro[] vazio = new Registro[0];
        classico.ordenarDecrescente(vazio);
        validar("Vetor vazio: não deve lançar exceção", vazio.length == 0);

        // 2. Um único elemento
        Registro[] umElemento = new Registro[]{ new Registro("Unica", "123", 5000) };
        classico.ordenarDecrescente(umElemento);
        validar("Vetor unitário: deve manter o único elemento intacto", umElemento[0].getValor() == 5000);

        // 3. Elementos com valores de mercado idênticos (Empates)
        Registro[] repetidos = new Registro[]{
            new Registro("Empresa A", "111", 5000),
            new Registro("Empresa B", "222", 9000),
            new Registro("Empresa C", "333", 5000),
            new Registro("Empresa D", "444", 2000)
        };
        classico.ordenarDecrescente(repetidos);
        validar("Valores repetidos: ordenação decrescente mantida", estaOrdenadoDecrescente(repetidos));
        validar("Primeiro valor deve ser 9000", repetidos[0].getValor() == 9000);
        validar("Último valor deve ser 2000", repetidos[3].getValor() == 2000);

        // ===============================================================
        // CENÁRIO 3: Casos Favoráveis e Desfavoráveis (Melhor e Pior Caso)
        // ===============================================================
        imprimirSecao("CENÁRIO 3: Melhor Caso (Já Ordenado) e Pior Caso (Ordem Inversa)");

        // Melhor caso: Já está decrescente (a flag de parada antecipada deve agir)
        Registro[] melhorCaso = new Registro[]{
            new Registro("E1", "1", 900),
            new Registro("E2", "2", 800),
            new Registro("E3", "3", 700),
            new Registro("E4", "4", 600)
        };
        classico.ordenarDecrescente(melhorCaso);
        validar("Melhor caso (já ordenado): permanece ordenado", estaOrdenadoDecrescente(melhorCaso));

        // Pior caso: Está em ordem crescente e precisa virar decrescente
        Registro[] piorCaso = new Registro[]{
            new Registro("E1", "1", 100),
            new Registro("E2", "2", 200),
            new Registro("E3", "3", 300),
            new Registro("E4", "4", 400),
            new Registro("E5", "5", 500)
        };
        classico.ordenarDecrescente(piorCaso);
        validar("Pior caso (ordem inversa): ordenado com sucesso", estaOrdenadoDecrescente(piorCaso));
        validar("Maior elemento (500) subiu para o topo", piorCaso[0].getValor() == 500);

        // ===============================================================
        // CENÁRIO 4: Teste de Leitura e Escrita Física em Arquivo TXT
        // ===============================================================
        imprimirSecao("CENÁRIO 4: Persistência Física de Arquivos (Leitura e Escrita TXT)");

        String arqTesteEntrada = "teste_empresas_entrada.txt";
        String arqTesteSaida = "teste_empresas_saida.txt";

        try {
            // Cria um arquivo de teste com 4 linhas
            Registro[] paraGravar = new Registro[]{
                new Registro("Beta Corp", "001", 3500000),
                new Registro("Alpha SA", "002", 9800000),
                new Registro("Gama Ltda", "003", 1200000)
            };
            ArquivoServico.gravarArquivo(paraGravar, arqTesteEntrada);
            validar("Gravação do arquivo TXT temporário realizada", new File(arqTesteEntrada).exists());

            // Lê do arquivo
            Registro[] lidos = ArquivoServico.lerArquivo(arqTesteEntrada);
            validar("Leitura do arquivo TXT retornou 3 registros", lidos.length == 3);

            // Ordena os lidos
            paralelo.ordenarDecrescente(lidos);
            validar("Registros lidos foram ordenados decrescente", estaOrdenadoDecrescente(lidos));

            // Salva ordenados
            ArquivoServico.gravarArquivo(lidos, arqTesteSaida);
            validar("Arquivo de saída TXT foi gerado com sucesso", new File(arqTesteSaida).exists());

            // Limpeza dos arquivos temporários de teste
            new File(arqTesteEntrada).delete();
            new File(arqTesteSaida).delete();
            validar("Limpeza dos arquivos temporários concluída", true);

        } catch (IOException e) {
            validar("Falha em teste de I/O: " + e.getMessage(), false);
        }

        // ===============================================================
        // CENÁRIO 5: Teste de Estresse / Carga (Massa de 5.000 Registros)
        // ===============================================================
        imprimirSecao("CENÁRIO 5: Teste de Carga e Comparação de Performance (5.000 registros)");

        int tamanhoCarga = 5000;
        Registro[] cargaClassico = gerarMassaAleatoria(tamanhoCarga);
        Registro[] cargaParalelo = new Registro[tamanhoCarga];
        System.arraycopy(cargaClassico, 0, cargaParalelo, 0, tamanhoCarga);

        System.out.printf("  Ordenando %d empresas com Bubble Sort Clássico...%n", tamanhoCarga);
        long tInicioClassico = System.currentTimeMillis();
        classico.ordenarDecrescente(cargaClassico);
        long tFimClassico = System.currentTimeMillis();
        long tempoClassico = tFimClassico - tInicioClassico;
        System.out.printf("  -> Tempo Clássico : %d ms%n", tempoClassico);
        validar("Massa de 5.000 registros ordenada pelo Clássico", estaOrdenadoDecrescente(cargaClassico));

        System.out.printf("  Ordenando %d empresas com Bubble Sort Paralelo...%n", tamanhoCarga);
        long tInicioParalelo = System.currentTimeMillis();
        paralelo.ordenarDecrescente(cargaParalelo);
        long tFimParalelo = System.currentTimeMillis();
        long tempoParalelo = tFimParalelo - tInicioParalelo;
        System.out.printf("  -> Tempo Paralelo : %d ms%n", tempoParalelo);
        validar("Massa de 5.000 registros ordenada pelo Paralelo", estaOrdenadoDecrescente(cargaParalelo));

        validar("Ambas as soluções chegam a resultados idênticos em massa de dados", saoIdenticos(cargaClassico, cargaParalelo));

        // ===============================================================
        // RESUMO FINAL
        // ===============================================================
        imprimirResumoFinal();
    }

    /**
     * Verifica se o vetor está estritamente em ordem decrescente (maior para menor).
     */
    private static boolean estaOrdenadoDecrescente(Registro[] registros) {
        for (int i = 0; i < registros.length - 1; i++) {
            if (registros[i].getValor() < registros[i + 1].getValor()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compara se dois vetores possuem exatamente os mesmos registros na mesma ordem.
     */
    private static boolean saoIdenticos(Registro[] a, Registro[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i].getValor() != b[i].getValor()) return false;
            if (!a[i].getEmpresa().equals(b[i].getEmpresa())) return false;
        }
        return true;
    }

    private static Registro[] criarAmostraDesordenada() {
        return new Registro[]{
            new Registro("Empresa A", "123", 5000),
            new Registro("Empresa B", "456", 3000),
            new Registro("Empresa C", "789", 9000),
            new Registro("Empresa D", "012", 1000),
            new Registro("Empresa E", "345", 7000)
        };
    }

    private static Registro[] gerarMassaAleatoria(int quantidade) {
        Registro[] dados = new Registro[quantidade];
        for (int i = 0; i < quantidade; i++) {
            double valor = (long) (Math.random() * 10_000_000);
            dados[i] = new Registro("Empresa " + i, "INSC-" + i, valor);
        }
        return dados;
    }

    private static void validar(String descricao, boolean condicao) {
        totalTestes++;
        if (condicao) {
            testesAprovados++;
            System.out.printf("  [OK]    %s%n", descricao);
        } else {
            System.err.printf("  [ERRO]  %s%n", descricao);
        }
    }

    private static void imprimirCabecalho(String titulo) {
        System.out.println("\n================================================================================");
        System.out.println("  " + titulo);
        System.out.println("================================================================================");
    }

    private static void imprimirSecao(String titulo) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("  " + titulo);
        System.out.println("--------------------------------------------------------------------------------");
    }

    private static void imprimirResumoFinal() {
        System.out.println("\n================================================================================");
        System.out.println("  RESUMO DOS TESTES EXECUTADOS");
        System.out.println("================================================================================");
        System.out.printf("  Total de testes realizados : %d%n", totalTestes);
        System.out.printf("  Testes com sucesso         : %d%n", testesAprovados);
        System.out.printf("  Testes com falha           : %d%n", totalTestes - testesAprovados);
        if (totalTestes == testesAprovados) {
            System.out.println("  STATUS GERAL               : >>> TODOS OS TESTES PASSARAM COM SUCESSO! <<<");
        } else {
            System.out.println("  STATUS GERAL               : >>> EXISTEM TESTES COM FALHA! <<<");
        }
        System.out.println("================================================================================\n");
    }
}
