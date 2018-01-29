/**
 * ���F�̃N���X
 * 
 * @author b113048
 */
public class Chromosome {
	private double mMutationRate; // �ˑR�ψٗ�
	private double mFitness; // �K���x
	private int[][] mGene; // ��`�q�z��
	private TimeLine mTimeLine; // �^�C�����C��
	private int mLength; // ��`�q��

	private final double LAMBDA = 16379.0;

	/**
	 * �R���X�g���N�^�D
	 * 
	 * @param l ��`�q��
	 * @param p_m �ˑR�ψٗ�
	 */
	public Chromosome(int n, int m, double p_m) {
		mLength = n * m;
		mTimeLine = new TimeLine(n, m);
		mGene = new int[mLength][2];
		mMutationRate = p_m;
		mFitness = 0.0;
	}

	public Chromosome(ProcessTime pTime, double p_m) {
		mLength = pTime.getJobCount() * pTime.getResourceCount();
		mTimeLine = new TimeLine(pTime);

		mGene = new int[mLength][2];
		mMutationRate = p_m;
		mFitness = 0.0;
	}

	public double getMutationRate() {
		return mMutationRate;
	}

	public double getFitness() {
		return mFitness;
	}

	public void setFitness(double fitness) {
		mFitness = fitness;
	}

	public int[][] getGene() {
		return mGene;
	}

	public int[] getGene(int locus) {
		return mGene[locus];
	}

	/**
	 * @param gene ��`�q�z��
	 */
	public void setGene(int[][] gene) {
		for (int i = 0; i < mLength; i++) {
			for (int j = 0; j < 2; j++)
				mGene[i][j] = gene[i][j];
		}
	}

	public int getLength() {
		return mLength;
	}

	/**
	 * @return �K���x�֐�
	 */
	public void calcFitness() {
		DispatchingRule.FIFO(mGene, mTimeLine.getProcessTime(), mTimeLine.getTimeLine(),
				mTimeLine.getProcessTime().isDuplicatable());

		mFitness = ((LAMBDA - mTimeLine.getMakeSpan()) / (double) LAMBDA);
	}

	public TimeLine getTimeLine() {
		return mTimeLine;
	}

	public void setTimeLine(TimeLine timeLine) {
		mTimeLine.setTimeLine(timeLine.getTimeLine());
	}

	public void setTimeLine(int[][] timeLine) {
		mTimeLine.setTimeLine(timeLine);
	}

	public void generateGene() {
		int[] randomRow = new int[mLength];
		int m = mTimeLine.getProcessTime().getResourceCount();

		int i;

		for (i = 0; i < mLength; i++) {
			randomRow[i] = i;
		}

		for (i = 0; i < mLength; i++) {
			int j = (int) (Math.random() * (mLength - i)) + i;
			int swp = randomRow[i];
			randomRow[i] = randomRow[j];
			randomRow[j] = swp;
		}

		for (i = 0; i < mLength; i++) {
			mGene[i][0] = randomRow[i] / m;
			mGene[i][1] = randomRow[i] % m;
		}

	}

	/**
	 * �ˑR�ψ� (�t��)
	 * 
	 * @param itr �J��Ԃ���
	 */
	public void mutate() {

		// �ˑR�ψق��s��������
		if (Math.random() >= mMutationRate) {
			return;
		}

		int idxA = (int) Math.random() * mLength;
		int idxC = ((int) (Math.random() * (mLength - 1) + 1) + idxA) % mLength;

		for (int k = 0; k < 2; k++) {
			int swp = mGene[idxA][k]; // �X���b�v�ϐ�
			mGene[idxA][k] = mGene[idxC][k];
			mGene[idxC][k] = swp;
		}
	}

	public void changeJobNeighbor(int jobNum) {
		int m = mTimeLine.getProcessTime().getResourceCount();
		int place = (int) (Math.random() * m) + 1;

		int fore = place - 1;
		if (fore == 0) {
			fore = m;
		}

		int idxPlace;
		int idxFore;
		int curJobCount = 0;

		for (idxFore = 0; idxFore < mLength; idxFore++) {
			if (mGene[idxFore][0] == jobNum) {
				curJobCount++;
				if (curJobCount == fore) {
					break;
				}
			}
		}

		curJobCount = 0;
		for (idxPlace = 0; idxPlace < mLength; idxPlace++) {
			if (mGene[idxPlace][0] == jobNum) {
				curJobCount++;
				if (curJobCount == place) {
					break;
				}
			}
		}

		int swp = mGene[idxFore][1];
		mGene[idxFore][1] = mGene[idxPlace][1];
		mGene[idxPlace][1] = swp;
	}

	public void changeResourceNeighbor(int resourceNum) {
		int n = mTimeLine.getProcessTime().getJobCount();
		int place = (int) (Math.random() * n) + 1;

		int fore = place - 1;
		if (fore == 0) {
			fore = n;
		}

		int idxPlace;
		int idxFore;
		int curResourceCount = 0;

		for (idxFore = 0; idxFore < mLength; idxFore++) {
			if (mGene[idxFore][1] == resourceNum) {
				curResourceCount++;
				if (curResourceCount == fore) {
					break;
				}
			}
		}

		curResourceCount = 0;
		for (idxPlace = 0; idxPlace < mLength; idxPlace++) {
			if (mGene[idxPlace][1] == resourceNum) {
				curResourceCount++;
				if (curResourceCount == place) {
					break;
				}
			}
		}

		int swp = mGene[idxFore][0];
		mGene[idxFore][0] = mGene[idxPlace][0];
		mGene[idxPlace][0] = swp;
	}

	/**
	 * 2opt�@�ɂ���`�q�̋���
	 * 
	 * @param m �n�}�f�[�^
	 */
	public void enforceGene() {

	}

}
