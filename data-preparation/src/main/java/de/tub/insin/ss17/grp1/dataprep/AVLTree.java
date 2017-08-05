package de.tub.insin.ss17.grp1.dataprep;

/**
 * Implementation of AVL Tree.
 *
 * @author Philip Wilson
 */
class AVLTree {

    /**
     *Class that represents each individual component of the tree
     *
     * @author Philip Wilson
     */
    protected static class AvlNode {

        protected String data;
        protected int id;
        protected AvlNode left;
        protected AvlNode right;
        protected int height;

        /**
         * Constructor; creates a node without any children
         *
         * @param value  The element contained in this node
         * @param id
         */
        public AvlNode (String value, int id){
            this (value, id, null, null);
        }

        /**
         * Constructor; creates a node with children
         *
         * @param data  The element contained in this node
         * @param id
         * @param lt      Left child
         * @param rt      Right child
         */
        public AvlNode (String value, int id, AvlNode lt, AvlNode rt){
            this.data = value;
            this.id = id;
            this.left = lt;
            this.right = rt;
        }
    }

    public AvlNode root;


    /**
     * Avl Tree Constructor.
     *
     * Creates an empty tree
     */
    public AVLTree (){
        this.root = null;
    }

    /**
     * Determine the height of the given node.
     *
     * @param t Node
     * @return Height of the given node.
     */
    public int height (AvlNode t){
        return t == null ? -1 : t.height;
    }

    /**
     * Find the maximum value among the given numbers.
     *
     * @param a First number
     * @param b Second number
     * @return Maximum value
     */
    public int max (int a, int b){
        if (a > b)
            return a;
        return b;
    }

    /**
     * Insert an element into the tree.
     *
     * @param x Element to insert into the tree
     * @param id
     * @return True - Success, the Element was added.
     *         False - Error, the element was a duplicate.
     */
    public boolean insert (String x, int id){
        try {
            this.root = this.insert (x, id, this.root);

            return true;
        } catch(Exception e){ // TODO: catch a DuplicateValueException instead!
            return false;
        }
    }

    /**
     * Internal method to perform an actual insertion.
     *
     * @param x Element to add
     * @param id
     * @param t Root of the tree
     * @return New root of the tree
     * @throws Exception
     */
    protected AvlNode insert (String x, int id, AvlNode t) throws Exception{
        if (t == null)
            t = new AvlNode (x, id);
        else if (x.compareTo (t.data) < 0){
            t.left = this.insert (x, id, t.left);

            if (this.height (t.left) - this.height (t.right) == 2){
                if (x.compareTo (t.left.data) < 0){
                    t = this.rotateWithLeftChild (t);
                }
                else {
                    t = this.doubleWithLeftChild (t);
                }
            }
        }
        else if (x.compareTo (t.data) > 0){
            t.right = this.insert (x, id, t.right);

            if ( this.height (t.right) - this.height (t.left) == 2)
                if (x.compareTo (t.right.data) > 0){
                    t = this.rotateWithRightChild (t);
                }
                else{
                    t = this.doubleWithRightChild (t);
                }
        }
        else {
            throw new Exception("Attempting to insert duplicate value");
        }

        t.height = this.max (this.height (t.left), this.height (t.right)) + 1;
        return t;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     *
     * @param k2 Root of tree we are rotating
     * @return New root
     */
    protected AvlNode rotateWithLeftChild (AvlNode k2){
        AvlNode k1 = k2.left;

        k2.left = k1.right;
        k1.right = k2;

        k2.height = this.max (this.height (k2.left), this.height (k2.right)) + 1;
        k1.height = this.max (this.height (k1.left), k2.height) + 1;

        return (k1);
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     *
     * @param k3 Root of tree we are rotating
     * @return New root
     */
    protected AvlNode doubleWithLeftChild (AvlNode k3){
        k3.left = this.rotateWithRightChild (k3.left);
        return this.rotateWithLeftChild (k3);
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     *
     * @param k1 Root of tree we are rotating.
     * @return New root
     */
    protected AvlNode rotateWithRightChild (AvlNode k1){
        AvlNode k2 = k1.right;

        k1.right = k2.left;
        k2.left = k1;

        k1.height = this.max (this.height (k1.left), this.height (k1.right)) + 1;
        k2.height = this.max (this.height (k2.right), k1.height) + 1;

        return (k2);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     *
     * @param k1 Root of tree we are rotating
     * @return New root
     */
    protected AvlNode doubleWithRightChild (AvlNode k1){
        k1.right = this.rotateWithLeftChild (k1.right);
        return this.rotateWithRightChild (k1);
    }

    /**
     * Search for an element within the tree.
     *
     * @param x Element to find
     * @param t Root of the tree
     * @return True if the element is found, false otherwise
     */
    public int idOf(String x){
        return this.idOf(x, this.root);
    }

    /**
     * Internal find method; search for an element starting at the given node.
     *
     * @param x Element to find
     * @param t Root of the tree
     * @return True if the element is found, false otherwise
     */
    protected int idOf(String x, AvlNode t) {
        if (t == null){
            return 0; // The node was not found

        } else if (x.compareTo(t.data) < 0){
            return this.idOf(x, t.left);
        } else if (x.compareTo(t.data) > 0){
            return this.idOf(x, t.right);
        }

        return t.id; // Can only reach here if node was found
    }
}
