public class OSSPWithGA {
	private ProcessTime mProcessTime;
	private int mPopCount;
	private int mNeighborCount;
	private int mTimeLimit;
	private double mCrossOverRate;
	private double mMutationRate;

	public OSSPWithGA(int n, int m) {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		mProcessTime = new ProcessTime(n, m);
	}

	public ProcessTime getProcessTime() {
		return mProcessTime;
	}

	public void setProcessTime(ProcessTime processTime) {
		mProcessTime.setProcessTime(processTime.getProcessTime());
		mProcessTime.setDueDate(processTime.getDueDate());
		mProcessTime.setDuplicatable(processTime.isDuplicatable());
	}

	public TimeLine methodOfGA(int N_pop, int N_nei, int t_Max, double p_c, double p_m) {
		if ((checkPopCount(N_pop, N_nei))) {
			return null;
		}

		if (checkParamaterError(t_Max, p_c, p_m)) {
			return null;
		}

		if (mProcessTime.getProcessTime() == null) {
			System.err.println("���Ԃ��ݒ肳��Ă��܂���");
		}

		System.out.println("��`�I�A���S���Y��");

		Individuals parents = new Individuals(mProcessTime, mPopCount, mCrossOverRate,
				mMutationRate);

		/* �����̌Q���� */
		parents.generateGene();

		int t = 0;
		System.out.printf("%4d \t %g\n", t, parents.getElite().getFitness());

		int i;
		int[] pairOrder = new int[N_pop];
		Individuals children;
		while (t < mTimeLimit) {
			/* �I�� */
			pairOrder = parents.getParentList();

			/* ���� */
			for (i = 0; i < N_pop; i += 2) {
				parents.crossover(pairOrder[i], pairOrder[i + 1], mProcessTime.getJobCount(),
						mProcessTime.getResourceCount());
			}

			/* �ψ� */
			for (Chromosome chromosome : parents.getChromosome()) {
				chromosome.mutate();
			}

			/* �ߖT�̐��� */
			children = null;
			children = new Individuals(mProcessTime, mNeighborCount, p_c, p_m);
			children.setElite(parents.getElite());
			children.generateNeighbor(parents);

			t++;

			/* �̂̕]�� */
			for (Chromosome chromosome : children.getChromosome()) {
				chromosome.calcFitness();
			}

			/* �̌Q�̐����c�� */
			parents = null;
			parents = new Individuals(mProcessTime, mPopCount, p_c, p_m);
			parents.survive(children);

			System.out.printf("%4d \t %g\n", t, parents.getElite().getFitness());
		}

		return parents.getElite().getTimeLine();
	}

	private boolean checkPopCount(int N_c, int N_p) {
		if (N_c < 1 || N_c % 2 == 1) {
			System.err.println("�̐���2�ȏ�̋����ł��I");
			return true;
		}

		mPopCount = N_c;

		if (N_p % 2 == 1) {
			System.err.println("�̐��͋����ł��I");
			return true;
		} else if (mPopCount > N_p) {
			mNeighborCount = mPopCount;
		} else {
			mNeighborCount = N_p;
		}
		return false;
	}

	private boolean checkParamaterError(int t_max, double p_c, double p_m) {
		if (t_max < 0) {
			System.err.println("�ő吢�㐔��0�ȏ�ł��I");
			return true;
		}
		mTimeLimit = t_max;

		setCrossOverRate(p_c);
		setMutationRate(p_m);

		return false;
	}

	public void setCrossOverRate(double crossOverRate) {
		if (crossOverRate < 0) {
			mCrossOverRate = 0.0;
		} else if (crossOverRate > 1) {
			mCrossOverRate = 1.0;
		} else {
			mCrossOverRate = crossOverRate;
		}

	}

	public void setMutationRate(double mutationRate) {
		if (mutationRate < 0) {
			mutationRate = 0.0;
		} else if (mutationRate > 1) {
			mutationRate = 1.0;
		} else {
			mMutationRate = mutationRate;
		}
	}
}
