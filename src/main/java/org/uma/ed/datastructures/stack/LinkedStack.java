package org.uma.ed.datastructures.stack;

/**
 * This class represents a Stack data structure implemented using a linked structure of nodes.
 * Each node in the structure contains a reference to the next node and an element of type T.
 * The top of the stack corresponds to the first node in the linked structure.
 *
 * @param <T> Type of elements in stack.
 *
 * @author Pepe Gallardo, Data Structures, Grado en Informática. UMA.
 */
public class LinkedStack<T> extends AbstractStack<T> implements Stack<T> {
  /**
   * This class represents a node in a linked structure. Each node contains an element and a reference to the next node.
   * @param <E> Type of elements in node.
   */
  private static final class Node<E> {
    E element;
    Node<E> next;

    Node(E element, Node<E> next) {
      this.element = element;
      this.next = next;
    }
  }

  /**
   * Reference to node on top of stack or null if stack is empty.
   */
  private Node<T> top;

  /**
   * Number of elements in stack.
   */
  private int size;

  /*
   * INVARIANT:
   *  - if stack is empty, `top` is null.
   *  - if stack is not empty, `top` references first node in stack.
   *  - each node in stack contains a reference to element that is below in stack or null if it is the node at the bottom.
   *  - `size` is number of elements in stack.
   */

  /**
   * Creates an empty LinkedStack.
   * <p> Time complexity: O(1)
   */
  public LinkedStack() {
    top = null;
    size = 0;
  }

  /**
   * Creates an empty LinkedStack.
   * <p> Time complexity: O(1)
   *
   * @param <T> Type of elements in stack.
   *
   * @return an empty LinkedStack.
   */
  public static <T> LinkedStack<T> empty() {
    return new LinkedStack<>();
  }

  /**
   * Creates a LinkedStack with given elements.
   * <p> Time complexity: O(n)
   *
   * @param elements elements to be added to stack.
   * @param <T> Type of elements in stack.
   *
   * @return a LinkedStack with given elements.
   */
  @SafeVarargs
  public static <T> LinkedStack<T> of(T... elements) {
    LinkedStack<T> stack = new LinkedStack<>();
    for (T element : elements) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Creates a LinkedStack with elements in given iterable.
   * <p> Time complexity: O(n)
   *
   * @param iterable {@code Iterable} of elements to be added to stack.
   * @param <T> Type of elements in iterable.
   *
   * @return a LinkedStack with elements in given iterable.
   */
  public static <T> LinkedStack<T> from(Iterable<T> iterable) {
    LinkedStack<T> stack = new LinkedStack<>();
    for (T element : iterable) {
      stack.push(element);
    }
    return stack;
  }

  /**
   * Returns a new LinkedStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that LinkedStack to be copied.
   *
   * @return a new LinkedStack with same elements and order as {@code that}.
   */
  public static <T> LinkedStack<T> copyOf(LinkedStack<T> that) {
    LinkedStack<T> copy = new LinkedStack<>();
    if (that.isEmpty()) {
      return copy;
    }

    // Create a reversed stack to preserve order during insertion
    LinkedStack<T> tempStack = new LinkedStack<>();

    // Push elements to tempStack in reverse order
    for (T element : that.elements()) {
      tempStack.push(element);
    }

    // Push elements to the new copy in original order
    for (T element : tempStack.elements()) {
      copy.push(element);
    }

    return copy;
  }

  /**
   * Returns a new LinkedStack with same elements in same order as argument.
   * <p> Time complexity: O(n)
   *
   * @param that Stack to be copied.
   *
   * @return a new LinkedStack with same elements and order as {@code that}.
   */
  public static <T> LinkedStack<T> copyOf(Stack<T> that) {
    LinkedStack<T> copy = new LinkedStack<>();
    LinkedStack<T> tempStack = new LinkedStack<>();

    // Push elements from 'that' into tempStack to reverse their order
    while (!that.isEmpty()) {
      T element = that.top();  // Get top element
      tempStack.push(element);  // Push into tempStack
      that.pop();  // Remove from original stack
    }

    // Push elements from tempStack back into 'that' and into 'copy' to maintain original order
    while (!tempStack.isEmpty()) {
      T element = tempStack.top();  // Get top element from tempStack
      that.push(element);  // Restore element back to 'that'
      copy.push(element);  // Push element into the new stack
      tempStack.pop();  // Remove from tempStack
    }

    return copy;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public boolean isEmpty() {
    return top == null;
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
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void push(T element) {
    top = new Node<>(element, this.top);
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
    return top.element;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   *
   * @throws EmptyStackException {@inheritDoc}
   */
  @Override
  public void pop() {
    if(isEmpty()){
      throw new EmptyStackException("pop on empty stack");
    }
    top = top.next;
    size--;
  }

  /**
   * {@inheritDoc}
   * <p> Time complexity: O(1)
   */
  @Override
  public void clear() {
    top = null;
    size = 0;
  }

  /**
   * Returns a protected iterable over elements in stack.
   */
  protected Iterable<T> elements() {
    return () -> new java.util.Iterator<>() {
      Node<T> current = top;

      public boolean hasNext() {
        return current != null;
      }

      public T next() {
        if (!hasNext()) {
          throw new java.util.NoSuchElementException();
        }
        T element = current.element;
        current = current.next;
        return element;
      }
    };
  }
}
