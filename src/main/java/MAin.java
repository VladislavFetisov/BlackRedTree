public class MAin {
    public static void main(String[] args) {
        BlackRedTree tree = new BlackRedTree();
        tree.insert(12);
        tree.insert(20);
        tree.insert(20);
        tree.insert(20);
        tree.insert(8);
        tree.insert(10);
        tree.insert(19);
        tree.insert(21);
        tree.insert(5);
        tree.insert(24);
        tree.insert(22);
        tree.insert(7);
        tree.delete(5);
        tree.delete(7);
        tree.delete(8);
        tree.delete(12);
        tree.delete(20);
        tree.delete(20);
        tree.delete(10);
        tree.delete(24);
        tree.delete(19);
        tree.delete(21);
        tree.delete(20);
        tree.delete(22);
        TreeVisualizer.print(tree.getRoot());
    }
}
