import java.util.concurrent.locks.Lock;
import java.util.concurrent.atomic.AtomicBoolean;

class Filosofo implements Runnable {
    private final int id;
    private final Lock palitoEsquerdo;
    private final Lock palitoDireito;
    private final AtomicBoolean continuar;

    public Filosofo(int id, Lock palitoEsquerdo, Lock palitoDireito, AtomicBoolean continuar) {
        this.id = id;
        this.palitoEsquerdo = palitoEsquerdo;
        this.palitoDireito = palitoDireito;
        this.continuar = continuar;
    }

    private void pensar() throws InterruptedException {
        System.out.println("Filósofo " + id + " está pensando.");
        Thread.sleep(1000);
    }

    private void comer() throws InterruptedException {
        System.out.println("Filósofo " + id + " está comendo.");
        Thread.sleep(1000);
    }

    @Override
    public void run() {
        try {
            while (continuar.get()) {
                pensar();

                // Ordem de bloqueio alternada impede que todos os filósofos
                // peguem o mesmo palito primeiro na primeira rodada.
                if (id % 2 == 0) {
                    // Filósofos com id par pegam primeiro o palito direito e depois o esquerdo
                    palitoDireito.lock();
                    System.out.println("Filósofo " + id + " pegou o palito direito.");
                    palitoEsquerdo.lock();
                    System.out.println("Filósofo " + id + " pegou o palito esquerdo.");

                    try {
                        comer();
                    } finally {
                        // Liberar palitos na ordem inversa
                        palitoEsquerdo.unlock();
                        System.out.println("Filósofo " + id + " liberou o palito esquerdo.");
                        palitoDireito.unlock();
                        System.out.println("Filósofo " + id + " liberou o palito direito.");
                    }
                } else {
                    // Filósofos com id ímpar pegam primeiro o palito esquerdo e depois o direito (ordem original)
                    palitoEsquerdo.lock();
                    System.out.println("Filósofo " + id + " pegou o palito esquerdo.");
                    palitoDireito.lock();
                    System.out.println("Filósofo " + id + " pegou o palito direito.");

                    try {
                        comer();
                    } finally {
                        // Liberar palitos na ordem inversa
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