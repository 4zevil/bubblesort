import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

   
    static class Registro {
        String empresa;
        String inscricao;
        double valor;

        Registro(String empresa, String inscricao,double valor) {
            this.empresa = empresa;
            this.valor = valor;
            this.inscricao = inscricao;
        }

        @Override
        public String toString() {
            return empresa + "|" +  inscricao + "|" + valor;
        }
    }

    // Lê o arquivo e monta a lista de registros
    static List<Registro> lerArquivo(String caminho) throws IOException {
        List<Registro> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;       // ignora linhas vazias
                String[] campos = linha.split("\\|");
                if (campos.length != 3) continue;           // ignora linhas inválidas
                lista.add(new Registro(
                        campos[0].trim(),
                        campos[1].trim(),
                        Double.parseDouble(campos[2].trim())));
            }
        }
        return lista;
    }

    // Bubble sort pelo campo valor (crescente)
    static void bubbleSort(List<Registro> lista) {
        int n = lista.size();
        boolean trocou;
        for (int i = 0; i < n - 1; i++) {
            trocou = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (lista.get(j).valor > lista.get(j + 1).valor) {
                    Registro tmp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, tmp);
                    trocou = true;
                }
            }
            if (!trocou) break; // já está ordenado
        }
    }

    public static void main(String[] args) {
        try {
            List<Registro> lista = lerArquivo("Empresas_BubbleSort.txt");
            bubbleSort(lista);
            for (Registro r : lista) {
                System.out.println(r);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Campo numérico inválido no arquivo.");
        }
    }
}