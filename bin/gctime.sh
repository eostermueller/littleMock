#!/bin/sh
#
#  Expected use:
#  $JAVA_HOME/bin/jstat -gc <myPid> 1s | gctime.sh
#
#  This script file is gctime.sh.
#  Used as above, this script shows GC times / second.  The using of measure is seconds.
#  This script subtracts previous row from the current row of jstat output  for the 3 selected columns).
#  jstat time columns display only cumulative gc time (also in seconds).
#
# 
# Author:  Erik Ostermueller
# The 3 column names in the output are derrived from this jstat doc:
# https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html
#
#  When dYGCT > 0.020 below, that's 20ms out of 1000ms spent in new gc -- that's > 2% overhead
# 
#   dYGCT    dFGCT    
#   -----    -----   
#   0.021    0.000  
#   0.023    0.000 
#   0.024    0.000
#
#
#  When dFGCT > 0.020 below, that's 20ms out of 1000ms spent in old gc -- that's > 2% overhead
#
#
#   0.002    0.291       littleMock settings to reproduce: X0,J25,K25,L0,QRS,A4096,B30000,C60000
#   0.007    0.030    
#   0.029    0.031   


awk  '{ pYGCT=cYGCT;cYGCT=$14;pFGCT=cFGCT;cFGCT=$16} 
NR==1 { printf "%8s %8s\n","dYGCT","dFGCT"} 
NR==1 { printf "%8s %8s\n","-----","-----"} 
NR>1  { printf "%8.3f %8.3f\n",  cYGCT-pYGCT,cFGCT-pFGCT}'


