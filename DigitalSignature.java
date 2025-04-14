import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;

public class DigitalSignature {

    static final BigInteger p = BigInteger.valueOf(8009);
    static final BigInteger q = BigInteger.valueOf(101);
    static final BigInteger g = BigInteger.valueOf(2);
    static final Random rand = new Random();

    public static BigInteger sha1Hash(String message) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(message.getBytes());
        return new BigInteger(1, hashBytes).mod(q);
    }

    public static BigInteger[] generateKeys() {
        BigInteger x = BigInteger.valueOf(rand.nextInt(q.intValue() - 1) + 1);
        BigInteger y = g.modPow(x, p);
        return new BigInteger[]{x, y};
    }

    public static BigInteger[] sign(String message, BigInteger x) throws Exception {
        BigInteger h = sha1Hash(message);
        BigInteger r, s;

        while (true) {
            BigInteger k = BigInteger.valueOf(rand.nextInt(q.intValue() - 1) + 1);
            r = g.modPow(k, p).mod(q);
            if (r.equals(BigInteger.ZERO)) continue;

            BigInteger kInv = k.modInverse(q);
            s = kInv.multiply(h.add(x.multiply(r))).mod(q);
            if (!s.equals(BigInteger.ZERO)) break;
        }

        return new BigInteger[]{r, s};
    }

    public static boolean verify(String message, BigInteger[] signature, BigInteger y) throws Exception {
        BigInteger r = signature[0], s = signature[1];
        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0) return false;
        if (s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) return false;

        BigInteger h = sha1Hash(message);
        BigInteger w = s.modInverse(q);
        BigInteger u1 = h.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        BigInteger v = g.modPow(u1, p).multiply(y.modPow(u2, p)).mod(p).mod(q);
        return v.equals(r);
    }

    public static void main(String[] args) throws Exception {
        String message = "Hello, Digital Signature!";

        BigInteger[] keys = generateKeys();
        BigInteger privateKey = keys[0];
        BigInteger publicKey = keys[1];

        BigInteger[] signature = sign(message, privateKey);
        System.out.println("Signature (r, s): " + signature[0] + ", " + signature[1]);

        boolean isValid = verify(message, signature, publicKey);
        System.out.println("Signature valid? " + isValid);
    }
}
