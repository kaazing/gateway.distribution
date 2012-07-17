package com.kaazing.mina.netty;

import org.apache.mina.core.session.AbstractIoSessionConfig;
import org.apache.mina.core.session.IoSessionConfig;
import org.jboss.netty.channel.ChannelConfig;

public class ChannelIoSessionConfig<T extends ChannelConfig> extends AbstractIoSessionConfig {

	protected final T channelConfig;
	
	public ChannelIoSessionConfig(T channelConfig) {
		this.channelConfig = channelConfig;
	}
	
	public void setOption(String name, Object value) {
		channelConfig.setOption(name, value);
	}

	@Override
	protected void doSetAll(IoSessionConfig config) {

		int minReadBufferSize = config.getMinReadBufferSize();
		int readBufferSize = config.getReadBufferSize();
		int maxReadBufferSize = config.getMaxReadBufferSize();
		
		int bothIdleTime = config.getBothIdleTime();
		int readerIdleTime = config.getReaderIdleTime();
		int writerIdleTime = config.getWriterIdleTime();
		int throughputCalculationInterval = config.getThroughputCalculationInterval();
		int writeTimeout = config.getWriteTimeout();
		boolean useReadOperation = config.isUseReadOperation();
		
		channelConfig.setOption("minReadBufferSize", minReadBufferSize);
		channelConfig.setOption("readBufferSize", readBufferSize);
		channelConfig.setOption("maxReadBufferSize", maxReadBufferSize);
		channelConfig.setOption("bothIdleTime", bothIdleTime);
		channelConfig.setOption("readerIdleTime", readerIdleTime);
		channelConfig.setOption("writerIdleTime", writerIdleTime);
		channelConfig.setOption("throughputCalculationInterval", throughputCalculationInterval);
		channelConfig.setOption("writeTimeout", writeTimeout);
		channelConfig.setOption("useReadOperation", useReadOperation);
	}

}
