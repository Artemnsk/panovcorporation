package functions;

import java.util.Random;
import junit.framework.TestCase;
import net.sourceforge.interval.ia_math.RealInterval;
import org.junit.Test;
import core.Box;

public class Function_Price5_2DTest extends TestCase {
    @Test
    public void testPoints() {
    	Function_Price5_2D f = new Function_Price5_2D();
    	Random rnd = new Random();
    	double point[] = new double[2];
    	Box b = new Box(2, new RealInterval());
    	

    	for (int i = 0; i < 10; i++) {
        	point[0] = rnd.nextDouble() * rnd.nextInt(4);
        	point[1] = rnd.nextDouble() * rnd.nextInt(10);
        	b.setInterval(0, new RealInterval(point[0]) );
        	b.setInterval(1, new RealInterval(point[1]) );
        	
        	f.calculate(b);
	
        	assertTrue(b.getFunctionValue().hi() - b.getFunctionValue().lo() < 1e-6);
        	assertTrue(b.getFunctionValue().hi() - f.calculatePoint(point) < 1e-6);
        }
        
    	// IAMath issue: 0.10548578434087275 //nvp: fixed.	
    	// known point: 0
        point[0] = point[1] = 0;
    	b.setInterval(0, new RealInterval(point[0]) );
    	b.setInterval(1, new RealInterval(point[1]) );
    	f.calculate(b);
    	assertTrue(Math.abs( f.calculatePoint(point) ) < 1e-6);
    	assertTrue(b.getFunctionValue().hi() - f.calculatePoint(point) < 1e-6);
    }
    
    @Test
    public void testWrongDimension() {
    	Function_Price5_2D f = new Function_Price5_2D();
    	Random rnd = new Random();
        int dim;
        do {
        	dim = rnd.nextInt(100);
        } while (dim == 2);
        
        Box box = new Box(dim, new RealInterval(-1, 1));
        double point[] = new double[dim];
        
        try {
        	f.calculate(box);
        	fail("exception expected! Don't you forget to add -ea option to JavaVM arguments? (Window->Preferences->Jnstalled JREs->Edit->Default VM arguments)");
        } catch (AssertionError e) {
        }
        try {
        	f.calculatePoint(point);
        	fail("exception expected");
        } catch (AssertionError e) {
        	// OK
        }
    }
}
