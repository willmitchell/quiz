package org.quiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Queue;

/**
 * The org.quiz.TrinaryNode class handles the bulk of the logic for managing a tree with between [0..3] children.
 * <p>
 * The class is generic, so it can support any kind of Comparable node value.
 * <p>
 * Will Mitchell
 * 2015
 */
public class TrinaryNode<T extends Comparable<T>> {
    public static final Logger log = LoggerFactory.getLogger(TrinaryNode.class);

    private T value;
    private TrinaryNode<T> left, mid, right;

    /**
     * The only to build new nodes is with a value.
     *
     * @param v an entry in the tree
     */
    public TrinaryNode(T v) {
        value = v;
    }

    /**
     * The add method is naturally recursive.  It always grows the tree.
     *
     * @param v a value to add at the end of the tree.
     */
    public void add(T v) {
        int comparison = v.compareTo(value);
        if (comparison < 0) {
            if (left != null) {
                left.add(v);
            } else {
                left = new TrinaryNode<>(v);
            }
        } else if (comparison == 0) {
            if (mid != null) {
                mid.add(v);
            } else {
                mid = new TrinaryNode<>(v);
            }
        } else {
            if (right != null) {
                right.add(v);
            } else {
                right = new TrinaryNode<>(v);
            }
        }
    }

    /**
     * Remove a reference to the specified child node.
     *
     * @param child a node that must exist as one of the 3 possible child nodes.
     */
    public void prune(TrinaryNode<T> child) {
        assert child != null;
        log.info("Pruning node: " + child);
        if (left == child) {
            left = null;
        } else if (mid == child) {
            mid = null;
        } else if (right == child) {
            right = null;
        } else {
            throw new IllegalStateException("Cannot prune node that is not a child.");
        }
    }

    /**
     * Helpful for debugging.
     */
    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", left=" + left +
                ", mid=" + mid +
                ", right=" + right +
                '}';
    }

    /**
     * Utility class that carries the terminalNode (the search target) as well as its possibly-null parent
     * back to the caller.
     */
    public class SearchResult {
        TrinaryNode<T> terminalNode;
        Optional<TrinaryNode<T>> parent;

        public SearchResult(TrinaryNode<T> terminalNode, Optional<TrinaryNode<T>> parent) {
            this.terminalNode = terminalNode;
            this.parent = parent;
        }
    }

    /**
     * Find the word within the tree.  Called recursively.
     *
     * @param word a sequence of values to search for.
     * @param parent the previous node on the stack.
     * @return a SearchResult if the word was found.
     */
    Optional<SearchResult> search(Queue<T> word, Optional<TrinaryNode<T>> parent) {
        if (value == null) {
            throw new IllegalStateException("TrieNode value should never be null");
        }

        if (word.isEmpty()) {
            throw new IllegalStateException("Search is broken.  Cannot search for empty word.");
        }
        final T v = word.remove();

        if (v != value) {
            // Did not match at this node.  Search is over.
            return Optional.empty();
        }

        // We did match.  Search might be over.
        if (word.isEmpty()) {
            return Optional.of(new SearchResult(this, parent));
        } else {
            // We need to look at the next value in the word in order to choose the next node.
            final T peek = word.peek();

            int comparison = peek.compareTo(value);

            if (comparison < 0) {
                if (left == null) {
                    return Optional.empty(); // failed search
                } else {
                    return left.search(word, Optional.of(this));
                }
            } else if (comparison == 0) {
                if (mid == null) {
                    return Optional.empty(); // failed search
                } else {
                    return mid.search(word, Optional.of(this));
                }
            } else {
                if (right == null) {
                    return Optional.empty(); // failed search
                } else {
                    return right.search(word, Optional.of(this));
                }
            }
        }
    }
}
