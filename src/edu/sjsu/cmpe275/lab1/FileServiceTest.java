package edu.sjsu.cmpe275.lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class FileServiceTest {

	FileServiceImpl fileService;

	@Before
	public void setUp() throws Exception {

		ApplicationContext context = new FileSystemXmlApplicationContext(
				"src/beans.xml");
		fileService = (FileServiceImpl) context.getBean("fileService");

		

		Textfile file1 = (Textfile) context.getBean("file");
		Textfile file2 = (Textfile) context.getBean("file");
		Textfile file3 = (Textfile) context.getBean("file");
		Textfile file4 = (Textfile) context.getBean("file");

		file1.setFileid("FileA");
		file1.setFilepath("/home/Alice/shared/alicetext1.txt");
		

		file2.setFileid("FileB");
		file2.setFilepath("/home/Bob/shared/bobtext1.txt");

		file3.setFileid("FileC");
		file3.setFilepath("/home/Carl/shared/carltext1.txt");

		file4.setFileid("FileD");
		file4.setFilepath("/home/Alice/shared/alicetext2.txt");

		Set<Textfile> a = new HashSet<Textfile>();
		a.add(file1);
		a.add(file4);
		Set<Textfile> b = new HashSet<Textfile>();
		b.add(file2);
		Set<Textfile> c = new HashSet<Textfile>();
		c.add(file3);

		HashMap<String, Set<Textfile>> fileMap = new HashMap<String, Set<Textfile>>();
		fileMap.put("Alice", a);
		fileMap.put("Bob", b);
		fileMap.put("Carl", c);

		List<Textfile> files = new ArrayList<Textfile>();
		files.add(file1);
		files.add(file2);
		files.add(file3);
		files.add(file4);

		fileService.setFileMap(fileMap);
		fileService.setFiles(files);
	}

	/**
	 * TestA checks that Bob cannot read the file of Alice as is it is not
	 * shared
	 */

	@Test(expected = UnauthorizedException.class)
	public void testA() {

		System.out.println("TestA");
		fileService.readFile("Bob", "/home/Alice/shared/alicetext1.txt");
	}

	@Test
	public void testB() {

		System.out.println("TestB");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Bob", "/home/Alice/shared/alicetext1.txt");
	}

	@Test
	public void testC() {
		System.out.println("TestC");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Bob", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Carl", "/home/Alice/shared/alicetext1.txt");
	}

	@Test(expected = UnauthorizedException.class)
	public void TestD() {
		System.out.println("TestD");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Bob", "Alice", "/home/Alice/shared/carltext1.txt");
	}

	@Test(expected = UnauthorizedException.class)
	public void testE() {
		System.out.println("TestE");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Bob", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService
				.unshareFile("Alice", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Carl", "/home/Alice/shared/alicetext1.txt");
	}

	@Test(expected = UnauthorizedException.class)
	public void testF() {
		System.out.println("TestF");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Alice", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Carl", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.unshareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Bob", "/home/Alice/shared/alicetext1.txt");
	}

	@Test(expected = UnauthorizedException.class)
	public void testG() {
		System.out.println("TestG");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Bob", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService.unshareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.shareFile("Bob", "Carl", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Carl", "/home/Alice/shared/alicetext1.txt");
	}

	@Test(expected = UnauthorizedException.class)
	public void testH() {
		System.out.println("TestH");
		fileService.shareFile("Alice", "Bob", "/home/Alice/shared/alicetext1.txt");
		fileService.readFile("Bob", "/home/Alice/shared/alicetext2.txt");
	}

}
