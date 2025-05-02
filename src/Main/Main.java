package Main;

import LecturaPGM.LecturaPGM;
import LecturaPGM.MostrarImagen;
import java.io.FileNotFoundException;
import Filtros.Filtros;
//1024*768
//bajo es oscuro y alto es claro
//Grupo Psi: Filtro con kernel variable



//Aplicar un filtro (o máscara) de convolución a la imagen utilizando 
//distintos kernels como suavizado, Sobel, Prewitt, Laplaciano o repujado, 
//que permiten desenfocar, detectar bordes, realzar contornos o generar efectos visuales

public class Main {

    public static void main(String[] args) {

        String rutaArchivo = "fotos/output_image_p2_16bit.pgm"; 
        
        try {
            
            MatrizImagen matriz = LecturaPGM.leerImagen(rutaArchivo);
            
            
            if (matriz != null) {
               
                int height = matriz.getHeight();
                int width = matriz.getWidth();
                int maxGray = matriz.getMaxGray();
                
                MostrarImagen.mostrar(matriz, width, height, maxGray);
                
                //ESTA ES LA SUAVIZADA PROM
                //MatrizImagen matrizSuav = Filtros.filtroSuavizadoPromedio(matriz, width, height, maxGray);
                //MostrarImagen.mostrar(matrizSuav, width, height, maxGray);
                //ESTA ES LA SUAVIZADA PROM GAUSS
                //MatrizImagen matrizSuav = Filtros.filtroSuavizadoPromedioGaussiano(matriz, width, height, maxGray);
                //MostrarImagen.mostrar(matrizSuav, width, height, maxGray);
                //ESTA ES LA SOBEL
                //MatrizImagen matrizSobel = Filtros.aplicarFiltroSobel(matriz, width, height, maxGray);
                //MostrarImagen.mostrar(matrizSobel, width, height, maxGray);
                //ESTA ES LA PREWITT
                //MatrizImagen matrizPrewitt = Filtros.aplicarFiltroPrewitt(matriz, width, height, maxGray);
                //MostrarImagen.mostrar(matrizPrewitt, width, height, maxGray);
                //ESTA ES LA LAPLACIANA
                MatrizImagen matrizLaplaciana = Filtros.aplicarFiltroLaplaciano(matriz, width, height, maxGray);
                MostrarImagen.mostrar(matrizLaplaciana, width, height, maxGray);
                //ESTA ES LA REPUJADA
                MatrizImagen matrizRepujada = Filtros.aplicarFiltroRepujado(matriz, width, height, maxGray);
                MostrarImagen.mostrar(matrizRepujada, width, height, maxGray);
                
            } else {
                System.err.println("No se pudo leer la imagen.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

