package waterRowerService;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SimulatorConfig {

    private static CommandLine cmd = null;
	
	public static void setConfigOptions(String[] args) {
	    Options options = new Options();

        Option port = new Option("p", "port", true, "water rower service port (default: 1963)");
        port.setRequired(false);
        options.addOption(port);

        Option path = new Option("f", "file", true, "file to replay");
        path.setRequired(false);
        options.addOption(path);
        
        Option factor = new Option("t", "timefactor", true, "time factor in ms");
        factor.setRequired(false);
        options.addOption(factor);        

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }
	}

	public static String getStringOptionValue(String option) {
	       return cmd.getOptionValue(option, null);

	}

	public static int getIntOptionValue(String option) {
        return Integer.parseInt(cmd.getOptionValue(option, "-1"));
	}

	public static int getIntOptionValue(String option, int defaultVal) {
		if( cmd.hasOption(option)) {
			return Integer.parseInt(cmd.getOptionValue( option));
		}
		return defaultVal;
	}
	
	

}
