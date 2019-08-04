/**
 *
 * Copyright 2019 Carsten Pratsch
 *
 * This file is part of waterRowerService.
 *
 * waterRowerService is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * waterRowerService is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with waterRowerService.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
