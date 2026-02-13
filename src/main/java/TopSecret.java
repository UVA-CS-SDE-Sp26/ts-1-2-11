/**
 * Commmand Line Utility
 */
public class TopSecret {
    public static void main(String[] args) {
        FileHandler fileHandler = new FileHandler();
        Cipher cipher = new Cipher();
        ProgramControl control = new ProgramControl(fileHandler, cipher);
        Userinterface ui = new Userinterface(control);
        ui.run(args);
    }
}
