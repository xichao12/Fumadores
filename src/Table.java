import java.util.*;
import java.util.concurrent.Semaphore;

public class Table implements Runnable {

    private Agent agent;
    private List<Smoker> smokers;
    private List<Pusher> pushers;
    private EnumMap<Component, Semaphore> components;
    private EnumMap<Component, Boolean> hasComponent;

    public Table() {
    	// Inicializacion de clases
        this.agent = new Agent(this);
        this.smokers = new ArrayList<>();
        this.pushers = new ArrayList<>();
        this.components = new EnumMap<>(Component.class);
        this.hasComponent = new EnumMap<>(Component.class);
        // A cada fumador / pusher se le asigna un componente
        for (Component component : Component.values()) {
            Smoker smoker = new Smoker(this, component);
            smokers.add(smoker);

            Pusher pusher = new Pusher(this, component);
            pushers.add(pusher);

            components.put(component, new Semaphore(0));
            hasComponent.put(component, Boolean.FALSE);
        }
    }

    public Boolean hasComponent(Component component) {
        return hasComponent.get(component);
    }

    public void setComponent(Component component) {
        hasComponent.put(component, Boolean.TRUE);
    }
    
    public void take(Component component) throws InterruptedException {
        components.get(component).acquire();
    }

    /**
     * Obtiene todos los componentes que hay en la mesa
     * @return los componentes que hay en la mesa
     */
    public EnumSet<Component> getComponents() {
        EnumSet<Component> components = EnumSet.noneOf(Component.class);
        for (Map.Entry<Component, Boolean> entry : hasComponent.entrySet()) {
            if (entry.getValue()) {
                components.add(entry.getKey());
            }
        }
        return components;
    }

    /**
     * Cambia los componentes de la mesa
     * @param components
     */
    public void putComponents(EnumSet<Component> components) {
    	System.out.println("Agent generated " + components);
        for (Component component : Component.values()) {
            hasComponent.put(component, Boolean.FALSE);
        }
        for (Component component : components) {
            this.components.get(component).release();
        }
    }

    /**
     * Despierta al agente
     */
    public void awakeAgent() {
        agent.awake();
    }

    /**
     * Despierta al fumador que tenga el componente pasado por parametro
     * @param component
     * @throws InterruptedException
     */
    public void awakeSmoker(Component component) throws InterruptedException {
        for (Smoker smoker: smokers) {
            if (smoker.hasComponent(component)) {
                smoker.awake();
                break;
            }
        }
    }

    public void run() {
    	// Crea un hilo para el agente
        new Thread(agent).start();
        // Crea varios hilos para los fumadores
        for (Smoker smoker : smokers) {
            new Thread(smoker).start();
        }
        // Crea varios hilos para los pushers
        for (Pusher pusher : pushers) {
            new Thread(pusher).start();
        }
    }
}