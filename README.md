# Jantar dos Filósofos (Dining Philosophers Problem)

## 📋 Descrição

Este projeto implementa uma solução clássica para o **Problema do Jantar dos Filósofos**, um problema de sincronização em programação concorrente. O projeto demonstra como usar `Locks` (ReentrantLock) e `AtomicBoolean` para evitar deadlock e gerenciar recursos compartilhados (garfos) entre múltiplas threads (filósofos).

## 🎯 O Problema

O Jantar dos Filósofos é um problema clássico de sincronização que ilustra desafios em programação concorrente:

- **5 filósofos** sentam em volta de uma mesa redonda
- Entre cada par de filósofos há **1 garfo**
- Cada filósofo alterna entre **pensar** e **comer**
- Para comer, um filósofo precisa de **ambos os garfos** (esquerdo e direito)
- Após comer, ele libera os garfos e volta a pensar

### Desafios:
- **Deadlock**: Se todos pegarem o garfo esquerdo simultaneamente, ninguém consegue pegar o direito
- **Starvation**: Um filósofo pode ficar esperando indefinidamente
- **Sincronização**: Coordenação de múltiplas threads compartilhando recursos

## 🛠️ Solução Implementada

### Estrutura do Projeto

```
JantarFilosofos-1/
├── Jantar.java          # Classe principal que inicia o programa
├── Filosofos.java       # Classe que implementa o filósofo (Runnable)
└── README.md           # Este arquivo
```

### Componentes Principais

#### **Clase `Filosofo`** (Filosofos.java)
- Implementa `Runnable` para executar em uma thread separada
- **Atributos**:
  - `id`: Identificador do filósofo
  - `palitoEsquerdo` e `palitoDireito`: Locks para sincronização
  - `continuar`: Flag `AtomicBoolean` para controlar o tempo de execução

- **Métodos**:
  - `pensar()`: Simula o filósofo pensando (sleep de 1 segundo)
  - `comer()`: Simula o filósofo comendo (sleep de 1 segundo)
  - `run()`: Loop principal que alterna entre pensar e comer

#### **Classe `Jantar`** (Jantar.java)
- Classe principal que inicia o programa
- **Responsabilidades**:
  - Criar 5 filósofos
  - Criar 5 garfos (ReentrantLocks)
  - Iniciar threads dos filósofos
  - Iniciar o timer para finalizar a execução

#### **Classe `Timer`** (Jantar.java)
- Gerencia o tempo de execução do programa
- Após 10 segundos (configurável), sinaliza para todos os filósofos pararem
- Aguarda que todas as threads terminem antes de finalizar

## 🚀 Como Executar

### Pré-requisitos
- Java 8 ou superior instalado
- Um compilador Java (javac)

### Passos

1. **Compilar o projeto**:
   ```bash
   javac Jantar.java Filosofos.java
   ```

2. **Executar o programa**:
   ```bash
   java Jantar
   ```

### Saída Esperada

```
Timer iniciado: a execução vai durar 10000ms
Filósofo 0 está pensando.
Filósofo 1 está pensando.
Filósofo 2 está pensando.
Filósofo 3 está pensando.
Filósofo 4 está pensando.
Filósofo 0 pegou o palito esquerdo.
Filósofo 0 pegou o palito direito.
Filósofo 0 está comendo.
Filósofo 0 liberou o palito direito.
Filósofo 0 liberou o palito esquerdo.
... (continua por 10 segundos)

=== TEMPO FINALIZADO ===
Todos os filósofos pararam de comer.
```

## 🔒 Mecanismo de Sincronização

### ReentrantLock
- **Utilizado para**: Sincronizar o acesso aos garfos
- **Vantagem**: Evita deadlock através de ordenação consistente de locks
- Cada filósofo sempre pega o garfo esquerdo antes do direito, mantendo uma ordem consistente

### AtomicBoolean
- **Utilizado para**: Sinalizar quando os filósofos devem parar
- **Vantagem**: Thread-safe sem necessidade de sincronização explícita
- O timer muda o valor para `false` após 10 segundos, fazendo todos os filósofos saírem do loop

## ⚙️ Configurações

### Tempo de Execução
Você pode alterar o tempo de execução modificando a variável `tempoExecucao` em `Jantar.java`:

```java
long tempoExecucao = 10000; // 10 segundos em milissegundos
```

### Número de Filósofos
Para mudar o número de filósofos, altere:

```java
int numFilosofos = 5; // Altere para o número desejado
```

### Tempo de Pensar e Comer
Modifique os valores em `Filosofos.java`:

```java
private void pensar() throws InterruptedException {
    System.out.println("Filósofo " + id + " está pensando.");
    Thread.sleep(1000); // Altere o tempo aqui
}

private void comer() throws InterruptedException {
    System.out.println("Filósofo " + id + " está comendo.");
    Thread.sleep(1000); // Altere o tempo aqui
}
