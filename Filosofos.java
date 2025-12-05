import java.util.concurrent.locks.Lock; 
import java.util.concurrent.atomic.AtomicBoolean; 

class Filosofo implements Runnable { 
    private final int id; // identificador do filósofo
    private final Lock palitoEsquerdo; // lock do palito esquerdo
    private final Lock palitoDireito; // lock do palito direito
    private final AtomicBoolean continuar; // flag compartilhada que indica se deve continuar

    public Filosofo(int id, Lock palitoEsquerdo, Lock palitoDireito, AtomicBoolean continuar) {
        this.id = id; // armazena id
        this.palitoEsquerdo = palitoEsquerdo; // armazena referência ao lock esquerdo
        this.palitoDireito = palitoDireito; // armazena referência ao lock direito
        this.continuar = continuar; // armazena a flag de continuação
    }

    private void pensar() throws InterruptedException { // simula pensamento
        System.out.println("Filósofo " + id + " está pensando.");
        Thread.sleep(1000); 
    }

    private void comer() throws InterruptedException { // simula comer
        System.out.println("Filósofo " + id + " está comendo."); 
        Thread.sleep(1000); 
    }

    @Override
    public void run() { 
        try {
            while (continuar.get()) { 
                pensar(); // pensar antes de tentar comer

                // alternar ordem de aquisição dos locks para evitar deadlock
                if (id % 2 == 0) {
                    // IDs pares pegam primeiro o palito direito
                    palitoDireito.lock(); 
                    System.out.println("Filósofo " + id + " pegou o palito direito."); 
                    palitoEsquerdo.lock(); 
                    System.out.println("Filósofo " + id + " pegou o palito esquerdo."); 

                    try {
                        comer(); 
                    } finally {
                        // liberar na ordem inversa para consistência
                        palitoEsquerdo.unlock(); 
                        System.out.println("Filósofo " + id + " liberou o palito esquerdo."); 
                        palitoDireito.unlock(); 
                        System.out.println("Filósofo " + id + " liberou o palito direito."); 
                    }
                } else {
                    // IDs ímpares pegam primeiro o palito esquerdo 
                    palitoEsquerdo.lock(); 
                    System.out.println("Filósofo " + id + " pegou o palito esquerdo."); // log
                    palitoDireito.lock(); 
                    System.out.println("Filósofo " + id + " pegou o palito direito."); // log

                    try {
                        comer(); 
                    } finally {
                        // liberar na ordem inversa
                        palitoDireito.unlock(); 
                        System.out.println("Filósofo " + id + " liberou o palito direito."); 
                        palitoEsquerdo.unlock(); 
                        System.out.println("Filósofo " + id + " liberou o palito esquerdo."); 
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }
}