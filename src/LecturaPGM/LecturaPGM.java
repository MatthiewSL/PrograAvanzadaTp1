package LecturaPGM;

import Main.MatrizImagen;

import java.io.*;
import java.util.*;

public class LecturaPGM {

    public static MatrizImagen leerImagen(String rutaArchivo) throws FileNotFoundException {
        try (InputStream input = new FileInputStream(rutaArchivo)) {
        	PushbackInputStream bufferedInput = new PushbackInputStream(input, 1);
            Scanner scanner = new Scanner(bufferedInput);
            scanner.useLocale(Locale.US);

            String magicNumber = leerSiguienteValor(scanner);

            int width = Integer.parseInt(leerSiguienteValor(scanner));
            int height = Integer.parseInt(leerSiguienteValor(scanner));
            int maxGray = Integer.parseInt(leerSiguienteValor(scanner));
            int b;
            
            do {
                b = bufferedInput.read();
            } while (b == '\r' || b == '\n');

            if (b != -1) {
                bufferedInput.unread(b);
            }

            int[][] image = new int[height][width];

            if (magicNumber.equals("P2")) {

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        image[i][j] = Integer.parseInt(leerSiguienteValor(scanner));
                    }
                }

            } else if (magicNumber.equals("P5")) {

                boolean is16Bit = maxGray > 255;
                DataInputStream data = new DataInputStream(bufferedInput);

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        int gray;
                        if (is16Bit) {
                            int high = data.readUnsignedByte();
                            int low = data.readUnsignedByte();
                            gray = (high << 8) | low;
                        } else {
                            gray = data.readUnsignedByte();
                        }
                        image[i][j] = gray;
                    }
                }

            } else {
                System.err.println("Formato PGM no soportado: " + magicNumber);
                return null;
            }

            return new MatrizImagen(image, maxGray);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String leerSiguienteValor(Scanner scanner) {
        while (scanner.hasNext()) {
            String token = scanner.next();
            if (token.startsWith("#")) {
                scanner.nextLine();
            } else {
                return token;
            }
        }
        return null;
    }
}
