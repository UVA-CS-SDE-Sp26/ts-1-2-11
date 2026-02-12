import java.util.Map;

/**
 * Immutable cipher key mapping.
 *
 * The mapping is cipherCharacter -> actualCharacter
 */
public final class CipherKey {

    private final Map<Character, Character> cipherToActual;

    public CipherKey(Map<Character, Character> cipherToActual) {
        this.cipherToActual = Map.copyOf(cipherToActual);
    }

    /**
     * Decode a single character using the cipher mapping.
     * If the character is not in the mapping, it is returned unchanged.
     */
    public char decode(char c) {
        Character mapped = cipherToActual.get(c);
        return mapped == null ? c : mapped;
    }

    public Map<Character, Character> getCipherToActual() {
        return cipherToActual;
    }
}
