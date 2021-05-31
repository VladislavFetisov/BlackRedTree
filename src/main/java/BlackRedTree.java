import org.jetbrains.annotations.Nullable;

public class BlackRedTree {
    public enum Color {
        BLACK, RED
    }

    public static class Node implements TreeVisualizer.Visualized {
        public Color color;
        public int key;
        public Node parent;
        public Node left;
        public Node right;

        public Node(Color color, int key, Node left, Node right, Node parent) {
            this.color = color;
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        @Override
        public TreeVisualizer.Visualized getLeft() {
            return this.left;
        }

        @Override
        public TreeVisualizer.Visualized getRight() {
            return this.right;
        }

        @Override
        public String getValue() {
            return String.valueOf(key);
        }

        @Override
        public Color getColor() {
            return this.color;
        }
    }

    private Node root = null;

    public Node getRoot() {
        return root;
    }

    public void insert(int number) {
        if (root == null) {
            root = new Node(Color.BLACK, number, null, null, null);
            return;
        }
        Node current = root;
        Node children = new Node(Color.RED, number, null, null, null);
        while (true) {
            if (number < current.key) {
                if (current.left == null) {
                    children.parent = current;
                    current.left = children;
                    break;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    children.parent = current;
                    current.right = children;
                    break;
                }
                current = current.right;
            }
        }
        fixInsertion(children);
    }

    private void fixInsertion(Node children) {
        if (children.parent.color.equals(Color.RED)) {
            Node uncle = findUncle(children);
            if (uncle != null && uncle.color.equals(Color.RED)) {
                children.parent.color = Color.BLACK;
                uncle.color = Color.BLACK;
                if (children.parent.parent.parent != null) {
                    children.parent.parent.color = Color.RED;
                    fixInsertion(children.parent.parent);
                }
            } else rotate(children);
        }
    }

    @Nullable
    private Node findUncle(Node node) {
        return node.parent.key >= node.parent.parent.key ? node.parent.parent.left : node.parent.parent.right;
    }

    private void rotate(Node node) {
        if (node.key >= node.parent.key) {
            if (node.parent.key < node.parent.parent.key) {
                node.left = node.parent;
                node.parent = node.parent.parent;
                node.parent.left = node;
                node.left.parent = node;
                node.left.right = null;
                node.color = Color.BLACK;
                node.parent.color = Color.RED;
                rightRotate(node);
            } else {
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                leftRotate(node.parent);
            }
        } else {
            if (node.parent.key >= node.parent.parent.key) {
                node.right = node.parent;
                node.parent = node.parent.parent;
                node.parent.right = node;
                node.right.parent = node;
                node.right.left = null;
                node.color = Color.BLACK;
                node.parent.color = Color.RED;
                leftRotate(node);
            } else {
                node.parent.color = Color.BLACK;
                node.parent.parent.color = Color.RED;
                rightRotate(node.parent);
            }
        }
    }


    public boolean delete(int number) {
        Node current = root;
        while (current != null) {
            if (number < current.key) current = current.left;
            else if (number > current.key) current = current.right;
            else break;
        }
        if (current == null) return false;
        if (current.right != null && current.left != null) {
            Node replaced = findMaxOnLeft(current);
            int value = replaced.key;
            replaced.key = current.key;
            current.key = value;
            current = replaced;
        }
        if ((current.left != null && current.right == null) || (current.right != null && current.left == null)) {
            if (current.color == Color.RED) throw new IllegalStateException("У красного узла плохо с детьми");
            if (current.right != null) {
                current.right.parent = current.parent;
                if (current.parent != null) {
                    if (current.key < current.parent.key) current.parent.left = current.right;
                    else current.parent.right = current.right;
                } else root = current.right;
                current.right.color = Color.BLACK;
            } else {
                current.left.parent = current.parent;
                if (current.parent != null) {
                    if (current.key < current.parent.key) current.parent.left = current.left;
                    else current.parent.right = current.left;
                } else root = current.left;
                current.left.color = Color.BLACK;
            }
            return true;
        } else {
            if (current.color == Color.BLACK) correction(current);
            else {
                if (current.parent.left != null && current.key == current.parent.left.key) current.parent.left = null;
                else current.parent.right = null;
            }
        }
        return true;
    }

    private void correction(Node node) {
        if (node.parent == null) {
            root = null;
            return;
        }
        if (node.key == node.parent.left.key) {
            node.parent.left = null;
            if (node.parent.right.color == Color.RED) {
                leftRotate(node.parent.right);
                node.parent.color = Color.RED;
                node.parent.parent.color = Color.BLACK;
            }
            if (node.parent.right.left == null && node.parent.right.right == null) {
                node.parent.right.color = Color.RED;
                node.parent.color = Color.BLACK;
                // correction(node.parent);
            } else if (node.parent.right.left != null && node.parent.right.left.color == Color.RED
                    && (node.parent.right.right == null || node.parent.right.right.color == Color.BLACK)) {
                rightRotate(node.parent.right.left);
                leftRotate(node.parent.right);
                node.parent.parent.color = Color.BLACK;
            } else if (node.parent.right.right.color == Color.RED) {
                node.parent.right.color = node.parent.color;
                node.parent.right.right.color = Color.BLACK;
                node.parent.color = Color.BLACK;
                leftRotate(node.parent.right);
            }
        } else {
            node.parent.right = null;
            if (node.parent.left.color == Color.RED) {
                rightRotate(node.parent.left);
                node.parent.color = Color.RED;
                node.parent.parent.color = Color.BLACK;
            }
            if (node.parent.left.left == null && node.parent.left.right == null) {
                node.parent.left.color = Color.RED;
                node.parent.color = Color.BLACK;
                // correction(node.parent);
            } else if (node.parent.left.right != null && node.parent.left.right.color == Color.RED
                    && (node.parent.left.left == null || node.parent.left.left.color == Color.BLACK)) {
                leftRotate(node.parent.left.right);
                rightRotate(node.parent.left);
                node.parent.parent.color = Color.BLACK;
            } else if (node.parent.left.left != null && node.parent.left.left.color == Color.RED) {
                node.parent.left.color = node.parent.color;
                node.parent.left.left.color = Color.BLACK;
                node.parent.color = Color.BLACK;
                rightRotate(node.parent.left);
            }
        }
    }

    private void leftRotate(Node node) {
        node.parent.right = node.left;
        if (node.left != null) node.left.parent = node.parent;
        node.left = node.parent;
        node.parent = node.parent.parent;
        node.left.parent = node;
        if (node.parent != null) {
            if (node.key >= node.parent.key) node.parent.right = node;
            else node.parent.left = node;
        } else root = node;
    }

    private void rightRotate(Node node) {
        node.parent.left = node.right;
        if (node.right != null) node.right.parent = node.parent;
        node.right = node.parent;
        node.parent = node.parent.parent;
        node.right.parent = node;
        if (node.parent != null) {
            if (node.key >= node.parent.key) node.parent.right = node;
            else node.parent.left = node;
        } else root = node;
    }

    private Node findMaxOnLeft(Node start) {
        Node current = start.left;
        if (current == null) throw new IllegalArgumentException("Слева нет потомка");
        while (current.right != null) current = current.right;
        return current;
    }
}
