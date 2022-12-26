
import java.util.ArrayList;

/* ドロネー三角形分割で生成した図形から，最大の1-単体の複体の構成情報,r=r_n時の単体の構成状況を格納 */
public class Simplex1 {

	private ArrayList<ArrayList<Boolean>> simplex1;
	private ArrayList<ArrayList<Integer>> birth_time;

	public int points_num;

	public Simplex1(int p_num) {
		int i, j;

		this.simplex1 = new ArrayList<>();
		this.birth_time = new ArrayList<>();
		this.points_num = p_num;

		for (i = 0; i < points_num; i++) {
			this.simplex1.add(new ArrayList<>());
			this.birth_time.add(new ArrayList<>());
			for (j = 0; j < points_num - (i + 1); j++) {
				this.simplex1.get(i).add(false);
				this.birth_time.get(i).add(0);
			}
		}
	}

	public Boolean getConect(int i, int j) {
		//
		return this.simplex1.get(i).get(j - (i + 1));
	}

	public void setConect(int i, int j, boolean b) {
		//
		this.simplex1.get(i).set(j - (i + 1), b);
	}

	public int getBirth(int i, int j) {
		//
		return this.birth_time.get(i).get(j - (i + 1));
	}

	public void setBirth(int i, int j, int birth_t) {
		//
		this.birth_time.get(i).set(j - (i + 1), birth_t);
	}

}
