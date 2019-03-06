<h1>Jray</h1>
<span>A distributed java raytracer</span>

<h2>About</h2>
<p>
This is a hobby project which I've been working on and off on for about 2 years. <br>
The code has been refactored many times as I learn more about java.

The cool thing about this raytracer is that you can run it on multiple machines to speed up the render, I've tried to keep the code as readable
as possible without sacrificing to much speed, but this ray tracer is not about speed and efficiency -perhaps ill focus on that in a later stadium-.

I've decided to host it on Github for my portfolio.
</p>

![Interesting renders](https://i.imgur.com/SdUB1Ro.jpg)
![Interesting renders](https://i.imgur.com/HZTOO48.jpg)
![Interesting renders](https://i.imgur.com/mCnuI1X.jpg)

<h2>Features</h2>
<ul>
    <li>Lighting
        <ul>
            <li>Point light</li>
            <li>Multiple light sources</li>
        </ul>
    </li>
    <li>Renderable object's
        <ul>
            <li>Infinite plane</li>
            <li>Sphere</li>
            <li>.OBJ model</li>
        </ul>
    </li>
    <li>Materials and textures
        <ul>
            <li>Lambert</li>
            <li>Specular</li>
            <li>Phong (beta)</li>
            <li>Checkerboard pattern texture</li>
        </ul>
    </li>
    <li>Misc
        <ul>
            <li>KD-Tree for mesh's</li>
        </ul>
    </li>
</ul>

<h2>To-do list</h2>
<ol>
    <li>Add support for model rotations</li>
    <li>Add more materials and textures</li>
    <li>Add support for sequence rendering</li>
    <li>Add more light types</li>
    <li>Add global illumination</li>
    <li>optimisation tactics</li>
</ol>

<h2>Trying it yourself</h2>
<p>
Right now it's simple. Simply clone the project, edit the sceneSettings and settingsXML in scenes/testSettings.xml if 
you like.<br><strong>Make sure you have the .obj models stored in <code>~/JRay/models</code></strong><br><br>
Build with maven; Run with args:<br>
<code>-h -s scenes/testSettings.xml -p 9090 -a localhost:9090</code>
</p>