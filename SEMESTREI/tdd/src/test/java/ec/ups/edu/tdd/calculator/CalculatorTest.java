package ec.ups.edu.tdd.calculator;

import org.junit.*;

import static org.junit.Assert.*;

public class CalculatorTest {
    Calculator calculator = null;
    @Before
    public void setUp(){
        calculator=new Calculator();
    }
    @BeforeClass
    public static void setUpClass(){
        System.out.println("setUpClass()");
    }
    @AfterClass
    public static void tearDownClass(){
        System.out.println("tearDownClass()");
    }
    @Test
    public void given_two_integer_when_addition_then_ok(){
        assertEquals(6,calculator.addition(4,2));
        System.out.println("Test 1");
    }
    @Test
    public void given_two_integer_when_substraction_then_ok(){
        assertEquals(2,calculator.substraction(4,2));
        System.out.println("Test 2");

    }
    @Test(expected = ArithmeticException.class)
    public void given_two_integer_when_division_then_exception(){
        assertEquals(3,calculator.division(6,0));
        System.out.println("Test 3");

    }

    @Test(timeout = 15050)
    public void given_two_integer_when_multiplication_then_timeout() {
        assertEquals(8,calculator.multiplication(4,2));
        System.out.println("Test 3");
    }
    //ejectura despues de cualquier metodo
    @After
    public void tearDown(){
        System.out.println("tearDown()");
        calculator.setAns(0);
        calculator=null;
    }


}