
public class ProcessTime {
	private int mJobCount;
	private int mResourceCount;
	private int[][] mProcessTime;
	private int[] mDueDate;
	private boolean mDuplicatable;

	public ProcessTime(int n, int m) {
		mJobCount = n;
		mResourceCount = m;
		mProcessTime = new int[n][m];
		mDueDate = new int[n];
		mDuplicatable = false;
	}

	public boolean isDuplicatable() {
		return mDuplicatable;
	}

	public void setDuplicatable(boolean duplicatable) {
		mDuplicatable = duplicatable;
	}

	public int[] getDueDate() {
		return mDueDate;
	}

	public int getDueDate(int j) {
		return mDueDate[j];
	}

	public void setDueDate(int[] dueDate) {
		for (int j = 0; j < mJobCount; j++)
			mDueDate[j] = dueDate[j];
	}

	public int[][] getProcessTime() {
		return mProcessTime;
	}

	public int getJobCount() {
		return mJobCount;
	}

	public int getResourceCount() {
		return mResourceCount;
	}

	public int getProcessTime(int j, int i) {
		return mProcessTime[j][i];
	}

	public void setProcessTime(int[][] processTime) {
		for (int j = 0; j < mJobCount; j++) {
			for (int i = 0; i < mResourceCount; i++) {
				mProcessTime[j][i] = processTime[j][i];
			}
		}
	}

	public void showStatus() {
		System.out.println("JobCount: " + mJobCount + ", ResourceCount: " + mResourceCount);

		int j;
		int i;

		System.out.print("     ");
		for (i = 0; i < mResourceCount; i++) {
			System.out.printf("R_%-2d ", i + 1);
		}

		System.out.println("Due");

		for (j = 0; j < mJobCount; j++) {
			System.out.printf("J_%-2d ", j + 1);
			for (i = 0; i < mResourceCount; i++) {
				System.out.printf("%4d ", mProcessTime[j][i]);
			}
			System.out.printf("%4d\n", mDueDate[j]);
		}
		System.out.println();
	}
}
