
import java.util.ArrayList;

/* ドロネー三角形分割で生成した図形から，最大の2-単体の複体の構成情報,r=r_n時の単体の構成状況を格納 */
public class Simplex2 {

	private ArrayList<ArrayList<ArrayList<Boolean>>> simplex2;
	private ArrayList<ArrayList<ArrayList<Integer>>> birth_time;

	public int points_num;

	public Simplex2(int p_num) {
		int i, j, k;

		this.simplex2 = new ArrayList<>();
		this.birth_time = new ArrayList<>();
		this.points_num = p_num;

		for (i = 0; i < points_num; i++) {
			simplex2.add(new ArrayList<>());
			birth_time.add(new ArrayList<>());
			for (j = 0; j < points_num - (i + 1); j++) {
				simplex2.get(i).add(new ArrayList<>());
				birth_time.get(i).add(new ArrayList<>());
				for (k = 0; k < points_num - (j + 1); k++) {
					simplex2.get(i).get(j).add(false);
					birth_time.get(i).get(j).add(0);
				}
			}
		}
	}

	public Boolean getConect(int i, int j, int k) {
		return this.simplex2.get(i).get(j - (i + 1)).get(k - (j + 1));
	}

	public void setConect(int i, int j, int k, boolean b) {
		this.simplex2.get(i).get(j - (i + 1)).set(k - (j + 1), b);
	}

	public int getBirth(int i, int j, int k) {
		return this.birth_time.get(i).get(j - (i + 1)).get(k - (j + 1));
	}

	public void setBirth(int i, int j, int k, int birth_t) {
		this.birth_time.get(i).get(j - (i + 1)).set(k - (j + 1), birth_t);
	}

}
