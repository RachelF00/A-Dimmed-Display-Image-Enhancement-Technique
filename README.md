# A Dimmed Display Image Enhancement Technique

This project aims to introduce a method to enhance the display quality of dimmed images using CSF and JND. It is implemented in Java and Matlab. This resulted in a conference paper published in IEEE ICCE 2024.

[A Dimmed Display Image Enhancement Technique for Energy Conservation, IEEE International Conference on Consumer Electronics, 2024, Las Vegas, USA](https://ieeexplore.ieee.org/document/10444396)

## Abstract

Lowering the brightness of digital displays as a means to reduce power consumption and extend battery life is a widely adopted strategy. However, this course of action inevitably results in decreased image contrast and a negative influence on the overall image quality. In this paper, we propose a method to enhance the visual quality of dimmed displays while keeping the overall brightness and power consumption unchanged. First, we amplify the contrast and brightness of each frame utilizing the human contrast sensitivity function (CSF). Subsequently, we introduce a technique for reducing brightness based on the concept of the just noticeable difference (JND), ensuring that the average brightness remains at the level of the initially dimmed frame, thus aligning with the targeted power consumption. Our subjective evaluation indicated that the integration of our CSF based enhancement method and our proposed JND based brightness reduction method yield superior visual quality, while preserving the mean brightness of the original image.

Pictures below demenstrate part of our outcomes. The left pictures are output of our models while the right ones are original ones.
![Method 1](/result1.jpg)
![Method 2](/result2.jpg)
