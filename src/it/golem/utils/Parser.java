package it.golem.utils;

import it.golem.datastore.OfyService;
import it.golem.enums.Routing;
import it.golem.model.Country;
import it.golem.model.Customer;
import it.golem.model.MCData;
import it.golem.model.Route;
import it.golem.resources.CountryResource;
import it.golem.resources.CustomerResource;
import it.golem.resources.RouteResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.googlecode.objectify.Ref;

public class Parser {

	private static final String REGEX = "^.*([0-9]{8})(_|=5F)([0-9]{6}).*?";
	private static final Pattern p = Pattern.compile(REGEX);
	public static final Logger _log = Logger.getLogger(Parser.class.getName());

	private static final Queue lineQueue = QueueFactory.getQueue("line-queue");
	private static final Queue defQueue = QueueFactory.getDefaultQueue();
	private static boolean balancing = true;
	private static final DateTimeFormatter fmt = DateTimeFormat
			.forPattern("yyyyMMdd HHmmss");

	public static int parseCSV(InputStream is, String fileName) {
		int lineNum = 0;
		try {

			String line;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				lineNum++;
				pushQueue(fileName, line);
			}

			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNum;
	}

	// public static int parseXLS(InputStream is, String filename) {
	//
	// int lineNum = 0;
	// try {
	// Workbook wb = WorkbookFactory.create(is);
	// for (int i = 0; i < wb.getNumberOfSheets(); i++) {
	// wb.getSheetAt(i);
	// lineNum += xlsLineBuild(wb.getSheetAt(i), filename);
	// }
	// } catch (InvalidFormatException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return lineNum;
	//
	// }

	// public static int xlsLineBuild(Sheet sheet, String fileName)
	// throws IOException, ParseException {
	//
	// for (int i = 2; i <= sheet.getLastRowNum(); i++) {
	// StringBuilder line = new StringBuilder();
	// Row row = sheet.getRow(i);
	// int j = 0;
	// for (; j < row.getLastCellNum(); j++) {
	// if (row.getCell(j) != null)
	// line.append(row.getCell(j) + ",");
	// else
	// line.append(",");
	// }
	//
	// pushQueue(fileName, line.toString());
	// }
	//
	// return sheet.getLastRowNum();
	// }

	public static DateTime measureDate(String fileName) throws ParseException {

		Matcher m = p.matcher(fileName);
		if (m.find()) {
			return DateTime.parse(m.group(1) + " " + m.group(3), fmt);
		} else {
			_log.warning("Not match");
			throw new ParseException(fileName, 0);
		}

	}

	public static MCData parseLine(String line, DateTime measureDateTime,
			String splitSymbol) throws IOException {
		try {

			String fields[] = line.replace("%", "").split(splitSymbol);

			if (fields.length == 0) {
				_log.warning("Uncorrect line format: " + line);
				return null;
			}

			if (fields[1] == null || fields[2] == null
					|| "Minutes".equals(fields[3]))
				return null;

			Customer customer = CustomerResource.getCreate(fields[1]).get(0);
			Country country = CountryResource.getCreate(fields[2]).get(0);

			Route r = new Route(customer.getName(), country.getName(),
					Ref.create(customer), Ref.create(country));

			float min = Float.parseFloat(fields[3]);

			Long minutes = Long.valueOf((long) min);

			Float aRSV = Float.parseFloat(fields[4]);
			Float aCDV = Float.parseFloat(fields[5]);
			Float aCDA = Float.parseFloat(fields[6]);
			Float buy = Float.parseFloat(fields[7]);
			Float sell = Float.parseFloat(fields[8]);
			Float revenue = Float.parseFloat(fields[9]);
			Float gp_percent = Float.parseFloat(fields[10]);
			Float gp = Float.parseFloat(fields[11]);
			// Float delta = Float.parseFloat(fields[12]); TODO da
			// calcolare?
			Routing routing = Routing.valueOf(fields[13]);
			Boolean on = "1".equals(fields[14]);
			Boolean block = "1".equals(fields[15]);

			MCData mm = new MCData();
			mm.setMinutes(minutes);
			mm.setRevenue(revenue);
			mm.setDelta(Float.valueOf(0));
			mm.setACDA(aCDA);
			mm.setACDV(aCDV);
			mm.setARSV(aRSV);
			mm.setBlock(block);
			mm.setSell(sell);
			mm.setBuy(buy);
			mm.setGp(gp);
			mm.setGpPercent(gp_percent);
			mm.setOn(on);
			mm.setRoute(Ref.create(r));
			mm.setCreationDate(new DateTime());
			mm.setMeasureDate(measureDateTime);
			mm.setRouting(routing);

			OfyService.ofy().save().entity(mm); // TODO REsource

			RouteResource.put(r);
			return mm;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void pushQueue(String fileName, String line) {

		if (balancing) {
			balancing = false;
			lineQueue.add(TaskOptions.Builder.withUrl("/worker")
					.param("line", line).param("name", fileName));
		} else {
			balancing = true;
			defQueue.add(TaskOptions.Builder.withUrl("/worker")
					.param("line", line.toString()).param("name", fileName));
		}
	}
}
