package b07lab1;

public class Polynomial {
  double[] coefficients;

  public Polynomial() {
    this.coefficients = new double[1];
    this.coefficients[0] = 0;
  }

  public Polynomial(double[] arr) {
    this.coefficients = new double[arr.length];
    this.coefficients = arr;
  }

  public Polynomial add(Polynomial arr) {
    int max = this.coefficients.length > arr.coefficients.length ? this.coefficients.length : arr.coefficients.length;
    double[] sum = new double[max];
    for (int i = 0; i < max; i++) {
      double left, right;
      left = i >= this.coefficients.length ? 0 : this.coefficients[i];
      right = i >= arr.coefficients.length ? 0 : arr.coefficients[i];
      sum[i] = left + right;
    }
    Polynomial n = new Polynomial(sum);
    return n;
  }

  public double evaluate(double x) {
    double sum = 0;
    for (int i = 0; i < this.coefficients.length; i++) {
      sum += this.coefficients[i] * Math.pow(x, i);
    }
    return sum;
  }

  public boolean hasRoot(double value) {
    return (evaluate(value) == 0);
  }
}