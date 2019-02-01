package hermes.dev.transasia.ru.fireapp.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class GenericsExample {
    public void foo() {
        List anyList = new LinkedList();
        anyList.add(1);
        Integer x = (Integer) anyList.iterator().next();


        List<Integer> integerList = new LinkedList<>();
        integerList.add(1);
        Integer y = integerList.iterator().next();


        /**
         * https://docs.oracle.com/javase/tutorial/extra/generics/subtype.html
         * In general, if Foo is a subtype (subclass or subinterface) of Bar, and G is some generic
         * type declaration, it is not the case that G<Foo> is a subtype of G<Bar>.
         * This is probably the hardest thing you need to learn about generics,
         * because it goes against our deeply held intuitions.
         */
        List<String> ls = new ArrayList<>();
//        compile error below
//        List<Object> lo = ls;
//        lo.add(new Object());
        String s = ls.get(0);

    }

    /**
     * https://docs.oracle.com/javase/tutorial/extra/generics/wildcards.html
     */
    public void printAll(Collection c) {
        Iterator iterator = c.iterator();
        for (int k = 0; k < c.size(); k++) {
            System.out.println(iterator.next());
        }
    }

    // run time error
    public void printAllNewUnCorrect(Collection<Object> c) {
        for (Object o : c) {
            System.out.println(o);
        }
    }

    //    public void printAllNewCorrect(Collection<?> c){  the same below
    public void printAllNewCorrect(Collection<? extends Object> c) {
        for (Object o : c) {

        }
    }


    public abstract class Shape {
        abstract void draw(Canvas canvas);
    }


    public class Circle extends Shape {

        int radius;
        double x_center;
        double y_center;

        @Override
        void draw(Canvas canvas) {
            canvas.draw(this);
        }
    }

    public class Rectangle extends Shape {

        int heigth;

        @Override
        void draw(Canvas canvas) {
            canvas.draw(this);
        }
    }

    public class Canvas {
        void draw(Shape shape) {
        }

        void drawAll(List<Shape> shapes) {
            for (Shape shape : shapes) {
                shape.draw(this);
            }
        }

        //Bounded Wildcard
        void drawAllCorrect(List<? extends Shape> shapes) {
            history.add(shapes);
            for (Shape s : shapes) {
                s.draw(this);
            }
        }
    }


    /**
     * GENERICS METHOD
     * https://docs.oracle.com/javase/tutorial/extra/generics/methods.html
     */

    public void fromArrayLoCollection(Object[] o, Collection<?> c) {
        for (Object object : o) {
//            c.add(object); //compile error
        }
    }

    public <T> void fromArrayToCollection(T[] o, Collection<T> c) {
        for (T obj : o) {
            c.add(obj);
        }
    }

    Shape[] array = new Shape[100];
    List<Shape> list = new ArrayList<>();

    public void example() {
        fromArrayToCollection(array, list);
    }

    public static <T> void copy(List<T> dest, List<? extends T> src) {  // using WildCard
        dest.addAll(src);
    }

    public static <T, S extends T> List<T> copy2(List<T> dest, List<S> src) {  // using Generics Method
        dest.addAll(src);
        return dest;
    }

    //Use wildcard prefer
    public static List<List<? extends Shape>> history = new ArrayList<List<? extends Shape>>();

    /**
     * FINE PRINT
     * https://docs.oracle.com/javase/tutorial/extra/generics/fineprint.html
     */

    public boolean check() {
        List<Integer> li = new ArrayList<Integer>();
        List<String> ls = new ArrayList<String>();

        return li.getClass() == ls.getClass(); // return true;
    }

    public void infstOF() {
        Collection cs = new ArrayList<String>();
// Illegal.
//        if (cs instanceof Collection<String>) {
//
//        }
    }


}
