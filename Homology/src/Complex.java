
import java.util.*;

public class Complex {

	private ArrayList<Point> points;
	private ArrayList<Edge> edges;
	private ArrayList<Triangle> triangles;

	private Simplex1 simplex1_state;
	private Simplex2 simplex2_state;

	private Simplex1 full_conect_simplex1;
	private Simplex2 full_conect_simplex2;

	private int current_simplex1_num;
	private int current_simplex2_num;

	private int full_conect_simplex1_num;
	private int full_conect_simplex2_num;

	private int cycle0;// Z0 = C0 - rank∂0 0-単体の数
	private int cycle1;// Z1 = KerA1 = n - rank∂1 = n - rankA1
	private int boundary0;// B0 = rank∂1 = rankA1
	private int boundary1;// B1 = rank∂2 =rankA2

	private int points_num;

	public Complex(ArrayList<Point> p) {
		points = p;
		DelaunayTriangulation delaunayDiagram = new DelaunayTriangulation(points);
		edges = delaunayDiagram.getEdges();
		triangles = delaunayDiagram.getTriangles();
		this.points_num = points.size();

		delaunayDiagram.TirangelDisp();

		this.cycle0 = this.points_num;

		simplex1_state = new Simplex1(points.size());
		simplex2_state = new Simplex2(points.size());

		full_conect_simplex1 = new Simplex1(points.size());
		full_conect_simplex2 = new Simplex2(points.size());

		this.current_simplex1_num = 0;
		this.current_simplex2_num = 0;

		makeFullConectSimplex1();
		makeFullConectSimplex2();

	}

	private int makeFullConectSimplex1() {
		this.full_conect_simplex1_num = 0;

		for (Edge item : this.edges) {
			this.full_conect_simplex1.setConect(item.start.getId(), item.end.getId(), true);
			full_conect_simplex1_num++;
		}
		return full_conect_simplex1_num;
	}

	private int makeFullConectSimplex2() {
		this.full_conect_simplex2_num = 0;
		for (Triangle item : this.triangles) {
			this.full_conect_simplex2.setConect(item.A.getId(), item.B.getId(), item.C.getId(), true);
			full_conect_simplex2_num++;
		}

		return full_conect_simplex2_num;
	}

	public ArrayList<Edge> getEdges() {
		return this.edges;
	}

	public ArrayList<Triangle> getTriangles() {
		return this.triangles;
	}

	public void dispFullConnectSimplex1() {
		System.out.print(" ");
		for (int i = 0; i < points_num; i++) {
			System.out.print(" " + i);
		}
		System.out.println();
		for (int i = 0; i < points_num; i++) {
			System.out.print(i + "  ");
			for (int j = 0; j < i; j++) {
				System.out.print("  ");
			}
			for (int j = i + 1; j < points_num; j++) {
				int tmp;
				if (this.full_conect_simplex1.getConect(i, j)) {
					tmp = 1;
				} else {
					tmp = 0;
				}
				System.out.print(" " + tmp);
			}
			System.out.println();
		}
	}

	public int calcCurrentEulerChar() {
		int simplex0_num = this.points_num;
		int simplex1_num = this.current_simplex1_num;
		int simplex2_num = this.current_simplex2_num;

		int EC = simplex0_num - simplex1_num + simplex2_num;

		return EC;
	}

	public int calcCurrentHomology0() {
		return this.cycle0 - this.boundary0;
	}

	public int calcCurrentHomology1() {
		return this.cycle1 - this.boundary1;
	}

	public Persistence makePersistence() {
		Persistence p = new Persistence();

		for (int t = 0; full_conect_simplex2_num != current_simplex2_num; t++) {
			updatePointsR(t);
			updateSimplexState(t);
			updateCyclesAndBoundaries();
			p.addHom0(calcCurrentHomology0());
			p.addHom1(calcCurrentHomology1());
			p.addEC(calcCurrentEulerChar());
		}

		return p;
	}

	public void updateSimplexState(int time) {
		// HACK 毎回全接続の単体を確認する作業は不要（なはず）
		for (int i = 0; i < points_num; i++) {
			for (int j = i + 1; j < points_num; j++) {
				if (full_conect_simplex1.getConect(i, j)) {
					if (Point.isInRange(points.get(i), points.get(j))) {
						if (!simplex1_state.getConect(i, j)) {
							this.current_simplex1_num++;
						}
						simplex1_state.setConect(i, j, true);
						simplex1_state.setBirth(i, j, time);
					}
				}
			}
		}
		for (int i = 0; i < points_num; i++) {
			for (int j = i + 1; j < points_num; j++) {
				for (int k = j + 1; k < points_num; k++) {
					if (full_conect_simplex2.getConect(i, j, k)) {
						if (Point.isInRange(points.get(i), points.get(j), points.get(k))) {
							if (!simplex2_state.getConect(i, j, k)) {
								this.current_simplex2_num++;
							}
							simplex2_state.setConect(i, j, k, true);
							simplex2_state.setBirth(i, j, k, time);
						}
					}
				}
			}
		}
	}

	public void updateCyclesAndBoundaries() {
		ArrayList<ArrayList<Boolean>> BM1To0 = makeBoundryMatrix_1To0();
		ArrayList<ArrayList<Boolean>> BM2To1 = makeBoundryMatrix_2To1();
		int rankBM1To0, rankBM2To1;

		rankBM1To0 = calcRankByXor(BM1To0);
		rankBM2To1 = calcRankByXor(BM2To1);

		// System.out.println("1To0 = " + rankBM1To0 + ", 2To1 = " + rankBM2To1);

		this.cycle1 = current_simplex1_num - rankBM1To0;
		this.boundary0 = rankBM1To0;
		this.boundary1 = rankBM2To1;
	}

	public void updatePointsR(int time) {
		for (Point item : this.points) {
			item.updateR(time);
		}
	}

	public ArrayList<ArrayList<Boolean>> makeBoundryMatrix_1To0() {// ∂1
		ArrayList<ArrayList<Boolean>> boundary_matrix = new ArrayList<>();

		if (current_simplex1_num == 0) {
			return boundary_matrix;
		}

		for (int i = 0; i < points_num; i++) {
			boundary_matrix.add(new ArrayList<>());
			for (int j = 0; j < current_simplex1_num; j++) {
				boundary_matrix.get(i).add(false);
			}
		}

		for (int i = 0, column = 0; i < points_num; i++) {
			for (int j = i + 1; j < points_num; j++) {
				if (simplex1_state.getConect(i, j)) {
					boundary_matrix.get(i).set(column, true);
					boundary_matrix.get(j).set(column++, true);
				}
			}
		}

		return boundary_matrix;
	}

	public ArrayList<ArrayList<Boolean>> makeBoundryMatrix_2To1() {// ∂2
		ArrayList<ArrayList<Boolean>> boundary_matrix = new ArrayList<>();
		ArrayList<Edge> Es = new ArrayList<Edge>();

		if (current_simplex2_num == 0) {
			return boundary_matrix;
		}

		for (int i = 0; i < points_num; i++) {
			for (int j = i + 1; j < points_num; j++) {
				if (simplex1_state.getConect(i, j)) {
					Es.add(new Edge(points.get(i), points.get(j)));
				}
			}
		}

		for (int i = 0; i < current_simplex1_num; i++) {
			boundary_matrix.add(new ArrayList<>());

			for (int j = 0; j < current_simplex2_num; j++) {
				boundary_matrix.get(i).add(false);
			}
		}

		for (int i = 0, column = 0; i < points_num; i++) {
			for (int j = i + 1; j < points_num; j++) {
				for (int k = j + 1; k < points_num; k++) {
					if (simplex2_state.getConect(i, j, k)) {
						// System.out.println("i = " + i + ", j = " + j + ", k = " + k);
						boundary_matrix.get(Edge.whereIsXY(Es, i, j)).set(column, true);
						boundary_matrix.get(Edge.whereIsXY(Es, i, k)).set(column, true);
						boundary_matrix.get(Edge.whereIsXY(Es, j, k)).set(column++, true);
					}
				}
			}
		}

		return boundary_matrix;
	}

	private void xorRows(ArrayList<ArrayList<Boolean>> x, int row1, int row2, int saveRow) {
		boolean tmp;
		for (int i = 0; i < x.get(0).size(); i++) {
			tmp = x.get(row1).get(i) ^ x.get(row2).get(i);
			x.get(saveRow).set(i, tmp);
		}
	}

	private boolean isRowAllFalse(ArrayList<ArrayList<Boolean>> x, int row) {
		for (int i = 0; i < x.get(0).size(); i++) {
			if (x.get(row).get(i)) {
				return false;
			}
		}
		return true;
	}

	private int calcRankByXor(ArrayList<ArrayList<Boolean>> x) {
		if (x == null || x.size() == 0) {
			return 0;
		}

		int rank = x.size();

		for (int i_pivot = 0, j_pivot = 0; i_pivot < x.size(); i_pivot++, j_pivot++) {
			for (j_pivot = 0; j_pivot < x.get(0).size(); j_pivot++) {
				if (x.get(i_pivot).get(j_pivot)) {
					break;
				}
			}
			if (j_pivot >= x.get(0).size()) {
				continue;
			}
			for (int i = i_pivot + 1; i < x.size(); i++) {
				if (x.get(i).get(j_pivot)) {
					xorRows(x, i_pivot, i, i);
				}
			}
		}

		for (int i = 0; i < x.size(); i++) {
			if (isRowAllFalse(x, i)) {
				rank--;
			}
		}

		return rank;
	}
}
