import java.util.concurrent.Semaphore;

public class Smoker implements Runnable {

    private Table table;
    private Component component;
    private Semaphore components;
    
    public Smoker(Table table, Component component) {
        this.table = table;
        this.component = component;
        this.components = new Semaphore(0);
    }

    public Component getComponent() {
        return component;
    }

    public boolean hasComponent(Component component) {
        return this.component == component;
    }
    
    /**
     * Funcion que imprime que esta fumando
     * @throws InterruptedException
     */
    public void smoke() throws InterruptedException {
    	System.out.println("Smoker " + component + " smoking during " + 3000 + " ms");
        Thread.sleep(3000);
    }

    public void awake() {
        components.release();
    }
    
    private void awaitComponents() throws InterruptedException {
        components.acquire();
    }
    
    public void run() {
    	/*
    	 * Espera a que le llegen componentes,
    	 * se hace el cigarro, se lo fuma y despierta al agente
    	 * para que cambie los ingredientes de la mesa
    	 */
        while (true) {
            try {
                awaitComponents();
                smoke();
                table.awakeAgent();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}