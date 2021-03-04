package Mandelbrot.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import Mandelbrot.data.Mdata
import Mandelbrot.data.Mgui
import groovyParallelPatterns.connectors.reducers.AnyFanOne
import groovyParallelPatterns.connectors.spreaders.OneFanAny
import groovyParallelPatterns.functionals.groups.AnyGroupAny
import groovyParallelPatterns.terminals.CollectUI
import groovyParallelPatterns.terminals.Emit
import groovyParallelPatterns.*
 
import java.awt.*
 

int workers
int maxIterations
int width                  //1400   700        350
int height                 //800    400        200
 
if (args.size() == 0){
workers = 16
maxIterations = 100
width = 350
height = 200
}
else {
// assumed to be running via runDemo
//    String folder = args[0] not required
workers = Integer.parseInt(args[1])
maxIterations = Integer.parseInt(args[2])
width = Integer.parseInt(args[3])
height = Integer.parseInt(args[4])
}
 
print "GUI, $width, $height, $maxIterations, $workers, "
System.gc()
 
long startTime = System.currentTimeMillis()
 
def emitDetails = new DataDetails(dName: Mdata.getName(),
dInitMethod: Mdata.initialiseClass,
dInitData: [width, maxIterations],
dCreateMethod: Mdata.createInstance)
 
def guiDetails = new ResultDetails( rName: Mgui.getName(),
rInitMethod: Mgui.init,
rInitData: [width, height, Color.WHITE],
rCollectMethod : Mgui.updateDisplay,
rFinaliseMethod : Mgui.finalise )
 
 

//NETWORK

def chan1 = Channel.one2one()
def chan2 = Channel.one2any()
def chan3 = Channel.any2any()
def chan4 = Channel.one2one()

def emit = new Emit(
    // input channel not required
    output: chan1.out(),
    eDetails: emitDetails)
 
def spread = new OneFanAny(
    input: chan1.in(),
    outputAny: chan2.out(),
    destinations: workers)
 
def group = new AnyGroupAny (
    inputAny: chan2.in(),
    outputAny: chan3.out(),
    function: Mdata.calculate,
    workers: workers)
 
def reduce = new AnyFanOne (
    inputAny: chan3.in(),
    output: chan4.out(),
    sources: workers)
 
def gui = new CollectUI(
    input: chan4.in(),
    // no output channel required
    guiDetails: guiDetails)

PAR network = new PAR()
 network = new PAR([emit , spread , group , reduce , gui ])
 network.run()
 network.removeAllProcesses()
//END

 
 
long endTime = System.currentTimeMillis()
println " ${endTime - startTime}"
