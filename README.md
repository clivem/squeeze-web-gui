squeeze-web-gui
===============

Web Configuration and Control Interface (Java version) for a Wandboard Squeeze Player

This project provides a Web Configuration GUI for the network interfaces and 
service control for the Community Squeeze Player project, using the Wandboard 
hardware, running the Community Squeeze Operating System, which is based on 
the Fedora Linux distribution.

The Web-GUI is built on Java servlet technology, using Apache Struts, and is 
deployed via Apache Tomcat. The source code in this repository is an Eclipse 
Dynamic Web Project. The WAR can be built, exported and deployed from the 
Eclipse project.

SqueezeWebGui/src/squeeze/web/util contains the Java source code for the 
utility classes, mostly concerned with executing commands, and 
reading/writing config files.

SqueezeWebGui/src/squeeze/web contains the Java source code for the Struts 
actions, which service the web page requests.
