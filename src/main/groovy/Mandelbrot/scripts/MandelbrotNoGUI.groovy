package Mandelbrot.scripts

import jcsp.lang.*
import groovyJCSP.*
 
import Mandelbrot.data.Mcollect
import Mandelbrot.data.Mdata
import groovyParallelPatterns.connectors.reducers.AnyFanOne
import groovyParallelPatterns.connectors.spreaders.OneFanAny
import groovyParallelPatterns.functionals.groups.AnyGroupAny
import groovyParallelPatterns.terminals.Collect
import groovyParallelPatterns.terminals.Emit
import groovyParallelPatterns.*
 

int workers
int maxIterations          // 100
int width                  //1400    700        350 in pixels  2800
//int height                 //800     400        200 in pixels  1600
 
 
if (args.size() == 0){
workers = 16
}
else {
workers = Integer.parseInt(args[0])
}
maxIterations = 1000
width = 5600
 
System.gc()
 
print "noGUI, $width, $maxIterations, $workers, "
long startTime = System.currentTimeMillis()
 
def emitDetails = new DataDetails(dName: Mdata.getName(),
dInitMethod: Mdata.initialiseClass,
dInitData: [width, maxIterations],
dCreateMethod: Mdata.createInstance)
 
def resultDetails = new ResultDetails(rName: Mcollect.getName(),
rInitMethod: Mcollect.init,
rCollectMethod: Mcollect.collector,
rFinaliseMethod: Mcollect.finalise)
 

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
 
def collector = new Collect(
    input: chan4.in(),
    // no output channel required
    rDetails: resultDetails)

PAR network = new PAR()
 network = new PAR([emit , spread , group , reduce , collector ])
 network.run()
 network.removeAllProcesses()
//END

 
def endtime = System.currentTimeMillis()
print "${endtime - startTime} \n"
