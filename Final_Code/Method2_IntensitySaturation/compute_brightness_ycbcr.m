function brightness = compute_brightness_ycbcr(img)
% Computes the brightness of the image in the YCbCr color space
% Input: image (2D matrix)
% Output: mean brightness of the image in YCbCr color space
img_YCbCr = rgb2ycbcr(img);
brightness = mean2(img_YCbCr(:,:,1));
end