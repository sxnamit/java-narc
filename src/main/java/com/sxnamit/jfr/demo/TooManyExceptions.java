package com.sxnamit.jfr.demo;

// Run the program for sometime (say 10 mins)
// Open the recording jfr file in JMC
public class TooManyExceptions {

    public static void main(String[] args) {
        int THREAD_COUNT = Runtime.getRuntime().availableProcessors() - 1;
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(new WhatDidIDoRunnable());
            t.setDaemon(false);
            t.start();
        }
    }

}

class WhatDidIDoRunnable implements Runnable {

    int count = 0;

    @Override
    public void run() {
        while (true) {
            try {
                doSomething();
            }
            catch (WhatDidIDoException e) {
                // do nothing
            }
        }

    }

    private void doSomething() throws WhatDidIDoException {
        if (count++ % 2 == 0) {
            throw new WhatDidIDoException("If I knew what I was doing, it wouldn't happen in the first place.");
        }
        else {
            // do nothing
        }
    }

}

class WhatDidIDoException extends Exception {

    private static final long serialVersionUID = 1L;

    public WhatDidIDoException(String message) {
        super(message);
    }

}
