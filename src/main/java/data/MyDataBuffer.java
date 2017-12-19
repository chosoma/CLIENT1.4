package data;

import java.util.Arrays;
import java.util.Collection;

public class MyDataBuffer {
	/**
	 * The array buffer into which the elements of the ArrayList are stored. The
	 * capacity of the ArrayList is the length of this array buffer.
	 */
	private transient byte[] elementData;
	/**
	 * The size of the ArrayList (the number of elements it contains).
	 * 
	 * @serial
	 */
	private int size;
	/**
	 * The number of times this list has been <i>structurally modified</i>.
	 * Structural modifications are those that change the size of the list, or
	 * otherwise perturb it in such a fashion that iterations in progress may
	 * yield incorrect results.
	 * 
	 * <p>
	 * This field is used by the iterator and list iterator implementation
	 * returned by the {@code iterator} and {@code listIterator} methods. If the
	 * value of this field changes unexpectedly, the iterator (or list iterator)
	 * will throw a {@code ConcurrentModificationException} in response to the
	 * {@code next}, {@code remove}, {@code previous}, {@code set} or
	 * {@code add} operations. This provides <i>fail-fast</i> behavior, rather
	 * than non-deterministic behavior in the face of concurrent modification
	 * during iteration.
	 * 
	 * <p>
	 * <b>Use of this field by subclasses is optional.</b> If a subclass wishes
	 * to provide fail-fast iterators (and list iterators), then it merely has
	 * to increment this field in its {@code add(int, E)} and
	 * {@code remove(int)} methods (and any other methods that it overrides that
	 * result in structural modifications to the list). A single call to
	 * {@code add(int, E)} or {@code remove(int)} must add no more than one to
	 * this field, or the iterators (and list iterators) will throw bogus
	 * {@code ConcurrentModificationExceptions}. If an implementation does not
	 * wish to provide fail-fast iterators, this field may be ignored.
	 */
	protected transient int modCount = 0;

	/**
	 * Constructs an empty list with the specified initial capacity.
	 * 
	 * @param initialCapacity
	 *            the initial capacity of the list
	 * @exception IllegalArgumentException
	 *                if the specified initial capacity is negative
	 */
	public MyDataBuffer(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException("Illegal Capacity: "
					+ initialCapacity);
		this.elementData = new byte[initialCapacity];
	}

	/**
	 * Constructs an empty list with an initial capacity of ten.
	 */
	public MyDataBuffer() {
		this(10);
	}

	/**
	 * Trims the capacity of this <tt>ArrayList</tt> instance to be the list's
	 * current size. An application can use this operation to minimize the
	 * storage of an <tt>ArrayList</tt> instance.
	 */
	public void trimToSize() {
		modCount++;
		int oldCapacity = elementData.length;
		if (size < oldCapacity) {
			elementData = Arrays.copyOf(elementData, size);
		}
	}

	/**
	 * Increases the capacity of this <tt>ArrayList</tt> instance, if necessary,
	 * to ensure that it can hold at least the number of elements specified by
	 * the minimum capacity argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity
	 */
	public void ensureCapacity(int minCapacity) {
		modCount++;
		int oldCapacity = elementData.length;
		if (minCapacity > oldCapacity) {
			byte oldData[] = elementData;
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			// minCapacity is usually close to size, so this is a win:
			elementData = Arrays.copyOf(elementData, newCapacity);
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns <tt>true</tt> if this list contains no elements.
	 * 
	 * @return <tt>true</tt> if this list contains no elements
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Returns the index of the first occurrence of the specified element in
	 * this list, or -1 if this list does not contain the element. More
	 * formally, returns the lowest index <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 */
	public int indexOf(byte b) {
		for (int i = 0; i < size; i++)
			if (b == elementData[i])
				return i;
		return -1;
	}

	/**
	 * Returns the index of the last occurrence of the specified element in this
	 * list, or -1 if this list does not contain the element. More formally,
	 * returns the highest index <tt>i</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
	 * or -1 if there is no such index.
	 */
	public int lastIndexOf(byte b) {
		for (int i = size - 1; i >= 0; i--)
			if (b == elementData[i])
				return i;
		return -1;
	}

	/**
	 * Returns a shallow copy of this <tt>ArrayList</tt> instance. (The elements
	 * themselves are not copied.)
	 * 
	 * @return a clone of this <tt>ArrayList</tt> instance
	 */
	public Object clone() {
		MyDataBuffer v = new MyDataBuffer();
		v.elementData = Arrays.copyOf(elementData, size);
		v.modCount = 0;
		return v;
	}

	/**
	 * Returns an array containing all of the elements in this list in proper
	 * sequence (from first to last element).
	 * 
	 * <p>
	 * The returned array will be "safe" in that no references to it are
	 * maintained by this list. (In other words, this method must allocate a new
	 * array). The caller is thus free to modify the returned array.
	 * 
	 * <p>
	 * This method acts as bridge between array-based and collection-based APIs.
	 * 
	 * @return an array containing all of the elements in this list in proper
	 *         sequence
	 */
	public byte[] toArray() {
		return Arrays.copyOf(elementData, size);
	}

	public byte[] toArray(int length) {
		return Arrays.copyOfRange(elementData, 0, length);
	}

	public byte[] toArray(int fromIndex, int toIndex) {
		return Arrays.copyOfRange(elementData, fromIndex, toIndex);
	}

	/**
	 * Returns the element at the specified position in this list.
	 * 
	 * @param index
	 *            index of the element to return
	 * @return the element at the specified position in this list
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public byte get(int index) {
		RangeCheck(index);
		return elementData[index];
	}

	/**
	 * Replaces the element at the specified position in this list with the
	 * specified element.
	 * 
	 * @param index
	 *            index of the element to replace
	 * @param element
	 *            element to be stored at the specified position
	 * @return the element previously at the specified position
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public byte set(int index, byte element) {
		RangeCheck(index);
		byte oldValue = elementData[index];
		elementData[index] = element;
		return oldValue;
	}

	/**
	 * Appends the specified element to the end of this list.
	 * 
	 * @param e
	 *            element to be appended to this list
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	public boolean add(byte e) {
		ensureCapacity(size + 1); // Increments modCount!!
		elementData[size++] = e;
		return true;
	}

	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent
	 * elements to the right (adds one to their indices).
	 * 
	 * @param index
	 *            index at which the specified element is to be inserted
	 * @param element
	 *            element to be inserted
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public void add(int index, byte element) {
		if (index > size || index < 0)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);
		ensureCapacity(size + 1); // Increments modCount!!
		System.arraycopy(elementData, index, elementData, index + 1, size
				- index);
		elementData[index] = element;
		size++;
	}

	/**
	 * Removes the element at the specified position in this list. Shifts any
	 * subsequent elements to the left (subtracts one from their indices).
	 * 
	 * @param index
	 *            the index of the element to be removed
	 * @return the element that was removed from the list
	 * @throws IndexOutOfBoundsException
	 *             {@inheritDoc}
	 */
	public byte remove(int index) {
		RangeCheck(index);
		modCount++;
		byte oldValue = elementData[index];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, elementData, index,
					numMoved);
		// elementData[--size] = null; // Let gc do its work
		--size;
		return oldValue;
	}

	/**
	 * Removes all of the elements from this list. The list will be empty after
	 * this call returns.
	 */
	public void clear() {
		modCount++;
		elementData = new byte[10];
		size = 0;
	}

	public void addAll(byte[] b) {
		if (b.length == 0) {
			return;
		}
		int numNew = b.length;
		ensureCapacity(size + numNew); // Increments modCount
		System.arraycopy(b, 0, elementData, size, numNew);
		size += numNew;
	}

	public void addRange(byte[] b, int length) {
		if (b.length < length) {
			length = b.length;
		}
		ensureCapacity(size + length); // Increments modCount
		System.arraycopy(b, 0, elementData, size, length);
		size += length;
	}

	/**
	 * Removes from this list all of the elements whose index is between
	 * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive. Shifts
	 * any succeeding elements to the left (reduces their index). This call
	 * shortens the list by <tt>(toIndex - fromIndex)</tt> elements. (If
	 * <tt>toIndex==fromIndex</tt>, this operation has no effect.)
	 * 
	 * @param fromIndex
	 *            index of first element to be removed
	 * @param toIndex
	 *            index after last element to be removed
	 * @throws IndexOutOfBoundsException
	 *             if fromIndex or toIndex out of range (fromIndex &lt; 0 ||
	 *             fromIndex &gt;= size() || toIndex &gt; size() || toIndex &lt;
	 *             fromIndex)
	 */
	public void removeRange(int fromIndex, int toIndex) {
		modCount++;
		int numMoved = size - toIndex;
		System.arraycopy(elementData, toIndex, elementData, fromIndex, numMoved);
		// Let gc do its work
		size -= (toIndex - fromIndex);
	}

	/**
	 * Checks if the given index is in range. If not, throws an appropriate
	 * runtime exception. This method does *not* check if the index is negative:
	 * It is always used immediately prior to an array access, which throws an
	 * ArrayIndexOutOfBoundsException if index is negative.
	 */
	private void RangeCheck(int index) {
		if (index >= size)
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
					+ size);
	}

	public String toString() {
		if (size == 0)
			return "[]";
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++) {
			sb.append(elementData[i]);
			if (i == size - 1) {
				sb.append(']');
			} else {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public int indexOf(String str) {
		return indexOf(str, 0);
	}

	public int indexOf(String str, int fromIndex) {
		byte[] target = str.getBytes();
		return indexOf(target, fromIndex);
	}

	public int indexOf(byte[] target, int fromIndex) {
		int targetCount = target.length;
		if (fromIndex >= size) {
			return (targetCount == 0 ? size : -1);
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (targetCount == 0) {
			return fromIndex;
		}

		byte first = target[0];
		int max = size - targetCount;

		for (int i = fromIndex; i <= max; i++) {
			if (elementData[i] != first) {
				while (++i <= max && elementData[i] != first)
					;
			}
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = 1; j < end && elementData[j] == target[k]; j++, k++)
					;
				if (j == end) {
					return i;
				}
			}
		}
		return -1;
	}

}
