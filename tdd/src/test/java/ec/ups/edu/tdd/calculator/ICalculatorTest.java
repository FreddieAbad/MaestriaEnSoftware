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
        Mockito.when(calculator.addition(2,3)).thenReturn(0);//.thenReturn(7);
        assertEquals(7, calculator.addition(2,3));
    }

}