public class OSSP {

	private static int N_pop = 100;
	private static int N_nei = 2100;
	private static int t_Max = 1000;
	private static double p_c = 0.8;
	private static double p_m = 0.05;

	private static boolean duplicatable = false;
	private static boolean readDueDate = false;

	private static String inputFile;
	private static String outputFile = "result.txt";

	public static void main(String[] args) throws Exception {
		CheckSyntax(args);

		if (!(osspIO.readData(inputFile, readDueDate))) {
			return;
		}

		OSSPWithGA solver = new OSSPWithGA(osspIO.getJobCount(), osspIO.getResourceCount());

		solver.getProcessTime().setDuplicatable(duplicatable);
		solver.getProcessTime().setProcessTime(osspIO.getProcessTime());
		solver.getProcessTime().setDueDate(osspIO.getDueDate());

		/* 入力データの表示 */
		solver.getProcessTime().showStatus();

		TimeLine osspResult = new TimeLine(solver.getProcessTime());

		long startTime = System.currentTimeMillis();

		/* OSSPを求解 */
		osspResult = solver.methodOfGA(N_pop, N_nei, t_Max, p_c, p_m);

		long finishTime = System.currentTimeMillis();

		System.out.println("Finished:\n");

		osspResult.showTimeLine();

		osspIO.writeData((finishTime - startTime), osspResult, outputFile);

	}

	private static void CheckSyntax(String args[]) {
		if (args.length < 1) {
			System.err.println("Usage: OSSP <InputFileName>[ <Option>]\n");
			System.exit(1);
		} else {
			for (int k = 0; k < args.length; k++) {
				try {
					if ("-o".equals(args[k])) {
						outputFile = args[++k];
					} else if ("-NP".equals(args[k])) {
						N_pop = Integer.parseInt(args[++k]);
					} else if ("-NN".equals(args[k])) {
						N_nei = Integer.parseInt(args[++k]);
					} else if ("-t".equals(args[k])) {
						t_Max = Integer.parseInt(args[++k]);
					} else if ("-pc".equals(args[k])) {
						p_c = Double.parseDouble(args[++k]);
					} else if ("-pm".equals(args[k])) {
						p_m = Double.parseDouble(args[++k]);
					} else if ("-dup".equals(args[k])) {
						duplicatable = !duplicatable;
					} else if ("-due".equals(args[k])) {
						readDueDate = !readDueDate;
					} else if ("-h".equals(args[k])) {
						showSyntax();
						System.exit(0);
					} else {
						inputFile = args[k];
					}
				} catch (Exception e) {
					System.err.println("想定されていないコマンドです．\n" + e);
					System.exit(1);
				}
			}

		}

	}

	private static void showSyntax() {
		System.out.println("Usage: OSSP <inputFile> [<Options>]\n");

		System.out.println("Options　スイッチ一覧");
		System.out.println("-o <outputFile>\n\t <outputFile>に結果を出力する．");
		System.out.println("-NP <PopCount> \n\t 個体数を<PopCount>に設定する."
				+ "（デフォルト：100）");
		System.out.println("-NN <PopCount> \n\t 近傍個体数と個体数の和を<PopCount>に設定する."
				+ "（デフォルト：2100）");
		System.out.println("-t <Integer> \n \t 遺伝子操作を行う回数を<Integer>にする．（デフォルト：1000）");
		System.out.println("-pc <realNumber> \n\t 交叉確率を<realNumber>にする．（デフォルト：0.8）");
		System.out.println("-pm <realNumber> \n\t 突然変異確率を<realNumber>にする．（デフォルト：0.05）");
		System.out.println("-dup\n\t ガントチャートに同時に同じ仕事の複数の作業の割り当てを可能にします．（デフォルト：無効）");
		System.out.println("-due\n\t 入力ファイルから同時に納期も読み込む．（デフォルト：無効）");
		System.out.println("-h\n\t ヘルプを見せる．");
	}

}
