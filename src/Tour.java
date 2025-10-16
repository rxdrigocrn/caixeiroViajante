import algs4.KdTree;
import algs4.Point2D;
import algs4.StdDraw;
import algs4.StdOut;
import java.util.HashMap;

public class Tour {

    private static class Node {
        private Point point;
        private Node next;
    }

    private Node start;
    private int count;
    private KdTree pointSet;
    // --- INÍCIO DA MODIFICAÇÃO COM HASHMAP ---
    private HashMap<Point, Node> nodeMap;
    // --- FIM DA MODIFICAÇÃO COM HASHMAP ---

    public Tour() {
        this.start = null;
        this.count = 0;
        this.pointSet = new KdTree();
        // --- INÍCIO DA MODIFICAÇÃO COM HASHMAP ---
        this.nodeMap = new HashMap<>();
        // --- FIM DA MODIFICAÇÃO COM HASHMAP ---
    }

    public int size() {
        return count;
    }

    public double length() {
        if (start == null) return 0.0;
        double totalLength = 0.0;
        Node current = start;
        do {
            totalLength += current.point.distanceTo(current.next.point);
            current = current.next;
        } while (current != start);
        return totalLength;
    }

    @Override
    public String toString() {
        if (start == null) return "(Tour vazio)";
        StringBuilder sb = new StringBuilder();
        Node current = start;
        do {
            sb.append(current.point.toString()).append("\n");
            current = current.next;
        } while (current != start);
        return sb.toString();
    }

    public void draw() {
        if (start == null) return;
        Node current = start;
        do {
            current.point.drawTo(current.next.point);
            current = current.next;
        } while (current != start);
    }
    
    public void insertNearest(Point p) {
        insertNearestKd(p);
    }
    
    public void insertNearestNaive(Point p) {
        // ... (o código do Naive continua o mesmo, mas também precisa atualizar o map)
        if (start == null) {
            start = new Node();
            start.point = p;
            start.next = start;
            nodeMap.put(p, start); // Atualiza o map
            count++;
            return;
        }

        Node nearestNode = null;
        double minDistance = Double.POSITIVE_INFINITY;
        Node currentNode = start;
        do {
            double currentDistance = p.distanceTo(currentNode.point);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestNode = currentNode;
            }
            currentNode = currentNode.next;
        } while (currentNode != start);

        Node newNode = new Node();
        newNode.point = p;
        newNode.next = nearestNode.next;
        nearestNode.next = newNode;
        nodeMap.put(p, newNode); // Atualiza o map
        count++;
    }

    public void insertNearestKd(Point p) {
        Point2D p2d = new Point2D(p.x(), p.y());

        if (start == null) {
            start = new Node();
            start.point = p;
            start.next = start;
            pointSet.insert(p2d);
            nodeMap.put(p, start); // Atualiza o map
            count++;
            return;
        }

        Point2D nearestP2D = pointSet.nearest(p2d);
        Point nearestPoint = new Point(nearestP2D.x(), nearestP2D.y());
        
        // --- INÍCIO DA OTIMIZAÇÃO COM HASHMAP ---
        // Busca o nó em tempo O(1) em vez de percorrer a lista
        Node nearestNode = nodeMap.get(nearestPoint);
        // --- FIM DA OTIMIZAÇÃO COM HASHMAP ---

        Node newNode = new Node();
        newNode.point = p;
        newNode.next = nearestNode.next;
        nearestNode.next = newNode;
        
        pointSet.insert(p2d);
        nodeMap.put(p, newNode); // Atualiza o map
        count++;
    }

    public static void main(String[] args) {
        Tour tour = new Tour();
        tour.insertNearest(new Point(1.0, 1.0));
        tour.insertNearest(new Point(1.0, 4.0));
        tour.insertNearest(new Point(4.0, 4.0));
        tour.insertNearest(new Point(4.0, 1.0));

        StdOut.println("# de pontos = " + tour.size());
        StdOut.println("Comprimento = " + tour.length());
        StdOut.println(tour);

        StdDraw.setXscale(0, 6);
        StdDraw.setYscale(0, 6);
        tour.draw();
    }
}