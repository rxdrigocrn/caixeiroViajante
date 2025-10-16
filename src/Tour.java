import algs4.KdTree;
import algs4.Point2D;
import algs4.StdDraw;
import algs4.StdOut;

public class Tour {

    // Classe interna para representar um nó na lista encadeada circular
    private static class Node {
        private Point point;
        private Node next;
    }

    private Node start;         // Ponto de partida do tour
    private int count;          // Número de pontos no tour
    private KdTree pointSet;    // KdTree para busca otimizada de pontos

    // Construtor
    public Tour() {
        this.start = null;
        this.count = 0;
        this.pointSet = new KdTree();
    }

    // Retorna o número de pontos no tour
    public int size() {
        return count;
    }

    // Calcula e retorna o comprimento total do tour
    public double length() {
        if (start == null) {
            return 0.0;
        }

        double totalLength = 0.0;
        Node current = start;
        do {
            totalLength += current.point.distanceTo(current.next.point);
            current = current.next;
        } while (current != start);
        return totalLength;
    }

    // Retorna uma representação em String do tour
    @Override
    public String toString() {
        if (start == null) {
            return "(Tour vazio)";
        }
        StringBuilder sb = new StringBuilder();
        Node current = start;
        do {
            sb.append(current.point.toString()).append("\n");
            current = current.next;
        } while (current != start);
        return sb.toString();
    }

    // Desenha o tour usando StdDraw
    public void draw() {
        if (start == null) {
            return;
        }
        Node current = start;
        do {
            current.point.drawTo(current.next.point);
            current = current.next;
        } while (current != start);
    }

    // Método principal de inserção, que agora chama a versão com KdTree
    public void insertNearest(Point p) {
        insertNearestKd(p);
    }

    // Implementação ingênua (mantida para referência)
    public void insertNearestNaive(Point p) {
        if (start == null) {
            start = new Node();
            start.point = p;
            start.next = start;
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
        count++;
    }

    /**
     * Insere o ponto p no tour após o ponto já existente mais próximo de p,
     * utilizando uma KdTree para otimizar a busca.
     *
     * @param p o ponto a ser inserido.
     */
    public void insertNearestKd(Point p) {
        // --- INÍCIO DA CORREÇÃO ---
        // Extrai as coordenadas x e y da string retornada por p.toString()
        String pAsString = p.toString(); // Ex: "(123.4, 567.8)"
        String[] parts = pAsString.replaceAll("[()\\s]", "").split(","); // Remove parênteses e espaços, divide por ","
        double px = Double.parseDouble(parts[0]);
        double py = Double.parseDouble(parts[1]);
        Point2D p2d = new Point2D(px, py);
        // --- FIM DA CORREÇÃO ---

        // 1. Se o tour está vazio, este é o primeiro ponto.
        if (start == null) {
            start = new Node();
            start.point = p;
            start.next = start;
            pointSet.insert(p2d); // Insere na KdTree
            count++;
            return;
        }

        // 2. Encontra o ponto mais próximo no tour usando a KdTree.
        Point2D nearestP2D = pointSet.nearest(p2d);

        // 3. Converte de Point2D de volta para Point para encontrar na lista encadeada
        Point nearestPoint = new Point(nearestP2D.x(), nearestP2D.y());

        // 4. Encontra o nó correspondente ao ponto mais próximo na lista.
        Node currentNode = start;
        // Usa distanceTo para comparar, pois a recriação do Point pode falhar em testes de igualdade direta (==)
        while (currentNode.point.distanceTo(nearestPoint) != 0) {
            currentNode = currentNode.next;
        }
        Node nearestNode = currentNode;

        // 5. Insere o novo ponto logo após o nó mais próximo encontrado.
        Node newNode = new Node();
        newNode.point = p;
        newNode.next = nearestNode.next;
        nearestNode.next = newNode;

        // 6. Insere o novo ponto na KdTree para futuras buscas.
        pointSet.insert(p2d);

        // 7. Incrementa o contador de pontos.
        count++;
    }


    // Método de teste
    public static void main(String[] args) {
        Tour tour = new Tour();
        // Os pontos serão inseridos usando a lógica com KdTree.
        tour.insertNearest(new Point(1.0, 1.0));
        tour.insertNearest(new Point(1.0, 4.0));
        tour.insertNearest(new Point(4.0, 4.0));
        tour.insertNearest(new Point(4.0, 1.0));

        StdOut.println("# de pontos = " + tour.size());   // Esperado: 4
        StdOut.println("Comprimento = " + tour.length()); // Esperado: 12.0
        StdOut.println(tour);

        StdDraw.setXscale(0, 6);
        StdDraw.setYscale(0, 6);
        tour.draw();
    }
}