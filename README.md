# Projeto: Estrutura de Dados S.A. (Ordenação Bubble Sort)
Disciplina de Estrutura de Dados - Universidade Tiradentes (Unit)

## Integrantes do Grupo
- Gabriel Lira Figueira
- Eduarda Santana Santos
- João Luís Aragão Rocha

---

## 🚀 Como Rodar o Projeto

Abra o terminal (PowerShell, CMD ou terminal do VS Code) na pasta raiz do projeto:

### 1. Compilar todos os arquivos Java:
```bash
javac -encoding UTF-8 -d target/classes *.java
```

---

### 2. Executar a Aplicação Principal (Main):
Ordena as empresas do arquivo de entrada e gera o arquivo `Empresas_Ordenadas.txt` com monitoramento de progresso real na tela:
```bash
java -cp target/classes org.example.Main
```
*(Opcional: Você pode passar arquivos customizados como argumento):*
```bash
java -cp target/classes org.example.Main arquivo_origem.txt arquivo_destino.txt
```

---

### 3. Executar a Suíte Completa de Testes Automatizados:
Executa 21 asserções cobrindo ordenação decrescente estrita, casos de borda (vazio, 1 elemento, empates), melhor/pior caso, I/O físico e teste de carga:
```bash
java -cp target/classes org.example.TestesBubbleSort
```

---

## ⚙️ Alternando entre Bubble Sort Clássico e Paralelo
No arquivo `Main.java` (linha 53):
- Para rodar a versão clássica: `Ordenador ordenador = new BubbleSortClassico();`
- Para rodar a versão paralela: `Ordenador ordenador = new BubbleSortParalelo();`
