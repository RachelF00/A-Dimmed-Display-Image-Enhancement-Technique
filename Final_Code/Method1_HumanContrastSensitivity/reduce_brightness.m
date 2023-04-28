function [img_out] = reduce_brightness(img, value, method)
% Reduces the brightness of an image
% img - the image to modify
% value - the amount to decrease the brightness by
% method - how to reduce ('subtract' or 'multiply')
    img_Lab = rgb2lab(img);
    switch method
        case 'subtract'
            img_Lab(:,:,1) = img_Lab(:,:,1) - value;
            img_out = lab2rgb(img_Lab);
        case 'multiply'
            img_Lab(:,:,1) = round(img_Lab(:,:,1).*value);
            img_out = lab2rgb(img_Lab);
    end
end