
/* x,yを格納する構造体のような扱い */
public class Point {

	private double x, y, z;
	private double r, step;
	private int id;

	final static double STEP = 0.1;

	public Point() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.step = STEP;
		this.r = 0;
		id = -1;
	}

	public Point(double xi, double yi) {
		this.x = xi;
		this.y = yi;
		this.z = 0;
		this.step = STEP;
		this.r = 0;
		this.id = -1;
	}

	public Point(double xi, double yi, double zi) {
		this.x = xi;
		this.y = yi;
		this.z = zi;
		this.step = STEP;
		this.r = 0;
		this.id = -1;
	}

	public Point(double xi, double yi, int idIn) {
		this.x = xi;
		this.y = yi;
		this.z = 0;
		this.step = STEP;
		this.r = 0;
		this.id = idIn;
	}

	public int getId() {
		return this.id;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return this.z;
	}

	public double getR() {
		return this.r;
	}

	public void setX(double xi) {
		this.x = xi;
	}

	public void setY(double yi) {
		this.x = yi;
	}

	public static boolean isInRange(Point A, Point B) {
		double X, Y, R;
		R = A.getR() + B.getR();
		X = A.getX() - B.getX();
		Y = A.getY() - B.getY();
		return (Math.pow(X, 2) + Math.pow(Y, 2) <= Math.pow(R, 2));
	}

	public void updateR(int time) {
		this.r = time * step;
	}

	public static boolean isInRange(Point A, Point B, Point C) {
		return (Point.isInRange(A, B) && Point.isInRange(A, C) && Point.isInRange(B, C));
	}

	public void Display() {
		System.out.println("id = " + this.id + ": x = " + this.x + " y = " + this.y);
	}

	public boolean IsEqual(Point other) {
		return (this.x == other.x) && (this.y == other.y);
	}

	public double dist(Point p1) {
		return Math.sqrt(Math.pow(this.x - p1.x, 2) + Math.pow(this.y - p1.y, 2));
	}

	public Point Cross(Point A) {
		return new Point(this.y * A.z - this.z * A.y, this.z * A.x - this.x * A.z, this.x * A.y - this.y * A.x);
	}

	public static boolean IsEqual(Point A, Point B) {
		return (A.x == B.x) && (A.y == B.y);
	}

	public static double dist(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
	}

	public static Point Cross(Point A, Point B, Point C) {
		// return ABxBC
		Point AB = Point.Sub(A, B);
		Point BC = Point.Sub(B, C);
		return AB.Cross(BC);
	}

	public static Point Sub(Point A, Point B) {
		return new Point(A.x - B.x, A.y - B.y);
	}
}
