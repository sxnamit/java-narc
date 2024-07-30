package com.sxnamit.jfr.event;

import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.StackTrace;
import jdk.jfr.Threshold;

@Category({ "MyApp", "Platform", "Persistence" })
@Name("com.sxnamit.myapp.platform.TransactionCommit")
@Description("Transaction commit persists an entity into the database."
        + " This event captures transaction commits that take longer than 500ms.")
@Label("Transaction Commit")
@Threshold("500ms")
@StackTrace(false)
public class TransactionCommitEvent extends Event {

    @Label("Message signifying whether it is transaction or session commit")
    public String message;

}
