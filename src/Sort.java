
public class Sort {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] idRow = {
				0, 1, 2, 3, 4, 5, 6, 7, 8
		};
		double[] valRow = new double[idRow.length];

		for (int i = 0; i < idRow.length; i++) {
			valRow[i] = (double) (int) (Math.random() * 100 + 0.5) / 10;

			System.out.printf("%4d\t%4.1f\n", idRow[i], valRow[i]);
		}

		System.out.println("並べ替え\n");

		sortValue(idRow, valRow);

		for (int i = 0; i < idRow.length; i++) {
			System.out.printf("%4d\t%4.1f\n", idRow[i], valRow[idRow[i]]);
		}

		System.out.println("終わり");
	}

	/**
	 * 個体価値列の値が大きい順番に認識個体列を並べ替える
	 * 
	 * @param idRow 認識個体列
	 * @param valRow 個体評価値列
	 */
	public static void sortValue(int[] idRow, double[] valRow) {
		int length;
		int swp;
		if (idRow.length != valRow.length) {
			return;
		} else {
			length = idRow.length;
		}
		for (int i = (length - 2) / 2; i >= 0; i--) {
			downHeap(i, length - 1, idRow, valRow);
		}
		for (int i = length - 1; i > 0; i--) {
			swp = idRow[0];
			idRow[0] = idRow[i];
			idRow[i] = swp;

			downHeap(0, i - 1, idRow, valRow);
		}

	}

	private static void downHeap(int k, int r, int[] id, double[] val) {
		int j, v;
		v = id[k];
		while (true) {
			j = 2 * k + 1;
			if (j > r) {
				break;
			}
			if (j != r) {
				if (val[id[j + 1]] < val[id[j]]) {
					j = j + 1;
				}
			}
			if (val[v] <= val[id[j]]) {
				break;
			}
			id[k] = id[j];
			k = j;
		}
		id[k] = v;
	}

	public static void sortValue(int[] idRow, int[] valRow) {
		int length;
		int swp;
		if (idRow.length != valRow.length) {
			return;
		} else {
			length = idRow.length;
		}
		for (int i = (length - 2) / 2; i >= 0; i--) {
			downHeap(i, length - 1, idRow, valRow);
		}
		for (int i = length - 1; i > 0; i--) {
			swp = idRow[0];
			idRow[0] = idRow[i];
			idRow[i] = swp;

			downHeap(0, i - 1, idRow, valRow);
		}

	}

	private static void downHeap(int k, int r, int[] id, int[] val) {
		int j, v;
		v = id[k];
		while (true) {
			j = 2 * k + 1;
			if (j > r) {
				break;
			}
			if (j != r) {
				if (val[id[j + 1]] < val[id[j]]) {
					j = j + 1;
				}
			}
			if (val[v] <= val[id[j]]) {
				break;
			}
			id[k] = id[j];
			k = j;
		}
		id[k] = v;
	}
}
