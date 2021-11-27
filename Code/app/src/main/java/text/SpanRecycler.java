package io.github.rosemoe.editor.text;

import android.util.Log;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.github.rosemoe.editor.struct.Span;

public class SpanRecycler {

    private static SpanRecycler INSTANCE;

    public static synchronized SpanRecycler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SpanRecycler();
        }
        return INSTANCE;
    }

    private Thread recycleThread;
    private final BlockingQueue<List<List<Span>>> taskQueue;

    private SpanRecycler() {
        taskQueue = new ArrayBlockingQueue<>(8);
    }

    public void recycle(List<List<Span>> spans) {
        if (spans == null) {
            return;
        }
        if (recycleThread == null || !recycleThread.isAlive()) {
            recycleThread = new RecycleThread();
            recycleThread.start();
        }
        taskQueue.offer(spans);
    }

    private class RecycleThread extends Thread {

        private final static String LOG_TAG = "Span Recycle Thread";

        RecycleThread() {
            setDaemon(true);
            setName("SpanRecycleDaemon");
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    try {
                        List<List<Span>> spanMap = taskQueue.take();
                        int count = 0;
                        for (List<Span> spans : spanMap) {
                            int size = spans.size();
                            for (int i = 0; i < size; i++) {
                                spans.remove(size - 1 - i).recycle();
                                count++;
                            }
                        }
                        //Log.i(LOG_TAG, "Recycled " + count + " spans");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (Exception e) {
                Log.w(LOG_TAG, e);
            }
            Log.i(LOG_TAG, "Recycler exited");
        }

    }

}
