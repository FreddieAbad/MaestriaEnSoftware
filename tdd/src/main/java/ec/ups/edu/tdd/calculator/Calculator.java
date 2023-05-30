package ec.ups.edu.tdd.calculator;

public class Calculator {
    private int ans;
    public int addition(int a, int b){
        return a+b;
    }
    public int substraction(int a, int b){
        return a-b;
    }
    public int division(int a, int b){
        return a/b;
    }
    public int multiplication(int a, int b)  {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return a*b;
    }

    public int getAns() {
        return ans;
    }

    public void setAns(int ans) {
        this.ans = ans;
    }
}
