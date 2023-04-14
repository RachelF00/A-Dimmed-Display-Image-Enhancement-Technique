function brightness = compute_brightness(img)
% Computes the brightness of the image
img_Lab = rgb2lab(img);
brightness = mean2(img_Lab(:,:,1));
end