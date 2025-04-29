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

            // Leer el magic number
            String magicNumber = leerSiguienteValor(scanner);

            // Leer ancho, alto y nivel máximo de gris, ignorando comentarios
            int width = Integer.parseInt(leerSiguienteValor(scanner));
            int height = Integer.parseInt(leerSiguienteValor(scanner));
            int maxGray = Integer.parseInt(leerSiguienteValor(scanner));

            // Saltar el byte del salto de línea final del encabezado
            int b;
            do {
                b = bufferedInput.read();
            } while (b == '\r' || b == '\n');

            // Ya hemos leído un byte de más: lo volvemos a meter en el flujo
            if (b != -1) {
                bufferedInput.unread(b);
            }

            int[][] image = new int[height][width];

            if (magicNumber.equals("P2")) {
                // Lectura ASCII
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        image[i][j] = Integer.parseInt(leerSiguienteValor(scanner));
                    }
                }

            } else if (magicNumber.equals("P5")) {
                // Lectura binaria
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

    // Método auxiliar que salta comentarios y devuelve el siguiente valor útil
    private static String leerSiguienteValor(Scanner scanner) {
        while (scanner.hasNext()) {
            String token = scanner.next();
            if (token.startsWith("#")) {
                scanner.nextLine(); // Saltar comentario completo
            } else {
                return token;
            }
        }
        return null;
    }
}
