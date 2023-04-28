function brightness = compute_brightness(img)
% Computes the brightness of the image
% Input: Image (2D matrix)
% Output: mean brightness of the image in the Lab color space
img_Lab = rgb2lab(img);
brightness = mean2(img_Lab(:,:,1));
end