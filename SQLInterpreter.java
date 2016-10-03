package models;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import error.SQLCustomErrorHandler;
import helpers.DBCatalog;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import utils.PropertyFileReader;
import utils.SelectExecutor;

/**
 * The <tt>SqlInterpreter</tt> class provides a client for accepting SQL query
 * requests and output the results.
 * <p>
 * This is the top-level entry for the program.
 * 
 * @authors Saarthak Chandra Shweta Srivastava Vikas P Nelamangala
 *
 */
public class SQLInterpreter {
	private static String FILE_NAME = "query";
	/**
	 * Output of the parser with the given input / output directory.
	 * 
	 * @param inPath
	 *            input directory
	 * @param outPath
	 *            output directory
	 * 
	 */

	PropertyFileReader reader = PropertyFileReader.getInstance();

	public void parse(String inPath, String outPath) {
		DBCatalog.createDirectories(inPath, outPath);
		DBCatalog.getCatalogInstance();

		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(DBCatalog.queryPath));
			Statement statement;
			int counter = 1;
			while ((statement = parser.Statement()) != null) {
				try {
					File file = new File(DBCatalog.outputDirectory + File.separator + FILE_NAME + counter);
					PrintStream printstream = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));

					// Call the main dude here
					SelectExecutor selectExecutor = new SelectExecutor(statement);
					System.out.println("Calling DUMP now.........");
                    selectExecutor.root.dump(printstream);
					// do some printing here
					printstream.close();

					// So we know number of query output files we created.
					counter++;

				} catch (Exception ex) {
					//SQLCustomErrorHandler handler = new SQLCustomErrorHandler(ex, this.getClass().getSimpleName());
					ex.printStackTrace();
					// Ensure one failure does not halt execution
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main function invoked by jar.
	 * 
	 * @param args
	 *            argument list
	 */
	public static void main(String[] args) {
		//
		// if (args.length != 2) {
		// throw new IllegalArgumentException("Incorrect input format");
		// }
		//
		SQLInterpreter sqlInterpret = new SQLInterpreter();
		// sqlInterpret.parse(args[0], args[1]);
		sqlInterpret.parse("samples/input", "samples/output");

	}
}
