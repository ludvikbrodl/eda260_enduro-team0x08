package test.acceptance;

import static common.ErrorMessage.error;
import static org.junit.Assert.assertEquals;
import generator.LapGenerator;
import generator.formatter.CSVFormatter;
import generator.sorter.Sorter;
import generator.sorter.StartNumberSorter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import model.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.Configuration;

import parser.FinishParser;
import parser.PersonParser;
import parser.StartParser;
import driver.AbstractDriver;
import driver.LapDriver;
import driver.factory.DriverFactory;
import driver.factory.LapFactory;

public class TestAcceptance13 {

	LapGenerator generator;
	private Model<LapDriver> model;

	@Before
	public void setUp() throws Exception {
		Configuration.debug = 1;
		Configuration.formatter = new CSVFormatter();
		Configuration.nameFile = "Iterationer/Iteration2/acceptanstest13/namnfil.txt";
		generator = new LapGenerator(new Sorter<LapDriver>());
		model = new Model<LapDriver>();
		DriverFactory<? extends AbstractDriver> factory = new LapFactory();
		try {

			StartParser startParser = new StartParser(
					"Iterationer/Iteration2/acceptanstest13/starttider.txt",
					factory);
			FinishParser finishParser = new FinishParser(
					"Iterationer/Iteration2/acceptanstest13/maltider1.txt",
					factory);
			FinishParser finishParser2 = new FinishParser(
					"Iterationer/Iteration2/acceptanstest13/maltider2.txt",
					factory);

			PersonParser personParser = new PersonParser(
					"Iterationer/Iteration2/acceptanstest13/namnfil.txt",
					factory);
			startParser.parse(model);
			finishParser.parse(model);
			finishParser2.parse(model);
			personParser.parse(model);
			
			Configuration.printPlacment = false;
			Configuration.sortBy = new StartNumberSorter();
			
			Configuration.printPlacment = false;
			Configuration.sortBy = new StartNumberSorter();
			
			generator.generateResult(model);

		} catch (IOException e) {
			error(e, "Could not instantiate or use parsers.");
		}
	}

	@Test
	public void testResults() {

		Scanner scanResults;
		String fileName = "data/result.txt";
		try {
			scanResults = new Scanner(new File(fileName));
			BufferedReader scanExpected = new BufferedReader(
					new FileReader(
							new File(
									"Iterationer/Iteration2/acceptanstest13/resultat.txt")));

			while (scanResults.hasNextLine()) {
				assertEquals(scanExpected.readLine(), scanResults.nextLine());
			}

			scanExpected.close();
			scanResults.close();
		} catch (Exception e) {
			error(e, "Could not scan file" + fileName);
		}
	}

	@After
	public void tearDown() {
		new File("data/starttider.txt").delete();
		new File("data/maltider.txt").delete();
		new File("data/namnfil.txt").delete();
		new File("data/result.txt").delete();

	}
}