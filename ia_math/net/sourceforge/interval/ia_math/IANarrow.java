package net.sourceforge.interval.ia_math;

/**
 * IANarrow.java 
 *   -- classes implementing narrowing of arithmetic and elementary functions,
 *      as part of the "ia_math library" version 0.1beta1, 10/97
 * 
 * <p>
 * Copyright (C) 2000 Timothy J. Hickey
 * <p>
 * License: <a href="http://interval.sourceforge.net/java/ia_math/licence.txt">zlib/png</a>
 * <p>
 * the class RealIntervalNarrow contains methods for narrowing
 * the arithmetic operations and elementary functions.
 */

import static net.sourceforge.interval.ia_math.IAMath.*;
import net.sourceforge.interval.ia_math.exceptions.IANarrowingFaildException;
//import static net.sourceforge.interval.ia_math.IAMath.intersect;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;


public class IANarrow {


	private static boolean checkNarrowing(	RealInterval one, RealInterval two, 
											RealInterval new1, RealInterval new2
									) throws IANarrowingFaildException {	
		if (new1 == null || new2 == null)
			throw new IANarrowingFaildException(); // incompatible intervals
		if (new1.equals(one) && new2.equals(two))
			return false; // none of the intervals was narrowed
		return true;
	}
	private static boolean checkNarrowing(	RealInterval one, RealInterval two, RealInterval three, 
										RealInterval new1, RealInterval new2, RealInterval new3
											) throws IANarrowingFaildException {	
		if (new1 == null || new2 == null || new3 == null)
			throw new IANarrowingFaildException(); // incompatible intervals
		if (new1.equals(one) && new2.equals(two) && new3.equals(three))
			return false; // none of the intervals was narrowed
		return true;
	}
	
	public static boolean narrowAdd(	RealInterval res, 
										RealInterval a, 
										RealInterval b) 	throws IANarrowingFaildException {
		
			RealInterval newRes = intersect(add(a, b), res);
			RealInterval newA   = intersect(sub(res, b), a);
			RealInterval newB   = intersect(sub(res, a), b);
			
			return checkNarrowing(res, a, b, newRes, newA, newB);
	}

	public static boolean narrowSub(RealInterval res, RealInterval a,
			RealInterval b) throws IANarrowingFaildException {
		return narrowAdd(a, res, b);
	}

	/* z = x*y */
	public static boolean narrowMul(	RealInterval res, 
										RealInterval a,
										RealInterval b) 	throws IANarrowingFaildException {
		RealInterval newRes =intersect(mul(a, b), res);
		RealInterval newA = intersect(div(res, b), a);
		RealInterval newB = intersect(div(res, a), b);
		return checkNarrowing(res, a, b, newRes, newA, newB);
	}

	public static boolean narrowDiv( // res = a/b
			RealInterval res, RealInterval a, RealInterval b)
			throws IANarrowingFaildException {
		return narrowMul(a, b, res);
	}

/*  
  public static boolean narrow_uminus(
       RealInterval r, RealInterval b) throws IANarrowingFaildException {
    try {
  	  RealInterval res_tmp = intersection(res, uminus(b) );
  	  RealInterval res_1 = intersection(res_tmp, res);
  	  double dRes= res.wid() - res_1.wid();
  	res = res_1;
  	  
  	  RealInterval b_tmp = intersection(b, uminus(res) );
  	  RealInterval b_1 = intersection(b_tmp, b);
  	  double dB= b.wid() - b_1.wid();
	  b = b_1;

  	  if (dRes > 0 || dB > 0)
  		  return true; // successful narrowing
        return false; // none of the intervals was narrowed   
    } catch (IAIntersectionException e) {
    	throw new IANarrowingFaildException(e.getMessage()); // incompatible intervals
    }
  }
*/

	public static boolean narrowExp(RealInterval r, RealInterval a)
			throws IANarrowingFaildException {
		RealInterval newR = intersect(exp(a), r);
		RealInterval newA = intersect(log(r), a);
		return checkNarrowing(r, a, newR, newA);
	}

	public static boolean narrowLog(RealInterval res, RealInterval a)
			throws IANarrowingFaildException {
		return narrowExp(a, res);
	}

	public static boolean narrowSin(RealInterval r, RealInterval a)
			throws IANarrowingFaildException {
		RealInterval newR = intersect(sin(a), r);
		RealInterval newA = intersect(asin(r), a);
		return checkNarrowing(r, a, newR, newA);
	}

	public static boolean narrowCos(RealInterval r, RealInterval a)
			throws IANarrowingFaildException {
		RealInterval newR = intersect(cos(a), r);
		RealInterval newA = intersect(acos(r), a);
		return checkNarrowing(r, a, newR, newA);
	}

	public static boolean narrowTan(RealInterval r, RealInterval a)
			throws IANarrowingFaildException {
		RealInterval newR = intersect(tan(a), r);
		RealInterval newA = intersect(atan(r), a);
		return checkNarrowing(r, a, newR, newA);
	}
  /*	  
  // res = asin(a)
  public static boolean 
    narrow_asin(RealInterval res, RealInterval a) {
    try {
         b.intersect(new RealInterval(-1.0,1.0));
         a.intersect(IAMath.asin(b));
         b.intersect(IAMath.sin(a));
         return true;
    } catch (IAException e) {
      return false;
    }
  }

  // a = acos(b)
  public static boolean 
    narrow_acos(RealInterval b,RealInterval a) {
	  throw new NotImplementedException();
	  /*	  
    try {
         b.intersect(new RealInterval(-1,1));
         a.intersect(IAMath.acos(b));
         b.intersect(IAMath.cos(a));
         return true;
    } catch (IAException e) {
      return false;
    }
    }

	// a = atan(b)
	public static boolean narrow_atan(RealInterval b, RealInterval a) {
	}
*/  

	/**
	 * z = x^y, where y is an integer
	 * 
	 * @throws IANarrowingFaildException
	 */
	public static boolean narrowPower(RealInterval r, RealInterval x, int y)
			throws IANarrowingFaildException {
		RealInterval newR = intersect(power(x, y), r);

		RealInterval possibleX = root(r, y);
		// possibleX = ((+/-x)^4)^1/4
		if (y % 2 == 0) {
			if (possibleX.lo() > x.hi()) // [x] .. 0 .. [posX]
				possibleX = uminus(possibleX);
			else if (x.contains(possibleX) && x.contains(uminus(possibleX))) {
				// [ x_lo .. -[posX] .. 0 .. [posX] .. x_hi ]
				possibleX = new RealInterval(-possibleX.hi(), possibleX.hi());
			}
		}
		RealInterval newX = intersect(possibleX, x);
		return checkNarrowing(r, x, newR, newX);
	}

	/**
	 * z = x^y,
	 * 
	 * @throws IANarrowingFaildException
	 */
	public static boolean narrowPower(RealInterval r, RealInterval x,
			RealInterval y) throws IANarrowingFaildException {
		RealInterval newR = intersect(power(x, y), r);
		RealInterval newX = intersect(root(r, y), x);
		RealInterval possibleY = div(log(r), log(x));
		RealInterval newY = intersect(possibleY, y);
		return checkNarrowing(x, y, r, newX, newY, newR);
	}
      
/*
public static boolean narrow_semi(
       RealInterval a,RealInterval b,RealInterval c) {
    return false; //true;
  }
  
  public static boolean narrow_colon_equals(
       RealInterval a,RealInterval b,RealInterval c) {
    b.lo = c.lo; b.hi = c.hi;
    return b.nonEmpty();
  }
*/
  
	public static boolean narrowEquals(RealInterval a, RealInterval b)
			throws IANarrowingFaildException {
		RealInterval newAB = intersect(a, b);
		return checkNarrowing(a, b, newAB, newAB);
	}
/*  
  public static boolean 
  narrow_eq(RealInterval a,RealInterval b,RealInterval c) {
    if ((b.lo==b.hi) && b.equals(c)) {
      a.lo = 1.0; a.hi = 1.0; 
      return(true);
    }
    else
      try {
        b.intersect(c);
        c.intersect(b);
        return true;
    } catch (IAException e) {
        return false;
    }
  }
*/
  
 /* x < y */
  /*
  public static boolean narrow_lt(
       RealInterval result,RealInterval x,RealInterval y) {
    try {
       if (y.lo < x.lo) y.lo = x.lo;
       if (x.hi > y.hi) x.hi = y.hi;
       if (y.hi <= x.lo)
         return false;
       else if (x.hi < y.lo) {
         result.lo = 1.0; result.hi=1.0;
       }
       else {
         result.intersect(new RealInterval(0.0,1.0));
       }
       return(x.nonEmpty()&&y.nonEmpty());
    } catch (IAException e) {
      return false;
    }
  }

  public static boolean narrow_le(
       RealInterval r,RealInterval x,RealInterval y) {
    try {
       if (y.lo <= x.lo) y.lo = x.lo;
       if (x.hi >= y.hi) x.hi = y.hi;
       if (y.hi < x.lo)
         return false;
       else if (x.hi <= y.lo) {
         r.lo = 1.0; r.hi=1.0;
       }
       else {
         r.intersect(new RealInterval(0.0,1.0));
       }
       return(x.nonEmpty()&&y.nonEmpty());
    } catch (IAException e) {
      return false;
    }
  }

  public static boolean narrow_gt(
       RealInterval r,RealInterval x,RealInterval y) {
    return narrow_lt(r,y,x);
  }

  public static boolean narrow_ge(
       RealInterval r,RealInterval x,RealInterval y) {
    return narrow_le(r,y,x);
  }

  public static boolean narrow_ne(
       RealInterval r,RealInterval x,RealInterval y) {
    return ((x.lo < x.hi) || (y.lo < y.hi) || (x.lo != y.lo));
  }
*/

}

