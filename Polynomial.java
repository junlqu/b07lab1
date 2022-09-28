package b07lab1;
import java.util.*;
import java.io.*;

public class Polynomial {
  double[] coefficients;
  int[] exponents;

  public Polynomial() {
    this.coefficients = null;
    this.exponents = null;
  }

  public Polynomial(double[] arr) {
    int len = 0;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == 0) continue;
      len++;
    }
    this.coefficients = new double[len];
    this.exponents = new int[len];
    int counter = 0;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == 0) continue;
      this.coefficients[counter] = arr[i];
      this.exponents[counter] = i;
      counter++;
    }
  }

  public Polynomial(double[] arr, int[] exp) {
    this.coefficients = new double[arr.length];
    for (int i = 0; i < arr.length; i++) {
      this.coefficients[i] = arr[i];
    }
    this.exponents = new int[exp.length];
    for (int i = 0; i < exp.length; i++) {
      this.exponents[i] = exp[i];
    }
  }

  public Polynomial(File f) throws Exception {
    // Read file to find string and degree of function
    Scanner fr = new Scanner(f);
    String str = fr.nextLine();
    int degree = 0;
    for (String i : str.split("-")) {
      for (String j : i.split("\\+")) if (!j.equals("")) degree++;
    }
    fr.close();
    // Initialize new polynomial
    this.coefficients = new double[degree];
    this.exponents = new int[degree];
    // Create new string array for each term
    String[] pos = str.split("\\+");
    String[] nomials = new String[degree];
    int counter = 0;
    for (String i : pos) {
      for (String j : i.split("(?=-)")) {
        nomials[counter] = j;
        counter++;
      }
    }
    // Add to each polynomial array
    for (int i = 0; i < degree; i++) {
      // Split string into constant and variable
      String[] conVar = nomials[i].split("x");
      // If split has length 0: "x" -> null
      if (conVar == null) {
        this.coefficients[i] = 1;
        this.exponents[i] = 1;
        continue;
      }
      // If split is "cx" -> {"c"} (of length 1, special case)
      if ((nomials[i].substring(nomials[i].length() - 1, nomials[i].length()).equals("x")) && (!conVar[0].equals("-")) && (nomials[i].length() > 1)) {
        this.coefficients[i] = Double.parseDouble(conVar[0]);
        this.exponents[i] = 1;
        continue;
      }
      // If split has length 1, 2 cases: "-x" -> {"-"}, "c" -> {"c"}
      if (conVar.length == 1) {
        String temp = conVar[0];
        // Case 1: -x
        if (temp.equals("-")) {
          this.coefficients[i] = -1;
          this.exponents[i] = 1;
        }
        // Case 2: const (regardless of polarity)
        else {
          this.coefficients[i] = Double.parseDouble(temp);
          this.exponents[i] = 0;
        }
        continue;
      }
      // If split has length 2, 2 cases: "cxe" -> {"c", "e"}, "-cxe" -> {"-c", "e"}
      if (conVar[0].equals("-")) this.coefficients[i] = -1;
      else this.coefficients[i] = Double.parseDouble(conVar[0]);
      this.exponents[i] = Integer.parseInt(conVar[1]);
    }
    // Sort both arrays in order of exponent
    for (int i = 0; i < this.exponents.length - 1; i++) {
      for (int j = 0; j < this.exponents.length - i - 1; j++) {
        if (this.exponents[j] > this.exponents[j + 1]) {
          double tempSum = this.coefficients[j];
          this.coefficients[j] = this.coefficients[j + 1];
          this.coefficients[j + 1] = tempSum;
          int tempExp = this.exponents[j];
          this.exponents[j] = this.exponents[j + 1];
          this.exponents[j + 1] = tempExp;
        }
      }
    }
  }

  public void print() {
    if (this.coefficients == null) return;
    for (int i = 0; i < this.coefficients.length; i++) {
      System.out.print("{ " + this.coefficients[i] + ", " + this.exponents[i] + " }");
    }System.out.println();
  }

  public Polynomial add(Polynomial arr) {
    // If either are empty, return the other
    if (this.coefficients == null) return arr;
    else if (arr.coefficients == null) return this;
    // Find difference in existing degrees
    int extraExp = 0;
    for (int i = 0; i < arr.exponents.length; i++) {
      if (Arrays.binarySearch(this.exponents, arr.exponents[i]) < 0) extraExp++;
    }
    // Initialized new arrays
    double[] sum = new double[this.exponents.length + extraExp];
    int[] exp = new int[this.coefficients.length + extraExp];
    // Add object array values to array
    for (int i = 0; i < this.exponents.length; i++) {
      sum[i] = this.coefficients[i];
      exp[i] = this.exponents[i];
    }
    // Add predicate array values to array
    for (int i = 0; i < arr.exponents.length; i++) {
      if (Arrays.binarySearch(this.exponents, arr.exponents[i]) < 0) {
        sum[i + this.exponents.length] = arr.coefficients[i];
        exp[i + this.exponents.length] = arr.exponents[i];
      }
      else {
        sum[Arrays.binarySearch(exp, arr.exponents[i])] += arr.coefficients[i];
      }
    }
    // Sort both arrays in order of exponent
    for (int i = 0; i < exp.length - 1; i++) {
      for (int j = 0; j < exp.length - i - 1; j++) {
        if (exp[j] > exp[j + 1]) {
          double tempSum = sum[j];
          sum[j] = sum[j + 1];
          sum[j + 1] = tempSum;
          int tempExp = exp[j];
          exp[j] = exp[j + 1];
          exp[j + 1] = tempExp;
        }
      }
    }
    Polynomial n = new Polynomial(sum, exp);
    return n;
  }

  public double evaluate(double x) {
    if (this.coefficients == null) return 0.0;
    double sum = 0;
    for (int i = 0; i < this.coefficients.length; i++) {
      sum += this.coefficients[i] * Math.pow(x, this.exponents[i]);
    }
    return sum;
  }

  public Polynomial multiply(Polynomial arr) {
    Polynomial[][] toBeAdded = new Polynomial[this.coefficients.length][arr.coefficients.length];
    for (int i = 0; i < this.coefficients.length; i++) {
      for (int j = 0; j < arr.coefficients.length; j++) {
        double[] prod = {this.coefficients[i] * arr.coefficients[j]};
        int[] exp = {this.exponents[i] + arr.exponents[j]};
        Polynomial temp = new Polynomial(prod, exp);
        toBeAdded[i][j] = temp;
      }
    }
    Polynomial prod = new Polynomial();
    for (int i = 0; i < this.coefficients.length; i++) {
      for (int j = 0; j < arr.coefficients.length; j++) {
        prod = prod.add(toBeAdded[i][j]);
      }
    }
    return prod;
  }

  public boolean hasRoot(double value) {
    if (this.coefficients == null) return false;
    return (evaluate(value) == 0);
  }

  public void saveToFile(String fileName) throws Exception {
    FileWriter f = new FileWriter(new File(fileName));
    for (int i = 0; i < this.coefficients.length; i++) {
      String left = Double.toString(this.coefficients[i]);
      String right = Integer.toString(this.exponents[i]);
      if (this.coefficients[i] % 1 == 0) left = Integer.toString((int)(this.coefficients[i]));
      if (left.equals("-1")) left = "-";
      if (left.equals("1")) left = "a";
      if ((right.equals("0")) || (right.equals("1"))) right = "";
      if (left.charAt(0) != '-') f.write("+");
      if (!left.equals("a")) f.write(left);
      if (this.exponents[i] != 0) f.write("x");
      f.write(right);
    }
    f.close();
  }
}