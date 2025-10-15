
import algs4.StdDraw;
import algs4.StdOut;

public class Tour {

    // Classe interna para representar um nó na lista encadeada circular
    private static class Node {

        private Point point;
        private Node next;
    }

    private Node start; // Ponto de partida do tour (qualquer nó na lista circular)
    private int count;  // Número de pontos no tour

    // Construtor principal (não utiliza a KdTree por padrão)
    public Tour() {
        this.start = null;
        this.count = 0;
    }

    // Retorna o número de pontos no tour
    public int size() {
        return count;
    }

    // Calcula e retorna o comprimento total do tour
    public double length() {
        if (start == null || start.next == start) {
            return 0.0;
        }

        double total = 0.0;
        Node current = start;
        do {
            total += current.point.distanceTo(current.next.point);
            current = current.next;
        } while (current != start);
        return total;
    }

    // Retorna uma representação em String do tour
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (start == null) {
            return "(Tour vazio)";
        }

        Node current = start;
        do {
            sb.append(current.point.toString()).append("\n");
            current = current.next;
        } while (current != start);

        return sb.toString();
    }

    // Desenha o tour usando StdDraw
    public void draw() {
        if (start == null || start.next == start) {
            return;
        }

        Node current = start;
        do {
            current.point.drawTo(current.next.point);
            current = current.next;
        } while (current != start);
    }

    // Método principal de inserção (ainda não usado para KdTree)
    public void insertNearest(Point p) {
        // Por enquanto, chama diretamente a versão ingênua.
        insertNearestNaive(p);
    }

    /**
     * Insere o ponto p no tour após o ponto já existente mais próximo de p.
     * Esta é a implementação ingênua com complexidade O(N^2).
     *
     * @param p o ponto a ser inserido.
     */
    public void insertNearestNaive(Point p) {
        // --- INÍCIO DA IMPLEMENTAÇÃO ---

        // 1. Se o tour está vazio, este é o primeiro ponto.
        if (start == null) {
            start = new Node();
            start.point = p;
            start.next = start; // Faz a lista ser circular, apontando para si mesmo.
            count++;
            return;
        }

        // 2. Encontrar o nó mais próximo no tour existente.
        Node nearestNode = null;
        double minDistance = Double.POSITIVE_INFINITY;
        Node currentNode = start;

        // Itera por toda a lista circular para encontrar o ponto mais próximo.
        do {
            double currentDistance = p.distanceTo(currentNode.point);
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                nearestNode = currentNode;
            }
            currentNode = currentNode.next;
        } while (currentNode != start);

        // 3. Inserir o novo ponto logo após o nó mais próximo encontrado.
        Node newNode = new Node();
        newNode.point = p;

        // O novo nó aponta para o que o nó mais próximo apontava.
        newNode.next = nearestNode.next;
        // O nó mais próximo agora aponta para o novo nó.
        nearestNode.next = newNode;

        // 4. Incrementar o contador de pontos.
        count++;

        // --- FIM DA IMPLEMENTAÇÃO ---
    }

    // Este método será implementado na próxima etapa.
    public void insertNearestKd(Point p) {
        throw new UnsupportedOperationException("Versão com KdTree não implementada.");
    }

    // Método de teste
    public static void main(String[] args) {
        Tour tour = new Tour();
        // Os pontos serão inseridos usando a lógica ingênua agora.
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
