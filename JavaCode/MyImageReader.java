/* 
 * authors: Aditi Majumder and Sandy Irani
 * University of California, Irvine
 * Last update: 07/11/08
 */

import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.*;

//Read image data from an input file
public class MyImageReader
{
	//read image data from an input file into buffer
    public static BufferedImage readImageIntoBufferedImage(String fileName)
    {
        BufferedImage image = null;

        //Check that the file name indicates we have a jpg file
        if ( !fileName.endsWith(".jpg") )
        {
            System.out.println("This is not a jpg file.");
            return null;
        }

        try {
            // Read from a file
            File file = new File(fileName);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.out.println("Could not open file.");
            return null;
        }
        return image;
    }

    // This method gives the number of bands (number of color channels) in for an input image
    public static int[] imgDimension(String fileName){
        BufferedImage image = null;
        int[] stat = new int[3];

        image = readImageIntoBufferedImage(fileName);
        WritableRaster raster = image.getRaster();

        stat[0] = raster.getNumBands();
        stat[1] = raster.getHeight();
        stat[2] = raster.getWidth();

        return stat;
    } 

    // This method converts an RGB image to Lxy format where L=X+Y+Z, x = X/(X+Y+Z) and y = Y/(X+Y+Z)
    public static double[][][] convertRGBtoLxy(String fileName )
    {
        BufferedImage image = null;
        int height, width, numbands;

        image = readImageIntoBufferedImage( fileName );

        WritableRaster raster = image.getRaster();

        int[] pixelValues = new int[ raster.getNumBands() ];
        height = raster.getHeight();
        width = raster.getWidth();
        numbands = raster.getNumBands();

        if (numbands < 3)
        {
            System.out.println("Cannot be converted: Not an RGB image");
            return null;
        }

        //allocate our two dimensional array.
        double rasterValues[][][] = new double[numbands][height][width];

        for ( int x = 0; x < width; x++ )
            for( int y = 0; y <height; y++ )
            {
                pixelValues = raster.getPixel( x, y, pixelValues);

                // Convert RGB pixelValues to CIE XYZ values
                double CIEX = (0.412453*pixelValues[0] + 0.357580*pixelValues[1] + 0.180423*pixelValues[2])/255.0;
                double CIEY = (0.212671*pixelValues[0] + 0.715160*pixelValues[1] + 0.072169*pixelValues[2])/255.0;
                double CIEZ = (0.019334*pixelValues[0] + 0.119193*pixelValues[1] + 0.950227*pixelValues[2])/255.0;

                rasterValues[0][y][x] = CIEX+CIEY+CIEZ;
                rasterValues[1][y][x] = CIEX/rasterValues[0][y][x];
                rasterValues[2][y][x] = CIEY/rasterValues[0][y][x];
            }

        return rasterValues;
    }

    // This function calculates the intersection of each color pixel ray with the cubical gamut.
    // This decides the maximum value the luminance at that pixel can take.
    // Hence, in this case we have a map of maxvals instead of a single one as is the case with grayscale images.
    public static double[][] genMaxValMap(String fileName) throws Exception
    {
        BufferedImage image = null;
        int height, width, band, numbands;

        image = readImageIntoBufferedImage( fileName );

        WritableRaster raster = image.getRaster();

        numbands = raster.getNumBands();
        int[] pixelValues = new int[numbands];
        double[] scaleValues = new double [numbands];
        height = raster.getHeight();
        width = raster.getWidth();
        
        if (numbands < 3)
        {
            System.out.println("Cannot be extracted: Not an RGB image");
            return null;
        }

        //allocate our two dimensional array.
        double LimitMap[][] = new double[height][width];

        // To find the ray cube intersection, find the maximum of RGB (normalized values).
        // Then find the scale factors that takes this maximum value to 1.
        // Scale the RGB color by this scale factor
        for ( int x = 0; x < width; x++ )
            for( int y = 0; y <height; y++ )
            {
                pixelValues = raster.getPixel( x, y, pixelValues);

                double CIEXpix = (0.412453*pixelValues[0] + 0.357580*pixelValues[1] + 0.180423*pixelValues[2])/255.0;
                double CIEYpix = (0.212671*pixelValues[0] + 0.715160*pixelValues[1] + 0.072169*pixelValues[2])/255.0;
                double CIEZpix = (0.019334*pixelValues[0] + 0.119193*pixelValues[1] + 0.950227*pixelValues[2])/255.0;
                
                double pixlum = CIEXpix+CIEYpix+CIEZpix;
                //System.out.println("At  ( " + y + " , " + x + " ) " + " luminance = " + pixlum);
                
                // Find scale factors to reach 1
                for (band = 0; band < numbands; band++)
                    scaleValues[band] = 255.0/pixelValues[band];

                // Find the minimum scale factor
                double minscale = scaleValues[0];
                if (scaleValues[1] < minscale)
                    minscale = scaleValues[1];
                if (scaleValues[2] < minscale)
                    minscale = scaleValues[2];

                // Convert RGB normValues to CIE XYZ values
                double CIEX = CIEXpix * minscale;
                double CIEY = CIEYpix * minscale;
                double CIEZ = CIEZpix * minscale;

                LimitMap[y][x] = CIEX+CIEY+CIEZ;
                //System.out.println("At  ( " + y + " , " + x + " ) " + " saturation = " + LimitMap[y][x]);
                if ( LimitMap[y][x] < pixlum )
                {
                    System.out.println("Saturation is lower than Pixel Value");
                }
            }
        return LimitMap;
    }

       // This method converts from Lxy to RGB
        public static int[][][] convertLxytoRGB(double[][][] LxyValues, int numbands, int ImgHeight, int ImgWidth )
        {
            int band;
            double[] pixelValues = new double[numbands];
            //allocate space for the RGB image
            int rasterValues[][][] = new int[numbands][ImgHeight][ImgWidth];

            for ( int x = 0; x < ImgWidth; x++ )
                for( int y = 0; y <ImgHeight; y++ )
                {
                    for (band=0;band < numbands; band++)
                        pixelValues[band] = LxyValues[band][y][x];

                    // Convert Lxy pixelValues to CIE XYZ values
                    double CIEX = pixelValues[0]*pixelValues[1];
                    double CIEY = pixelValues[0]*pixelValues[2];
                    double CIEZ = pixelValues[0] - CIEX - CIEY;

                    // Convert XYZ Values to RGB
                    double R = (3.240479*CIEX - 1.537150*CIEY - 0.498535*CIEZ)*255;
                    double G = (-0.969256*CIEX + 1.875992*CIEY + 0.041556*CIEZ)*255;
                    double B = (0.055648*CIEX - 0.204043*CIEY + 1.057311*CIEZ)*255;

                    rasterValues[0][y][x] = (int)R;
                    rasterValues[1][y][x] = (int)G;
                    rasterValues[2][y][x] = (int)B;
                }
            return rasterValues;
        }
}
