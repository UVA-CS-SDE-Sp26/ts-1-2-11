package topsecret;

public interface CipherService {
    String decipher(String cipherText, String keyPath) throws Exception;
}
