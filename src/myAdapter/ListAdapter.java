package myAdapter;

import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.List;
import java.util.NoSuchElementException;
//import java.util.Objects;

/**
 * Class that implements {@link HList} and so also {@link HCollection}
 * since the first extends the latter. It is an adapter wich allows the usage of lists, 
 * through the Vector class, with all of their optional operations, in Java Micro Edition, precisly in CLDC 1.1.
 * 
 * @author Davide Seghetto
 * @see myAdapter.HList Interface: HList 
 * 
 */
public class ListAdapter implements HList {
    private int from, to;
    private Vector list;
    private ListAdapter parentList;
    boolean isFather;

    /**
     * Constructs an empty list.
     */
    public ListAdapter() {
        from = 0;
        to = 0;
        list = new Vector();
        isFather = true;
        parentList = null;
    }
    
    /**
     * Constructs a list containing the elements of the specified collection,
     * in the order they are returned by the collection's iterator.
     * @param coll collection whose elements are to be placed into this list.
     */
    public ListAdapter(HCollection coll) {
        if(coll == null) throw new NullPointerException();
        from = 0;
        to = coll.size();
        list = new Vector();
        isFather = true;
        parentList = null;
        HIterator iter = coll.iterator();
        while(iter.hasNext()) list.addElement(iter.next());
    }

    
    /** 
     * Returns the number of elements in this list. If this list contains more than
     * Integer.MAX_VALUE elements, returns Integer.MAX_VALUE.
     * @return the number of elements in this list.
     */
    @Override
    public int size() {
        return to - from >= 0 ? to - from : Integer.MAX_VALUE;
    }

    
    /** 
     * Returns true if this list contains no elements.
     * @return true if this list contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return from == to;
    }

    
    /** 
     * Returns true if this list contains the specified element. More formally,
     * returns true if and only if this list contains at least one element e such
     * that (o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e)).
     * @param obj element whose presence in this list is to be tested.
     * @return true if this list contains the specified element.
     */
    @Override
    public boolean contains(Object obj) {
        for(int i = from; i < to; i++) if(get(i) == null ? obj == null : get(i).equals(obj)) return true;
        return false;
    }

    
    /** 
     * Returns an iterator over the elements in this list in proper sequence.
     * @return an iterator over the elements in this list in proper sequence.
     */
    @Override
    public HIterator iterator() {
        return new ListIterator();
    }

    
    /** 
     * Returns an array containing all of the elements in this list in proper
     * sequence.
     * @return an array containing all of the elements in this list in proper
     *         sequence.
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int index = 0;
        for(int i = from; i < to; i++) array[index++] = get(i); 
        return array;
    }

    
    /** 
     * Returns an array containing all of the elements in this list in proper
     * sequence.
     * @param arrayTarget the array into which the elements of this list are going to be
     * stored, if it is big enough; otherwise, a new array of type Object is allocated
     * for this purpose so the user can cast the elements contained inside to the actual
     * type if it is desired.
     * @return an array containing the elements of this list.
     * @throws NullPointerException if the specified array is null.
     */
    @Override
    public Object[] toArray(Object[] arrayTarget) {
        if(arrayTarget == null) throw new NullPointerException();
        Arrays.fill(arrayTarget, null);
        if(arrayTarget.length < size()) {
            Object[] array = new Object[size()];
            int index = 0;
            for(int i = from; i < to; i++) array[index++] = get(i); 
            return array;
        }
        int index = 0;
        for(int i = from; i < to; i++) arrayTarget[index++] = get(i); 
        return arrayTarget;
    }

    
    /** 
     * Appends the specified element to the end of this list. It returns always true.
     * @param obj element to be appended to this list.
     * @return true (as per the general contract of the Collection.add method).
     */
    @Override
    public boolean add(Object obj) {
        add(to, obj);
        return true;
    }

    
    /** 
     * Removes the first occurrence in this list of the specified element.
     * If this list does not contain the element, it is unchanged. 
     * @param obj element to be removed from this list, if present.
     * @return true if this list contained the specified element.
     */
    @Override
    public boolean remove(Object obj) {
        if(list.removeElement(obj)) {
            to--;
            boolean father = isFather;
            ListAdapter dad = parentList;
            while(!father) {
                dad.to--;
                father = dad.isFather;
                dad = dad.parentList;
            }
            return true;
        }
        return false;
    }

    
    /** 
     * Returns true if this list contains all of the elements of the specified
     * collection. Notice that if the parameter contains duplicates, this
	 * collection just needs to contain one element equals to the duplicates 
	 * and not the same number of elements.
     * @param coll collection to be checked for containment in this list.
     * @return true if this list contains all of the elements of the specified
     *         collection.
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean containsAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();
        HIterator iter = coll.iterator();
        boolean contained = false;
        while(iter.hasNext()) {
            Object elem = iter.next();
            for (int i = from; i < to; i++) {
                if (elem == null ? get(i) == null : elem.equals(get(i))) {
                    contained = true;
                    break;
                }
            }
            if(!contained) return false;
            contained = false;
        }
        return true;
    }

    
    /** 
     * Appends all of the elements in the specified collection to the end of this
     * list, in the order that they are returned by the specified collection's
     * iterator. This method throws {@link IllegalArgumentException} if coll == this,
     * in other words, if list itself is passed as parameter. Notice that this method
     * inserts all the elements of coll, including the duplicates.
     * @param coll collection whose elements are to be added to this list.
     * @return true if this list changed as a result of the call.
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean addAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();
        if(this == coll) throw new IllegalArgumentException();
        boolean changed = false;
        HIterator iter = coll.iterator();
        while(iter.hasNext()) {
            add(iter.next());
            changed = true;
        }
        return changed;
    }

    
    /** 
     * Inserts all of the elements in the specified collection into this list at the
     * specified position. Shifts the element currently at that
     * position (if any) and any subsequent elements to the right (increases their
     * indices). The new elements will appear in this list in the order that they
     * are returned by the specified collection's iterator. This method throws 
     * {@link IllegalArgumentException} if coll == this, in other words, if list 
     * itself is passed as parameter. Notice that
	 * this method inserts all the elements, including the duplicates.
     * @param index index at which to insert first element from the specified
     * collection.
     * @param coll elements to be inserted into this list.
     * @return true if this list changed as a result of the call.
     * @throws NullPointerException if
     * the specified collection is null.
     * @throws IndexOutOfBoundsException if the index is out of range (index
     * &lt; 0 || index &gt; size()).
     */
    @Override
    public boolean addAll(int index, HCollection coll) {
        if(coll == null) throw new NullPointerException();
        if(this == coll) throw new IllegalArgumentException();
        if(index < from || index > to) throw new IndexOutOfBoundsException();
        boolean changed = false;
        HIterator iter = coll.iterator();
        while(iter.hasNext()) {
            add(index++, iter.next());
            changed = true;
        }
        return changed;
    }

    
    /** 
     * Removes from this list all the elements that are contained in the specified
     * collection. This method affects all the duplicates currently in the list. 
     * For istance, if there are more than one element that is equal to an element
     * contained in coll, all istances of that element are removed from the list and 
     * not only the first one to be found.
     * @param coll collection that defines which elements will be removed from this
     * list.
     * @return true if this list changed as a result of the call.
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean removeAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();
        boolean changed = false;
        HIterator iter = coll.iterator();
        while(iter.hasNext()) {
            Object elem = iter.next();
            for(int i = from; i < to; i++) {
                if(elem == null ? get(i) == null : elem.equals(get(i))) {
                    remove(i);
                    changed = true;
                }
            }
        }
        return changed;
    }

    
    /** 
     * Retains only the elements in this list that are contained in the specified
     * collection. In other words, removes from this list all the elements that are
     * not contained in the specified collection. This method affects all the duplicates
     * currently in the list. For istance, if there are more than one element that is not 
     * equal to any elements contained in coll, all istances of that element are removed
     * from the list and not only the first one to be found.
     * @param coll collection that defines which elements this set will retain.
     * @return true if this list changed as a result of the call.
     * @throws NullPointerException if the specified collection is null.
     */
    @Override
    public boolean retainAll(HCollection coll) {
        if(coll == null) throw new NullPointerException();
        boolean changed = false; 
        for(int i = from; i < to; i++) {
            if(!coll.contains(get(i))) {    
                remove(i--);
                changed = true;
            }
        }
        return changed;
    }
    /**
     * Removes all of the elements from this list. This list
     * will be empty after this call returns (unless it throws an exception).
     */
    @Override
    public void clear() {
        retainAll(new ListAdapter());
    }

    
    /** 
     * Compares the specified object with this list for equality. Returns true if
     * and only if the specified object is also a list, both lists have the same
     * size, and all corresponding pairs of elements in the two lists are
     * <i>equal</i>. (Two elements e1 and e2 are <i>equal</i> if (e1==null ?
     * e2==null : e1.equals(e2)).) In other words, two lists are defined to be equal
     * if they contain the same elements in the same order.
     * @param obj the object to be compared for equality with this list.
     * @return true if the specified object is equal to this list.
     */
    @Override
    public boolean equals(Object obj) {
        if((obj == null || getClass() != obj.getClass())) return false;
        ListAdapter LAobj = (ListAdapter) obj;
        if(size() != LAobj.size()) return false;
        HIterator LI = LAobj.iterator();
        int i = 0;
        while(LI.hasNext()) {
            Object elem = LI.next();
            Object elem2 = get(i++);
            if(!(elem == null ? elem2 == null : elem.equals(elem2))) return false;
        }
        return true;
    }

    
    /** 
     * Returns the hash code value for this list. In particular, list1.equals(list2)
     * implies that list1.hashCode()==list2.hashCode() for any two lists, list1 and list2, as
     * required by the general contract of Object.hashCode.
     * @return the hash code value for this list.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        if(list.size() == 0) return hashCode;
        for(int i = from; i < to; i++) {
            Object obj = list.elementAt(i);
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    
    /** 
     * Returns the element at the specified position in this list.
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0
     * || index &gt;= size()).
     */
    @Override
    public Object get(int index) {
        if(index < from || index >= to) throw new IndexOutOfBoundsException();
        return list.elementAt(index);
    }

    
    /** 
     * Replaces the element at the specified position in this list with the
     * specified element. 
     * @param index index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range (index
     * &lt; 0 || index &gt;= size()).
     */
    @Override
    public Object set(int index, Object element) {
        Object obj = get(index);
        list.setElementAt(element, index);
        return obj;
    }

    
    /** 
     * Inserts the specified element at the specified position in this list. 
     * Shifts the element currently at that position (if any)
     * and any subsequent elements to the right (adds one to their indices).
     * @param index index at which the specified element is to be inserted.
     * @param obj element to be inserted.
     * @throws IndexOutOfBoundsException if the index is out of range (index
     * &lt; 0 || index &gt; size()).
     */
    @Override
    public void add(int index, Object obj) {
        if(index < from || index > to) throw new IndexOutOfBoundsException();
        list.insertElementAt(obj, index);
        to++;
        boolean father = isFather;
        ListAdapter dad = parentList;
        while(!father) {
            dad.to++;
            father = dad.isFather;
            dad = dad.parentList;
        }
    }

    
    /**
     * Removes the element at the specified position in this list. Shifts any 
     * subsequent elements to the left (subtracts one from their indices). 
     * Returns the element that was removed from the list.
     * @param index the index of the element to removed.
     * @return Object the element previously at the specified position.
     * @throws IndexOutOfBoundsException if the index is out of range (index
     * &lt; 0 || index &gt;= size()).
     */
    @Override
    public Object remove(int index) {
        Object obj = get(index);
        list.removeElementAt(index);
        to--;
        boolean father = isFather;
        ListAdapter dad = parentList;
        while(!father) {
            dad.to--;
            father = dad.isFather;
            dad = dad.parentList;
        }
        return obj;
    }

    
    /** 
     * Returns the index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element. More formally,
     * returns the lowest index i such that (o==null ? get(i)==null :
     * o.equals(get(i))), or -1 if there is no such index.
     * @param obj element to search for.
     * @return the index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element
     */
    @Override
    public int indexOf(Object obj) {
        for(int i = from; i < to; i++) if(get(i).equals(obj)) return i;
        return - 1;
    }

    
    /**
     * Returns the index in this list of the last occurrence of the specified
     * element, or -1 if this list does not contain this element. More formally,
     * returns the highest index i such that (o==null ? get(i)==null :
     * o.equals(get(i))), or -1 if there is no such index.
     * @param obj element to search for.
     * @return the index in this list of the last occurrence of the specified
     * element, or -1 if this list does not contain this element
     */
    @Override
    public int lastIndexOf(Object obj) {
        for(int i = to - 1; i > from - 1; i--) if(get(i).equals(obj)) return i;
        return - 1;
    }

    
    /** 
     * Returns a list iterator of the elements in this list (in proper sequence).
     * @return a list iterator of the elements in this list (in proper sequence).
     */
    @Override
    public HListIterator listIterator() {
        return new ListIterator();
    }

    
    /** 
     * Returns a list iterator of the elements in this list (in proper sequence),
     * starting at the specified position in this list. The specified index
     * indicates the first element that would be returned by an initial call to the
     * next method. An initial call to the previous method would return the element
     * with the specified index minus one.
     * @param index index of first element to be returned from the list iterator (by
     * a call to the next method).
     * @return a list iterator of the elements in this list (in proper sequence),
     * starting at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0
     * || index &gt; size()).
     */
    @Override
    public HListIterator listIterator(int index) {
        if(index < from || index > to) throw new IndexOutOfBoundsException();
        HListIterator LI = listIterator();
        for(int i = from; i < index; i++) LI.next();
        return LI;
    }

    
    /** 
     * Returns a view of the portion of this list between the specified fromIndex,
     * inclusive, and toIndex, exclusive. (If fromIndex and toIndex are equal, the
     * returned list is empty.) The returned list is backed by this list, so
     * non-structural changes in the returned list are reflected in this list, and
     * vice-versa. The returned list supports all of the optional list operations
     * supported by this list.
     * <p>
     * The semantics of the list returned by this method become undefined if the
     * backing list (i.e., this list) is <i>structurally modified</i> in any way
     * other than via the returned list. In other words, modifying the father list,
     * the behavior of the sublist is undefinied.
     * @param fromIndex low endpoint (inclusive) of the subList.
     * @param toIndex high endpoint (exclusive) of the subList.
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     * (fromIndex &lt; 0 || toIndex &gt; size ||
     * fromIndex &gt; toIndex).
     */
    @Override
    public HList subList(int fromIndex, int toIndex) {
        if(fromIndex < from || toIndex > to || fromIndex > toIndex) throw new IndexOutOfBoundsException();
        ListAdapter sl = new ListAdapter();
        sl.list = list;
        sl.from = fromIndex;
        sl.to = toIndex;
        sl.isFather = false;
        sl.parentList = this;
        return sl;
    }

    /**
     * Inner class of {@link ListAdapter} wich implements {@link HListIterator}
     * and so also {@link HIterator} since the first extends the latter.
     * The iterator allows the user to traverse the list in either direction, 
     * modify the list during iteration, and obtain the iterator's
     * current position in the list. A ListIterator has no current element;
     * its <I>cursor position</I> always lies between the element that would be
     * returned by a call to previous() and the element that would be
     * returned by a call to next().
     * @see HListIterator
     */
    private class ListIterator implements HListIterator {

        int prev, succ, state;

        /**
         * Constructs an iterator at the beginning of the list.
        */
        ListIterator() {
            prev = from - 1;
            succ = from;
            state = - 1;
        }

        /**
         * Returns true if this list iterator has more elements when traversing
         * the list in the forward direction. (In other words, returns true if
         * next would return an element rather than throwing an exception.)
         * @return true if the list iterator has more elements when traversing
         * the list in the forward direction
         */
        @Override
        public boolean hasNext() {
            return succ < to;
        }

        /**
         * Returns the next element in the list. This method may be called repeatedly to
         * iterate through the list, or intermixed with calls to previous to go
         * back and forth.
         * @return the next element in the list.
         * @exception NoSuchElementException if the iteration has no next element.
         */
        @Override
        public Object next() {
            if(!hasNext()) throw new NoSuchElementException();
            Object obj = get(succ);
            succ++;
            prev++;
            state = 0;
            return obj;
        }
        
        /**
         * Returns true if this list iterator has more elements when traversing
         * the list in the reverse direction. (In other words, returns true if
         * previous would return an element rather than throwing an exception.)
         * @return true if the list iterator has more elements when traversing
         * the list in the reverse direction.
         */
        @Override
        public boolean hasPrevious() {
            return prev > from - 1; 
        }
        
        /**
         * Returns the previous element in the list. This method may be called
         * repeatedly to iterate through the list backwards, or intermixed with calls to
         * next to go back and forth.
         * @return the previous element in the list.
         * @exception NoSuchElementException if the iteration has no previous element.
         */
        @Override
        public Object previous() {
            if(!hasPrevious()) throw new NoSuchElementException();
            Object obj = get(prev--);
            succ--;
            state = 1;
            return obj;
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call
         * to next.
         * @return the index of the element that would be returned by a subsequent call
         * to next, or list size if list iterator is at end of list.
         */
        @Override
        public int nextIndex() {
            return succ;            
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call
         * to previous.
         * @return the index of the element that would be returned by a subsequent call
         * to previous, or -1 if list iterator is at beginning of list.
         */
        @Override
        public int previousIndex() {
            return prev;
        }

        /**
         * Removes from the list the last element that was returned by next or
         * previous. This call can only be made once per
         * call to next or previous. It can be made only if
         * ListIterator.add has not been called after the last call to
         * next or previous.
         * @exception IllegalStateException neither next nor previous have been called,
         * or remove or add have been called after the last call to next or previous.
         */
        @Override
        public void remove() {
            if(state == - 1) throw new IllegalStateException();
            if(state == 0) { //next appena fatto
                ListAdapter.this.remove(prev);
                succ--;
                prev = succ - 1;
            }
            else if(state == 1) ListAdapter.this.remove(succ); //previous appena fatto
            state = - 1;
        }
        
        /**
         * Replaces the last element returned by next or previous with
         * the specified element. This call can be made only if
         * neither ListIterator.remove nor ListIterator.add have been
         * called after the last call to next or previous.
         * @param obj the element with which to replace the last element returned by
         * next or previous.
         * @exception IllegalStateException if neither next nor previous have been
         * called, or remove or add have been called after the last call to next
         * or previous.
         */
        @Override
        public void set(Object obj) {
            if(state == - 1) throw new IllegalStateException();
            if(state == 0) ListAdapter.this.set(prev, obj); //next appena fatto
            else if(state == 1) ListAdapter.this.set(succ, obj); //previous appena fatto
        }

        /**
         * Inserts the specified element into the list. The element
         * is inserted immediately before the next element that would be returned by
         * next, if any, and after the next element that would be returned by
         * previous, if any. The new element is inserted before the
         * implicit cursor: a subsequent call to next would be unaffected, and
         * a subsequent call to previous would return the new element.
         * @param obj the element to insert.
         */
        @Override
        public void add(Object obj) {
            ListAdapter.this.add(succ++, obj);
            prev = succ - 1;
            state = - 1;
        }
    }
}