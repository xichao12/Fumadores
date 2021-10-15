import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Agent implements Runnable {

    private Table table;
    private Random random;
    private Semaphore semaphore;

    public Agent(Table table) {
        this.table = table;
        this.random = new Random();
        this.semaphore = new Semaphore(1);
    }
    
    /**
     * Funcion que crea dos ingredientes aleatorios
     * @return un EnumSet de ingredientes
     */
    public EnumSet<Component> nextComponents() {
        EnumSet<Component> components = EnumSet.allOf(Component.class);
        Component[] array = components.toArray(new Component[0]);
        int i = random.nextInt(array.length);
        components.remove(array[i]);
        return components;
    }
    
    public void putComponents() {
         table.putComponents(nextComponents());
    }

    public void sleep() throws InterruptedException {
        semaphore.acquire();
    }

    public void awake() {
        semaphore.release();
    }
    
    public void run() {
    	/*
    	 *  Se duerme hasta que le llame el fumador,
    	 *  cuando le llaman, pone los ingredientes en la mesa
    	 */
        while (true) {
            try {
                sleep();
                putComponents();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}