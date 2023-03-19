/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

/*
 * This class contains all the pixels for a hillock. Most of the operations
 * reside here because the primary operation is to raise the threshold value
 * and compute new connected components (i.e. hillocks) from the subset of pixels
 * whose value is at least as high as the threshold.
 */
public class Hillock
{
    final int UNMARKED = -1;
    final int BELOW_THRESHOLD = -2;
    final double MIN_STEP = .2;    // The minimum step between consecutive thresholds
    final int INIT_NUM_COMPONENTS = 10;
    final int MAX_NUM_NEIGHBORS = 4;

    int numPixels;
    double maxStretchRatio;
    int[] xCoords, yCoords;
    int[][] neighbors; // Each vertex has an array for four pixels, denoting its N, E, S, W neighbor.
                       // If the corresponding neighbor is not in the hillock, the value is -1
                       // otherwise, the value indicates the index of the neighbor
    int[] marked;
    double threshold;
    double stretched; // The amount by which the hillock has already been stretched
    double[][] imageData;
    boolean[][] criticalMap; // 2D array of booleans. Same dimension as the image.
                            // indicates whether the pixel is a critical value
    double[][] saturationValue;    // the maximum value any pixel can be
    double delta;           // the maximum stretch that can be achieved

    // this constructor is used for all hillocks, except the first.
    // pixels and edges are added to the image one at a time from the old hillock
    public Hillock( int size, double[][] inputImageData, boolean[][] crit, double[][] sat, double del, double oldStretched )
    {
        numPixels = size;
        imageData = inputImageData;
        criticalMap = crit;
        saturationValue = sat;
        stretched = oldStretched;
        delta = del;
        xCoords = new int[ numPixels ];
        yCoords = new int[ numPixels ];
        marked = new int[ numPixels ];

        neighbors = new int[ numPixels ][ MAX_NUM_NEIGHBORS ];
        for ( int i = 0; i < numPixels; i++ )
            for ( int j = 0; j < MAX_NUM_NEIGHBORS; j++ )
                neighbors[i][j] = -1;
    }

    // this constructor is only used the first time a hillock is created
    // and makes a hillock consisting of the entire image.
    public Hillock( double[][] inputImageData, boolean[][] crit, double[][] sat, double del  )
    {
        //double[] minmax;
        int height = inputImageData.length;
        int width = inputImageData[0].length;
        numPixels = height * width;
        xCoords = new int[ numPixels ];
        yCoords = new int[ numPixels ];
        marked = new int[ numPixels ];

        // initialize neighbors[i][j] = -1
        neighbors = new int[ numPixels ][ MAX_NUM_NEIGHBORS ];
        for ( int i = 0; i < numPixels; i++ )
            for ( int j = 0; j < MAX_NUM_NEIGHBORS; j++ )
                neighbors[i][j] = -1;

        int index = 0;
        for ( int y = 0; y < height; y++ )
            for ( int x = 0; x < width; x++ )
                {
                    xCoords[ index ] = x;
                    yCoords[ index ] = y;
                    // Initialize north neighbor
                    if ( y > 0 )
                        neighbors[ index ][0] = (index - width);
                    // Initialize east neighbor
                    if ( y < height-1 )
                        neighbors[ index ][2] = (index + width);
                    // Initialize south neighbor
                    if ( x > 0 )
                        neighbors[ index ][3] = (index - 1);
                    // Initialize west neighbor
                    if ( x < width-1 )
                        neighbors[ index ][1] = (index + 1);

                    index++;
                }

        threshold = 0;
        stretched = 1.0;
        delta = del;
        saturationValue = sat;
        criticalMap = crit;
        imageData = inputImageData;
        //minmax = ImageUtilities.findminmax(imageData);
        //System.out.println("HILLOCK: Min and Max of Hillock is " + minmax[0] + " and " + minmax[1]);
        setMaxRatio();
        stretch( threshold );
        setMaxRatio();
    }

    //This method is used for adding pixel location
    public void addPixel( int index, int x, int y )
    {
        xCoords[index] = x;
        yCoords[index] = y;
    }

    //Add edges to a certain direction
    public void addEdge( int fromIndex, int toIndex, int edgeIndex )
    {
        neighbors[fromIndex][edgeIndex] = (int)toIndex;
    }
        
    //Search and set max ratio
    public double setMaxRatio()
    {
        maxStretchRatio = Double.MAX_VALUE;
        double pixelRatio;

        for ( int i = 0; i < numPixels; i++ )
        {
            pixelRatio = (saturationValue[yCoords[i]][xCoords[i]] - threshold)/(imageData[yCoords[i]][xCoords[i]] - threshold);
           
            if ( pixelRatio < maxStretchRatio )
            {
                maxStretchRatio = pixelRatio;
            }
        }
        return maxStretchRatio;
    }

    // count number at or above threshold.
    // mark those pixels that are below threshold
    public int findTarget()
    {
        int target = 0;
        double pixelValue;

        for ( int i = 0; i < numPixels; i++ )
        {
            pixelValue = imageData[ yCoords[i] ][ xCoords[i] ];
            if ( pixelValue >= threshold )
                target++;
            else
                marked[i] = BELOW_THRESHOLD;
        }
        return target;
    }

    //Clear all marks
    public void clearMarks()
    {
        for ( int i = 0; i < numPixels; i++ )
            marked[i] = UNMARKED;
    }

    // Find the first unmarked pixel
    public int findFirstUnmarked()
    {
        int index = 0;
        for ( index = 0; index < numPixels; index++ )
            if ( marked[ index ] == UNMARKED )
                return index;
        System.out.println("There is no unmarked pixel.");
        return -1;
    }

    // performs DFS until all pixels above the treshold have been found.
    // returns an array with the sizes of the different components
    public int[] findComponents()
    {
        clearMarks();
        int componentSizes[] = new int[ INIT_NUM_COMPONENTS ];

        // find out how many pixels are at or above the threshold.
        // this will be the sum of the sizes of the resulting hillocks
        // this also marks pixels that are below threshold in "marked" array
        int target = findTarget();

        int numComponents = 0;
        int totalReached = 0;
        int numReached = 0;

        while ( totalReached < target )
        {
            int start = findFirstUnmarked();
            if ( start < 0 )
            {
                System.out.println("There should be an unmarked node.");
                return null;
            }
            // DFS returns the number of pixels reached from the start vertex
            numReached = DFS( start, numComponents );
            totalReached += numReached;

            numComponents++;

            // we don't know in advance the number of components, so may need to make room for more
            if ( numComponents > componentSizes.length )
                componentSizes = resizeComponentSizes( componentSizes );
            // will need to know the size of each component in order to create a new hillock for it
            componentSizes[ numComponents-1 ] = numReached;
        }
        System.out.println(" Number of components is " + numComponents );
        return componentSizes;
    }

    // increase size of array by a factor of 2
    public int[] resizeComponentSizes( int[] sizes )
    {
        int[] newSizes = new int[ sizes.length * 2 ];
        for ( int i = 0; i < sizes.length; i++ )
            newSizes[i] = sizes[i];
        return newSizes;
    }

    /*
     * The main routine.
     * Find pixels above the threshold. Compute the connected components.
     * If there is more than one component, create a new hillock for each component.
     * If there is only one component return "this".
     */
    public Hillock[] processHillock()
    {
        // find the components
        // the new component for each pixel is stored in the "marked" array
        // componentSizes indicates the number of pixels in each component
        int[] componentSizes = findComponents();
        int numComponents = 0;

        // find number of Components.
        while ( numComponents < componentSizes.length && componentSizes[ numComponents ] > 0 )
            numComponents++;

        Hillock[] hillockList = new Hillock[ numComponents ];

        if (numComponents == 0)
            return hillockList;

        // If there are no new connected components, the hillock is still stretched to the
        // saturation level. Compute the new threshold value and return the same hillock.
        if ( numComponents == 1 )
        {
            hillockList[0] = this;
            hillockList[0].setNewThreshold( threshold );
            return hillockList;
        }

        // create a new hillock for each component
        for ( int i = 0; i < numComponents; i++ )
            hillockList[i] = new Hillock( componentSizes[i], imageData, criticalMap, saturationValue, delta, stretched );
        // For each pixel, we will need to know its new index in the new hillock
        int[] newLocation = new int[ numPixels ];
        // This keeps track of how many pixels have been already added to each hillock
        int[] currentLocation = new int[ numComponents ];

        int component, neighborIndex;
        // Go through each pixel and add it to its new hillock
        for ( int i = 0; i < numPixels; i++ )
        {
            if ( marked[i] != BELOW_THRESHOLD )
            {
                component = marked[i];
                hillockList[ component ].addPixel( currentLocation[ component ], xCoords[i], yCoords[i] );
                newLocation[i] = (int)currentLocation[ component ];
                // add edges incident to the i-th pixel if the other endpoint has already been added
                for ( int j = 0; j < MAX_NUM_NEIGHBORS; j++ )
                {
                    // find index of the neighbor in the old hillock
                    neighborIndex = neighbors[i][j];
                    // if the neighbor is above the threshold, in the same component and has already been
                    // added to the new hillock, add the edge
                    if ( neighborIndex >= 0 && marked[neighborIndex] == component && neighborIndex < i )
                    {
                        int endPoint1 = newLocation[ i ];
                        int endPoint2 = newLocation[ neighborIndex ];
                        // need to add edges in each direction
                        hillockList[ component ].addEdge( endPoint1, endPoint2, j );
                        hillockList[ component ].addEdge( endPoint2, endPoint1, (j+2)%4 );
                    }
                }
                currentLocation[ component ]++;
            }
        }

        // now stretch each new hillock and set new threshold
        for ( int i = 0; i < numComponents; i++ )
        {
            hillockList[i].setMaxRatio();
            // if a stretch of delta is achieved, there is no need to process further
            if ( hillockList[i].stretch( threshold ) )
                hillockList[i] = null;
            else
            {
                hillockList[i].setMaxRatio();
                hillockList[i].setNewThreshold( threshold );
            }
        }
        return hillockList;
    }

    // returns true of a stretch of delta is achieved.
    // returns false if saturation level is reached.
    public boolean stretch( double oldThreshold )
    {
        double minstretch = Math.min(delta/stretched, maxStretchRatio);

        // potentialMax is the maximum value that would be achieved if we stretched a factor of delta
        int y,x;
        double pixelValue;
        // in this case, we can stretch the hillock by delta and will be able to stretch no further.
        for ( int i = 0; i < numPixels; i++ )
            {
                y = yCoords[i];
                x = xCoords[i];
                pixelValue = imageData[y][x];
                imageData[y][x] = (oldThreshold + (pixelValue - oldThreshold)*minstretch);
                if (imageData[y][x] > saturationValue[y][x])
                    imageData[y][x] = saturationValue[y][x];
            }
        if ((delta/stretched) > maxStretchRatio) return false;
        else return true;
    }

    public void setNewThreshold( double oldThreshold )
    {
        double minCriticalValue = Double.MAX_VALUE;

        // find lowest critical point that is above the threshold
        for ( int i = 0; i < numPixels; i++ )
        {
            int x = xCoords[i], y = yCoords[i];
            if( criticalMap[y][x] )
            {
                if ( imageData[y][x] < minCriticalValue && imageData[y][x] > threshold )
                    minCriticalValue = imageData[y][x];
            }
        }

        // if there is no critical point above the threshold, just increment by MIN_STEP
        if ( minCriticalValue == Double.MAX_VALUE )
            threshold = oldThreshold + MIN_STEP;
        else
            threshold = Math.max( oldThreshold + MIN_STEP, minCriticalValue );

        return;
    }

    // Depth first search, starting at pixel start.
    // Each pixel that is reached is marked as being in "component".
    public int DFS( int start, int component )
    {
        StackOfInts nodeStack = new StackOfInts();
        // this stack stores a number from 0 to 3 and indicates the next edge out of the
        // node to be traversed
        StackOfInts edgeStack = new StackOfInts();

        nodeStack.push(start);
        edgeStack.push(0);

        int numReached = 1;
        marked[start] = component;

        while( !nodeStack.isEmpty() )
        {
            int topNode = nodeStack.pop();
            int topEdge = edgeStack.pop();

            // if there are edges out of the node that have not been traveresed...
            if ( topEdge < MAX_NUM_NEIGHBORS )
            {
                // put topNode back and go on to next edge.
                nodeStack.push( topNode );
                edgeStack.push( (topEdge + 1) );
                // if the edge to be traversed leads to an unmarked node,
                // mark it and push it on the stack
                int neighborIndex = neighbors[ topNode ][ topEdge ];
                if ( neighborIndex >= 0 && marked[ neighborIndex ]==UNMARKED )
                {
                    marked[neighborIndex] = component;
                    numReached++;
                    nodeStack.push( neighborIndex );
                    edgeStack.push( 0 );
                }
            }
        }
        return numReached;
    }
}