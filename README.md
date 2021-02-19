# Stegsolve
Stegsolve is a stegano solver for challenges.

It provides these main functions:
- A quick view of different bit planes and some simple transformations.
- Data extraction from planes. This can be row order or column order, with bits treated as a bitstream and converted into bytes.
- Some simple checking of file formats and reporting on the filesize, additional bytes, file holes, etc. This is highly dependent upon the type of image.
- Stereogram solver - simply change the offset until the image becomes visible.
- Frame browser for animated images. This should also work for viewing layers in multi-layered PNG files.
- Image combiner to combine two images in a variety of ways and browse through the different combinations.

It was created by Caesum originally, and you can find the original version [here](http://www.caesum.com/handbook/Stegsolve.jar).
## License
[MIT](https://choosealicense.com/licenses/mit/)
