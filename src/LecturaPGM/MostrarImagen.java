package LecturaPGM;

import java.awt.image.BufferedImage;
import javax.swing.*;
import Main.MatrizImagen;


public class MostrarImagen {

    public static void mostrar(MatrizImagen matriz, int width, int height, int maxGray) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Convertir a escala de 0–255 si maxGray > 255
        double escala = maxGray > 255 ? 255.0 / maxGray : 1.0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int gray = matriz.getValue(i, j);
                int grayEscalado = (int)(gray * escala);
                grayEscalado = Math.max(0, Math.min(255, grayEscalado)); 
                bufferedImage.getRaster().setSample(j, i, 0, grayEscalado);
            }
        }

        // Mostrar la imagen en una ventana
        JFrame frame = new JFrame("Imagen PGM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        JLabel label = new JLabel(new ImageIcon(bufferedImage));
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}
