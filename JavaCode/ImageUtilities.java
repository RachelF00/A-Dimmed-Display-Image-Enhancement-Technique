/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

//Some useful methods to operate on images.
public class ImageUtilities
{
    /*
     * This methods takes in a 2D array representing an image
     * and returns a 2D array of booleans with the same dimensions.
     * The booleans indicate whether a pixel is a critical point.
     * Right now, only minima are identified as critical points.
     */
    public static boolean[][] makeCriticalMap( double[][] imageData )
    {
        
        int height = imageData.length;
        int width = imageData[0].length;
        
        boolean criticalMap[][] = new boolean[height][width];

        boolean test;
        for ( int y = 0; y < height; y++ )
        	for ( int x = 0; x < width; x++ )
            {
                test = true;
                for ( int xInc = -1; xInc <= 1 && test; xInc++ )
                    for ( int yInc = -1; yInc <=1 && test; yInc++ )
                        if ( xInc+x >= 0 && xInc+x < width &&
                             yInc+y >= 0 && yInc+y < height &&
                             imageData[y+yInc][x+xInc] < imageData[y][x] )
                             test = false;
                criticalMap[y][x] = test;
               }

          return( criticalMap );
    }

    // This method inverts each individual pixel within the [minValue,maxValue] range
    public static double[][] invertImage( double[][] imageData, double[][] maxValueMap, double minValue )
    {

        int height = imageData.length;
        int width = imageData[0].length;

        for ( int y = 0; y < height; y++ )
            for ( int x = 0; x < width; x++ )
            {
                imageData[y][x] = ( maxValueMap[y][x] - imageData[y][x] + minValue );
            }
        return imageData;
    }
    
    // translate entire image downwards so that minimum pixel value is 0
    public static double[][] shifttozero(double[][] imageData)
    {
        double minPixelValue = Double.MAX_VALUE;
        int height = imageData.length;
        int width = imageData[0].length;
        
        // find minimum pixel value
        for ( int y = 0; y < height; y++ )
            for ( int x = 0; x < width; x++ )
                if ( imageData[y][x] < minPixelValue )
                    minPixelValue = imageData[y][x];

        // translate entire image downwards so that min = 0
        for ( int y = 0; y < height; y++ )
            for ( int x = 0; x < width; x++ )
                imageData[y][x] = imageData[y][x] - minPixelValue;
               
        return imageData;
    }              
}