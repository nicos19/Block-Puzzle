package blockpuzzle;

import java.util.ArrayList;
import java.util.List;

/**
 * A SingleContainer is used to store Objects.
 * It is either empty or it contains exactly one Object.
 */
public class SingleContainer<T> {
    private final List<T> storage = new ArrayList<>();

    /**
     * Checks if the SingleContainer is empty or not.
     * @return true if storage is empty, false otherwise
     */
    boolean isEmpty() {
        return storage.isEmpty();
    }

    /**
     * Stores given item in this SingleContainer.
     * Throws IllegalStateException if SingleContainer is not empty.
     */
   void store(T item) {
       if (isEmpty()) {
           storage.add(item);
       }
       else {
           throw new IllegalStateException(
                   "store() should not be called if SingleContainer is not empty.");
       }
   }

    /**
     * Clears the SingleContainer, i.e. removes Object from storage if any exists.
     */
   void clear() {
       storage.clear();
   }

    /**
     * Gets the content of this SingleContainer.
     * Throws IllegalStateException if storage is empty.
     * @return the Object in storage
     */
   T getContent() {
       if (isEmpty()) {
           throw new IllegalStateException(
                   "getContent should not be called if SingleContainer is empty.");
       }
       return storage.get(0);
   }

}
