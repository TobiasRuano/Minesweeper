package Main;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class buscaMinas {

	static class par {
		int primer;
		int segundo;
	}

	static class celda {
		char valor;
		boolean visitado;
		
		public celda() {
			this.valor = 'X';
			this.visitado = false;
		}
	}

	public static void main(String[] args) {

		try {
			File file = new File("Path for the location of your test goes file here!");
			FileReader fr = new FileReader(file);
			BufferedReader  br = new BufferedReader(fr);
			
			int i, j, a, b;

			String line;
			line = br.readLine();
			String[] test;
			test = line.split(",");
			int filas = Integer.parseInt(test[0]);
			int columnas = Integer.parseInt(test[1]);
			int bombas = Integer.parseInt(test[2]);
			System.out.print("filas: " + filas);
			System.out.print("; Columnas: " + columnas);
			System.out.print("; Minas: " + bombas + "\n\n");

			celda[][] matrizInicial = new celda[filas][columnas];
			for(i = 0; i < filas ; i++) {
				for(j = 0; j < columnas; j++) {
					matrizInicial[i][j] = new celda();
				}
			}
			
			char caracter;
			for (i = 0; i < filas; i++) {
				for (j = 0; j < columnas; j++) {
					caracter = (char) br.read();
					if (caracter != '\n' && caracter != ' ') {
						matrizInicial[i][j].valor = caracter;
						matrizInicial[i][j].visitado = false;
						System.out.print(matrizInicial[i][j].valor);
					} else {
						j--;
					}
				}
				System.out.print("\n");
			}
			System.out.print("\n");
			fr.close(); // termino la lectura del archivo, por ende lo cierro

			ArrayList<par> resultado;
			resultado = new ArrayList<par>();

			for(i = 0; i < filas; i++) {
				for(j = 0; j < columnas; j++) {
					celda[][] copiaMatriz = new celda[filas][columnas];
					for(a = 0; a < filas ; a++) {
						for(b = 0; b < columnas; b++) {
							copiaMatriz[a][b] = new celda();
						}
					}
					for(a = 0; a < matrizInicial.length; a++) {
						  for(b = 0; b < matrizInicial[a].length; b++) {
							  copiaMatriz[a][b].valor = matrizInicial[a][b].valor;
							  copiaMatriz[a][b].visitado = matrizInicial[a][b].visitado;
						  }
					}
					ArrayList<par> listaClicks = new ArrayList<par>();
					ArrayList<par> listaEntrada = new ArrayList<par>();
					for(a = 0; a < resultado.size(); a++)
						listaEntrada.add(a, resultado.get(a));
					resultado = juego(listaEntrada, listaClicks, copiaMatriz, i, j, bombas);
				}
			}
			System.out.print("Se encontro la solucion mas optima en " + resultado.size() + " clicks. \n");
			for (i = 0; i < resultado.size(); i++) {
				System.out.print(resultado.get(i).primer + "," + resultado.get(i).segundo + "\n");
			}
		} catch(IOException e) {
			System.out.print("Error al ejecutar el algoritmo");
			e.printStackTrace();
		}
	}

	private static ArrayList<par> juego(ArrayList<par> listaSolucion, ArrayList<par> listaClicks, celda[][] matriz, int posF, int posC, int cantBombas) {
		int filas = matriz.length;
		int columnas = matriz[0].length;
		int i = 0, j = 0, pendientes;

		if(matriz[posF][posC].valor != 'X' && matriz[posF][posC].visitado == false) {
			
			par casilla = new par();
			casilla.primer = posF;
			casilla.segundo = posC;
			listaClicks.add(casilla);

			matriz = revelar(matriz, posF, posC);

			pendientes = contarPendientes(matriz);
			if(cantBombas == pendientes) { // Aca llegue a una hoja, por ende chequeo si los clicks actuales son o no la mejor solucion
				if(listaClicks.size() < listaSolucion.size() || listaSolucion.size() == 0) {
					int a;
					for(a = 0; a < listaClicks.size(); a++) {
						listaSolucion = listaClicks;
					}
				}
			} else {
				i = 0;
				while(i < filas) {
					j = 0;
					while(j < columnas && (listaClicks.size() < listaSolucion.size() || listaSolucion.size() == 0)) {
						listaSolucion = juego(listaSolucion, listaClicks, matriz, i, j, cantBombas);
						j++;
					}
					i++;
				}
			}
		}
		return listaSolucion;
	}

	private static celda[][] revelar(celda[][] matriz, int fila, int columna) {
		matriz[fila][columna].visitado = true;
		if(matriz[fila][columna].valor == '0') {
			int i = fila - 1, j = columna - 1;
			while(i <= fila + 1) {
				j = columna - 1;
				while(j <= columna + 1) {
					if(i >= 0 && i < matriz.length && j >= 0 && j < matriz[0].length && matriz[i][j].visitado == false) {
						matriz = revelar(matriz, i, j);
					}
					j++;
				}
				i++;
			}
		}
		return matriz;
	}

	private static int contarPendientes(celda[][] matriz) {
		int i, j, cant = 0;
		for(i = 0; i < matriz.length; i++) {
			for(j = 0; j < matriz[0].length; j++) {
				if(matriz[i][j].visitado == false) {
					cant ++;
				}
			}
		}
		return cant;
	}

}
