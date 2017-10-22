# Install and Run littleMock

littleMock is demo environment for Java performance tuning. Runs on Windows/Mac/Linux, internet connection required.

1. Make sure a 1.7+ JDK and [Maven](http://maven.apache.org) are both installed.  Make sure java and mvn are in the PATH.
2. Download zip or clone littleMock to your own machine.  Use green button on the top right.
3. Launch startWar.sh (or startWar.cmd) to launch the WAR file.
4. From a different cmd/terminal window, launch load.sh (or load.cmd).  It will apply 3 threads of load of traffic.
5. Both of these scripts will take a few minutes to download dependencies.
6. ![Screencast for install](https://user-images.githubusercontent.com/175773/31864156-6618087e-b71e-11e7-8fa1-1104220c3438.gif)


# Optimize It!
1. Use your favorite tools (glowroot.org, jstack, jprofiler, etc...) to see what parts of littleMock are slow.
2. Once startWar.sh is running, open http://localhost:8080/ui in your browser.
3. Click the "Monitoring" link at the top of the page to launch [Glowroot](http://glowroot.org), monitoring software.  It will show you the impact of the optimizations you select on the above web page.
4. Improrve/degrade performance immediately by selecting options on this web page.
5. ![Using glowroot.org to monitor littleMock](https://user-images.githubusercontent.com/175773/31866957-a4a8cc80-b74c-11e7-92ee-b878052b6060.gif)



