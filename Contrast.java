/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

// Main class
public class Contrast
{

    public static final short MAX_PIXEL_VALUE = 255;
    public static final short MIN_PIXEL_VALUE = 0;

    private static final String INPUT_FILE_NAME = "input.jpg";
    private static final String OUTPUT_FILE_NAME = "output.jpg";
    private static final String DELTA_STRING = "3";

    // Main method
    public static void main( String args[] ) throws Exception
    {
        double delta;

        // check that the third parameter, delta is a real number at least 1.0
        try
        {
            delta = Double.parseDouble( DELTA_STRING );
        }
        catch ( NumberFormatException e )
        {
            System.out.println(" The last parameter must be a double.");
            return;
        }
        if ( delta < 1.0 )
        {
            System.out.println("The last parameter, delta, must be at least 1.0.");
            return;
        }
        startEnhancement( INPUT_FILE_NAME, OUTPUT_FILE_NAME, delta );
    }

    // Initiate enhancement
    public static boolean startEnhancement( String inputFileName, String outputFileName, double delta) throws Exception
    {
        double[][][] imageData;
        int[] dimimage;
        int[][][] outputData;
        boolean[][] criticalMap;
        double[][] LumImage;
        double[][] MaxLumMap;

        System.gc();
        System.out.println("Reading input file.");

        // Gets the image dimensions : bands, height and width
        dimimage = MyImageReader.imgDimension(inputFileName);
        System.out.println("Dimensions" + dimimage[0] + " " +  dimimage[1] + " " + dimimage[2]);

        // Reads the image file and converts RGB to Lxy representation
        imageData = MyImageReader.convertRGBtoLxy(inputFileName);
        System.out.println("Converted the Image to Lxy notation");

        // Generates the Luminance Image from the Lxy Image
        LumImage = imageData[0];
        
        // translate entire image downwards so that minimum pixel value is 0
        LumImage = ImageUtilities.shifttozero(LumImage);

        // Generates the MaxLuminance Map (essentially instead of a single limiting maximum, each pixel
        // now have a different maximum limit)
        MaxLumMap = MyImageReader.genMaxValMap(inputFileName);
        System.out.println("Generated the Maximum Envelop");
  
        //Run first pass
        System.out.println("Starting first pass.");
        
        //Map critical points at which hillocks are divided
        criticalMap = ImageUtilities.makeCriticalMap( LumImage );
        
        // This is the primary method call.
        // Given a two dimensional array, max and min values, as well as delta,
        // this will return a new image with contrast enhanced.
        LumImage = ContrastEnhancer.enhance( LumImage, criticalMap, MaxLumMap, 0.0, delta );
 
        //Invert the image
        ImageUtilities.invertImage( LumImage, MaxLumMap, 0.0);

        //Run again for inverted image
        System.out.println("Starting second pass.");
        criticalMap = ImageUtilities.makeCriticalMap( LumImage);
        LumImage = ContrastEnhancer.enhance( LumImage, criticalMap, MaxLumMap, 0.0, delta );

        //Invert the image back
        ImageUtilities.invertImage( LumImage, MaxLumMap, 0.0 );

        //Converts Lxy back to RGB
        outputData = MyImageReader.convertLxytoRGB(imageData, dimimage[0], dimimage[1], dimimage[2]);

        System.out.println("Writing output file.");

        //Write output data
        if (!MyImageWriter.writeImage( inputFileName, outputFileName, outputData ))
            return false;

        System.out.println("DONE");

        return true;
    }
}
