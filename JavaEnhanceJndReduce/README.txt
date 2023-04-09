Run main.mlx.

The input folder contains the reference images.
The tmp folder stores an intermediate result, which is cleared afterwards.
The output folder contains the final results from running the algorithm on the input image. 
- The numbers in the file name denote the brightness in the Lab colour space (L channel)
- "input" is the original input image, dimmed by a factor
- "enchanced" is the output of the Java code using the dimmed image
- "output" is the final output of the algorithm (after applying brightness reduction using JND)
- "uniform" is a baseline output (using a uniform brightness reduction, not considering JND)
- "jnd_adjust" is the adjustment (elementwise multiplication) that was applied to the enhanced image to produce the output
- "jnd_raw" is the raw JND thresholds computed on the enhanced image
