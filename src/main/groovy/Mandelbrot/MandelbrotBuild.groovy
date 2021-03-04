package Mandelbrot

import gppBuilder.GPPbuilder
import gppClusterBuilder.CGPPbuilder

def builder = new GPPbuilder()
String fileRoot = "./scripts/"

builder.runBuilder("${fileRoot}MandelbrotGUI")
builder.runBuilder("${fileRoot}MandelbrotNoGUI")
