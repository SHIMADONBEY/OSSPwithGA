import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class osspIO {
	private static int jobCount;
	private static int resourceCount;

	private static int[][] processTime;
	private static int[] dueDate;

	public osspIO() {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public static int getJobCount() {
		return jobCount;
	}

	public static int getResourceCount() {
		return resourceCount;
	}

	public static int[][] getProcessTime() {
		return processTime;
	}

	public static int[] getDueDate() {
		return dueDate;
	}

	/**
	 * @param fileName �t�@�C����
	 * @param readDueDate �[����ǂݍ��ނ�
	 * @return �����Ftrue, ���s�Ffalse
	 * @throws Exception ArrayIndexOutOfBoundsException
	 */
	public static boolean readData(String fileName, boolean readDueDate) throws Exception {
		FileInputStream inputStream = null;

		try {
			File file = new File(fileName);
			inputStream = new FileInputStream(file);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("�t�@�C�����J�����Ƃ��ł��܂���ł����D" + e);
			return false;
		}

		BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream));

		String line;

		int j = 0;
		boolean isDeclaration = false;

		try {
			while ((line = bReader.readLine()) != null) {
				String[] token = line.split(" ");
				if (isDeclaration) {
					if (readTimeData(token, j, readDueDate)) {
						j++;
					}

					if (j == jobCount)
						break;
				} else {
					declareTime(token);
					isDeclaration = true;
				}

			}
		} catch (ArrayIndexOutOfBoundsException aiobe) {
			return false;
		} catch (NumberFormatException nfe) {
			System.err.println("���l����͂��Ă��������D");
			return false;
		} finally {
			bReader.close();
		}

		return true;
	}

	public static boolean writeData(long t, TimeLine tl, String fileName) throws Exception {
		/* �^�C�����C�������o�� */
		try {
			// �����o���t�@�C���w��
			PrintWriter fout = new PrintWriter(fileName);

			// �����o��
			int[][] startTime = tl.getTimeLine();
			for (int j = 0; j < jobCount; j++) {
				for (int i = 0; i < resourceCount; i++) {
					fout.printf("%d ", startTime[j][i]);
				}
				fout.println();
			}

			fout.printf("Excecution Time(Seconds) = %.3f\n", t / 1000.0);

			fout.close();
		} catch (Exception e) {
			System.err.println("�t�@�C���̏����o���Ɏ��s�D" + e);
			System.exit(1);
		}

		return true;
	}

	private static void declareTime(String[] token) throws NumberFormatException {
		if (token.length == 2) {
			jobCount = Integer.parseInt(token[0]);
			resourceCount = Integer.parseInt(token[1]);

			processTime = new int[jobCount][resourceCount];
			dueDate = new int[jobCount];
		}
	}

	private static boolean readTimeData(String[] token, int j,
			boolean readDueDate) throws NumberFormatException {

		if (token.length < resourceCount) {
			return false;
		} else if (readDueDate && token.length <= resourceCount) {
			return false;
		}

		for (int k = 0; k < resourceCount; k++) {
			processTime[j][k] = Integer.parseInt(token[k]);
		}

		if (readDueDate) {
			dueDate[j] = Integer.parseInt(token[resourceCount]);
		}
		return true;
	}

}
