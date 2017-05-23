# littleMock

littleMock is demo environment for Java performance tuning. Runs on Windows/Mac/Linux, internet connection required.

1. Make sure a 1.7+ JDK and [Maven](http://maven.apache.org) are both installed.
2. Download zip or clone littleMock to your own machine.  Use green button on the top right.
3. Launch startWar.sh (or startWar.cmd) to launch the WAR file.
4. From a different cmd/terminal window, launch load.sh (or load.cmd) to apply 3 threads of load of traffic.
5. Both of these scripts will take a few minutes to download dependencies.


# Optimize It!
1. Use your favorite Java Profiler to see what parts of littleMock are slow.
2. Open http://localhost:8080/ui in your browser.
3. Select options on this web page to deploy live optimizations to the running load test.

![](https://cloud.githubusercontent.com/assets/175773/24088253/416d1078-0cf6-11e7-874d-c82044120bcd.png)

# Monitor It!
1. Click the "Monitoring" link at the top of the page to launch [Glowroot](http://glowroot.org), monitoring software that will show you the impact of the optimizations you select on the above web page.
2. Scroll down a bit and select the 'throughput' radio button.

![](https://cloud.githubusercontent.com/assets/175773/24088328/134fa4d4-0cf7-11e7-9271-239aa058d1da.png)
