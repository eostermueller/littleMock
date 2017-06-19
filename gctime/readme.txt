
 ####
 ####   gctime
 ####
 ####   Plug-it-in-now GC overhead measurements
 ####   Why wait for verbosegc analysis?  Get Java GC overhead metrics now.


  bash:    ==> ./gctime.sh <myJavaPid>
  ms-win:  ==> gctime.cmd  <myJavaPid>

  Used as above, this script shows GC times / second for young and new generation GC.   
  This script subtracts previous row from the current row of jstat output  for the 3 selected columns).
  jstat time columns display only cumulative gc time (also in seconds).

 The 2 column names in the output are derrived from this jstat doc:
 https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html

  GC overhead:  When metrics below are 0.020 , that's 20ms out of 1000ms spent in gc -- that's > 2% overhead

  GC Health
  ---------
  RED:  	GC overhead 8% or higher, then high priority should be given to improving GC performance.
  YELLOW:  	GC overhead 2%-7%, then start planning to tune GC, but immediate changes are not required.
  GREEN: 	GC overhead 2% or less, then GC is healthy.  only high throughput, low latency (<10ms) apps will benefit from tuning further.


   dYGCT    dFGCT     young gen is ~4-5% overhead.  0.047 seconds / 1s = 4.7%
   -----    -----     littleMock settings to reproduce: X0,J25,K25,L0,QRS,A10,B5000000,C10
   0.039    0.000
   0.047    0.000
   0.051    0.000


   dYGCT    dFGCT     littleMock settings to reproduce: X0,J25,K25,L0,QRS,A4096,B30000,C60000
   -----    -----
   0.002    0.291
   0.007    0.030
   0.029    0.031

 Heap settings used for above experiment:
              -Xmx1g
              -XX:NewSize=512m
              -XX:MaxNewSize=512m
              -XX:+UseConcMarkSweepGC
              -XX:ConcGCThreads=4

 Author:  Erik Ostermueller / eostermueller@gmail.com
