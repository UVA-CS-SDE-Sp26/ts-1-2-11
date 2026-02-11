/**
 * Commmand Line Utility
 */
public class TopSecret {
    public static void main(String[] args) {
        Userinterface ui = new Userinterface(new ProgramControl());
        ui.run(args);
    }
}
