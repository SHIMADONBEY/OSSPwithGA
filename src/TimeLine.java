
public class TimeLine {
	private ProcessTime mProcessTime;
	private int[][] mTimeLine;

	private int mJobCount;
	private int mResourceCount;

	public TimeLine(int n, int m) {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		mJobCount = n;
		mResourceCount = m;

		mProcessTime = new ProcessTime(n, m);
		mTimeLine = new int[n][m];
	}

	public TimeLine(ProcessTime pTime) {
		mJobCount = pTime.getJobCount();
		mResourceCount = pTime.getResourceCount();

		mTimeLine = new int[mJobCount][mResourceCount];

		mProcessTime = new ProcessTime(mJobCount, mResourceCount);
		mProcessTime.setProcessTime(pTime.getProcessTime());
		mProcessTime.setDuplicatable(pTime.isDuplicatable());
	}

	public ProcessTime getProcessTime() {
		return mProcessTime;
	}

	public void setProcessTime(ProcessTime processTime) {
		mProcessTime.setProcessTime(processTime.getProcessTime());
		mProcessTime.setDuplicatable(processTime.isDuplicatable());
	}

	public int[][] getTimeLine() {
		return mTimeLine;
	}

	public void setTimeLine(int[][] timeLine) {
		for (int j = 0; j < mJobCount; j++) {
			for (int i = 0; i < mResourceCount; i++)
				mTimeLine[j][i] = timeLine[j][i];
		}

	}

	/**
	 * �t���[�^�C���̌v�Z
	 * 
	 * @param j �W���u�ԍ�
	 * @return �t���[�^�C��
	 */
	public int getFlowTime(int j) {
		int C = 0;
		for (int i = 0; i < mResourceCount; i++) {
			/* �S��Ƃōł��x���I����Ǝ������X�V */
			if (C < mTimeLine[j][i] + mProcessTime.getProcessTime(j, i)) {
				C = mTimeLine[j][i] + mProcessTime.getProcessTime(j, i);
			}
		}

		return C;
	}

	/**
	 * ���C�N�X�p���̌v�Z
	 * 
	 * @return Return ���C�N�X�p��
	 */
	public int getMakeSpan() {
		int ms = 0;
		int C;
		for (int j = 0; j < mJobCount; j++) {
			/* �S��Ƃōł��x���I����Ǝ������X�V */
			if ((C = getFlowTime(j)) > ms)
				ms = C;
		}

		return ms;
	}

	public void showTimeLine() {
		int[] workOrder = new int[mProcessTime.getResourceCount()];
		int i;

		for (int j = 0; j < mProcessTime.getJobCount(); j++) {
			System.out.printf("J_%-2d ", j);

			for (i = 0; i < mProcessTime.getResourceCount(); i++) {
				workOrder[i] = i;
			}

			Sort.sortValue(workOrder, mTimeLine[j]);
			for (i = mProcessTime.getResourceCount() - 1; i >= 0; i--) {
				System.out.printf("{%2d; %4d %4d} ", workOrder[i], mTimeLine[j][workOrder[i]],
						mTimeLine[j][workOrder[i]] + mProcessTime.getProcessTime(j, workOrder[i]));
			}
			System.out.printf("%4d\n", getFlowTime(j));
		}
		System.out.printf("\n makespan = %4d", getMakeSpan());
	}
}
