import cv2 as cv
import numpy as np

img = cv.imread('image.jpg')

img_gray = cv.cvtColor(img, cv.COLOR_BGR2GRAY)

avg_brightness = np.mean(img_gray)

# Convert the image to Lab color space
img_lab = cv.cvtColor(img, cv.COLOR_BGR2LAB)

# Calculate the JND threshold using the Watson's model
jnd_map = cv.ximgproc.jnd.fwdJndWatson(img_lab)

# Apply a filter to smooth out areas where the JND threshold is exceeded
filtered_img = cv.ximgproc.jndFilter(img, jnd_map)

# Enhance image details in areas where the JND threshold is high
enhanced_img = cv.ximgproc.jndEnhance(img, jnd_map)

# Convert the enhanced image to grayscale
enhanced_gray = cv.cvtColor(enhanced_img, cv.COLOR_BGR2GRAY)

# Calculate the average brightness of the enhanced grayscale image
enhanced_brightness = np.mean(enhanced_gray)

# Adjust the brightness of the enhanced grayscale image to match that of the input grayscale image
adjusted_gray = enhanced_gray * (avg_brightness / enhanced_brightness)

# Convert the adjusted grayscale image back to BGR color space
adjusted_img = cv.cvtColor(adjusted_gray, cv.COLOR_GRAY2BGR)

cv.imshow('Original Image', img)
cv.putText(img, f"Brightness: {avg_brightness:.2f}", (10, img.shape[0]-10), cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
cv.imshow('Filtered Image', filtered_img)
cv.putText(adjusted_img, f"Brightness: {avg_brightness:.2f}", (10, adjusted_img.shape[0]-10), cv.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
cv.waitKey(0)
cv.destroyAllWindows()
