package myTest;

import myAdapter.*;
import org.junit.*;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

/**
 * Test class of {@link ListAdapter} methods
 * <p>
 * <p>
 * <strong>Summary</strong>: this class tests the functionality of all ListAdapter methods that are
 * declared in HCollection and HList. 
 * <p>
 * <strong>Test Suite Design</strong>: every method of this class tests a different method of ListAdapter.
 * For istance, the method testSize() tests the working of the method size() of ListAdapter.
 * The ListAdapterTest class deals with the testing for all the methods related
 * to the main list and the list iterator, always of the main list.
 * <p>
 * <strong>Pre-condition</strong>: a new istance of ListAdapter of type HList must always be istantiated
 * before each test. In fact, in setup() method, wich is invoked before every test case,
 * a variable called l1 of type Hlist is initialized with an istance of ListAdapter.
 * <p>
 * <strong>Post-condition</strong>: the methods implemented must always modify the list so that the
 * elements contained are exactly those expected starting from their manual insertion
 * <p>
 * <strong>Execution variables</strong>:
 * <p>
 * HList l1 - main empty list on which all methods common to HCollection and HList
 * are tested.
 * <p>
 * HList l2 - secondary list sometimes used as helper istance during the test cases.
 * <p>
 * HListIterator li - iterator used to scroll the main list.
 * <p>
 * String[] argv - array of strings used to add elements to the main list.
 * <p>
 * Others execution variables might be declared locally in the method to run the test case.
 * @author Davide Seghetto
 * @see HList
 * @see HIterator
 * @see HListIterator
 * @see HCollection
 */

public class TestList
{
	HList l1 = null, l2 = null;
	HListIterator li = null;
	static String[] argv = {"pippo", "qui", "pluto", "paperino", "qui", "ciccio"};

	/**
	 * Method for initializing execution varibles before tests
	 * <p>
	 * A new empty Hlist is created before each test method,
	 * since the precondition in common with every test case is to have an 
	 * empty instance of ListAdapter; in this way the adapter on
	 * wich the various test methods are invoked always has a
	 * valid state.
	 * <p>
	 * Notice that it is no necessary a method that is run after every
	 * single test case.
	 * <p>
	 * Test Description: The constructor successfully instantiates a new object
	 * of the ListAdapter class wich is going to be used in every test case.
	*/
	@Before
	public void setup()
	{
		System.out.println("Instantiate an empty List");
		l1 = new ListAdapter();
	}

	/**
	 * Test of {@link ListAdapter#subList(int, int)}
	 * <p>
	 * <strong>Summary</strong>: test checks if the subList method is properly designed and adapted 
	 * to the rest of the class. Specifically it checks if add and remove methods
	 * increase and decrease the istance variable "to" of the father list.
	 * <p>
	 * <strong>Test Case Design</strong>: the method consists in modifying the precondition istance
	 * of {@link ListAdapter} l1, once it has been instantiated a subList from the latter.
	 * <p>
	 * <strong>Test Description</strong>: test starts adding in l1 the elements contained in argv,
	 * then is created a sublist and the lengths of the two lists is checked. After
	 * some structural changes caused by methods add and remove, the size of the 
	 * list and his sublist is periodically checked with the expected value.
	 * <p>
	 * <strong>Pre-condition</strong>: add, remove and subList methods must work properly also with sublists.
	 * <p>
	 * <strong>Post-condition</strong>: size of list and sublist must be the same as the expected value.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#remove(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testBacking()
	{
		System.out.println("TestBacking");
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		System.out.println("List.toString() ? " + l1);

		int dl0, dl1, dli, dsl0, dsl1, dsli;

		System.out.println(l1 + " " + l1.size());
		dl0 = l1.size();
		
		l2 = l1.subList(0, argv.length/2);
		dsl0 = l2.size();
		
		l2.add("pipperissimo");
		dli = l1.size();
		dsli = l2.size();

		assertEquals("\n*** sublist add is NOT backed correctly ***\n", dli, dl0+1);
		assertEquals("\n*** sublist add is NOT backed correctly ***\n", dsli, dsl0+1);

		l2.remove("pipperissimo");
		assertEquals("\n*** list remove is NOT backed correctly ***\n", l1.size(), dl0);
		assertEquals("\n*** list remove is NOT backed correctly ***\n", l2.size(), dsl0);


		iterate(l2.iterator());
		System.out.println(l2 + " " + l2.size());

		l2.clear();
		dl1 = l1.size();
		dsl1 = l2.size();
		System.out.println(l1 + " " + l1.size());
		iterate(l1.iterator());
		System.out.println(l2 + " " + l2.size());
		iterate(l2.iterator());

		System.out.println(dl0 + " " + dl1 + " " + dsl0 + " " + dsl1);
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dsl0, (dl0/2));
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dsl1, 0);
		assertEquals("\n*** sublist is NOT backed correctly ***\n", dl1, (dl0 - dsl0));

	}

	/**
	 * Test of {@link ListAdapter#subList(int, int)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of add and especially subList
	 * methods by calling the latter several times and creating a hierarchical structure
	 * of sublists.
	 * <p>
	 * <strong>Test Case Design</strong>: test is done adding some elements to list l1 and then calling
	 * many times subList method. By doing that, test shows how, after every cicle o
	 * f the while loop, the sublist obtained gets smaller and smaller.
	 * <p>
	 * <strong>Test Case Description</strong>: first is checked the working of add method invoked on
	 * l1. Then with a while loop, the invocation to subList is made several times
	 * and so the check on the latter method is.
	 * <p>
	 * <strong>Pre-condition</strong>: add, size and subList methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test the sublist must be empty.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testRecursiveSublist()
	{
		System.out.println("TestRecursive SubListing");
		System.out.println(l1.size());
		
		assertEquals("List Starts not empty", l1.size(), 0);
		int prev = l1.size();
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println(l1.size());
		prev = l1.size();
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println(l1.size());
		prev = l1.size();
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		assertEquals("List add not working correctly", l1.size(), (prev + argv.length));
		System.out.println(l1.size());
		iterate(l1.iterator());

		int after = 0;
		int count = 0;
		while(l1.size()>=2)
		{
			count++;
			prev = l1.size();
			l1 = l1.subList(1, prev-1);
			after = l1.size();
			System.out.println(after);
			assertEquals("Iterative Sublisting not working at " + count + " iteration", after, (prev-2));
			iterate(l1.iterator());
		}
	}

	/**
	 * Test of methods of {@link ListAdapter.ListIterator}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of next, previous, hasNext,
	 * hasPrevious and remove methods of {@link ListAdapter.ListIterator}, in particular
	 * the forward and backward removal of an element contained in a list.
	 * <p>
	 * <strong>Test Case Design</strong>: test is made of three variables in wich the updated size of
	 * l1 is stored. During the test the methods that have to be checked are invoked
	 * through two while loops. In the end the sizes stored in the variables are
	 * compared with the expected values.
	 * <p>
	 * <strong>Test Case Description</strong>: test starts storing the initial size of l1 in a variable
	 * then some elements are added to the list. The new size is saved in another
	 * variable. Then the size of l1 is modified again through the usage of the four
	 * methods that have to be checked. The size is stored one last time in a third
	 * variable and in the end test checks if the obtained sizes equal the expected
	 * values.
	 * <p>
	 * <strong>Pre-condition</strong>: add and size methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end l1 must be empty.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()	 
	 * @see ListAdapter#iterator()
	 */
	@Test
	public void testIterator3()
	{
		System.out.println("TestListIterator #3");
		int dl0, dl1, dl2;

		dl0 = l1.size();
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		dl1 = l1.size();
		iterate(l1.iterator());
		li = l1.listIterator();
		while(li.hasNext())
			li.next();
		while(li.hasPrevious())
		{
			System.out.print(li.previous() + " ");
			iterate(l1.iterator());
			li.remove();
		}
		dl2 = l1.size();
		iterate(l1.iterator());

		assertEquals("\n*** insertion and forward to end and backward removal not working ***\n", dl1, (dl0+argv.length));
		assertEquals("\n*** insertion and forward to end and backward removal not working ***\n", dl2, 0);
	}

	/**
	 * Test of methods of {@link ListAdapter.ListIterator}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of hasNext and remove methods
	 * of {@link ListAdapter.ListIterator}, in particular the forward removal of an
	 * element contained in a list.
	 * <p>
	 * <strong>Test Case Design</strong>: test is made of three variables in wich the updated size of
	 * l1 is stored. During the test the methods that have to be checked are invoked 
	 * through a while loops. In the end the sizes stored in the variables are 
	 * compared with the expected values.
	 * <p>
	 * <strong>Test Case Description</strong>: test starts storing the initial size of l1 in a variable
	 * then some elements are added to the list. The new size is saved in another 
	 * variable. Then the size of l1 is modified again through the usage of the two
	 * methods that have to be checked. The size is stored one last time in a third
	 * variable and in the end test checks if the obtained sizes equal the expected
	 * values.
	 * <p>
	 * <strong>Pre-condition</strong>: iterator, add and size methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end l1 must be empty.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#iterator()
 	 */
	@Test
	public void testIterator2()
	{
		System.out.println("TestListIterator #2");
		int dl0, dl1, dl2;
		dl0 = l1.size();
		for(int i=0;i<argv.length;i++)
		{
			l1.add(argv[i]);
		}
		dl1 = l1.size();
		iterate(l1.iterator());
		li = l1.listIterator();
		while(li.hasNext())
		{
			System.out.print(li.next() + " ");
			iterate(l1.iterator());
			li.remove();
		}
		dl2 = l1.size();
		iterate(l1.iterator());

		assertEquals("\n*** insertion and forward removal not working ***\n", dl1, (dl0+argv.length));
		assertEquals("\n*** insertion and forward removal not working ***\n", dl2, 0);
	}

	/**
	 * Test of methods of {@link ListAdapter.ListIterator}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of hasPrevious of 
	 * {@link ListAdapter.ListIterator}. This method return true if the list
	 * iterator has at least one element when traversing the list in the reverse
	 * direction.
	 * <p>
	 * <strong>Test Case Design</strong>: no elements are added to list l1, wich is empty by default,
	 * during the test, so hasPrevious returns false.
	 * <p>
	 * <strong>Test Case Description</strong>: l1 is empty by default. No elements are added to the latter.
	 * When the test reaches the while loop with that kind of condition, it does not
	 * entry into the loop. So the list remains empty. In the end its size is compared
	 * with the expected value.
	 * <p>
	 * <strong>Pre-condition</strong>: iterator and size methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test list l1 must be empty.
	 * @see ListAdapter#size()
	 * @see ListAdapter#iterator()
	 */
	@Test
	public void testIterator1()
	{
		System.out.println("TestListIterator #1");
		iterate(l1.iterator());
		li = l1.listIterator(l1.size());
		while(li.hasPrevious())
		{
			System.out.print(li.previous() + " ");
			iterate(l1.iterator());
			li.remove();
		}
		iterate(l1.iterator());

		assertEquals("\n*** listiterator from end not working ***\n", l1.size(), 0);
	}

	public static void iterate(HIterator iter)
	{
		System.out.print("{");
		while(iter.hasNext())
		{
			System.out.print(iter.next() + "; ");
		}
		System.out.println("}");
	}

	/**
	 * Test of {@link ListAdapter#ListAdapter(HCollection)}
	 * <p>
	 * <strong>Summary</strong>: test to verify the correct working of the constructor with parameters,
	 * wich creats an object of type ListAdapter that contains the same values as the
	 * collection passed as a parameter. In this test, the collection corresponds
	 * to an istance of ListAdapter, l1
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in istantiating a ListAdapter using an already
	 * existing collection, thanks to the constructor with parameters
	 * <p>
	 * <strong>Test Case Description</strong>: first of all the test checks if the constructor with
	 * parameters works properly even with an empty collection. Then some elements
	 * are added to the empty collection and a new instance of ListAdapter is created
	 * from the first one. Notice that test uses method toArray() wich is tested later.
	 * In the end is also the checked the behavior of this constructor when the
	 * parameter is null.
	 * <p>
	 * <strong>Pre-condition</strong>: method toArray() works properly.
	 * <p>
	 * <strong>Post-condition</strong>: the object instatiated must be the same used as parameter
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testConstructorWithParameter() {
		l2 = new ListAdapter(l1);
		assertArrayEquals(l1.toArray(), l2.toArray());
		l1.add(1);
		l1.add(2);
		l1.add(3);
		l1.add(4);
		l1.add(5);
		l2 = new ListAdapter(l1);
		assertArrayEquals(l1.toArray(), l2.toArray());
		HCollection testCollection = null;
		try {
			l2 = new ListAdapter(testCollection);
			throw new Exception();
		}
		catch(Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
	}

	/**
	 * Test of {@link ListAdapter#size()}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of size() method also for
	 * sublists. The method returns the size of this collection
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in some structural changes obtained with
	 * methods add and remove and in checking if the size matches the expected value.
	 * In the end is also checked the size for a sublist got from l1.
	 * <p>
	 * <strong>Test Case Description</strong>: size method is invoked both before adding and element
	 * and after its removal. It is also invoked after creating a sublist.
	 * <p>
	 * <strong>Pre-condition</strong>: methods add, remove and sublist must work correctly
	 * <p>
	 * <strong>Post-condition</strong>: size of list and sublist must be the same as the expected
	 * value.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#remove(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testSize() {
		assertEquals(0, l1.size());
		l1.add("Ciao");
		l1.add("Hola");
		assertEquals("l1 size is not 2", 2, l1.size());
		l1.remove("Hola");
		assertEquals("l1 size is not 1", 1, l1.size());
		HList l2 = l1.subList(0, 1);
		assertEquals("l1 size is not 0", 1, l2.size());
	}

	/**
	 * Test of {@link ListAdapter#isEmpty()}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of isEmpty(). That method
	 * indicates whether a collection is empty or not; it returns true if the
	 * collection is empty.
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in checking the emptiness of l1 before and
	 * after invoking the add method.
	 * <p>
	 * <strong>Test Case Description</strong>: first is immediately checked the emptiness of l1, then,
	 * after an invocation of add, it is checked again. The same is done for method
	 * remove. In the end the method assures the emptiness of a sublist created
	 * passing as parameters 0 and 0, even if the father list has elements
	 * <p>
	 * <strong>Pre-condition</strong>: methods add and subList must properly work.
	 * <p>
	 * <strong>Post-condition</strong>: the assertions must corrispond to the boolean returned by
	 * the isEmpty method.
	 */
	@Test
	public void testIsEmpty() {
		assertTrue(l1.isEmpty());
		l1.add("prova");
		assertFalse(l1.isEmpty());
		l1.remove("prova");
		assertTrue(l1.isEmpty());
		l1.add("prova");
		l1.add("provina");
		l2 = l1.subList(0, 0);
		assertTrue(l2.isEmpty());
	}

	/**
	 * Test of {@link ListAdapter#contains(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of method contains also for
	 * sublists. This method returns true if the object passed as parameter is 
	 * contained in the collection.
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in adding some elements to l1 and creating
	 * a sublist from the latter. Then the content is checked both with element
	 * contained in the collection and not contained and even with null.
	 * <p>
	 * <strong>Test Case Description</strong>: object "prova" and null are added to l1. Then l2 is
	 * created as sublist from l1; at it is added object "provina". Then starts
	 * all the assertions that the test uses to check the working of the method.
	 * In particular, the method checks the presence of an element wich is not in a
	 * sublist but it is in its list father.
	 * <p>
	 * <strong>Pre-condition</strong>: methods add and sublist are properly implemented.
	 * <p>
	 * <strong>Post-condition</strong>: the assertions must corrispond to the boolean returned by
	 * the contains method.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testContains() {
		l1.add("prova");
		l1.add(null);
		l2 = l1.subList(0, 1);
		l2.add("provina");
		assertTrue(l1.contains("prova"));
		assertTrue(l1.contains(null));
		assertTrue(l1.contains("provina"));
		assertTrue(l2.contains("provina"));
		assertFalse(l1.contains("provona"));
		assertFalse(l2.contains(null));
	}
	/**
	 * Test of {@link ListAdapter#iterator()} and of all the methods declared in
	 * {@link HIterator}
	 * <p> 
	 * <strong>Summary</strong>: test checks the correct implementation of
	 * <p>
	 * {@link ListAdapter#iterator()}
	 * <p>
	 * {@link ListAdapter.ListIterator#hasNext()}
	 * <p>
	 * {@link ListAdapter.ListIterator#next()}
	 * <p>
	 * {@link ListAdapter.ListIterator#remove()}
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in invoking HIterator methods on an iterator
	 * and checking its behavior on a list and a sublist. Specifically, the test
	 * checks if the methods throw exceptions properly and if the iterator still
	 * works after several structural changes.
	 * <p>
	 * <strong>Test Case Description</strong>: a HIterator gets created. First the test checks the
	 * remove method, wich can't be called if the next method has not yet been called
	 * or the remove method has already been called after the last call to the next
	 * method. Then some strings are added to l1 and it is checked the right
	 * working of methods next and hasNext, both with add and remove method. In the
	 * end, a sublist l2 from list l1 is created and the same test is made also for
	 * l2.
	 * <p>
	 * <strong>Pre-condition</strong>: the list l1 must not be null but must be empty. Add, remove,
	 * subList must work properly
	 * <p>
	 * <strong>Post-condition</strong>: the iterator must be able to sequentially return and delete
	 * each file contained in the collection and throw exception when the state of
	 * the collection is illegal
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#add(int, Object)
	 * @see ListAdapter#remove(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testIteratorMethods() {
		HIterator iter = l1.iterator();
		try {
            iter.remove();
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
		try {
            iter.next();
            throw new Exception();
        }
		catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
		iter.next();
		iter.remove();
		try{
			iter.remove();
			throw new Exception();
		}
		catch (Exception e) {
			assertEquals(IllegalStateException.class, e.getClass());
		}
		l1.add(0, "prova");
		iter = l1.iterator();
        Object[] testArray = new Object[l1.size()];
		for(int i = 0; iter.hasNext(); i++) testArray[i] = iter.next();
		assertArrayEquals(l1.toArray(), testArray);
		iter = l1.iterator();
		while(iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		assertEquals(0, l1.size());

		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
        l1.add("provaccia");
		l2 = l1.subList(2, 4);
		iter = l2.iterator();
		try {
            iter.remove();
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
        testArray = new Object[l2.size()];
		for(int i = 0; iter.hasNext(); i++) testArray[i] = iter.next();

		assertArrayEquals(l2.toArray(), testArray);
		iter = l2.iterator();
		while(iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		assertEquals(0, l2.size());
	}

	/**
	 * Test of {@link ListAdapter#toArray()}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of toArray() both for lists
	 * and sublists. This method returns an array containing all of the elements
	 * in this collection.
	 * <p>
	 * <strong>Test Case Design</strong>: after four adds test checks if the array obtained by the
	 * invocation of this method is equal to the expected one. The same test is
	 * done also for a sublist of l1.
	 * <p>
	 * <strong>Test Case Description</strong>: four strings are added to list l1 and then the array
	 * obtained by the invocation of this method is equal to the expected one. Then
	 * the method is done also for a sublist of l1. In the end the correct working
	 * of the method is checked also in case in wich the list is empty.
	 * <p>
	 * <strong>Pre-condition</strong>: add and clear methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test l1 must be empty.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#clear()
	 */
	@Test
	public void testToArray() {
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
        assertArrayEquals(new Object[]{"prova", "provina", "provona", "provetta"}, l1.toArray());
		l1 = l1.subList(1, 3);
        assertArrayEquals(new Object[]{"provina", "provona"}, l1.toArray());
		l1.clear();
        assertArrayEquals(new Object[0], l1.toArray());
	}

	/**
	 * Test of {@link ListAdapter#toArray(Object[])}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of toArray(Object[]) both
	 * for lists and sublists. This method returns an array containing all of the
	 * elements in this list in proper sequence.
	 * <p>
	 * <strong>Test Case Design</strong>: test uses add method to insert some elements in l1 and then
	 * invokes the method to cover all the cases.
	 * <p>
	 * <strong>Test Case Description</strong>: first test checks if the method throws the expected
	 * exception when null is passed as parameter of the method. Then some elements
	 * are inserted in list l1 and the array obtained by the invocation of the method
	 * is compared to the expected array. A test is done also with an array shorter
	 * than the size of the list. In the end test checks the implementation also for
	 * sublists.
	 * <p>
	 * <strong>Pre-condition</strong>: add and subList methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test l1 is sublist with two elemets:
	 * "provetta" and "PROVA".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testToArrayWithParameter() {
		Object[] arrayTest = null;
        try {
            l1.toArray(arrayTest);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
        arrayTest = new Object[]{"prova", "provina", "provona"};
        assertArrayEquals(new Object[]{null, null, null}, l1.toArray(arrayTest));
        l1.add("provetta");
        l1.add("provetta");
        assertArrayEquals(new Object[]{"provetta", "provetta", null}, l1.toArray(arrayTest));
        l1.add("PROVA");
        l1.add("PROVA");
        assertArrayEquals(new Object[]{"provetta", "provetta", "PROVA", "PROVA"}, l1.toArray(arrayTest));
		l1 = l1.subList(1, 3);
        assertArrayEquals(new Object[]{"provetta", "PROVA", null}, l1.toArray(arrayTest));

	}

	/**
	 * Test of {@link ListAdapter#add(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks if add method is properly implemented both for lists.
	 * This method add an element in the queue to the list and returns always true.
	 * Notice that this method invokes {@link ListAdapter#add(int, Object)}, 
	 * wich is going to be tested later.
	 * <p>
	 * <strong>Test Case Design</strong>: a simple test wich after insertion in the list checks
	 * the correct working of the add method.
	 * <p>
	 * Test Case Descriprion: first "prova" is added to l1 and it is checked if
	 * the method worked correctly, then it is done the same adding null to the list.
	 * <p>
	 * <strong>Pre-condition</strong>: get method, wich is going to be tested later, must work properly
	 * <p>
	 * <strong>Post-condition</strong>: the elements of l1 are, in order, "prova" and null.
	 * @see ListAdapter#get(int)
	 */
	@Test
	public void testAdd() {
		l1.add("prova");
		assertEquals("prova", l1.get(0));
		l1.add(null);
		assertEquals(null, l1.get(1));
	}

	/**
	 * Test of {@link ListAdapter#remove(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the funcitonality of remove method. This method removes
	 * the specified object from the list and return true if this happens, otherwise
	 * it returns false, like it's shown at the beginning.
	 * <p>
	 * <strong>Test Case Design</strong>: it's checked the correct working of remove by first adding
	 * some elements to l1 and then removing them and trying to remove an element
	 * that does not belong to the list.
	 * <p>
	 * <strong>Test Case Description</strong>: first it is checked if the method returns false when
	 * you try to remove an element wich doesn't belong to the list. Then some
	 * elements are added and some of them are removed. In the end it is checked
	 * the equality between the expected array and the actual array matching the list.
	 * <p>
	 * <strong>Pre-condition</strong>: add and toArray method must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: the element of l1 are, in order, "provona" and "provetta".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testRemove() {
		assertFalse(l1.remove(new Object()));
		l1.add("prova");
		l1.add("provina");
		l1.add("provona");
		l1.add("provetta");
		l1.add(null);
		assertFalse(l1.remove("provaccia"));
		assertTrue(l1.remove("prova"));
		assertTrue(l1.remove("provina"));
		assertTrue(l1.remove(null));
		assertArrayEquals(new Object[] {"provona", "provetta"}, l1.toArray());
	}

	/**
	 * Test of {@link ListAdapter#containsAll(HCollection)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct working of containsAll both for lists
	 * and sublists. This method verifies if this collection contains all the
	 * elements of the collection passed as parameter. The method returns true
	 * if all the elements in the collection passed as parameter are contained
	 * in this list. Notice that if the parameter contains duplicates, this
	 * collection just needs to contain one element equals to the duplicates 
	 * and not the same number of elements.
	 * <p>
	 * <strong>Test Case Design</strong>: the method is verified by adding some elemts to l1, instantiating
	 * l2 with the constructor with parameter and then removing a few elements from the latter
	 * <p>
	 * <strong>Test Case Description</strong>: first test checks if the method throws correctly the 
	 * excpetion when it is passed null as argument. Then some elements are added to
	 * l1 and l2 is created using the HList constructor with parameter, after
	 * removing a few elements. In the end is checked if l1 contains all elemets in l2
	 * and the same test is done also for a sublist of l1.
	 * <p>
	 * <strong>Pre-condition</strong>: constructor with parameter, add remove and clear methods must work
	 * properly.
	 * <p>
	 * <strong>Post-condition</strong>: l2 wich is a sublist of l1, does not contain all the elements
	 * of l1 wich is its father list.
	 * @see ListAdapter#ListAdapter(HCollection)
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#remove(Object)
	 */
	@Test
	public void testContainsAll() {
		try {
            l1.containsAll(null);
            throw new Exception();
        } 
		catch (Exception e) {
        	assertEquals(NullPointerException.class, e.getClass());
        }
		l1.add("prova");
		l1.add("prova");
		l1.add("prova");
		l1.add("prova");
		l1.add("prova");
		l1.add("prova");
		l2 = new ListAdapter();
		l2.add("prova");
		assertTrue(l2.containsAll(l1));
		l1.clear();
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
        l1.add(null);
        l2 = new ListAdapter(l1);
		l2.remove("provona");
		l2.remove("provina");
        assertTrue(l1.containsAll(l2));
		l2 = l1.subList(1, 3);
		assertFalse(l2.containsAll(l1));
	}

	/**
	 * Test of {@link ListAdapter#addAll(HCollection)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of addAll(HCollection) method
	 * both for list and sublist.
	 * This method add all the elements contained in the collection passed as
	 * parameter into this list. This method returns true only if it modifies this
	 * list, otherwise returns false. Notice that this method inserts all the elements,
	 * including the duplicates.
	 * <p>
	 * <strong>Test Case Design</strong>: at first l2 is null in order to check if the method throws
	 * the expected exception. Then some elements are added to l2 and addAll(HCollection)
	 * is invoked on l1.
	 * <p>
	 * <strong>Test Case Description</strong>: first of all it is checked if the method throws the expected
	 * exception. Then there are 4 invocations of add, 3 on l2 and 1 on l1. The size of
	 * l1 gets memorized and then the method is called. Then it is checked if all
	 * the elements contained in l2 were added to l1 using containsAll and if the size
	 * of l1 was updated. In the end the method is tested also if it is invoked on a sublist.
	 * <p>
	 * <strong>Pre-condition</strong>: construcotr with parameter add, size and containsAll methods must work properly
	 * <p>
	 * <strong>Post-condition</strong>: all the elements in l2 must also be contained in l1 as well as
	 * null wich was contained in l1 before the invocation of addAll(HCollection). Furthermore,
	 * l3, at the end of the test, contains "prova", "provina", null, "prova", "provina", "provona".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#containsAll(HCollection)
	 */
	@Test
	public void testAddALL() {
		l2 = null;
		try {
            l1.addAll(l2);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
		l2 = new ListAdapter();
        l2.add("prova");
        l2.add("provina");
        l2.add("provona");
        l1.add(null);
		int l1Dim = l1.size();
		l1.addAll(l2);
        assertTrue(l1.containsAll(l2));
        assertTrue(l1.contains(null));
        assertEquals(l1Dim + l2.size(), l1.size());
		HList l3 = new ListAdapter(l2);
		l3 = l3.subList(0, 2);
		l3.addAll(l1);
		Object [] testArray = {"prova", "provina", null, "prova", "provina", "provona"};
		assertArrayEquals(testArray, l3.toArray());

	}

	/**
	 * Test of {@link ListAdapter#addAll(int, Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of addAll(int, HCollection)
	 * method both for lists and sublists. This method adds all the elements contained
	 * in the collection passed as parameter into this list, starting from the index 
	 * specified. This method returns true only if it modifies this list, otherwise 
	 * returns false. Notice that this method inserts all the elements, including the duplicates.
	 * <p>
	 * <strong>Test Case Design</strong>: at first l2 is null in order to check if the method throws
	 * the expected exception. Then some elements are added to l2 and 
	 * addAll(int, HCollection) is invoked on l1. In the end test checks the correct
	 * working also un sublist.
	 * <p>
	 * <strong>Test Case Description</strong>: first of all it is checked if the method throws the 
	 * expected exception. Then there are 6 invocations of add, 3 on l2 and 3 on l1. The
	 * size of l1 gets memorized and then the method is called. In the end is checked
	 * if all the elements contained in l2 were added to l1, using toArray() in the
	 * right spot and if the size of l1 was updated. In the end test creates a sublist
	 * and checks the correct working of the method also for it.
	 * <p>
	 * <strong>Pre-condition</strong>: add, size, toArray() and containsAll methods must work properly
	 * <p>
	 * <strong>Post-condition</strong>: in the end l1 is a sublist and after the invoctation of
	 * addAll(2, l2) it contains "prova", "provina", "provona", "provina", "provona".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#containsAll(HCollection)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testAddAll() {
		l2 = null;
		try {
            l1.addAll(l2);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
		l2 = new ListAdapter();
        l2.add("prova");
        l2.add("provina");
        l2.add("provona");
        l1.add(null);
        l1.add("provaccia");
        l1.add("provetta");
		int l1Dim = l1.size();
		try {
			l1.addAll(6, l2);
			throw new Exception();
		}
		catch(Exception e) {
			assertEquals(IndexOutOfBoundsException.class, e.getClass());
		}
		l1.addAll(1, l2);
        assertTrue(l1.containsAll(l2));
        assertTrue(l1.contains(null));
        assertEquals(l1Dim + l2.size(), l1.size());
		Object [] arrayTest = {null, "prova", "provina", "provona", "provaccia", "provetta"};
		assertArrayEquals(arrayTest, l1.toArray());
		l1 = l1.subList(2, 4);
		l1.addAll(2, l2);
		Object [] arrayTest2 = {"prova", "provina", "provona", "provina", "provona"};
		assertArrayEquals(arrayTest2, l1.toArray());
	}

		/**
	 * Test of {@link ListAdapter#removeAll(HCollection)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of the removeAll method. This
	 * method removes all the elements contained in the collection passed as parameter
	 * from this list. Notice that it removes all the duplicates in this list and not
	 * only the first occurance of the duplicate. This method returns true only if
	 * it modifies this list, otherwise returns false.
	 * <p>
	 * <strong>Test Case Design</strong>: test checks if the expected exception are thrown. After adding
	 * some elements to l1 and l2 the method is called and it is verified if it works
	 * properly.
	 * <p>
	 * Test Case Descriprion: at first l2, wich is null, is passed to check if the method
	 * throws the expected exception. The after some adds the method is invoked and it
	 * is checked if the list corresponds to the expected array, calling toArray.
	 * <p>
	 * <strong>Pre-condition</strong>: add, toArray methods must work correctly
	 * <p>
	 * <strong>Post-condition</strong>: l1 and l2 have no elements in common
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testRemoveAll() {
		try {
            l1.removeAll(l2);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
		l2 = new ListAdapter();
		assertFalse(l1.removeAll(l2));
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provina");
		l2.add("provetta");
        assertFalse(l1.removeAll(l2));
		Object [] arrayTest = {"prova", "provina", "provona", "provina"};
        assertArrayEquals(arrayTest, l1.toArray());
		l2.add("provina");
        l1.removeAll(l2);
        assertArrayEquals(new Object[]{"prova", "provona"}, l1.toArray());
	}

	/**
	 * Test of {@link ListAdapter#retainAll(HCollection)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of retainAll(HCollection).
	 * This method retains only the elements in this list that are contained in the
	 * collection passed as parameter. In other words, removes from this list all the
	 * elements that are not contained in the specified collection. This method returns
	 * true only if it modifies this list, otherwise returns false. Notice that, as
	 * shown during the test, this method removes all the duplicates of that element
	 * that is also contained in the specified collection and not only the first occurance.
	 * <p>
	 * <strong>Test Case Design</strong>: during the test there are several adds and clears of l1 in
	 * order to check every case that can occure.
	 * <p>
	 * Test Case Descriprion: first of all test checks if, passing null as a parameter,
	 * the method throws the expected exception. Then the are several adds to l1 and
	 * checks to see if the method modifies the structure of this list in different cases.
	 * First, in case both this list and the specified collection are empty; then 
	 * in case there are no elements in common between this and the specified;then in
	 * case only the specified collection is empty; then in case there are elements
	 * in common; then in case there are elements in common with duplicates in this
	 * list; and in the end in case there are no elements in common but there are
	 * duplicates in this list.
	 * <p>
	 * <strong>Pre-condition</strong>: add and toArray methods must work properly
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test l1 does not contain anything, while l2
	 * contains "provetta" and "provona".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testRetainAll() { 
		try {
            l1.retainAll(l2);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(NullPointerException.class, e.getClass());
        }
		l2 = new ListAdapter();
		assertFalse(l1.retainAll(l2));
		l1.add("prova");
        l1.add("provona");
        l1.add("provina");
        assertTrue(l1.retainAll(l2));
        assertEquals(0, l1.size());
		l1.add("prova");
        l1.add("provona");
        l1.add("provina");
		l2.add("provetta");
		assertTrue(l1.retainAll(l2));
		assertEquals(0, l1.size());
		l1.add("prova");
        l1.add("provona");
        l1.add("provina");
		l2.add("provona");
		assertTrue(l1.retainAll(l2));
		assertArrayEquals(new Object[]{"provona"}, l1.toArray());
        l1.clear();
		l1.add("prova");
        l1.add("provona");
        l1.add("provona");
		assertTrue(l1.retainAll(l2));
		assertArrayEquals(new Object[]{"provona", "provona"}, l1.toArray());
        l1.clear();
		l1.add("prova");
		l1.add("prova");
		l1.add("prova");
		assertTrue(l1.retainAll(l2));
		assertEquals(0, l1.size());
	}

	/**
	 * Test of {@link ListAdapter#clear()} 
	 * <p>
	 * <strong>Summary</strong>: test checks if clear method works properly both for lists and sublists.
	 * This method removes all the elements from this list and set to = from
	 * so method size returns 0, if this list is father. This metod can be used to 
	 * remove portion of a list using a sublist. For istance, l1.subList(3, 6).clear().
	 * <p>
	 * <strong>Test Case Design</strong>: test makes some adds, clear the list and the sublists and
	 * then checks if size is equal to the expected value.
	 * <p>
	 * <strong>Test Case Description</strong>: test starts with two adds and a clear, testing the method
	 * on a simple list. Then, after adding 10 elements to l1, a sublists of the latter
	 * is created. The sublist invokes clear and both size of l1 and l2 are checked to
	 * see if they respects the expected values.
	 * <p>
	 * <strong>Pre-condition</strong>: add and subList method must work properly
	 * <p>
	 * <strong>Post-condition</strong>: in the end l2 invokes clear so it must be empty, l1 must be 
	 * affected by this structural change of its sublist, so its size must be reduced.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testClear() {
		l1.add("prova");
		l1.add("provina");
		l1.clear();
		assertEquals(0, l1.size());
		for(int i = 0; i < 10; i++) l1.add(i);
		l2 = l1.subList(4, 9);
		l2.clear();
		assertEquals(0, l2.size());
		assertEquals(5, l1.size());
	}

	/**
	 * Test of {@link ListAdapter#equals(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks if equals method works properly both for lists and sublists.
	 * The method returns true if this list contain the same elements of the collection
	 * passed as parameter and if it has the same size of the latter.
	 * <p>
	 * <strong>Test Case Design</strong>: after a few invocations of add on l1 and l2 the test checks
	 * the equality between the two lists.
	 * <p>
	 * <strong>Test Case Description</strong>: first test checks if the method works with two lists
	 * both empty. Then different cases are checked: first, the same elements are
	 * added to both lists; second, the second list contains an elements that the 
	 * first one does not contain; third the first list has elements while the second
	 * one does not. In the end two sublists are created and their size are checked.
	 * <p>
	 * <strong>Pre-condition</strong>: add clear and toArray() methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end, l1 and l2 are different, since the first one 
	 * contains three elements while the second one is empty.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 * @see ListAdapter#clear()
	 */
	@Test
	public void testEquals() {
		l2 = new ListAdapter();
		assertEquals(l1, l2);
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l2.add("prova");
        l2.add("provina");
        l2.add("provona");
		assertEquals(l1, l2);
		l2.add("provetta");
		assertNotEquals(l1, l2);
		l2.clear();
		assertNotEquals(l1, l2);
		l2.clear();
		l2.add("prova");
        l2.add("provina");
        l2.add("provona");
		l1 = l1.subList(1, 3);
		l2 = l2.subList(1, 3);
		assertArrayEquals(l1.toArray(), l2.toArray());
	}

	/**
	 * Test of {@link ListAdapter#hashCode()}
	 * <p>
	 * <strong>Summary</strong>: test checks to correct working of hasCode method both for lists and
	 * sublists. This method returns the hash code value for this list, wich is
	 * defined with an arithmetic expression.
	 * <p>
	 * <strong>Test Case Design</strong>: the test makes a few adds to l1 and l2 and checks the 
	 * behavior of the method in different cases. In the end it is checked also
	 * the case in wich the method is invoked on sublists.
	 * <p>
	 * <strong>Test Case Description</strong>: same data are added to l1 and l2. First, the test checks
	 * the method when two lists contain the exact same elements. Second, it is checked
	 * if the hashcode is different if l1 and l2 have the same elements but in different
	 * order. Last is checked the case in wich l1 has some data and l2 is an empty list.
	 * In the end the method is checked also for sublists wich have the same data.
	 * <p>
	 * <strong>Pre-condition</strong>: add, clear and subList methods must work properly.
	 * <p>
	 * <strong>Post-conditons</strong>: in the end l1 and l2 are two sublists with the same element:
	 * "provina".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#clear()
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testHashCode() {
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
    	l2 = new ListAdapter();
        l2.add("prova");
        l2.add("provina");
        l2.add("provona");
        assertEquals(l2.hashCode(), l1.hashCode());
		l2.clear();
        l2.add("prova");
        l2.add("provona");
        l2.add("provina");
        assertNotEquals(l2.hashCode(), l1.hashCode());
		l2.clear();
        assertNotEquals(l2.hashCode(), l1.hashCode());
		l2.clear();
		l2.add("prova");
        l2.add("provina");
        l2.add("provona");
		l1 = l1.subList(1, 2);
		l2 = l2.subList(1, 2);
		assertEquals(l2.hashCode(), l1.hashCode());
	}

	/**
	 * Test of {@link ListAdapter#get(int)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of get for both lists and
	 * sublists. This method returns the object in the list at the specified index.
	 * If the index is out of bounds, it throws an exception.
	 * <p>
	 * <strong>Test Case Design</strong>: the test uses add method to insert elements in l1. Then
	 * checks, invoking the get method, if the returned object matches the expected
	 * value.
	 * <p>
	 * <strong>Test Case Description</strong>: test makes 5 invocation to add and checks if the method
	 * throws the expected exception when is passed an index out of bounds. Then
	 * the method is invoked for all the indexes of the list. In the end the same test
	 * is done for a sublist of l1.
	 * <p>
	 * <strong>Pre-condition</strong>: add, size and subList methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end, the sublist of l1 has 4 elements: 1, 2, 3, 4.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#subList(int, int)
	 */
	@Test
	public void testGet() {
		l1.add(0);
        l1.add(1);
        l1.add(2);
        l1.add(3);
        l1.add(4);
        l1.add(5);
		try {
            l1.get(l1.size());
            throw new Exception();
        } 
		catch (Exception e) {
        	assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
		for (int i = 0; i < l1.size(); i++) assertEquals(i, l1.get(i));
		l1 = l1.subList(1, 5);
		try {
            l1.get(5);
            throw new Exception();
        } 
		catch (Exception e) {
        	assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
		for(int i = 1; i < 5; i++) assertEquals(i, l1.get(i));
	}

	/**
	 * Test of {@link ListAdapter#set(int, Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of set for both lists and
	 * sublists. This method sets the element at the given index to be the
	 * object passed as parameter.
	 * <p>
	 * <strong>Test Case Design</strong>: test uses add method to insert elements in the list l1
	 * and then invokes the method to change all the elements inserted. Before of 
	 * that is checked if the method throws the expected exception when an index out
	 * of bounds is passed.
	 * <p>
	 * <strong>Test Case Description</strong>: test starts with 5 adds to l1 and checks if the after
	 * passing an index out of bounds, it throws the expected exception. Then the
	 * method get is invoked as many times as possible to change all the elements.
	 * In the end, the same test is done for a sublist of l1.
	 * <p>
	 * <strong>Pre-condition</strong>: add, size and subList method must work properly
	 * <p>
	 * <strong>Post-condition</strong>: in the end the sublist l1 contains 3 elements: 2, 3, 4.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#subList(int, int)  
	 */
	@Test
	public void testSet() {
        l1.add(4);
        l1.add(3);
        l1.add(2);
        l1.add(1);
        l1.add(0);
		try {
			l1.set(9, 4);
			throw new Exception();
		}
		catch(Exception e) {
			assertEquals(IndexOutOfBoundsException.class, e.getClass());
		}
		for(int i = 0; i < l1.size(); i++) l1.set(i, i);
		assertArrayEquals(new Object[] {0, 1, 2, 3, 4}, l1.toArray());
		l1 = l1.subList(1, 4);
		try {
			l1.set(9, 4);
			throw new Exception();
		}
		catch(Exception e) {
			assertEquals(IndexOutOfBoundsException.class, e.getClass());
		}
		for(int i = 1; i < 4; i++) l1.set(i, i + 1);
		assertArrayEquals(new Object[] {2, 3, 4}, l1.toArray());		
	}

	/**
	 * Test of {@link ListAdapter#add(int, Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of add(int, Objecct). This 
	 * method add inserts the specified element at the specified position in this
	 * list and shifts the element currently at that position (if any) and any
	 * subsequent elements to the right (adds one to their indices). Notice that
	 * the test of this method on sublists is done later in testSublist().
	 * <p>
	 * <strong>Test Case Design</strong>: test is done using a few incovation to the method and
	 * showing the various cases.
	 * <p>
	 * <strong>Test Case Description</strong>: first is checked if the method throws the expected
	 * exception when an index out of bounds is passed as parameter. Then the test
	 * is invoked 4 times always on the same index to show that elements are not 
	 * add in queue of the list.
	 * <p>
	 * <strong>Pre-condition</strong>: toArray method must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end list l1 contains 4 elements: "provona", "provina",
	 * "prova" and null.
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testAddWithIndex() {
		try{
			l1.add(2, null);
			throw new Exception();
		}
		catch(Exception e) {
			assertEquals(IndexOutOfBoundsException.class, e.getClass());
		}
		l1.add(0, null);
		assertArrayEquals(new Object[] {null}, l1.toArray());
		l1.add(0, "prova");
		l1.add(0, "provina");
		l1.add(0, "provona");
		Object [] testArray = {"provona", "provina", "prova", null};
		assertArrayEquals(testArray, l1.toArray());
	}

	/**
	 * Test of {@link ListAdapter#remove(int)}
	 * <p>
	 * <strong>Summary</strong>: test checks if remove(int) works properly for list. This method
	 * removes the element at the specified index in this list and shifts any 
	 * subsequence elements to the left substracting one from their index.
	 * Notice that it does not test the method for sublists since it is done later
	 * in testSublist().
	 * <p>
	 * <strong>Test Case Design</strong>: test checks if the method throws the expected exception
	 * when an index out of bounds is passed as parameter. Then a few add are invoked
	 * to insert elements to l1 and two invocation to the method are done.
	 * <p>
	 * <strong>Test Case Description</strong>. first the test checks if the method throws the expected
	 * exception when an index out of bounds is passed as parameter. Then some elements
	 * are added to the list and after two removes the contenent of the latter is checked.
	 * <p>
	 * <strong>Pre-condition</strong>: add and toArray methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end list l1 contains two elements: "provina" and "provona".
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testRemoveWithIndex() {
		try {
            l1.remove(0);
            throw new Exception();
        } 
		catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
		l1.remove(3);
		l1.remove(0);
		assertArrayEquals(new Object[] {"provina", "provona"}, l1.toArray());
	}

	/**
	 * Test of {@link ListAdapter#indexOf(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of the method both for lists
	 * and sublists. This method returns the index in this list of the first occurence
	 * of the specified element, or -1 if this list does not contain this element.
	 * <p>
	 * <strong>Test Case Design</strong>: test is done using some invocations of add and then 
	 * checking if the returned index is equal to the expected one.
	 * <p>
	 * <strong>Test Case Description</strong>: first test checks if the method return -1 if the
	 * element passed as parameter is not contained in this list. Then there are
	 * 5 adds and the checks for the method. In the end the same test is done for
	 * a sublist of l1.
	 * <p>
	 * <strong>Pre-condition</strong>: add method must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end l1 contains two elements: "provina" and "provona".
	 * @see ListAdapter#add(Object)
	 */
	@Test
	public void testIndexOf() {
		assertEquals(-1, l1.indexOf("prova"));
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
		l1.add("prova");
		assertEquals(1, l1.indexOf("provina"));
        assertEquals(0, l1.indexOf("prova"));
        assertEquals(-1, l1.indexOf(6));
		l1 = l1.subList(1, 3);
        l1.add("provona");
		assertEquals(2, l1.indexOf("provona"));
		assertEquals(-1, l1.indexOf("prova"));
	}

	/**
	 * Test of {@link ListAdapter#lastIndexOf(Object)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of lastIndexOf both for lists
	 * and sublists. This method returns the index in this list of the last occurance
	 * of the specified element or -1 if this list does not contain this element.
	 * <p>
	 * <strong>Test Case Design</strong>: test is done using some invocations of add and then 
	 * checking if the returned index is equal to the expected one.
	 * <p>
	 * <strong>Test Case Description</strong>: first test checks if the method return -1 if the
	 * element passed as parameter is not contained in this list. Then there are
	 * 5 adds and the checks for the method. In the end the same test is done for
	 * a sublist of l1.
	 * <p>
	 * <strong>Pre-condition</strong>: add method must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: in the end l1 contains two elements: "provina" and "provona".
	 * @see ListAdapter#add(Object)
	 */
	@Test
	public void testLastIndexOf() {
		assertEquals(-1, l1.lastIndexOf("prova"));
		l1.add("prova");
        l1.add("provina");
        l1.add("provona");
        l1.add("provetta");
		l1.add("prova");
		assertEquals(1, l1.lastIndexOf("provina"));
        assertEquals(4, l1.lastIndexOf("prova"));
        assertEquals(-1, l1.lastIndexOf(6));
		l1 = l1.subList(1, 3);
        l1.add("provona");
		assertEquals(3, l1.lastIndexOf("provona"));
		assertEquals(-1, l1.lastIndexOf("prova"));
	}

	/**
	 * Test of {@link ListAdapter#listIterator(int)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of this method and all the
	 * methods declared in {@link HListIterator} interface.
	 * <p>
	 * <strong>Test Case Design</strong>: the beginning of the test comprehends the testing of the
	 * constructor of {@link ListAdapter.ListIterator}, checking if this throws
	 * the expected exceptions. Then all the remaing methods of the interface is
	 * checked in different cases. 
	 * <p>
	 * <strong>Test Case Description</strong>: first is checked the proper working of the construcor
	 * of {@link ListAdapter.ListIterator} with index. Then hasNext, hasPrevious,
	 * previous and next methods are checked usign also an auxiliary array. In the end
	 * also add remove and set methods are tested positively.
	 * <p>
	 * <strong>Pre-condition</strong>: add, clear, size and toArray methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: at the end of the test, list l1 contains 5 elements: 0, "prova",
	 * 2, 3, 4.
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#size()
	 * @see ListAdapter#clear()
	 * @see ListAdapter#toArray()
	 */
	@Test
	public void testListIteratorIndexWithItsMethod() {
		for(int i = 0; i < 5; i++) l1.add(i);
		HListIterator iter = l1.listIterator(2);
		assertEquals(2, iter.next());
		iter = l1.listIterator(l1.size());
		try {
            iter.next();
            throw new Exception();
        }
		catch (Exception e) {
            assertEquals(NoSuchElementException.class, e.getClass());
        }
		try {
            iter = l1.listIterator(l1.size() + 1);
            throw new Exception();
        }
		catch (Exception e) {
            assertEquals(IndexOutOfBoundsException.class, e.getClass());
        }
		l1.clear();
		iter = l1.listIterator();
		assertFalse(iter.hasNext());
        assertFalse(iter.hasPrevious());
		iter.add("prova");
        assertEquals("prova", iter.previous());
		l1.clear();
		for(int i = 0; i < 5; i++) l1.add(i);
		iter = l1.listIterator();
		Object[] arrayTest = new Object[l1.size()];
		int i = 0;
		while(iter.hasNext()) arrayTest[i++] = iter.next();
		assertArrayEquals(new Object[]{0, 1, 2, 3, 4}, arrayTest);
		assertEquals(4, iter.previousIndex());
		i = 0;
        while (iter.hasPrevious()) arrayTest[i++] = iter.previous();
        assertArrayEquals(new Object[]{4, 3, 2, 1, 0}, arrayTest);
        assertEquals(0, iter.nextIndex());
		iter.next();
        iter.add("prova");
		assertArrayEquals(new Object[]{0, "prova", 1, 2, 3, 4}, l1.toArray());
		iter.previous();
        iter.remove();
        assertArrayEquals(new Object[]{0, 1, 2, 3, 4}, l1.toArray());
		iter.next();
       	iter.set("prova");
        assertArrayEquals(new Object[]{0, "prova", 2, 3, 4}, l1.toArray());
	}
	/**
	 * Test of {@link ListAdapter#subList(int, int)}
	 * <p>
	 * <strong>Summary</strong>: test checks the correct implementation of the subList method, in
	 * particular the behavior of the method with a more than one generation of
	 * sublist. In other words, it checks the correct working of the method if
	 * a list is sublist of another sublist and so on...
	 * <p>
	 * <strong>Test Case Design</strong>: test consists in creating different generation of sublists
	 * and modyfing them through methods like add and remove to see if these ones
	 * modify correctly the lists that come before in the hierarchical structure.
	 * Notice that, as it is specified in HList documentation, if you modify a list
	 * that has a sublist, the behavior of the latter is undefined. In other words,
	 * it is not sure that the change gets applied also on the sublist.
	 * <p>
	 * Test Description: test starts with adding the elements contained in argv,
	 * then a hierarchical structure of sublists is created and various structural 
	 * changes are applied on it. Test checks if the size of each sublist
	 * equals the expected value. In the end, it is also checked if trying to create
	 * a sublist with illegal indexes throws the expected exception.
	 * <p>
	 * <strong>Pre-condition</strong>: add, remove and size methods must work properly.
	 * <p>
	 * <strong>Post-condition</strong>: size of list and sublist must be the same as the expected
	 * value and the exeption thrown must be the same as the expected one. 
	 * @see ListAdapter#add(Object)
	 * @see ListAdapter#remove(int)
	 */
	@Test
	public void testSubList() {
		int i = 0;
		while(i < argv.length)l1.add(argv[i++]);
		l2 = l1.subList(2, 5);
		HList l3 = l2.subList(3, 5);
		l3.add("prova");
		l3.add("provina");
		l3.add("provona");
		l3.remove(4);
		assertEquals(4, l3.size());
		assertEquals(8, l1.size());
		HList l4 = l3.subList(4, 7);
		l4.add("provetta");
		l4.add("provaccia");
		l4.remove(4);
		assertEquals(4, l4.size());
		assertEquals(6, l2.size());
		try {
			l4 = l4.subList(3, 5);
			throw new Exception();
		}
		catch (Exception e) {
			 assertEquals(IndexOutOfBoundsException.class, e.getClass());
		}
	}
}