import java.util.AbstractQueue;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.LinkedList;
/**
 * This program implements the queue. The purpose of this queue is to store the N most recently
 * inserted elements. The queue is bounded in size. If the queue is already full (Queue#size() == capacity),
 * the oldest element that was inserted (the head) should be evicted, and then the new element can be added at the tail.
 * @version 1.01 2016-09-24
 * @author Denis Bezschastnuy
 */

public class MostRecentlyInsertedQueueTest {
    public static void main(String[] args) {
        Queue<Integer> queue = new MostRecentlyInsertedQueue<>(6);
        System.out.println(queue); // queue.size(): 0, contents (head -> tail): [ ]
        queue.offer(1);
        System.out.println(queue); // queue.size(): 1, contents (head -> tail): [ 1 ]
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue); // queue.size(): 5, contents (head -> tail): [ 1, 2, 3, 4, 5 ]
        queue.offer(6);
        System.out.println(queue);  // queue.size(): 5, contents (head -> tail): [ 2, 3, 4, 5, 6 ]

        int poll1 = queue.poll();
        System.out.println("poll1 : " + poll1); // poll1 : 2
        System.out.println(queue); //queue.size(): 4, contents (head -> tail): [3, 4, 5, 6],

        int poll2 = queue.poll();
        System.out.println("poll2 : " + poll2); // poll2 : 3
        System.out.println(queue); //queue.size(): 3, contents (head -> tail): [4, 5, 6]

        queue.clear();
        System.out.println(queue); //queue.size(): 0, contents (head -> tail): []

    }
}
/**
 A first-in, first-out bounded collection.
 */

class MostRecentlyInsertedQueue<E> extends AbstractQueue<E> {

    private final Queue<E> elements;
    private int modcount;
    private final int capacity;

    /**
     Constructs an empty queue.
     @param capacity the maximum capacity of the queue
     */

    public MostRecentlyInsertedQueue(final int capacity) {
        this.elements = new LinkedList<>();
        this.capacity = capacity;
    }

    @Override
    public boolean offer(E newElement) {
        assert newElement != null;

        if (elements.size() < capacity) {
            elements.add(newElement);
        } else {
            elements.remove();
            elements.add(newElement);
        }

        modcount++;

        return true;
    }

    @Override
    public E poll() {
        if (elements.size() == 0) return null;

        E result = elements.poll();
        modcount++;

        return result;
    }

    @Override
    public E peek() {
        if (elements.size() == 0) return null;

        return elements.peek();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public String toString() {
        return "queue.size(): " + elements.size() + ", contents (head -> tail): " +
                elements;
    }

    private class QueueIterator implements Iterator<E> {

        private Iterator<E> iterator;
        private int modcountAtConstruction;
        private int offset;

        public QueueIterator() {
            modcountAtConstruction = modcount;
            iterator = elements.iterator();
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();

            E result = iterator.next();
            offset++;

            return result;
        }

        @Override
        public boolean hasNext() {
            if (modcount != modcountAtConstruction)
                throw new ConcurrentModificationException();

            return offset < elements.size();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}




