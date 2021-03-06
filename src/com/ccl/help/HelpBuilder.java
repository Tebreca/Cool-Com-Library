package com.ccl.help;

import com.ccl.Command;
import com.ccl.args.Argument;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;

public class HelpBuilder
{

	private boolean displayArgTypes = true;
	private boolean showOptionals = true;

	private HelpBuilder()
	{
	}

	public static HelpBuilder builder()
	{
		return new HelpBuilder();
	}

	public HelpBuilder disableArgTypes()
	{
		this.displayArgTypes = false;
		return this;
	}
	
	public HelpBuilder disableOptionals()
	{
		this.showOptionals = false;
		return this;
	}

	public String build(Command<?, ?> command)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(command.getCommandManager().getPrefix() + command.getName() + " ");

		String argType = "";

		for (Argument arg : command.arguments)
		{
			argType = this.displayArgTypes ? "(" + arg.getType().toString().toLowerCase() + ")" : "";

			if (arg instanceof RequiredArgument)
				builder.append(arg.getName() + argType + " ");
			else if (arg instanceof OptionalArgument && this.showOptionals)
				builder.append("[" + arg.getName() + "]" + argType + " ");
		}

		builder.append("\n");

		return builder.toString().trim();
	}
}
