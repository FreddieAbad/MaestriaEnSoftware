package ec.ups.edu.tdd.calculator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ICalculatorTest {
    Calculator calculator = null;
    @Before
    public void setUp(){
        calculator=new Calculator();
    }
    @Test
    public void give_two_integers_when_addition_then_ok() {
        ICalculator iCalculator= Mockito.mock(ICalculator.class);
        Mockito.when(iCalculator.addition(2,3)).thenReturn(5);//.thenReturn(7);
        assertEquals(5, iCalculator.addition(2,3));
    }
    @Test
    public void give_two_integers_when_substraction_then_ok() {
        ICalculator iCalculator= Mockito.mock(ICalculator.class);
        Mockito.when(iCalculator.substraction(3,1)).thenReturn(2);//.thenReturn(7);
        assertEquals(2, iCalculator.substraction(3,1));
    }
    @Test
    public void give_two_integers_when_multiplication_then_ok() {
        ICalculator iCalculator= Mockito.mock(ICalculator.class);
        Mockito.when(iCalculator.multiplication(3,1)).thenReturn(3);//.thenReturn(7);
        assertEquals(3, iCalculator.multiplication(3,1));
    }
    @Test
    public void give_two_integers_when_division_then_ok() {
        ICalculator iCalculator= Mockito.mock(ICalculator.class);
        Mockito.when(iCalculator.division(4,2)).thenReturn(2);//.thenReturn(7);
        assertEquals(2, iCalculator.division(4,2));
    }

}