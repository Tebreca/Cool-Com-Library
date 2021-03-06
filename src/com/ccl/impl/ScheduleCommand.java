package com.ccl.impl;

import java.util.concurrent.TimeUnit;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Result;
import com.ccl.schedule.Task;

public final class ScheduleCommand<T, R> extends Command<T, R>
{
	public ScheduleCommand()
	{
		this.setName("schedule");

		this.setAliases(new String[]
		{ "sched", "timer" });

		this.addArgument(new RequiredArgument("command", ParamType.STRING));

		this.addArgument(new OptionalArgument("time", ParamType.STRING));
		this.addArgument(new OptionalArgument("count", ParamType.INT));
		
	}

	@Override
	public R onExecute(T obj, Arguments in)
	{
		String command = in.getString();
		String rawTime = in.getStringFor("time", "1min");
		long timeAmount = 0;
		TimeUnit unit = null;

		if (rawTime.contains("sec") || rawTime.contains("s"))
		{
			rawTime = rawTime.replace("sec", "").replace("s", "");
			timeAmount = Long.parseLong(rawTime.replace("sec", "").replace("s", ""));
			unit = TimeUnit.SECONDS;
		}
		else if (rawTime.contains("min") || rawTime.contains("m"))
		{
			rawTime = rawTime.replace("min", "").replace("m", "");
			timeAmount = Long.parseLong(rawTime);
			unit = TimeUnit.MINUTES;
		}
		else if (rawTime.contains("hour") || rawTime.contains("hr") || rawTime.contains("h"))
		{
			rawTime = rawTime.replace("hour", "").replace("hr", "").replace("h", "");
			timeAmount = Long.parseLong(rawTime);
			unit = TimeUnit.HOURS;
		}

		int count = in.getIntFor("count", 1);
		boolean isInfinite = count < 0 ? true : false;

		this.getCommandManager().getScheduler().addTask(new Task<T, R>(obj, command, timeAmount, unit).setInfinite(isInfinite).setExecutionCount(count));
		return null;
	}

	@Override
	public void result(Result result, String response)
	{
		if (result == Result.SUCCESS)
			System.out.println("Successfully added command to tasks!");
		super.result(result, response);
	}

}
