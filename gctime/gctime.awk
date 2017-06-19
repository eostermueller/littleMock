{ pYGCT=cYGCT;cYGCT=$14;pFGCT=cFGCT;cFGCT=$16} 
NR==1 { printf "%8s %8s\n","dYGCT","dFGCT"} 
NR==1 { printf "%8s %8s\n","-----","-----"} 
NR>1  { printf "%8.3f %8.3f\n",  cYGCT-pYGCT,cFGCT-pFGCT }

