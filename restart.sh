jps | grep littleMock | awk '{ print $1 }' | xargs -n 1 kill
sleep 2
./start.sh 1> out.txt 2>&1 &
sleep 2
tail -f out.txt
