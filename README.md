# A how-to on Java Flight Recorder  
  
Something tragic happens in the world of aeronautics, #FlightRecorder trends on Twitter until the device is recovered. A Flight Recorder, commonly referred to as a "black box," is an electronic device installed in aircrafts to record crucial flight data and cockpit conversations. The data it captures is essential for investigating accidents, helping to understand what happened during the flight and how to improve aviation safety.  
  
Like its aeronautic namesake, Java Flight Recorder also operates as a "black box," continuously collecting diagnostic and profiling data from a running Java application. This data can then be analysed using JFR tool or APIs or JMC, when an anomaly is detected. Formally speaking, Java Flight Recorder (JFR) is a diagnostic and continuous monitoring tool built into the Java Runtime, designed to collect detailed data about a running Java application with minimal impact on performance.  
  
## Overview
-Open sourced in JDK 11  
-Event based tracing framework  
-Built into Java Runtime (JVM), piggybacks on the information captured by the JVM. 
 Collects event data from multiple levels, ranging from the OS to the JVM, JDK libraries, and the application. Enables in-depth analysis; start on high level, go deep as needed.
-Extremely low overhead, suitable for production environments. Less than 1% overhead with the default setting.  
-Provides APIs to  
    1. Produce application level events  
    2. Consuming event streams  
-Event correlation 
  
## Anatomy of a JFR event  
A JFR event is a small data blob containing:  
  
Event ID:  Unique identifier  
Timestamp (CPU ticks):  Time of the event  
Duration (CPU ticks):  Event duration (not all events have this, e.g., instant events)  
Thread ID:  ID of the thread generating the event; not all events are tied to a thread so not all events have a thread ID  
StackTrace ID:  StackTraces are stored separately, stacktrace IDs are stored with the event  
Event specific payload:  Specific data points for the event, as defined in the event declaration. Refer src/main/java/com/sxnamit/jfr/event/TransactionCommitEvent.java  
  
## Custom event declaration  
```
import jdk.jfr.Event;

// minimal fields to prevent jfr recording bloat
class OrderEvent extends Event {
  public int orderId;
}
```
```
void placeOrder() {
  OrderEvent e = new OrderEvent();
  e.begin(); // captures the event start timestamp

  // *** important work profiled by the event ***
  e.orderId = 123; // order ID generated by the code profiled above

  e.end(); // captures the event end timestamp
  e.commit(); // calls end() implicitly, so end() call is optional
}
```

## JFR annotations  
The annotations specified below help with the presentation of recorded data, for example, in tools like JMC. Take a look at the class TransactionCommitEvent.java to see how to use the JFR annotations, and open and view the recording TransactionCommitDemo.jfr in JMC to see how the event data is presented in the Event Browser.  
  
@Name  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Name.html  
  
@Label  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Label.html  
  
@Description  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Description.html  
  
@Category  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Category.html  
  
Annotations that modify the recording behavior...  
  
@Threshold  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Threshold.html  
  
@StackTrace  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/StackTrace.html  
  
@Enabled  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/jdk/jfr/Enabled.html  
  
## Event filtering  
Events can be filtered in/out of a recording by  
- Duration. For app level events, use the annotation @Threshold. For JFR configured OOTB events, modify the configuration file (.jfc) using JMC or "jfr configure" command.  
- Name. Modify the configuration file (.jfc) using JMC or "jfr configure" command to enable/disable an event by name, use setting name "enabled".  
  
  
## Effects on performance  
-Default configuration ({JAVA_HOME}/lib/jfr/default.jfc) claimed to have less than 1% overhead; other configurations can have more overhead.  
-Stack depth chosen effects performance. Default is 64, however deep call stacks can impact performance.  
-Takes advantage of JIT compilation inlining to optimize the code; check out this explanation [here](https://youtu.be/xrdLLx6YoDM?feature=shared&t=1457)  
-Comparison with other logging tools is [here](https://youtu.be/xrdLLx6YoDM?feature=shared&t=1697)  


  
P.S:  
Here is some other relevant useful info on the topic -  
Intro vids  
https://www.youtube.com/watch?v=plYESjZ12hM  
https://www.youtube.com/watch?v=AgFOJEkBVjg  
  
JDK tools to work with jfr  
https://docs.oracle.com/en/java/javase/21/docs/specs/man/jcmd.html  
https://docs.oracle.com/en/java/javase/21/docs/specs/man/jfr.html  
  
Javadoc  
https://docs.oracle.com/en/java/javase/21/docs/api/jdk.jfr/module-summary.html  
  
JFR API guide  
https://docs.oracle.com/en/java/javase/21/jfapi/index.html  
  
Additional links  
https://docs.oracle.com/en/java/javase/21/troubleshoot/index.html  
https://inside.java/tag/jfr  
https://developers.redhat.com/blog/2020/08/25/get-started-with-jdk-flight-recorder-in-openjdk-8u#about_jdk_flight_recorder  
  
JFR Event Collection for JDK 21
https://sap.github.io/SapMachine/jfrevents/21.html  

JEP  
https://openjdk.org/jeps/328  
  
hotspot-jfr-dev Mailing List  
https://mail.openjdk.org/mailman/listinfo/hotspot-jfr-dev  
  
