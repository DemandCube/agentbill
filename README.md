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

  plugins.d/FlumeBeat/lib/FlumeBeat.jar
  plugins.d/FlumeBeat/libext/DependencyXXXX-1.0.0.jar
  plugins.d/FlumeBeat/native/

Example java properties configuration

  a1.sources = r1
  a1.channels = c1
  a1.sources.r1.type = jmx
  a1.sources.r1.channels = c1
  a1.sources.r1.bind = 0.0.0.0
  a1.sources.r1.port = 4141
