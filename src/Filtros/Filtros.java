package Filtros;

import Main.MatrizImagen;

public class Filtros {

	public static MatrizImagen filtroSuavizadoPromedio(MatrizImagen matriz, int width, int height, int maxGray) {
		int[][] suavizada = new int[height][width];

		int[][] vecinos = {
				{ -1, -1 }, { -1, 0 }, { -1, 1 },
				{  0, -1 }, {  0, 0 }, {  0, 1 },
				{  1, -1 }, {  1, 0 }, {  1, 1 }
		};

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int suma = 0;
				int contador = 0;

				for (int k = 0; k < vecinos.length; k++) {
					int coordX = i + vecinos[k][0];
					int coordY = j + vecinos[k][1];

					// Si esta en los limites:
					if (coordX >= 0 && coordX < height && coordY >= 0 && coordY < width) {
						suma += matriz.getValue(coordX, coordY);
						contador++;
					}
				}

				suavizada[i][j] = suma / contador;
			}
		}

		return new MatrizImagen(suavizada, maxGray);
	}

	public static MatrizImagen filtroSuavizadoPromedioGaussiano(MatrizImagen matriz, int width, int height,
			int maxGray) {
		int[][] suavizada = new int[height][width];

		int[][] pesosVecinos = { { 1, 2, 1 }, { 2, 4, 2 }, { 1, 2, 1 } };

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int suma = 0;
				int pesoTotal = 16;

				for (int di = -1; di <= 1; di++) {
					for (int dj = -1; dj <= 1; dj++) {
						int cordX = i + di;
						int cordY = j + dj;

						if (cordX >= 0 && cordX < height && cordY >= 0 && cordY < width) {
							int peso = pesosVecinos[di + 1][dj + 1];
							suma += matriz.getValue(cordX, cordY) * peso;
						}
					}
				}

				suavizada[i][j] = suma / pesoTotal;
			}
		}

		return new MatrizImagen(suavizada, maxGray);
	}

	private static final int[][] SOBEL_X = { { -1, 0, 1 }, { -2, 0, 2 }, { -1, 0, 1 } };

	private static final int[][] SOBEL_Y = { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	public static MatrizImagen aplicarFiltroSobel(MatrizImagen matriz, int width, int height, int maxGray) {
		int[][] resultado = new int[height][width];
		int maxValor = 0;

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int gradX = 0;
				int gradY = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						gradX += pixel * SOBEL_X[i + 1][j + 1];
						gradY += pixel * SOBEL_Y[i + 1][j + 1];
					}
				}

				int magnitud = (int) Math.sqrt(gradX * gradX + gradY * gradY);
				resultado[y][x] = magnitud;

				if (magnitud > maxValor) {
					maxValor = magnitud;
				}
			}
		}

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				if (maxGray == 255) {
					resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * 255);
				} else if (maxGray == 65535) {
					resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * 65535);
				}

				resultado[y][x] = Math.min(maxGray, Math.max(0, resultado[y][x]));
			}
		}

		return new MatrizImagen(resultado, maxGray);
	}

	public static MatrizImagen aplicarFiltroPrewitt(MatrizImagen imagen, int width, int height, int maxGray) {
		int[][] originalMatriz = imagen.getMatriz();

		int[][] prewittHorizontal = new int[height][width];
		int[][] prewittVertical = new int[height][width];

		int[][] kernelHorizontal = { { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 } };

		int[][] kernelVertical = { { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 } };

		for (int i = 1; i < height - 1; i++) {
			for (int j = 1; j < width - 1; j++) {
				int gx = 0;
				int gy = 0;

				for (int mascI = -1; mascI <= 1; mascI++) {
					for (int mascJ = -1; mascJ <= 1; mascJ++) {
						gx += kernelHorizontal[mascI + 1][mascJ + 1] * originalMatriz[i + mascI][j + mascJ];
						gy += kernelVertical[mascI + 1][mascJ + 1] * originalMatriz[i + mascI][j + mascJ];
					}
				}

				prewittHorizontal[i][j] = Math.min(Math.abs(gx), maxGray);
				prewittVertical[i][j] = Math.min(Math.abs(gy), maxGray);
			}
		}

		return new MatrizImagen(prewittHorizontal, maxGray);
	}

	private static final int[][] LAPLACIANO_KERNEL = { { 0, 1, 0 }, { 1, -4, 1 }, { 0, 1, 0 } };

	public static MatrizImagen aplicarFiltroLaplaciano(MatrizImagen matriz, int width, int height, int maxGray) {
		int[][] resultado = new int[height][width];
		int maxValor = 0;

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int valor = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						valor += pixel * LAPLACIANO_KERNEL[i + 1][j + 1];
					}
				}

				valor = Math.abs(valor);
				resultado[y][x] = valor;

				if (valor > maxValor) {
					maxValor = valor;
				}
			}
		}

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				resultado[y][x] = (int) ((resultado[y][x] / (double) maxValor) * maxGray);
			}
		}

		return new MatrizImagen(resultado, maxGray);
	}

	private static final int[][] REPUJADO_KERNEL =
			{ { -1, -1, -1 },
			{0, 0, 0 },
			{ 1, 1, 1 } };

	public static MatrizImagen aplicarFiltroRepujado(MatrizImagen matriz, int width, int height, int maxGray) {
		int[][] resultado = new int[height][width];
		int maxInt = Integer.MAX_VALUE;
		int minInt = Integer.MIN_VALUE;

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int suma = 0;

				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int pixel = matriz.getValue(y + i, x + j);
						suma += pixel * REPUJADO_KERNEL[i + 1][j + 1];
					}
				}

				resultado[y][x] = suma;

				if (suma < maxInt)
					maxInt = suma;
				if (suma > minInt)
					minInt = suma;
			}
		}

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				int valor = resultado[y][x];
				valor = (int) (((valor - maxInt) / (double) (minInt - maxInt)) * maxGray);
				resultado[y][x] = Math.min(Math.max(0, valor), maxGray);
			}
		}

		return new MatrizImagen(resultado, maxGray);
	}
}
