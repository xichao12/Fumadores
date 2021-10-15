public class Main {

    public static void main(String[] args) {
    	// Empezamos el hilo principal con la clase Mesa
        new Thread(new Table()).start();
    }
}