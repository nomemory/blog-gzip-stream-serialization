package net.andreinc.gzipserialization;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static net.andreinc.mockneat.unit.types.Ints.ints;

public class BigOne implements Serializable  {

    // Randomly generates a list<list<int>>
    // every int value from is either 0 or 1
    // rows = 1<<12 = 4096
    // cols = 1<<12 = 4096
    //
    public final List<List<Integer>> bigOne =
            ints().from(new int[]{0, 1})
            .list(1<<12)
            .list(LinkedList::new, 1<<12)
            .get();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigOne bigOne1 = (BigOne) o;
        return bigOne.equals(bigOne1.bigOne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bigOne);
    }
}