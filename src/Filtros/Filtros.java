package Filtros;

import Main.MatrizImagen;

public class Filtros {

	public static MatrizImagen filtroSuavizadoPromedio(MatrizImagen matriz, int width, int height, int maxGray) {
		int[][] suavizada = new int[height][width];

		// Definimos los desplazamientos para los vecinos
		int[] dx = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
		int[] dy = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int suma = 0;
				int contador = 0;

				for (int k = 0; k < 9; k++) {
					int ni = i + dy[k];
					int nj = j + dx[k];

					// Solo usamos el pixel que este en los parametros
					if (ni >= 0 && ni < height && nj >= 0 && nj < width) {
						suma += matriz.getValue(ni, nj);
						contador++;
					}
				}

				suavizada[i][j] = suma / contador;
			}
		}

		MatrizImagen matrizSuavizada = new MatrizImagen(suavizada, maxGray);

		return matrizSuavizada;
	}

	public static MatrizImagen filtroSuavizadoPromedioGaussiano(MatrizImagen matriz, int width, int height,
			int maxGray) {
		int[][] suavizada = new int[height][width];

		// Máscara Gaussiana 3x3
		int[][] pesos = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int suma = 0;
				int pesoTotal = 0;

				for (int di = -1; di <= 1; di++) {
					for (int dj = -1; dj <= 1; dj++) {
						int ni = i + di;
						int nj = j + dj;

						if (ni >= 0 && ni < height && nj >= 0 && nj < width) {
							int peso = pesos[di + 1][dj + 1];
							suma += matriz.getValue(ni, nj) * peso;
							pesoTotal++;
						}
					}
				}

				suavizada[i][j] = suma / pesoTotal;
			}
		}

		MatrizImagen matrizSuavizada = new MatrizImagen(suavizada, maxGray);

		return matrizSuavizada;
	}

	private static final int[][] SOBEL_X = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

	private static final int[][] SOBEL_Y = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	public static MatrizImagen aplicarFiltroSobel(MatrizImagen matriz, int width, int height, int maxGray) {
		int alto = height;
		int ancho = width;
		int[][] resultado = new int[alto][ancho];
		int maxValor = 0;

		// Paso 1: aplicar filtro y encontrar el máximo
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				int gx = 0;
				int gy = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						gx += pixel * SOBEL_X[i + 1][j + 1];
						gy += pixel * SOBEL_Y[i + 1][j + 1];
					}
				}

				// Calcular el valor de gradiente
				int valor = (int) Math.sqrt(gx * gx + gy * gy);
				resultado[y][x] = valor;

				// Encontrar el máximo valor para luego escalar
				if (valor > maxValor) {
					maxValor = valor;
				}
			}
		}

		// Paso 2: escalar los valores al rango 0-255 o 0-65535 dependiendo del maxGray
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				// Escalar los valores según el rango de maxGray
				if (maxGray == 255) {
					// Para imágenes de 8 bits (0-255)
					resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * 255);
				} else if (maxGray == 65535) {
					// Para imágenes de 16 bits (0-65535)
					resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * 65535);
				}

				// Asegurarse de que el valor esté dentro del rango
				resultado[y][x] = Math.min(maxGray, Math.max(0, resultado[y][x]));
			}
		}

		// Devolver la imagen resultante con la escala adecuada (maxGray puede ser 255 o
		// 65535)
		return new MatrizImagen(resultado, maxGray);
	}

	public static MatrizImagen aplicarFiltroPrewitt(MatrizImagen imagen, int width, int height, int maxGray) {
		// Validar los parámetros
		if (imagen == null || width <= 0 || height <= 0 || maxGray <= 0) {
			throw new IllegalArgumentException("Parámetros inválidos.");
		}

		int[][] originalMatriz = imagen.getMatriz();

		// Crear matrices para los bordes horizontales y verticales
		int[][] prewittHorizontal = new int[height][width];
		int[][] prewittVertical = new int[height][width];

		// Definir los kernels Prewitt para horizontal y vertical
		int[][] kernelHorizontal = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };

		int[][] kernelVertical = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };

		// Aplicar el filtro Prewitt sobre cada píxel (exceptuando los bordes)
		for (int i = 1; i < height - 1; i++) {
			for (int j = 1; j < width - 1; j++) {
				int gx = 0;
				int gy = 0;

				// Convolución con los kernels horizontal y vertical
				for (int ki = -1; ki <= 1; ki++) {
					for (int kj = -1; kj <= 1; kj++) {
						gx += kernelHorizontal[ki + 1][kj + 1] * originalMatriz[i + ki][j + kj];
						gy += kernelVertical[ki + 1][kj + 1] * originalMatriz[i + ki][j + kj];
					}
				}

				// Calcular la magnitud del gradiente
				int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
				prewittHorizontal[i][j] = Math.min(magnitude, maxGray);
				prewittVertical[i][j] = Math.min(magnitude, maxGray);
			}
		}

		// Retornar el objeto MatrizImagen con la matriz procesada
		return new MatrizImagen(prewittHorizontal, maxGray);
	}

	private static final int[][] LAPLACIANO_KERNEL = { { 0, 1, 0 }, { 1, -4, 1 }, { 0, 1, 0 } };

	public static MatrizImagen aplicarFiltroLaplaciano(MatrizImagen matriz, int width, int height, int maxGray) {
		int alto = height;
		int ancho = width;
		int[][] resultado = new int[alto][ancho];
		int maxValor = 0;

		// Aplicar el filtro y encontrar el máximo
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				int valor = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						valor += pixel * LAPLACIANO_KERNEL[i + 1][j + 1];
					}
				}

				valor = Math.abs(valor); // Asegura valores positivos
				resultado[y][x] = valor;

				if (valor > maxValor) {
					maxValor = valor;
				}
			}
		}

		// Escalado proporcional al maxGray original
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * maxGray);
			}
		}

		return new MatrizImagen(resultado, maxGray); // conserva el mismo maxGray de entrada
	}

	private static final int[][] REPUJADO_KERNEL = { { -2, -1, 0 }, { -1, 1, 1 }, { 0, 1, 2 } };

	public static MatrizImagen aplicarFiltroRepujado(MatrizImagen matriz, int width, int height, int maxGray) {
		int alto = height;
		int ancho = width;
		int[][] resultado = new int[alto][ancho];
		int minValor = Integer.MAX_VALUE;
		int maxValor = Integer.MIN_VALUE;

		// Aplicar el filtro y buscar valores extremos
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				int suma = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						suma += pixel * REPUJADO_KERNEL[i + 1][j + 1];
					}
				}

				resultado[y][x] = suma;

				if (suma < minValor)
					minValor = suma;
				if (suma > maxValor)
					maxValor = suma;
			}
		}

		// Normalizar los valores al rango [0, maxGray]
		for (int y = 1; y < alto - 1; y++) {
			for (int x = 1; x < ancho - 1; x++) {
				int valor = resultado[y][x];
				valor = (int) (((valor - minValor) / (double) (maxValor - minValor)) * maxGray);
				resultado[y][x] = Math.min(Math.max(0, valor), maxGray);
			}
		}

		return new MatrizImagen(resultado, maxGray);
	}
}
