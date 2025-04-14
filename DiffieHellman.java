import java.math.BigInteger;
import java.security.SecureRandom;

public class DiffieHellman {

    // Use large prime and primitive root (for real-world usage, use very large values)
    private static final BigInteger p = new BigInteger("8009"); // A prime number
    private static final BigInteger g = new BigInteger("2");    // Primitive root modulo p

    private static final SecureRandom random = new SecureRandom();

    public static BigInteger generatePrivateKey() {
        // Generate random private key: 1 < a < p
        return new BigInteger(p.bitLength() - 1, random).mod(p.subtract(BigInteger.ONE)).add(BigInteger.ONE);
    }

    public static BigInteger computePublicKey(BigInteger privateKey) {
        return g.modPow(privateKey, p);
    }

    public static BigInteger computeSharedSecret(BigInteger receivedPublicKey, BigInteger ownPrivateKey) {
        return receivedPublicKey.modPow(ownPrivateKey, p);
    }

    public static void main(String[] args) {
        // Alice's private and public key
        BigInteger alicePrivate = generatePrivateKey();
        BigInteger alicePublic = computePublicKey(alicePrivate);

        // Bob's private and public key
        BigInteger bobPrivate = generatePrivateKey();
        BigInteger bobPublic = computePublicKey(bobPrivate);

        // Exchange public keys and compute shared secrets
        BigInteger aliceSharedSecret = computeSharedSecret(bobPublic, alicePrivate);
        BigInteger bobSharedSecret = computeSharedSecret(alicePublic, bobPrivate);

        // Display keys and shared secret
        System.out.println("Alice's Public Key: " + alicePublic);
        System.out.println("Bob's Public Key  : " + bobPublic);
        System.out.println("Shared Secret (Alice): " + aliceSharedSecret);
        System.out.println("Shared Secret (Bob)  : " + bobSharedSecret);

        // Check if both secrets match
        System.out.println("Keys match? " + aliceSharedSecret.equals(bobSharedSecret));
    }
}

