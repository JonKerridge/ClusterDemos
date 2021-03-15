package Mandelbrot

import gpp_builder.GPPbuilder

def builder = new GPPbuilder()
String fileRoot = "./scripts/"

builder.runBuilder("${fileRoot}MandelbrotGUI")
builder.runBuilder("${fileRoot}MandelbrotNoGUI")
