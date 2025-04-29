package Main;

public class MatrizImagen {

	private int matriz[][];
	private int maxGray;
	
	public MatrizImagen() {

		this.matriz = new int[0][0];
		this.maxGray = 0;
		
	}
	
	public MatrizImagen(int matriz[][], int maxGray) {

		this.matriz = matriz;
		this.maxGray = maxGray;
		
	}
	
	public int[][] getMatriz(){
		return this.matriz;
	}
	
	public int getMaxGray() {
		return this.maxGray;
	}
	
	public int getHeight() {
		return this.matriz.length;
	}
	
	public int getWidth() {
		return this.matriz[0].length;
	}
	
	
	public int getValue(int i, int j) {
        if (i < 0 || i >= getHeight() || j < 0 || j >= getWidth()) {
            throw new IndexOutOfBoundsException("Índices fuera de rango.");
        }
        return this.matriz[i][j];
    }
}
