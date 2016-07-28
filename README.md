# InterruptDemo

This class demonstrates a portable way for Java code to remain responsive to asynchronous
signals (such as Ctrl-C) when called from the Matlab command line or a Matlab script. There is no
build-time dependency on the Matlab JMI jar which should allow it to build (ex: on CI servers)
without a Matlab installation. The Matlab JMI jar is naturally required at run time.

Tested with Matlab version 9.0.0.341360 (R2016a).                       

# Building
    $ javac InterruptDemo.java
    $ jar -cfe interrupt_demo.jar InterruptDemo InterruptDemo.class

# Running (native)
    $ java -jar interrupt_demo.jar

# Running (Matlab)
    >> javaaddpath('interrupt_demo.jar');
    >> InterruptDemo.blockForever
