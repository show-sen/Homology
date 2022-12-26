
import java.util.ArrayList;

public class Edge {

	Point start;
	Point end;

	public Edge(Point s, Point e) {
		this.start = s;
		this.end = e;
	}

	public void Display() {
		System.out.print("A:");
		this.start.Display();
		System.out.print("B:");
		this.end.Display();
	}

	public Edge PointSort() {
		int start_id = this.start.getId(), end_id = this.end.getId();

		if (start_id < end_id) {
			return new Edge(start, end);
		} else {
			return new Edge(end, start);
		}
	}

	public boolean IsEqual(Edge other) {
		return ((this.start.IsEqual(other.start) && this.end.IsEqual(other.end))
				|| (this.start.IsEqual(other.end) && this.end.IsEqual(other.start)));
	}

	public static boolean IsEqual(Edge A, Edge B) {
		return (Point.IsEqual(A.start, B.start) && Point.IsEqual(A.end, B.end))
				|| (Point.IsEqual(A.start, B.end) && Point.IsEqual(A.end, B.start));
	}

	public static int whereIsXY(ArrayList<Edge> edges, int id_x, int id_y) {// edgesの中からPoint_id=x,yを持つ辺のlistの番号を返す
		int i;
		for (i = 0; i < edges.size(); i++) {
			// System.out.println("start = " + edges.get(i).start.getId() + ", end = " +
			// edges.get(i).end.getId());
			if ((edges.get(i).start.getId() == id_x && edges.get(i).end.getId() == id_y)
					|| (edges.get(i).start.getId() == id_y && edges.get(i).end.getId() == id_x)) {
				return i;
			}
		}
		return -1;

	}
}
