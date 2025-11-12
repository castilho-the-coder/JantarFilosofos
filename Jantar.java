import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicBoolean;

public class Jantar {
    public static void main(String[] args) {
        int numFilosofos = 5;
        long tempoExecucao = 10000; // 10 segundos em milissegundos
        
        Lock[] garfos = new ReentrantLock[numFilosofos];
        Thread[] filosofos = new Thread[numFilosofos];
        AtomicBoolean continuar = new AtomicBoolean(true);

        // Inicializar os garfos
        for (int i = 0; i < numFilosofos; i++) {
            garfos[i] = new ReentrantLock();
        }

        // Inicializar e iniciar as threads dos filósofos
        for (int i = 0; i < numFilosofos; i++) {
            filosofos[i] = new Thread(new Filosofo(i, garfos[i], garfos[(i + 1) % numFilosofos], continuar));
            filosofos[i].start();
        }

        // Timer para finalizar a execução
        Timer timer = new Timer(tempoExecucao, continuar, filosofos);
        timer.start();
    }
}

// Classe para gerenciar o timer
class Timer extends Thread {
    private final long duracao;
    private final AtomicBoolean continuar;
    private final Thread[] filosofos;

    public Timer(long duracao, AtomicBoolean continuar, Thread[] filosofos) {
        this.duracao = duracao;
        this.continuar = continuar;
        this.filosofos = filosofos;
    }

    @Override
    public void run() {
        try {
            System.out.println("Timer iniciado: a execução vai durar " + duracao + "ms");
            Thread.sleep(duracao);
            
            System.out.println("\n=== TEMPO FINALIZADO ===");
            continuar.set(false);
            
            // Aguardar que todas as threads de filósofos terminem
            for (Thread filosofo : filosofos) {
                filosofo.join();
            }
            
            System.out.println("Todos os filósofos pararam de comer.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}