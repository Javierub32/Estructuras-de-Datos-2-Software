package org.uma.ed.datastructures.stack;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * This class represents a Stack data structure implemented using an array of elements.
 * The size of the array (capacity) is automatically increased when it runs out of capacity.
 *
 * @param <T> The type of elements in stack.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class ArrayStack<T> extends AbstractStack<T> implements Stack<T> {
  /**
   * Default initial capacity for stack.
   */
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  /**
   * Array of elements in stack.
   */
  private T[] elements;

  /**
   * Number of elements in stack.
   */
  private int size;

  /**
   * INVARIANT:
   *  - elements in the stack are stored in the array in bottom to top order.
   *  - if stack is non-empty, the element at the top of the stack is stored at index size - 1 in the array.
   *  - size is the number of elements in stack.
   */

  /**
   * Creates an empty ArrayStack. Initial capacity is {@code initialCapacity} elements. Capacity is automatically
   * increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param initialCapacity Initial capacity.
   *
   * @throws IllegalArgumentException if initial capacity is less than 1.
   */
  @SuppressWarnings("unchecked")
  public ArrayStack(int initialCapacity) {
    if (initialCapacity <= 0) {
      throw new IllegalArgumentException("initial capacity must be greater than 0");
    }
    elements = (T[]) new Object[initialCapacity];
    size = 0;
  }

  /**
   * Creates an empty ArrayStack with default initial capacity. Capacity is automatically increased when needed.
   * <p> Time complexity: O(1)
   */
  public ArrayStack() {
    this(DEFAULT_INITIAL_CAPACITY);
  }

  /**
   * Creates an empty ArrayStack with default initial capacity. Capacity is automatically increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param <T> Type of elements in stack.
   *
   * @return an empty ArrayStack.
   */
  public static <T> ArrayStack<T> empty() {
    return new ArrayStack<>();
  }

  /**
   * Creates an empty ArrayStack. Initial capacity is {@code initialCapacity} elements. Capacity is automatically
   * increased when needed.
   * <p> Time complexity: O(1)
   *
   * @param initialCapacity Initial capacity.
   *
   * @throws IllegalArgumentException if initial capacity is less than 1.
   */
  public static <T> ArrayStack<T> withCapacity(int initialCapacity) {
    return new ArrayStack<>(initialCapacity);
  }

  /**
   * Creates a ArrayStack with given elements.
   * <p> Time complexity: O(n)
   *
   * @param elements elements to be added to stack.
   * @param <T> Type of elements in stack.
   *
   * @return a ArrayStack with given elements.
   */
  @SafeVarargs
  public static <T> ArrayStack<T> of(T... elements) {
    ArrayStack<T> stack = new ArrayStack<>(elements.length);
    for (T element : elements) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Creates an ArrayStack with elements in given iterable.
   * <p> Time complexity: O(n)
   *
   * @param iterable {@code Iterable} of elements to be added to stack.
   * @param <T> Type of elements in iterable.
   *
   * @return an ArrayStack with elements in given iterable.
   */
  public static <T> ArrayStack<T> from(Iterable<T> iterable) {
    ArrayStack<T> stack = new ArrayStack<>();
    for (T element : iterable) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Returns a new ArrayStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that ArrayStack to be copied.
   *
   * @return a new ArrayStack with same elements and order as {@code that}.
   */
  public static <T> ArrayStack<T> copyOf(ArrayStack<T> that) {
    ArrayStack<T> copy = new ArrayStack<>(that.elements.length);
    int size = that.size;
    if (size > 0) {
      // copy elements
      System.arraycopy(that.elements, 0, copy.elements, 0, size);
      copy.size = size; // set size
    }
    return copy;
  }

  /**
   * Returns a new ArrayStack with same elements in same order as argument.
   *
   * @param that Stack to be copied.
   *
   * @return a new ArrayStack with same elements and order as {@code that}.
   */
  public static <T> ArrayStack<T> copyOf(Stack<T> that) {
    if (that instanceof ArrayStack<T> arrayStack) {
      // use specialized version for ArrayStack
      return copyOf(arrayStack);
    }
    int size = that.size();
    ArrayStack<T> copy = size == 0 ? new ArrayStack<>() : new ArrayStack<>(size);

    if (size > 0) {
      // copy elements from that
      for (int i = size - 1; i >= 0; i--) {
        copy.elements[i] = that.top();
        that.pop();
      }
      copy.size = size; // set size

      // restore elements in that
      for (int i = 0; i < size; i++) {
        that.push(copy.elements[i]);
      }
    }
    return copy;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Ensures that the capacity of the ArrayStack is sufficient to hold a new element.
   * If the current capacity is not enough, it is increased by doubling its size.
   */
  private void ensureCapacity() {
    if (size >= elements.length) {
      elements = Arrays.copyOf(elements, 2 * elements.length);
    }
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: mostly O(1). O(n) when stack capacity has to be increased.
   */
  @Override
  public void push(T element) {
    ensureCapacity();
    elements[size] = element;
    size++;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public T top() {
    if (isEmpty()) {
      throw new EmptyStackException("top on empty stack");
    }
    return elements[size - 1];
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public void pop() {
    if (isEmpty()) {
      throw new EmptyStackException("pop on empty stack");
    }
    size--;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(n)
   */
  @Override
  public void clear() {
    size = 0;
  }

  /**
   * Returns a protected iterable over elements in stack.
   */
  protected Iterable<T> elements() {
    return () -> new java.util.Iterator<>() {
      int current = size - 1;

      public boolean hasNext() {
        return current >= 0;
      }

      public T next() {
        if (!hasNext()) {
          throw new java.util.NoSuchElementException();
        }
        T element = elements[current];
        current--;
        return element;
      }
    };
  }
}