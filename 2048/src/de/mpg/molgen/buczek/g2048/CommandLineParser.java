package de.mpg.molgen.buczek.g2048;

import java.util.ArrayList;

public class CommandLineParser {

	public static enum OptionType {
		UNSPECIFIED,
		INTEGER
	}
	
	public static class OptionDescription {			
		String   name;
		OptionType	 type;
		Object	 value;
		
		public OptionDescription(String name,OptionType type) {
			this.name=name;
			this.type=type;
			value=null;
		}		
	}

		
	public interface OptionProcessor {
		int getIntegerOption(String name,int defaultValue);
	}

	public interface OptionsCreator {
		public abstract void createOptions(OptionProcessor op);
	}

	
	private int i=0;		
	private ArrayList<OptionDescription> options;		
	private ArrayList<String> remainingArgs;
	public boolean hasError;

	private OptionDescription findOption(String arg) {
		OptionDescription odReturn=null;
		for (OptionDescription od : options) {
			if (od.name.startsWith(arg)) {
				if (odReturn!=null) {
					return null;
				}
				odReturn=od;
			}
		}
		return odReturn;
	}
	
	public String[] getRemainingArgs() {
		return remainingArgs.toArray(new String[remainingArgs.size()]);
	}

	public int getRemainingArgCount() {
		return remainingArgs.size();
	}
	
	private boolean _parse(String args[]) {
	
		int	i=0;
		while (i<args.length) {
			String arg=args[i++];
			if (arg.startsWith("--")) {
				OptionDescription od=findOption(arg.substring(2));
				if (od==null) {
					return false;
				}
				if (i>=args.length) {
					return false;
				}
				od.value=new Integer(args[i++]);
			} else {
				remainingArgs.add(arg);
			}
		}
		return true;
	}


	
	
	private void registerOption(String name,OptionType type) {
		options.add(new OptionDescription(name,type));
	}
		
	public boolean parse(String[] args, OptionsCreator optionSupplier) {

		options=new ArrayList<OptionDescription>();
		remainingArgs=new ArrayList<String>();
		
		optionSupplier.createOptions(new OptionProcessor() {
			public int getIntegerOption(String name, int defaultValue) { registerOption(name,OptionType.INTEGER);return defaultValue;}			
		});
		
		if (!_parse(args)) {
			return false;
		}
						
		optionSupplier.createOptions(new OptionProcessor() {
			public int getIntegerOption(String name, int defaultValue) {
					OptionDescription o=options.get(i++);
					return (o.value==null ? defaultValue : (Integer)o.value);}						
		});	

		return true;
	}
}
