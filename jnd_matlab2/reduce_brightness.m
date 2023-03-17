function [img_out] = reduce_brightness(img, value, method)
% Reduces the brightness of an image
% img - the image to modify
% value - the amount to decrease the brightness by
% method - how to reduce ('subtract' or 'multiply')
img_YCbCr = rgb2ycbcr(img);
switch method
    case 'subtract'
        img_YCbCr(:,:,1) = img_YCbCr(:,:,1) - value;
        img_out = ycbcr2rgb(img_YCbCr);
    case 'multiply'
        img_YCbCr(:,:,1) = round(img_YCbCr(:,:,1).*value);
        img_out = ycbcr2rgb(img_YCbCr);
end
end