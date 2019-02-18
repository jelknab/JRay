<h1>Jray</h1>
<span>A distributed java raytracer</span>

<h2>About</h2>
<p>
This is a hobby project which I've been working on and off on for about 2 years. <br>
The code has been refactored many times as I learn more about java.

The cool thing about this raytracer is that you can run it on multiple machines to speed up the render, I've tried to keep the code as readable
as possible without sacrificing to much speed, but this ray tracer is not about speed and efficientcy -perhaps ill focus
on that in a later stadium-.

I've decided to host it on Github for my portfolio.
</p>

![render of latest version](https://i.imgur.com/SdUB1Ro.jpg)

<h2>To-do list</h2>
<ol>
    <li>Add model loading (.obj)</li>
    <li>Add more materials and textures</li>
    <li>Add support for sequence rendering</li>
    <li>Add more light types</li>
    <li>Add global illumination</li>
    <li>optimisation tactics</li>
</ol>

<h2>Trying it yourself</h2>
<p>
Right now it's simple. Simply clone the project, edit the scene and settings in scenes/testSettings.xml if you like.<br><br>
Build with maven; Run with args:<br>
<code>-h -s scenes/testSettings.xml -p 9090 -a localhost:9090</code>
</p>