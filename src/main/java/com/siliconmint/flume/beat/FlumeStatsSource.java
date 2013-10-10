package com.siliconmint.flume.beat;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.instrumentation.util.JMXPollUtil;
import org.apache.flume.source.AbstractSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FlumeStatsSource extends AbstractSource implements Configurable, EventDrivenSource {
	
	private static final Gson gson = new Gson();
	private static final Type mapType = new TypeToken<Map<String, Map<String, String>>>() {}.getType();
	
	private int sampleInterval = 1000;
	
	private ScheduledExecutorService executor;
	private ScheduledFuture<?> poller;
	
	@Override
	public void configure(Context context) {
		sampleInterval = context.getInteger("sampleInterval", sampleInterval);
		if (poller != null) {
			poller.cancel(true);
		}
	}
	
	@Override
	public synchronized void start() {
		super.start();
		executor = Executors.newSingleThreadScheduledExecutor();
		poller = executor.scheduleAtFixedRate(new StatisticsPoller(), 0, sampleInterval, TimeUnit.MILLISECONDS);
	}

	@Override
	public synchronized void stop() {
		if (poller != null) {
			poller.cancel(true);
			poller = null;
		}
		executor.shutdown();
		super.stop();
	}

	public int getSampleInterval() {
		return sampleInterval;
	}

	public void setSampleInterval(int sampleInterval) {
		this.sampleInterval = sampleInterval;
	}
	
	private class StatisticsPoller implements Runnable {
		@Override
		public void run() {
			Map<String, Map<String, String>> metricsMap = JMXPollUtil.getAllMBeans();
	        String json = gson.toJson(metricsMap, mapType);
	        Event event = EventBuilder.withBody(json.toString(), Charsets.UTF_8);
	        try {
	        	getChannelProcessor().processEvent(event);
	        } catch (ChannelException e) {
	        	log.error("{} could not write to channel", getName());
	        }
		}
	}
	
	private static final Logger log = LoggerFactory.getLogger(FlumeStatsSource.class);

}
