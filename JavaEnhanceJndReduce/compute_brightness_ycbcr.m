function brightness = compute_brightness_ycbcr(img)
% Computes the brightness of the image
img_YCbCr = rgb2ycbcr(img);
brightness = mean2(img_YCbCr(:,:,1));
end