function brightness = compute_brightness(img)
% Computes the brightness of the image
% Input: image (2D matrix)
% Output: the mean brightness of the image in the Lab color space
img_Lab = rgb2lab(img);
brightness = mean2(img_Lab(:,:,1));
end