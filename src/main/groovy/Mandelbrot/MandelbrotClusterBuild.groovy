package Mandelbrot

import gpp_cluster_builder.CGPPbuilder

def builder = new CGPPbuilder()
String fileRoot = "./scripts/"

builder.runClusterBuilder("${fileRoot}ClusterMandelbrot1")
//builder.runClusterBuilder("${fileRoot}ClusterMandelbrot2")
builder.runClusterBuilder("${fileRoot}ClusterGUIMandelbrot1")
//builder.runClusterBuilder("${fileRoot}EvaluateMandelbrot_152_1")
//builder.runClusterBuilder("${fileRoot}EvaluateMandelbrot_152_2")
//builder.runClusterBuilder("${fileRoot}EvaluateMandelbrot_152_3")
//builder.runClusterBuilder("${fileRoot}EvaluateMandelbrot_152_4")
//builder.runClusterBuilder("${fileRoot}EvaluateMandelbrot_152_5")

