package org.leplan73.outilssgdf.gui.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class Appender extends AppenderBase<ILoggingEvent> {

	private static LoggedDialog dialog_;
	
	public static void setLoggedDialog(LoggedDialog dialog)
	{
		dialog_ = dialog;
	}
	
	@Override
	protected void append(ILoggingEvent eventObject) {
		if (dialog_ != null)
		synchronized(dialog_)
		{
			dialog_.addLog(eventObject.toString());
		}
	}
}
