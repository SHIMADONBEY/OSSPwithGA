public class OSSP {

	private static int N_pop = 100;
	private static int N_nei = 2100;
	private static int t_Max = 1000;
	private static double p_c = 0.8;
	private static double p_m = 0.05;

	private static boolean duplicatable = false;
	private static boolean readDueDate = false;

	private static String outputFile = "result.txt";

	public static void main(String[] args) throws Exception {
		CheckSyntax(args);

		if (!(osspIO.readData(args[0], readDueDate))) {
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
		} else if (args.length >= 2) {

			for (int k = 1; k < args.length; k++) {
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
					} else {
						System.err.println("想定されていないコマンドです．" + args[k]);
						System.exit(1);
					}
				} catch (Exception e) {
					System.err.println("想定されていないコマンドです．\n" + e);
					System.exit(1);
				}
			}

		}

	}

}
