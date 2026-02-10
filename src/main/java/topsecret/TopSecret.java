package topsecret;

public class TopSecret {
    public static void main(String[] args) {
        FileHandler fileHandler = new DiskFileHandler_PLACEHOLDER_B("data");
        CipherService cipherService = new IdentityCipherService_PLACEHOLDER_D(); // placeholder D (plaintext)

        ProgramControl pc = new ProgramControl(fileHandler, cipherService, "ciphers/key.txt");
        System.out.println(pc.execute(args));
    }
}
