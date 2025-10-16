import algs4.StdDraw;
import algs4.StdIn;
import java.util.Objects;

public class Point { 
    private final double x;   // coordenada cartesiana x
    private final double y;   // coordenada cartesiana y
    
    // cria e inicializa um ponto com (x, y) dados
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retorna a coordenada x deste ponto.
     * @return a coordenada x
     */
    public double x() {
        return this.x;
    }

    /**
     * Retorna a coordenada y deste ponto.
     * @return a coordenada y
     */
    public double y() {
        return this.y;
    }

    // retorna a distância euclidiana entre dois pontos
    public double distanceTo(Point that) {
        double dx = this.x - that.x;
        double dy = this.y - that.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    // desenha este ponto na tela padrão
    public void draw() {
        StdDraw.point(x, y);
    }

    // desenha o segmento de linha entre este ponto e outro na tela padrão
    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // retorna uma representação em string deste ponto
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
    
    // --- INÍCIO DA MODIFICAÇÃO COM HASHMAP ---
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Point that = (Point) other;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    // --- FIM DA MODIFICAÇÃO COM HASHMAP ---

    // lê um arquivo TSP da entrada padrão e plota os pontos
    public static void main(String[] args) {
        int width = StdIn.readInt();
        int height = StdIn.readInt();
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.setPenRadius(0.005);

        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point p = new Point(x, y);
            p.draw();
        }
    }
}