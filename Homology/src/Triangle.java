
public class Triangle {

	Point A;
	Point B;
	Point C;

	Edge AB;
	Edge BC;
	Edge CA;

	public Triangle(Point a, Point b, Point c) {
		this.A = a;
		this.B = b;
		this.C = c;
		this.AB = new Edge(this.A, this.B);
		this.BC = new Edge(this.B, this.C);
		this.CA = new Edge(this.C, this.A);
	}

	public void Display() {
		System.out.print("A:");
		this.A.Display();
		System.out.print("B:");
		this.B.Display();
		System.out.print("C:");
		this.C.Display();
	}

	public boolean IsEqual(Triangle other) {
		return ((this.AB.IsEqual(other.AB) || this.AB.IsEqual(other.BC) || this.AB.IsEqual(other.CA))
				&& (this.BC.IsEqual(other.AB) || this.BC.IsEqual(other.BC) || this.BC.IsEqual(other.CA)));
	}

	public static boolean IsEqual(Triangle A, Triangle B) {
		return (Edge.IsEqual(A.AB, B.AB) || Edge.IsEqual(A.AB, B.BC) || Edge.IsEqual(A.AB, B.CA))
				&& (Edge.IsEqual(A.BC, B.AB) || Edge.IsEqual(A.BC, B.BC) || Edge.IsEqual(A.BC, B.CA));
	}

	public boolean Contains(Point p) {
		return Contains(GetCircumscribedCircle(), p);
	}

	public boolean Contains(Circle circle, Point p) {
		return Point.dist(circle.center, p) < circle.radius;
	}

	public boolean Contains(Edge e) {
		return (this.AB.IsEqual(e) || this.BC.IsEqual(e) || this.CA.IsEqual(e));
	}

	public Circle GetCircumscribedCircle() {
		double x1 = this.A.getX();
		double y1 = this.A.getY();
		double x2 = this.B.getX();
		double y2 = this.B.getY();
		double x3 = this.C.getX();
		double y3 = this.C.getY();

		double c = 2.0 * ((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1));
		double x = ((y3 - y1) * (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1)
				+ (y1 - y2) * (x3 * x3 - x1 * x1 + y3 * y3 - y1 * y1)) / c;
		double y = ((x1 - x3) * (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1)
				+ (x2 - x1) * (x3 * x3 - x1 * x1 + y3 * y3 - y1 * y1)) / c;

		Point center = new Point(x, y);
		double r = center.dist(this.A);

		return new Circle(center, r);
	}

	public boolean IsSharingPoint(Triangle other) {
		return (this.A.IsEqual(other.A) || this.A.IsEqual(other.B) || this.A.IsEqual(other.C) || this.B.IsEqual(other.A)
				|| this.B.IsEqual(other.B) || this.B.IsEqual(other.C) || this.C.IsEqual(other.A)
				|| this.C.IsEqual(other.B) || this.C.IsEqual(other.C));
	}

	public Edge GetOppositeEdge(Point point) {
		if (this.A.IsEqual(point)) {
			return this.BC;
		} else if (this.B.IsEqual(point)) {
			return this.CA;
		} else {
			return this.AB;
		}
	}

	public boolean IsInsideOfTriangle(Point p) {
		Point A = this.A;
		Point B = this.B;
		Point C = this.C;

		// AB x BP, BC x CP, CA x AP
		Point ABxBP = Point.Cross(A, B, p);
		Point BCxCP = Point.Cross(B, C, p);
		Point CAxAP = Point.Cross(C, A, p);

		return ((ABxBP.getZ() >= 0 && BCxCP.getZ() >= 0 && CAxAP.getZ() >= 0)
				|| (ABxBP.getZ() <= 0 && BCxCP.getZ() <= 0 && CAxAP.getZ() <= 0));
	}

	public Triangle PointSort() {
		int a_id = this.A.getId(), b_id = this.B.getId(), c_id = this.C.getId();

		if (a_id < b_id && a_id < c_id) {
			if (b_id < c_id) {
				return new Triangle(this.A, this.B, this.C);
			} else {
				return new Triangle(this.A, this.C, this.B);
			}
		} else if (b_id < a_id && b_id < c_id) {
			if (a_id < c_id) {
				return new Triangle(this.B, this.A, this.C);
			} else {
				return new Triangle(this.B, this.C, this.A);
			}
		} else if (c_id < a_id && c_id <= b_id) {
			if (a_id <= b_id) {
				return new Triangle(this.C, this.A, this.B);
			} else {
				return new Triangle(this.C, this.B, this.A);
			}
		}

		return null;

	}

	public Point GetVertexPoint(Edge edge) {
		if (this.AB.IsEqual(edge)) {
			return this.C;
		} else if (this.BC.IsEqual(edge)) {
			return this.A;
		} else {
			return this.B;
		}
	}
}
