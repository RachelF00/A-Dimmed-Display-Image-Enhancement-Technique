/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

import java.util.Stack;

public class ContrastEnhancer
{

    /* This is the primary method call.
     * Given a two dimensional array, max and min values, as well as delta,
     * this will return a new image with contrast enhanced.
     */
    public static double[][] enhance(double[][] imageData, boolean[][] criticalMap,
                                    double[][] maxValueMap, double minValue, double delta )
    {
        Stack hillockStack = new Stack();

        // create a new hillock consisting of entire image and push it onto the stack
        hillockStack.push( new Hillock( imageData, criticalMap, maxValueMap, delta ) );

        // keep processing hillocks until there are non left
        while ( !hillockStack.isEmpty() )
        {
        	//pop a hillock
            Hillock nextHillock = (Hillock)hillockStack.pop();
            
            Hillock[] newHillocks = nextHillock.processHillock();
            for ( int i = 0; i < newHillocks.length; i++ )
            	
            	//push a hillock 
                if ( newHillocks[i] != null )
                    hillockStack.push( newHillocks[i] );
            System.out.println("Number of hillocks is " + hillockStack.size());
        }
        System.out.println("END Contrast Enhancer");
        return imageData;
    }
}
