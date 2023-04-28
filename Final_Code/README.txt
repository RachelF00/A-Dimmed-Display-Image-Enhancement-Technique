EECE 541: Improving Image Visual Quality on a Dimmed Display
Kevin Jin, Rui Fang, Akhil Rajavaram, Hanxin Feng

The code is developed primarily in MATLAB (R2022b) and calls a Java-based executable. We also use MATLAB's Image Processing Toolbox.
Development was done on a Windows 10 device.

Method1_HumanContrastSensitivity contains the code for the human contrast sensitivity-based image enhancement method. 
Method2_IntensitySaturation contains the code for the intensity saturation-based image enhancement method.
Please refer to the README files in the respective folders for how to run the code.

We use the following third-party code:
https://www.ics.uci.edu/~majumder/contrastcode/codecontrast.html   --->   used to enhance our image in Method 1
https://github.com/scienstanford/iqmetrics/tree/master/utility   --->   used to reduce the brightness of our enhanced images

There are sample images generated from each method in the output folder within each method's directory. 



