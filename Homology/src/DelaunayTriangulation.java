
import java.util.*;

public class DelaunayTriangulation {

	Deque<Point> points = new LinkedList<Point>();
	Deque<Triangle> triangle_diagram = new LinkedList<Triangle>();
	Deque<Edge> edge_diagram = new LinkedList<Edge>();
	Triangle super_triangle;

	public DelaunayTriangulation(ArrayList<Point> p) {
		this.super_triangle = GenerateSuperTriangle(p);
		triangle_diagram.add(this.super_triangle);
		for (int i = 0; i < p.size(); i++) {
			AddPoint(p.get(i));
		}
		Finalize();
		this.triangle_diagram = SortPointDiagram(triangle_diagram);
		// TirangelDisp();
		this.edge_diagram = MakeEdgeDiagram(triangle_diagram);
		// EdgeDisp();
	}

	public Triangle GenerateSuperTriangle(ArrayList<Point> p) {
		Point maxP = new Point();
		Point minP = new Point();

		for (int i = 0; i < p.size(); i++) {
			if (maxP.getX() < p.get(i).getX()) {
				maxP.setX(p.get(i).getX());
			}
			if (maxP.getY() < p.get(i).getY()) {
				maxP.setY(p.get(i).getY());
			}
			if (minP.getX() > p.get(i).getX()) {
				minP.setX(p.get(i).getX());
			}
			if (minP.getY() > p.get(i).getY()) {
				minP.setY(p.get(i).getY());
			}
		}

		double width = maxP.getX() - minP.getX();
		double height = maxP.getY() - minP.getY();
		Point center = new Point(width / 2, height / 2);
		double radius = center.dist(new Point(100 * width, 100 * height));

		return new Triangle(new Point(center.getX() - radius * Math.sqrt(3), center.getY() - radius),
				new Point(center.getX() + radius * Math.sqrt(3), center.getY() - radius),
				new Point(center.getX(), center.getY() + 2 * radius));
	}

	void AddPoint(Point p) {
		points.add(p);
		Triangulation(p);
	}

	void Finalize() {
		// triangle_diagramから、super_triangleの各頂点を含む三角形を除外する
		Deque<Triangle> S = CopyStackOf(triangle_diagram);
		triangle_diagram.clear();
		while (S.size() > 0) {
			Triangle checking = S.pop();
			if (!checking.IsSharingPoint(super_triangle)) {
				triangle_diagram.push(checking);
			}
		}
	}

	void Triangulation(Point p) {
		// triangle_diagramと同じ内容のスタックを組む。
		Deque<Triangle> baseTriangles = CopyStackOf(triangle_diagram);

		// 分割後の三角形を格納するスタック
		Deque<Triangle> newTriangles = new LinkedList<Triangle>();

		// pを含む三角形ABCを探す
		Triangle ABC = IsInsideOfTriangle(baseTriangles, p);

		for (Triangle t : Divide(baseTriangles, ABC, p))
			newTriangles.push(t);

		while (baseTriangles.size() > 0)
			newTriangles.push(baseTriangles.pop());

		// 新しい三角形達でtriangle_diagramを更新
		this.triangle_diagram = CopyStackOf(newTriangles);
	}

	Deque<Triangle> Divide(Deque<Triangle> baseTriangles, Triangle checking, Point p) {
		// pがABCの内側にあるため、ABP, BCP, CAPに分割
		Deque<Triangle> divided = new LinkedList<Triangle>();
		divided.push(new Triangle(checking.A, checking.B, p));
		divided.push(new Triangle(checking.B, checking.C, p));
		divided.push(new Triangle(checking.C, checking.A, p));

		Deque<Triangle> newTriangles = new LinkedList<Triangle>();
		// 新しくできた三角形の集合dividedに対して処理を行っていく。
		// 三角形をABPとして考える。pの対角辺である辺ABを共有する三角形ADBをbaseTrianglesから探す。
		// 戻すトライアングル
		while (divided.size() > 0) {
			Triangle ABC = divided.pop();
			Edge AB = ABC.GetOppositeEdge(p);
			Triangle ADB = GetTriangleShareEdge(ABC, AB, baseTriangles);
			if (ABC.IsEqual(ADB)) {
				newTriangles.push(ABC);
				continue;
			}

			Point D = ADB.GetVertexPoint(AB);
			if (ABC.Contains(D)) {
				// FLIP
				Deque<Triangle> FlipedTriangles = Flip(ADB, AB, p);
				for (Triangle t : FlipedTriangles)
					divided.push(t);
			} else {
				newTriangles.push(ABC);
				newTriangles.push(ADB);
			}
		}

		return newTriangles;
	}

	Deque<Triangle> Flip(Triangle ADB, Edge AB, Point p) {
		Deque<Triangle> FlipedTriangles = new LinkedList<Triangle>();

		Point D = ADB.GetVertexPoint(AB);
		Point A = AB.start;
		Point B = AB.end;

		FlipedTriangles.push(new Triangle(A, D, p));
		FlipedTriangles.push(new Triangle(D, B, p));

		return FlipedTriangles;
	}

	Deque<Triangle> CopyStackOf(Deque<Triangle> stack) {
		Deque<Triangle> returnTriangles = new LinkedList<Triangle>();

		for (Triangle item : stack) {
			returnTriangles.push(item);
		}
		return returnTriangles;
	}

	Deque<Triangle> SortPointDiagram(Deque<Triangle> stack) {
		Deque<Triangle> returnTriangles = new LinkedList<Triangle>();

		for (Triangle item : stack) {
			returnTriangles.add(item.PointSort());
		}
		return returnTriangles;

	}

	Deque<Edge> MakeEdgeDiagram(Deque<Triangle> stack) {
		Deque<Edge> dupedEdges = new LinkedList<Edge>();
		for (Triangle item : stack) {
			dupedEdges.add(item.AB.PointSort());
			dupedEdges.add(item.BC.PointSort());
			dupedEdges.add(item.CA.PointSort());
		}
		Deque<Edge> returnEdges = new LinkedList<Edge>();
		for (Edge item : dupedEdges) {
			boolean isAdd = true;
			for (Edge exsit : returnEdges) {
				if (exsit.IsEqual(item)) {
					isAdd = false;
					break;
				}
			}
			if (isAdd) {
				returnEdges.add(item);
			}
		}

		return returnEdges;
	}

	public Triangle IsInsideOfTriangle(Deque<Triangle> triangles, Point p) {
		Triangle retTriangle = triangles.peek();
		Deque<Triangle> S = new LinkedList<Triangle>();

		while (triangles.size() > 0) {
			Triangle checking = triangles.pop();
			if (checking.IsInsideOfTriangle(p)) {
				retTriangle = checking;
				break;
			} else {
				S.push(checking);
			}
		}

		while (S.size() > 0)
			triangles.push(S.pop());

		return retTriangle;
	}

	public Triangle GetTriangleShareEdge(Triangle ABC, Edge AB, Deque<Triangle> triangles) {
		Triangle ADB = ABC; // 仮置
		Point A = AB.start;
		Point B = AB.end;

		Deque<Triangle> S = new LinkedList<Triangle>();

		while (triangles.size() > 0) {
			Triangle checking = triangles.pop();
			// 三角形がABを含むものかを調べる
			if (checking.Contains(AB)) {
				if (checking.IsEqual(ABC)) {
					// ABCと同じやつだった場合
					S.push(checking);
				} else {
					Point D = checking.GetVertexPoint(AB);
					ADB = new Triangle(A, D, B);
					break;
				}
			} else {
				// 条件に合わなかった場合
				S.push(checking);
			}
		}

		while (S.size() > 0)
			triangles.push(S.pop());

		return ADB;
	}

	public void TirangelDisp() {
		int cnt = 0;
		for (Triangle item : this.triangle_diagram) {
			item.Display();
			System.out.println();
			cnt++;
		}
		System.out.println("三角形の個数 = " + cnt);
		System.out.println("----------------------------------------------------------------");
	}

	public void EdgeDisp() {
		int cnt = 0;
		for (Edge item : this.edge_diagram) {
			item.Display();
			System.out.println();
			cnt++;
		}
		System.out.println("辺の数 = " + cnt);
		System.out.println("----------------------------------------------------------------");
	}

	public ArrayList<Edge> getEdges() {
		ArrayList<Edge> returnEdges = new ArrayList<Edge>();
		for (Edge item : this.edge_diagram) {
			returnEdges.add(item);
		}
		return returnEdges;
	}

	public ArrayList<Triangle> getTriangles() {
		ArrayList<Triangle> returnEdges = new ArrayList<Triangle>();
		for (Triangle item : this.triangle_diagram) {
			returnEdges.add(item);
		}
		return returnEdges;
	}
}
