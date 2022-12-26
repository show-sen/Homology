
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        ArrayList<Point> points = new ArrayList<>();

        String root_path = "";// プロジェクトのルートパスを入力
        String data = "data";
        String read_file = "01.dat";

        Path read_path = Paths.get(root_path, data, read_file);

        try {
            Scanner scanner = new Scanner(read_path.toFile(), "UTF-8");

            String str_input;
            String[] Coordinates;
            int cnt = 0;

            while (scanner.hasNextLine()) {
                str_input = scanner.nextLine();
                Coordinates = str_input.split(" ");
                points.add(new Point(Double.parseDouble(Coordinates[0]), Double.parseDouble(Coordinates[1]), cnt++));
            }
            scanner.close();
        } catch (IOException e) {
            System.out.println(e);
        }

        Complex complex = new Complex(points);
        Persistence persistence = complex.makePersistence();
        ArrayList<Edge> edges = complex.getEdges();

        String output = "output";

        String hom0 = "Homology0.dat";
        String hom1 = "Homology1.dat";
        String ec = "EulerCharacteristic.dat";
        String edge = "Edge.dat";
        String edge_view = "EdgeView.dat";

        Path hom0_path = Paths.get(root_path, output, hom0);
        Path hom1_path = Paths.get(root_path, output, hom1);
        Path ec_path = Paths.get(root_path, output, ec);
        Path edge_path = Paths.get(root_path, output, edge);
        Path edge_view_path = Paths.get(root_path, output, edge_view);

        try {
            FileWriter fw_hom0 = new FileWriter(hom0_path.toFile());
            FileWriter fw_hom1 = new FileWriter(hom1_path.toFile());
            FileWriter fw_ec = new FileWriter(ec_path.toFile());
            FileWriter fw_edge = new FileWriter(edge_path.toFile());
            FileWriter fw_edge_view = new FileWriter(edge_view_path.toFile());

            for (int t = 0; t < persistence.size(); t++) {
                fw_hom0.write("t = " + t + ", Hom0 = " + persistence.getHom0(t) + "\r\n");
                fw_hom1.write("t = " + t + ", Hom1 = " + persistence.getHom1(t) + "\r\n");
                fw_ec.write("t = " + t + ", EulerChar = " + persistence.getEC(t) + "\r\n");
            }

            for (Edge item : edges) {
                fw_edge.write(item.start.getX() + " " + item.start.getY() + " ");
                fw_edge.write(item.end.getX() + " " + item.end.getY() + "\r\n");
                fw_edge_view.write("A:( " + item.start.getX() + ", " + item.start.getY() + " )  ");
                fw_edge_view.write("B:( " + item.end.getX() + ", " + item.end.getY() + " )\r\n");
            }

            fw_hom0.close();
            fw_hom1.close();
            fw_ec.close();
            fw_edge.close();
            fw_edge_view.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("計算終了");

    }
}
