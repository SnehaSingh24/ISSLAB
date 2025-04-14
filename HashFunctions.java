import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HashFunctions {

    private static int leftRotate(int x, int amount) {
        return (x << amount) | (x >>> (32 - amount));
    }

    public static String md5(String message) {
        byte[] msgBytes = message.getBytes(StandardCharsets.UTF_8);
        int origLength = msgBytes.length * 8;

        int padLen = (56 - (msgBytes.length + 1) % 64 + 64) % 64;
        byte[] padded = new byte[msgBytes.length + 1 + padLen + 8];
        System.arraycopy(msgBytes, 0, padded, 0, msgBytes.length);
        padded[msgBytes.length] = (byte) 0x80;
        ByteBuffer.wrap(padded, padded.length - 8, 8).putLong(origLength);

        int[] s = {
            7,12,17,22, 7,12,17,22, 7,12,17,22, 7,12,17,22,
            5, 9,14,20, 5, 9,14,20, 5, 9,14,20, 5, 9,14,20,
            4,11,16,23, 4,11,16,23, 4,11,16,23, 4,11,16,23,
            6,10,15,21, 6,10,15,21, 6,10,15,21, 6,10,15,21
        };

        int[] T = new int[64];
        for (int i = 0; i < 64; i++) {
            T[i] = (int)(long)(Math.floor(Math.abs(Math.sin(i + 1)) * (1L << 32)));
        }

        int a0 = 0x67452301;
        int b0 = 0xefcdab89;
        int c0 = 0x98badcfe;
        int d0 = 0x10325476;

        for (int offset = 0; offset < padded.length; offset += 64) {
            int[] M = new int[16];
            for (int i = 0; i < 16; i++) {
                M[i] = ByteBuffer.wrap(padded, offset + i * 4, 4).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
            }

            int A = a0, B = b0, C = c0, D = d0;
            for (int i = 0; i < 64; i++) {
                int F, g;
                if (i < 16) {
                    F = (B & C) | (~B & D);
                    g = i;
                } else if (i < 32) {
                    F = (D & B) | (~D & C);
                    g = (5 * i + 1) % 16;
                } else if (i < 48) {
                    F = B ^ C ^ D;
                    g = (3 * i + 5) % 16;
                } else {
                    F = C ^ (B | ~D);
                    g = (7 * i) % 16;
                }

                int temp = D;
                D = C;
                C = B;
                B = B + leftRotate(A + F + T[i] + M[g], s[i]);
                A = temp;
            }

            a0 += A;
            b0 += B;
            c0 += C;
            d0 += D;
        }

        return String.format("%08x%08x%08x%08x", a0, b0, c0, d0);
    }

    private static int leftRotate32(int val, int bits) {
        return (val << bits) | (val >>> (32 - bits));
    }

    public static String sha1(String input) {
        byte[] message = input.getBytes(StandardCharsets.UTF_8);
        int origLength = message.length * 8;

        int numZeroBytes = (56 - (message.length + 1) % 64 + 64) % 64;
        byte[] padded = Arrays.copyOf(message, message.length + 1 + numZeroBytes + 8);
        padded[message.length] = (byte) 0x80;
        ByteBuffer.wrap(padded, padded.length - 8, 8).putLong(origLength);

        int h0 = 0x67452301;
        int h1 = 0xEFCDAB89;
        int h2 = 0x98BADCFE;
        int h3 = 0x10325476;
        int h4 = 0xC3D2E1F0;

        for (int i = 0; i < padded.length; i += 64) {
            int[] w = new int[80];
            for (int j = 0; j < 16; j++) {
                w[j] = ByteBuffer.wrap(padded, i + j * 4, 4).getInt();
            }
            for (int j = 16; j < 80; j++) {
                w[j] = leftRotate32(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);
            }

            int a = h0, b = h1, c = h2, d = h3, e = h4;
            for (int j = 0; j < 80; j++) {
                int f, k;
                if (j < 20) {
                    f = (b & c) | (~b & d);
                    k = 0x5A827999;
                } else if (j < 40) {
                    f = b ^ c ^ d;
                    k = 0x6ED9EBA1;
                } else if (j < 60) {
                    f = (b & c) | (b & d) | (c & d);
                    k = 0x8F1BBCDC;
                } else {
                    f = b ^ c ^ d;
                    k = 0xCA62C1D6;
                }
                int temp = leftRotate32(a, 5) + f + e + k + w[j];
                e = d;
                d = c;
                c = leftRotate32(b, 30);
                b = a;
                a = temp;
            }

            h0 += a;
            h1 += b;
            h2 += c;
            h3 += d;
            h4 += e;
        }

        return String.format("%08x%08x%08x%08x%08x", h0, h1, h2, h3, h4);
    }

    public static void main(String[] args) {
        String msg = "Rohith Dasari found a bug in the code";
        System.out.println("MD5  : " + md5(msg));
        System.out.println("SHA-1: " + sha1(msg));
    }
}


