import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * A Tri-nary tree.  It is like a Trie, only it has between 0..3 children per node.
 * <p>
 * <p>
 * Will Mitchell
 * 2015
 */
public class TrinaryTree<T extends Comparable<T>> {

    Logger log = LoggerFactory.getLogger(TrinaryTree.class);

    TrinaryNode<T> root;


    /**
     * Insert another new value into the tree.  Always grows the tree.
     *
     * @param val a single element such as an Integer
     */
    void insert(T val) {
        if (root != null) {
            root.add(val);
        } else {
            root = new TrinaryNode<>(val);
        }
    }

    /**
     * Is this word in the tree?
     *
     * @return true if the value exists in the tree
     */
    boolean search(ArrayList<T> word) {
        if (root == null || word == null) {
            return false;
        }
        Optional<TrinaryNode<T>.SearchResult> result = getSearchResult(word);

        // External clients just want to know whether we found the word or not.
        return result.isPresent();
    }

    /**
     * Transform the word into a Queue that makes it easier to use during traversal.
     *
     * @return an Optional that can be used to satisfy both search and delete operations.
     */
    private Optional<TrinaryNode<T>.SearchResult> getSearchResult(ArrayList<T> word) {
        Queue<T> q = new ArrayDeque<T>(word);
        return root.search(q, Optional.empty());
    }

    /**
     * Delete the last element of the specified word from the tree, if it can be found.
     * This method can remove any node in the tree, including the root.
     *
     * @param word the search target
     * @return true if the node was found and deleted.
     */
    boolean delete(ArrayList<T> word) {
        if (root == null || word == null) {
            return false;
        }

        Optional<TrinaryNode<T>.SearchResult> result = getSearchResult(word);
        if (result.isPresent()) {
            final TrinaryNode<T>.SearchResult sr = result.get();
            if (sr.parent.isPresent()){
                sr.parent.get().prune(sr.terminalNode);
            } else {
                root = null;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Dump the contents of the tree to the logger
     */
    void print() {
        if (root != null) {
            log.info("Tree dump:" + root);
        } else {
            log.info("Tree is empty");
        }
    }

}
