
import java.util.ArrayList;

public class Persistence {

	private ArrayList<Integer> Homology0;
	private ArrayList<Integer> Homology1;
	private ArrayList<Integer> EulerChar;

	private int max_time;

	public Persistence() {
		Homology0 = new ArrayList<Integer>();
		Homology1 = new ArrayList<Integer>();
		EulerChar = new ArrayList<Integer>();
		max_time = 0;
	}

	public void addHom0(int hom0) {
		this.Homology0.add(hom0);
	}

	public void addHom1(int hom1) {
		this.Homology1.add(hom1);
	}

	public void addEC(int ec) {
		this.EulerChar.add(ec);
	}

	public int getHom0(int time) {
		return this.Homology0.get(time);
	}

	public int getHom1(int time) {
		return this.Homology1.get(time);
	}

	public int getEC(int time) {
		return this.EulerChar.get(time);
	}

	public int size() {
		if (Homology0.size() > EulerChar.size() && Homology0.size() > Homology1.size()) {
			this.max_time = Homology0.size();
		} else if (Homology1.size() > EulerChar.size() && Homology1.size() > Homology0.size()) {
			this.max_time = Homology1.size();
		} else {
			this.max_time = EulerChar.size();
		}
		return this.max_time;
	}

}
