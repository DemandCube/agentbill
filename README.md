FlumeBeat
=========



JMXSource - Overview
=========

JMXSource is a Flume Source that can be configured using java properties file of the Flume Config

The Type of MBeans Supported for polling access are:

* Standard MBeans
* Dynamic MBeans
* Open MBeans
* Model MBeans
* MXBeans

It is deployed as a plugin to Flume.  This is an example of it's deployment.

    plugins.d/FlumeBeat/lib/flume-beat-XXX.jar

Example java properties configuration

    a1.sources = r1
    a1.channels = c1
    
    a1.sources.r1.type = com.siliconmint.flume.beat.FlumeStatsSource
    a1.sources.r1.channels = c1
    # Sampling interval in ms
    a1.sources.r1.sampleInterval = 1000
