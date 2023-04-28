Run main.mlx.

This folder contains the human contrast sensitivity-based image enhancement method. We use the Java code from the paper 
"Contrast Enhancement of Images using Human Contrast Sensitivity" by Aditi Majumder, Sandy Irani
to enhance our images. We use a JND-based brightness reduction to constrain the output image brightness. The Java code has not been modified.
The JND pixel-level thresholds were computed using code from: https://github.com/scienstanford/iqmetrics/tree/master/utility. A troublesome section of the code that caused issues for high-resolution images has been commented out.

The input folder contains the reference images.
The tmp folder stores an intermediate result, which is cleared afterwards.
The output folder contains the final results from running the algorithm on the input image. We have left sample output images at 3 different brightness levels for reference.
- The numbers in the file name denote the brightness in the Lab colour space (L channel)
- "input" is the original input image, dimmed by a factor
- "output" is the final output of the algorithm (after applying brightness reduction using JND)

- "uniform" is a baseline output (using a uniform brightness reduction, not considering JND) (this part of the code has been commented out)
- "jnd_adjust" is the adjustment (elementwise multiplication) that was applied to the enhanced image to produce the output (this part of the code has been commented out)
- "jnd_raw" is the raw JND thresholds computed on the enhanced image (this part of the code has been commented out)
