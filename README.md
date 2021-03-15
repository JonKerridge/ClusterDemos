ClusterDemos

Demontrations of gppClusterBuiler and groovyParallelPatterns library

These demos have been used to check and then evaluate the use  of the 
Cluster Builder mechanism.

The examples use a Mandelbrot set example, which is run on a single machine 
or a cluster of machines.  

The non *.cgpp and *.gpp files have been created by 
gppClusterBuilder and gppBuilder respectively.  
These have created the *.groovy files.

All the codes use the class files contained in Mandelbrot/data.

**Note**  
In the //@emit annotation you WILL have to change the IP address 
to that of your own machine(s) for all the .cgpp scripts.

The Cluster*Mandelbrot1 codes run as a cluster on a single 
machine to check for basic operation. Where * is either GUI or empty.

To run the tests run the *Host Loader FIRST and then run the *NodeLoader as 
two separate Runs on the same machine.  They will execute as if they were 
running on separate machines, a host machine and a cluster.  There will be 
no performance improvement as this is a check for correct functionality.

The ClusterMandelbrot2.cgpp script will cause a version to be created that runs on 
host and two separate machines in the cluster.  It uses IP addresses
that are in a local network.

The EvaluateMandelbrot*.cgpp scripts show how the effect of using additional 
machines in the cluster, signified by the single digit in the name.  The host machine
has a final element of 152 to its IP address.  Assuming you have access to a cluster then 
alter the //@emit ip address as required.

In all cases apart from the versions that run on a single machine for sanity checking you
will need to create a jar artifact that contains the NodeLoader process.

This jar artifact will have to be copied to each of the machines in the cluster.
Run the HostLoader code first, probably from your IDE.
Then run the jar artifact on each of the machines in the cluster.  Once all the nodes
are running then the application will start automatically getting the Node 
process and class files from the host machine automatically.

YOU DO NOT HAVE TO COPY ANY FILES FROM THE HOST OTHER THAN THE JAR ARTIFACT THAT IS THE NodeLoader!
