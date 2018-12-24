package br.net.buzu.util;

import br.net.buzu.exception.PplException;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class PplReader {

	public static String read(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String currentLine;
			StringBuilder sb = new StringBuilder();
			while ((currentLine = br.readLine()) != null) {
				sb.append(currentLine).append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			throw new PplException(e);
		}

	}

	@Test
	public void testRead() {
		String person = read("./src/test/resources/person.ppl");
		assertTrue(person.contains("name"));
		assertTrue(person.contains("age"));
		assertTrue(person.contains("city"));
	}
}
