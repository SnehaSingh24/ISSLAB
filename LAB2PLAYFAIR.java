import java.util.*;
public class LAB2PLAYFAIR {

        private String key;
        private char[][] matrix;

        public LAB2PLAYFAIR(String key) {
            this.key = key;
            this.matrix = generateMatrix(key);
        }

        private char[][] generateMatrix(String key) {
            String alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // J is omitted
            Set<Character> usedChars = new LinkedHashSet<>();

            for (char c : key.toUpperCase().replaceAll("J", "I").toCharArray()) {
                if (Character.isLetter(c)) {
                    usedChars.add(c);
                }
            }
            for (char c : alphabet.toCharArray()) {
                usedChars.add(c);
            }

            char[][] matrix = new char[5][5];
            Iterator<Character> iterator = usedChars.iterator();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    matrix[i][j] = iterator.next();
                }
            }
            return matrix;
        }

        private String prepareText(String text) {
            text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
            StringBuilder preparedText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (i > 0 && text.charAt(i) == text.charAt(i - 1) && i % 2 == 1) {
                    preparedText.append('X');
                }
                preparedText.append(text.charAt(i));
            }
            if (preparedText.length() % 2 == 1) {
                preparedText.append('X');
            }
            return preparedText.toString();
        }

        private int[] findPosition(char c) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (matrix[i][j] == c) {
                        return new int[]{i, j};
                    }
                }
            }
            return null;
        }

        public String encrypt(String plaintext) {
            String preparedText = prepareText(plaintext);
            StringBuilder ciphertext = new StringBuilder();
            for (int i = 0; i < preparedText.length(); i += 2) {
                char a = preparedText.charAt(i);
                char b = preparedText.charAt(i + 1);
                int[] posA = findPosition(a);
                int[] posB = findPosition(b);

                if (posA[0] == posB[0]) {
                    ciphertext.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                    ciphertext.append(matrix[posB[0]][(posB[1] + 1) % 5]);
                } else if (posA[1] == posB[1]) {
                    ciphertext.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                    ciphertext.append(matrix[(posB[0] + 1) % 5][posB[1]]);
                } else {
                    ciphertext.append(matrix[posA[0]][posB[1]]);
                    ciphertext.append(matrix[posB[0]][posA[1]]);
                }
            }
            return ciphertext.toString();
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter key: ");
            String key = scanner.nextLine();
            LAB2PLAYFAIR  cipher = new LAB2PLAYFAIR (key);

            System.out.print("Enter plaintext: ");
            String plaintext = scanner.nextLine();
            String encryptedText = cipher.encrypt(plaintext);
            System.out.println("Encrypted Text: " + encryptedText);

            scanner.close();
        }
}
