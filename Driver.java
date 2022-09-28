package b07lab1;
import java.io.*;

public class Driver {
  public static void main(String [] args) throws Exception { 
    // Null evaluation
    Polynomial p = new Polynomial(); 
    System.out.println(p.evaluate(3));

    // Evaluate function (return 5.8041)
    double [] c1 = {6,0,0,5}; 
    Polynomial p1 = new Polynomial(c1); 
    double [] c2 = {0,-2,0,0,-9};
    Polynomial p2 = new Polynomial(c2); 
    Polynomial s = p1.add(p2); 
    System.out.println("s(0.1) = " + s.evaluate(0.1)); 

    // Check root (return true)
    if(s.hasRoot(1)) System.out.println("1 is a root of s"); 
    else System.out.println("1 is not a root of s");

    // Check multiplcation (return 77.5)
    double[] d1 = {2, 8, 7};
    int[] i1 = {0, 1, 2};
    double[] d2 = {5, 6, 8};
    int[] i2 = {0, 1, 2};
    Polynomial t1 = new Polynomial(d1, i1);
    Polynomial t2 = new Polynomial(d2, i2);
    Polynomial t = t1.multiply(t2);
    System.out.println("t(0.5) = " + t.evaluate(0.5));

    // Check reading from file
    Polynomial v1 = new Polynomial(new File("test1.txt"));
    Polynomial v2 = new Polynomial(new File("test2.txt"));
    v1.print();
    v2.print();

    // Write to file
    v2.saveToFile("output.txt");
  } 
} 