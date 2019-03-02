package com.ccl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ccl.args.Argument;
import com.ccl.enumerations.Result;
import com.ccl.utils.MathUtils;

public abstract class Command<T extends Object>
{

	public final List<Argument> requiredParams = new ArrayList<>();
	public final List<Argument> optionalParams = new ArrayList<>();

	private String name = "";
	private String help = "";
	private String[] aliases;

	private int cooldown = 0;
	private long lastUsage = 0;

	private int timesUsed = 0;

	private boolean shouldExecute = true;

	public Command()
	{
	}

	public void onExecute(T obj, String[] in)
	{
	}

	public void execute(T obj, String in)
	{
		String[] processedInput = this.processInput(in);

		if (this.shouldExecute && this.isCooldownReady())
		{
			this.onExecute(obj, processedInput);

			timesUsed++;
			this.lastUsage = System.currentTimeMillis();
			this.shutdown(Result.SUCCESS);
		}
		else
		{
			this.shutdown(Result.FAILURE);
		}

		// reset the command for usage.
		this.shouldExecute = true;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHelp()
	{
		return help;
	}

	public void setHelp(String help)
	{
		this.help = help;
	}

	public boolean isShouldExecute()
	{
		return shouldExecute;
	}

	public String[] getAliases()
	{
		return aliases;
	}

	public void setAliases(String[] aliases)
	{
		this.aliases = aliases;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public void setCooldown(int cooldown)
	{
		this.cooldown = cooldown;
	}

	public boolean isCooldownReady()
	{
		long currentTime = System.currentTimeMillis();

		if (((currentTime - lastUsage) / 1000) >= this.cooldown)
		{
			return true;
		}

		return false;
	}

	public void result(Result result)
	{

	}

	private void shutdown(Result result)
	{
		this.result(result);
		this.shouldExecute = false;
	}

	private String[] processInput(String input)
	{

		String[] rawArgs = Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);
		try
		{
			for (int i = 0; i < rawArgs.length; i++)
			{

				if (!this.shouldExecute || i > requiredParams.size() - 1)
				{
					break;
				}
				else if (rawArgs.length < requiredParams.size())
				{
					this.shutdown(Result.FAILURE);
					break;
				}

				switch (requiredParams.get(i).getType())
				{
				case BOOLEAN:
					if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
					{
					}
					else
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case BYTE:
					Byte.parseByte(rawArgs[i]);
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case DOUBLE:
					Double.parseDouble(rawArgs[i]);
					break;
				case FLOAT:
					Float.parseFloat(rawArgs[i]);
					break;
				case INT:
					Integer.parseInt(rawArgs[i]);

					break;
				case LONG:
					Long.parseLong(rawArgs[i]);
					break;
				case SHORT:
					Short.parseShort(rawArgs[i]);
					break;
				default:
					break;
				}
			}

			int counter = -1;
			for (int i = this.requiredParams.size(); i < rawArgs.length && rawArgs.length - requiredParams.size() != 0; i++)
			{
				counter++;

				if (!this.shouldExecute || i > (optionalParams.size() + requiredParams.size() - 1))
				{
					break;
				}

				switch (optionalParams.get(counter).getType())
				{
				case BOOLEAN:
					if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
					{
						break;
					}
					else
					{
						this.shutdown(Result.FAILURE);
						break;
					}
				case BYTE:
					Byte.parseByte(rawArgs[i]);
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case DOUBLE:
					Double.parseDouble(rawArgs[i]);
					break;
				case FLOAT:
					Float.parseFloat(rawArgs[i]);
					break;
				case INT:
					int value = Integer.parseInt(rawArgs[i]);
					rawArgs[i] = MathUtils.clamp(value, optionalParams.get(counter));
					break;
				case LONG:
					Long.parseLong(rawArgs[i]);
					break;
				case SHORT:
					Short.parseShort(rawArgs[i]);
					break;
				default:
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			this.shutdown(Result.FAILURE);
		}

		return rawArgs;
	}
}
