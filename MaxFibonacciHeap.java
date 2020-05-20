public class MaxFibonacciHeap{
    NodeStructure currentNode;
    NodeStructure maxNode;
    int totalNode;
    int maxDegree;

    MaxFibonacciHeap(){
        currentNode = null;
        maxNode = null;
        totalNode = 0;
        maxDegree = 0;
    }

    public void removeParentSibling(NodeStructure node){
        // remove the parent of a node and child of its parent
        if(node.parent != null){
            if(node.parent.child == node){
                if(node.parent.degree == 1){
                    node.parent.child = null;
                }
                else{
                    node.parent.child = node.left;
                }
            }
            node.parent.degree -= 1;
            node.parent = null;
        }
        //remove a node from its sibling list, keep sibling list connected
        if(node.left != node){
            node.left.right = node.right;
            node.right.left = node.left;
        }
    }

    public void insert(NodeStructure node){
        this.totalNode++;
        //if inserted node is the first node in the heap
        if(this.totalNode == 1) {
            node.left = node;
            node.right = node;
            this.currentNode = node;
            this.maxNode = node;
        }
        else{//insert the node to left side of current node
            node.left = this.currentNode.left;
            node.right = this.currentNode;
            this.currentNode.left.right = node;
            this.currentNode.left = node;
            //check if it is the max node, break tie using alphabetical order: a > z. In compareTo function. positive when z vs a, so reverse.
            if(node.val > this.maxNode.val || (node.val == this.maxNode.val && node.key.compareToIgnoreCase(this.maxNode.key) < 0)){
                this.maxNode = node;
            }
        }
    }

    public void cascadingCut(NodeStructure node) {
        if(node.parent != null){
            if(node.parent.parent == null){ // first not root node
                if(node.mark == true){
                    this.removeParentSibling(node);
                    this.insert(node);
                }
                else
                    node.mark = true;
            }
            else{// other not root nodes
                NodeStructure parent = node.parent;
                this.removeParentSibling(node);
                this.insert(node);
                this.cascadingCut(parent);
            }
        }
    }

    public void increaseKey(NodeStructure node, int k) {
        node.val = node.val +  k;
        // if it is root node, check max node only
        if(node.parent == null){
            if(node.val > this.maxNode.val || (node.val == this.maxNode.val && node.key.compareToIgnoreCase(this.maxNode.key) < 0)){
                this.maxNode = node;
            }
        }
        else{//if it is not root node, cascading cut its parent until node mark is false or root node
            if (node.val > node.parent.val || (node.val == node.parent.val && node.key.compareTo(node.parent.key) < 0)) {
                NodeStructure parent = node.parent;
                this.removeParentSibling(node);
                this.insert(node);
                this.cascadingCut(parent);
            }
        }
    }

    private void pairwiseCombine(NodeStructure[] table){
        //counter is the elements in table
        int counter = 0;
        while(counter < this.totalNode){
            if(table[this.currentNode.degree] == null){ // add to table if it is null
                table[this.currentNode.degree] = this.currentNode;
                counter++;
                this.currentNode = this.currentNode.left;
            }
            else{// if find a node in table, merge them
                NodeStructure root;// with a bigger value
                NodeStructure child;
                if(table[this.currentNode.degree].val > this.currentNode.val ||
                        (table[this.currentNode.degree].val == this.currentNode.val &&
                                table[this.currentNode.degree].key.compareTo(this.currentNode.key) < 0)){
                    root = table[this.currentNode.degree];
                    child = this.currentNode;
                    //if root was already saved to table, move it to current location to avoid traverse repeated nodes
                    if(child.right == root){
                        root.left = child.left;
                        child.left.right = root;
                    }
                    else if(child.left == root){
                        root.right = child.right;
                        child.right.left = root;
                    }
                    else{
                        root.left.right = root.right;
                        root.right.left = root.left;
                        root.left = child.left;
                        root.right = child.right;
                        child.left.right = root;
                        child.right.left = root;
                    }
                    child.left = child;
                    child.right = child;
                    this.currentNode = root;
                    // move root node forward end, child node removed, set total node numbers here
                    this.totalNode--;
                    if(this.totalNode == 1) {
                        this.maxNode = root;
                        root.left = root;
                        root.right = root;
                    }
                }
                else{// remove child from root linked list
                    root = this.currentNode;
                    child = table[this.currentNode.degree];
                    this.totalNode--;
                    if(this.totalNode == 1) {
                        this.maxNode = root;
                        root.left = root;
                        root.right = root;
                    }
                    else{
                        child.left.right = child.right;
                        child.right.left = child.left;
                    }
                }
                // remove degree node in table
                table[root.degree] = null;
                counter = counter - 1;
                //merge child to root
                if(root.child == null){
                    root.child = child;
                    child.left = child;
                    child.right = child;
                }
                else{
                    root.child.right.left = child;
                    child.right = root.child.right;
                    child.left = root.child;
                    root.child.right = child;
                }
                child.parent = root;
                child.mark = false;
                root.degree++;
                if (root.degree > this.maxDegree) {
                    this.maxDegree = root.degree;
                }
                if (table[root.degree] == null) {
                    table[root.degree] = root;
                    counter++;
                    this.currentNode = this.currentNode.left;
                }
            }
        }
        this.currentNode = this.maxNode;
    }

    public void deleteMax() {
        NodeStructure maxNode = this.maxNode;
        // insert all child nodes to root linked list first, then remove max node
        while(maxNode.child != null){
            NodeStructure thisChild = maxNode.child;
            this.removeParentSibling(thisChild);
            this.insert(thisChild);
        }
        //delete the maxNode from its sibling
        this.totalNode = this.totalNode - 1;
        if(this.totalNode == 0) {//if only one root node
            this.currentNode = null;
            this.maxNode = null;
        }
        else{// many root nodes
            maxNode.left.right = maxNode.right;
            maxNode.right.left = maxNode.left;
            this.maxNode = maxNode.right;
            maxNode.left = maxNode;
            maxNode.right = maxNode;
            this.currentNode = this.maxNode.right;
            //re-find the new max node
            NodeStructure start = this.maxNode;
            while(this.currentNode != start){
                if (this.currentNode.val > this.maxNode.val || (this.currentNode.val == this.maxNode.val && this.currentNode.key.compareTo(this.maxNode.key) < 0)) {
                    this.maxNode = this.currentNode;
                }
                this.currentNode = this.currentNode.right;
            }
        }
        // pairwise combine the same order heaps into one, node pointers are stored in the array
        NodeStructure[] table;
        if(this.maxDegree == 0){
            table = new NodeStructure[this.totalNode];
        }
        else{
            table = new NodeStructure[this.maxDegree  * this.totalNode];
        }
        if(this.totalNode > 1)
            this.pairwiseCombine(table);
    }
}