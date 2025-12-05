import java.util.concurrent.locks.Lock; 
import java.util.concurrent.locks.ReentrantLock; 
import java.util.concurrent.atomic.AtomicBoolean; 

public class Jantar { 
    public static void main(String[] args) {
        int numFilosofos = 5; // quantidade de filósofos
        long tempoExecucao = 10000; // tempo total
        
        Lock[] palitos = new ReentrantLock[numFilosofos]; // array de locks representando palitos
        Thread[] filosofos = new Thread[numFilosofos]; // array de threads dos filósofos
        AtomicBoolean continuar = new AtomicBoolean(true); // flag para controlar execução

       
        for (int i = 0; i < numFilosofos; i++) {
            palitos[i] = new ReentrantLock(); // cria lock para cada palito
        }

        // iniciar as threads dos filósofos
        for (int i = 0; i < numFilosofos; i++) {
            // passa o palito i como esquerdo e (i+1)%n como direito
            filosofos[i] = new Thread(new Filosofo(i, palitos[i], palitos[(i + 1) % numFilosofos], continuar));
            filosofos[i].start(); // inicia a thread
        }

        // timer para finalizar a execução 
        Timer timer = new Timer(tempoExecucao, continuar, filosofos);
        timer.start(); 
    }
}

// classe para gerenciar o timer que encerra a simulação
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
            Thread.sleep(duracao); // espera o tempo configurado
            
            System.out.println("\n=== TEMPO FINALIZADO ==="); 
            continuar.set(false); // sinaliza para as threads pararem
            
            // aguarda que todas as threads de filósofos terminem
            for (Thread filosofo : filosofos) {
                filosofo.join(); 
            }
            
            System.out.println("Todos os filósofos pararam de comer."); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }
}