import java.util.EnumSet;
import java.util.concurrent.locks.ReentrantLock;

public class Pusher implements Runnable {

    private static ReentrantLock lock;

    private Table table;
    private Component component;

    static {
        lock = new ReentrantLock();
    }

    public Pusher(Table table, Component component) {
        this.table = table;
        this.component = component;
    }


    public void run() {
    	// Cada pusher intenta coger su ingrediente asignado de la mesa
        while (true) {
            try {
                table.take(component);
                // Si encuentra el ingrediente, se bloquea la mesa
                lock.lock();
                // Busca que ingrediente es el que falta
                table.setComponent(component);
                EnumSet<Component> components = EnumSet.complementOf(table.getComponents());

                if (components.size() == 1) {
                	// Llama al fumador que tiene el ingrediente que falta
                    table.awakeSmoker(components.iterator().next());
                }
                // Desbloquea la mesa
                lock.unlock();
            } catch (InterruptedException e) { // En caso de no tener el ingrediente
                e.printStackTrace();
            }
        }
    }
}