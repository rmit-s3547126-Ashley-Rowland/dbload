import org.apache.commons.cli.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.DataOutputStream;


class dbload {

	public static void main (String [] args) {


		//command line parsing was taken from https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
		Options options = new Options();

		Option pageSize = new Option("p", "pagesize", true, "declares the pagesize to be used when generating the heapfile");
		pageSize.setRequired(true);
		options.addOption(pageSize);

		Option filePath = new Option("f", "filepath", true, "path to the input file");
		filePath.setRequired(true);
		options.addOption(filePath);

		CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        try {

	        int pagesize = Integer.parseInt(cmd.getOptionValue("pagesize"));

			File input = new File(cmd.getOptionValue("filepath"));
			BufferedReader br = new BufferedReader(new FileReader(input));

			DataOutputStream os = new DataOutputStream(new FileOutputStream("binout.dat"));

			String currentLine; //to store current read line
			String[] sL = new String[9]; //tokenise of currentLine

			br.readLine(); //skip header

			while(br.ready()) {
				currentLine = br.readLine();
				sL = currentLine.split("\t", -1);

				String business_name = sL[1];
				boolean registered = ((sL[2].equals("Registered")) ? true : false);
				String registration_date = sL[3];
				String cancel_date = sL[4];
				String renew_date = sL[5];
				String state_num = sL[6];
				String state_registered = sL[7];
				long abn = ((sL[8].equals("")) ? -1 : Long.parseLong(sL[8]));

				os.writeBytes(business_name);
				os.writeBoolean(registered);
				os.writeBytes(registration_date);
				os.writeBytes(cancel_date);
				os.writeBytes(renew_date);
				os.writeBytes(state_num);
				os.writeBytes(state_registered);
				os.writeLong(abn);
			}

			os.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}