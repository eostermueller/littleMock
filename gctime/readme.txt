
 ####
 ####   gctime
 ####
 ####   Plug-it-in-now GC overhead measurements
 ####   Why wait for verbosegc analysis?  Get Java GC overhead metrics now.


  bash:    ==> ./gctime.sh <myJavaPid>
  ms-win:  ==> gctime.cmd  <myJavaPid>

  Instructions:  
	1) Make sure JAVA_HOME/bin is in the PATH (for jstat)
	2) For MS-Win, download and unzip the following zip file.  Unzip gawk.exe and place it in a folder in the PATH
	   http://unxutils.sourceforge.net/UnxUtils.zip

  Used as above, this script shows GC times / second for young and new generation GC.   
  This script subtracts previous row from the current row of jstat output  for the 3 selected columns).
  jstat time columns display only cumulative gc time (also in seconds).

  The 2 column names in the output (dYGCT, dFGCT) are derrived from this jstat doc:
 	 https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html

  GC overhead = % time spent in GC.
  GC overhead example: When dYGCCT or dFGCT are 0.020 , that's 20ms out of 1000ms spent in gc -- that's > 2% overhead

  GC Health
  ---------
  RED:  	GC overhead 8% or higher, then high priority should be given to improving GC performance.
  YELLOW:  	GC overhead 2%-7%, then start planning to tune GC, but immediate changes are not required.
  GREEN: 	GC overhead 2% or less, then GC is healthy.  only high throughput, low latency (<10ms) apps will benefit from tuning further.


####
####  gctime sample output
#### 

   dYGCT    dFGCT     |** young gen gctime (dYGCT) is 0.039 to 0.051. seconds, that is roughly 4% to 5% overhead. 0.047=4.7% GC overhead.
   -----    -----     |** 4-5% GC overhead in dYGCT column means "GC Health" for young gen is YELLOW per the table above.
   0.039    0.000     |** 0% GC overhead in dFGCT column means "GC Health" for old gen is GREEN 
   0.047    0.000     |** littleMock settings to reproduce: X0,J25,K25,L0,QRS,A10,B5000000,C10
   0.051    0.000


   dYGCT    dFGCT     |** old gen gctime (dFGCT) is .030 to 0.291.  That's between 3% and 29% GC overhead.
   -----    -----     |** That means "GC Health" for the old gen is borderline YELLOW-RED.
   0.002    0.291     |** young gen gctime is .002 to .009, which is less than 1% GC overhead, which means GC health is GREEN.
   0.007    0.030     |** littleMock settings to reproduce: X0,J25,K25,L0,QRS,A4096,B30000,C60000
   0.009    0.031

 Heap settings used for above experiment:
              -Xmx1g
              -XX:NewSize=512m
              -XX:MaxNewSize=512m
              -XX:+UseConcMarkSweepGC
              -XX:ConcGCThreads=4

 Author:  Erik Ostermueller / eostermueller@gmail.com
