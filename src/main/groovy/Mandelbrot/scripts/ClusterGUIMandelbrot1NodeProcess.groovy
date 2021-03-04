package Mandelbrot.scripts
import jcsp.lang.*
import groovyJCSP.*
import jcsp.net2.*
import jcsp.net2.mobile.*
import jcsp.net2.tcpip.*
import gppClusterBuilder.*
 
import Mandelbrot.data.Mdata
import Mandelbrot.data.Mgui
import groovyParallelPatterns.DataDetails
import groovyParallelPatterns.ResultDetails
import groovyParallelPatterns.cluster.connectors.NodeRequestingFanAny
import groovyParallelPatterns.cluster.connectors.OneNodeRequestedList
import groovyParallelPatterns.connectors.reducers.AnyFanOne
import groovyParallelPatterns.functionals.groups.AnyGroupAny
import groovyParallelPatterns.terminals.CollectUI
import groovyParallelPatterns.terminals.Emit
import java.awt.Color
 
class ClusterGUIMandelbrot1NodeProcess implements CSProcess, Serializable, NodeConnection, LoaderConstants{
String nodeIP, hostIP
NetLocation toHostLocation
NetChannelInput fromHost
 
def connectFromHost (NetChannelInput fromHost){
this.fromHost = fromHost
}
 
@Override
void run() {


int cores = 4  // number of workers on each node
int clusters = 1 // number of clusters
int maxIterations = 100
int width  = 350                //1400   700   350
int height = 200                // 800   400   200
 
long initialTime = System.currentTimeMillis()
// create basic connections for node
NetChannelOutput node2host = NetChannel.one2net(toHostLocation as NetChannelLocation)
node2host.write(nodeProcessInitiation)
// read in application net input channel VCNs [ vcn, ... ]
List inputVCNs = fromHost.read() as List
//@inputVCNs
//node Input Insert

ChannelInput nodeFromEmit = NetChannel.numberedNet2One(inputVCNs[0])

 
// acknowledge creation of net input channels
node2host.write(nodeApplicationInChannelsCreated)
 
// read in application net output channel locations [ [ip, vcn], ... ]
List outputVCNs = fromHost.read()
//@outputVCNs
//node Output Insert

ChannelOutput node2emit = NetChannel.one2net(new TCPIPNodeAddress(outputVCNs[0][0], 2000), outputVCNs[0][1])
ChannelOutput node2collect = NetChannel.one2net(new TCPIPNodeAddress(outputVCNs[1][0], 2000), outputVCNs[1][1])

 
// acknowledge creation of net output channels
node2host.write(nodeApplicationOutChannelsCreated)
println "Node starting application process"
long processStart = System.currentTimeMillis()
// now start the process - inserted by builder
//@nodeProcess
//node Process Insert

 
def chan3 = Channel.one2any()
def chan4 = Channel.any2any()


def nrfa = new NodeRequestingFanAny(
    request: node2emit,
    response: nodeFromEmit,
    outputAny: chan3.out(),
    destinations: cores
    )
def group = new AnyGroupAny(
    inputAny: chan3.in(),
    outputAny: chan4.out(),
    workers: cores,
    function: Mdata.calculate
    )
def afo1 = new AnyFanOne(
    inputAny: chan4.in(),
    output: node2collect,
    sources: cores )
 



new PAR([nrfa , group , afo1 ]).run()

 
long processEnd = System.currentTimeMillis()
println "Node application component has Finished"

node2host.write([nodeIP, (processStart - initialTime), (processEnd - processStart), ])
println " Node $nodeIP has sent times to host"

}
}
