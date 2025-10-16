package algs4;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb; // left/bottom subtree
        private Node rt; // right/top subtree

        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // Construtor
    public KdTree() {
        root = null;
        size = 0;
    }

    // A árvore está vazia?
    public boolean isEmpty() {
        return size == 0;
    }

    // Número de pontos na árvore
    public int size() {
        return size;
    }

    // Insere um ponto na árvore
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to insert() is null");
        root = insert(root, p, 0, 
                      Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                      Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    // Método auxiliar recursivo para inserção
    private Node insert(Node x, Point2D p, int level, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            size++;
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            return new Node(p, rect);
        }

        // Se o ponto já existe, não faz nada
        if (x.p.equals(p)) {
            return x;
        }

        // Determina se a divisão é vertical ou horizontal
        boolean isVertical = (level % 2 == 0);
        
        if (isVertical) {
            if (p.x() < x.p.x()) {
                x.lb = insert(x.lb, p, level + 1, xmin, ymin, x.p.x(), ymax);
            } else {
                x.rt = insert(x.rt, p, level + 1, x.p.x(), ymin, xmax, ymax);
            }
        } else {
            if (p.y() < x.p.y()) {
                x.lb = insert(x.lb, p, level + 1, xmin, ymin, xmax, x.p.y());
            } else {
                x.rt = insert(x.rt, p, level + 1, xmin, x.p.y(), xmax, ymax);
            }
        }
        return x;
    }

    // A árvore contém o ponto p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return contains(root, p, 0);
    }
    
    private boolean contains(Node x, Point2D p, int level) {
        if (x == null) return false;
        if (x.p.equals(p)) return true;

        boolean isVertical = (level % 2 == 0);
        double cmp;
        if (isVertical) {
            cmp = p.x() - x.p.x();
        } else {
            cmp = p.y() - x.p.y();
        }

        if (cmp < 0) {
            return contains(x.lb, p, level + 1);
        } else {
            return contains(x.rt, p, level + 1);
        }
    }

    // Encontra o ponto mais próximo de p na árvore
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to nearest() is null");
        if (isEmpty()) return null;
        return nearest(root, p, root.p, 0);
    }
    
    private Point2D nearest(Node x, Point2D p, Point2D champion, int level) {
        if (x == null) return champion;

        // Se o ponto atual é mais próximo, atualiza o campeão
        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(champion)) {
            champion = x.p;
        }
        
        // Se o retângulo do nó atual está mais longe que o campeão, poda a busca
        if (x.rect.distanceSquaredTo(p) >= p.distanceSquaredTo(champion)) {
            return champion;
        }

        boolean isVertical = (level % 2 == 0);
        Node first, second;

        // Escolhe qual sub-árvore explorar primeiro (a que contém o ponto de busca)
        if ((isVertical && p.x() < x.p.x()) || (!isVertical && p.y() < x.p.y())) {
            first = x.lb;
            second = x.rt;
        } else {
            first = x.rt;
            second = x.lb;
        }

        // Explora a primeira sub-árvore
        champion = nearest(first, p, champion, level + 1);
        // Explora a segunda sub-árvore (se necessário)
        champion = nearest(second, p, champion, level + 1);
        
        return champion;
    }
}