import junit.framework.TestCase;
import org.quiz.TrinaryTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Test the org.quiz.TrinaryTree implementation using the provided data.
 *
 * Will Mitchell
 * 2015
 */
public class TestTrinaryTree extends TestCase {

    public static final Logger log = LoggerFactory.getLogger(TestTrinaryTree.class);


    ArrayList<Integer> toArray(int[] ia) {
        ArrayList<Integer> array = new ArrayList<>(ia.length);
        for (int i = 0; i < ia.length; i++) {
            array.add(i, ia[i]);
        }
        return array;
    }

    TrinaryTree<Integer> tt ;

    public void setUp (){
        rebuild();
        tt.print();
    }

    private void rebuild() {
        tt = new TrinaryTree<>();
        int[] ia = {5, 4, 9, 5, 7, 2, 2};
        for (int i : ia) {
            tt.insert(i);
        }
    }

    public void testSearchAndDelete() {
        int[] search1 = {5};

        assert tt.search(toArray(search1));

        int[] search2 = {5, 4};
        assert tt.search(toArray(search2));

        int[] search3 = {5, 4, 2, 2};
        assert tt.search(toArray(search3));

        assert tt.delete(toArray(search3));
        tt.print();

        assert tt.delete(toArray(search2));
        assert !tt.delete(toArray(search2));
        tt.print();

        assert tt.delete(toArray(search1));
        assert !tt.delete(toArray(search1));
        tt.print();
    }

}
