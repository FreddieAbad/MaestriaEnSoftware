package ec.ups.edu.tdd.calculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(value= Parameterized.class)
public class CalculatorParametersTest {
    private int a, b, expected;

    public CalculatorParametersTest(int a, int b, int expected) {
        this.a = a;
        this.b = b;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static List<Object[]> parameters() {
        return Arrays.asList(
                new Object[]{2, 4, 6},
                new Object[]{1, 8, 9},
                new Object[]{2, 5, 7},
                new Object[]{7, 3, 10},
                new Object[]{22, 4, 26},
                new Object[]{12, 14, 26}
        );
    }

    @Test
    public void given_parameters_when_addition_then_ok() {
        Calculator calculator = new Calculator();
        int actual = calculator.addition(a, b);
        assertEquals(expected, actual);
        System.out.println("Test given_parameters_when_addition_then_ok");

    }
}
