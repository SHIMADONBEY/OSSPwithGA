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

		/* ���̓f�[�^�̕\�� */
		solver.getProcessTime().showStatus();

		TimeLine osspResult = new TimeLine(solver.getProcessTime());

		long startTime = System.currentTimeMillis();

		/* OSSP������ */
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
					System.err.println("�z�肳��Ă��Ȃ��R�}���h�ł��D\n" + e);
					System.exit(1);
				}
			}

		}

	}

	private static void showSyntax() {
		System.out.println("Usage: OSSP <inputFile> [<Options>]\n");

		System.out.println("Options�@�X�C�b�`�ꗗ");
		System.out.println("-o <outputFile>\n\t <outputFile>�Ɍ��ʂ��o�͂���D");
		System.out.println("-NP <PopCount> \n\t �̐���<PopCount>�ɐݒ肷��."
				+ "�i�f�t�H���g�F100�j");
		System.out.println("-NN <PopCount> \n\t �ߖT�̐��ƌ̐��̘a��<PopCount>�ɐݒ肷��."
				+ "�i�f�t�H���g�F2100�j");
		System.out.println("-t <Integer> \n \t ��`�q������s���񐔂�<Integer>�ɂ���D�i�f�t�H���g�F1000�j");
		System.out.println("-pc <realNumber> \n\t �����m����<realNumber>�ɂ���D�i�f�t�H���g�F0.8�j");
		System.out.println("-pm <realNumber> \n\t �ˑR�ψيm����<realNumber>�ɂ���D�i�f�t�H���g�F0.05�j");
		System.out.println("-dup\n\t �K���g�`���[�g�ɓ����ɓ����d���̕����̍�Ƃ̊��蓖�Ă��\�ɂ��܂��D�i�f�t�H���g�F�����j");
		System.out.println("-due\n\t ���̓t�@�C�����瓯���ɔ[�����ǂݍ��ށD�i�f�t�H���g�F�����j");
		System.out.println("-h\n\t �w���v��������D");
	}

}
