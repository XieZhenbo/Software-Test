package project2_F22;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class Tester_DoeJane {
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testProject2TwoParams() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"), 10);
		assertNotEquals(null, p);
		p.close();
		Reader in = new StringReader("Hello\nJava\n");
		assertThrows(IllegalArgumentException.class, () -> {new Project2(in, 0); });
		in.close();
	}

	@Test
	void testProject2OneParams() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"));
		assertNotEquals(null, p);
		p.close();
	}

	@Test
	void testLines() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"));
		Iterator<String> iter = p.lines().iterator();
		String lines = "";
		while (iter.hasNext()) {
			lines += iter.next();
		}
		assertEquals("HelloJava", lines);
		p.close();
	}

	@Test
	void testReset() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"));
		p.mark(10);
		assertEquals("Hello", p.readLine());
		p.reset();
		assertEquals("Hello", p.readLine());
		assertThrows(IllegalArgumentException.class, () -> { p.mark(-1); });
		assertThrows(IOException.class, () -> {
			Class<?> clazz = p.getClass();
			Field field = clazz.getDeclaredField("markedChar");
			field.setAccessible(true);
			field.set(p, -2);
			p.reset();
		});
		assertThrows(IOException.class, () -> {
			Class<?> clazz = p.getClass();
			Field field = clazz.getDeclaredField("markedChar");
			field.setAccessible(true);
			field.set(p, -5);
			p.reset();
		});
		p.close();
	}
	
	@Test
	void testClose() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"));
		assertNotEquals(null, p);
		p.close();
		p.close();
		assertThrows(IOException.class, () -> { p.readLine(); });
	}
	
	@Test
	void testMarkSupported() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava\n"));
		assertEquals(true, p.markSupported());
		p.close();
	}
	
	@Test
	void testReady() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\r\nJava"), 3);
		assertEquals(true, p.ready());
		assertEquals("Hello", p.readLine());
		while(p.readLine() != null) {
		}
		p.close();
	}
	
	@Test
	void testSkip() throws Exception {
		Project2 p = new Project2(new StringReader("HelloJava\r\nHi"));
		assertEquals(5, p.skip(5));
		assertEquals("Java", p.readLine());
		assertEquals(2, p.skip(100));
		assertThrows(IllegalArgumentException.class, () -> {
			p.skip(-1);
		});
		p.close();
	}
	
	@Test
	void testReadLine() throws Exception {
		Project2 p = new Project2(new StringReader("Hello\nJava"), 3);
		assertEquals("Hello", p.readLine(false));
		assertEquals("Java", p.readLine(true));
		p.close();
	}
	
	@Test
	void testRead3Params() throws Exception {
		char cbuf[] = new char[4];
		Project2 p = new Project2(new StringReader("Hello\r\nJavaHi\r\nJava"), 6);
		assertEquals("Hello", p.readLine());
		assertEquals(true, p.ready());
		p.read(cbuf, 0, 4);
		assertEquals('J', cbuf[0]);
		assertEquals('a', cbuf[1]);
		assertEquals('v', cbuf[2]);
		assertEquals('a', cbuf[3]);
		p.read(cbuf, 0, 1);
		p.readLine();
		p.read(cbuf, 0, 4);
		assertEquals(0, p.read(cbuf, 1, 0));
		assertThrows(IndexOutOfBoundsException.class, () -> { p.read(cbuf, -1, 5); });
		assertThrows(IndexOutOfBoundsException.class, () -> { p.read(cbuf, cbuf.length + 10, 5); });
		assertThrows(IndexOutOfBoundsException.class, () -> { p.read(cbuf, 0, -1); });
		assertThrows(IndexOutOfBoundsException.class, () -> { p.read(cbuf, cbuf.length - 5, 6); });
		p.close();
	}
	
	@Test
	void testReadZeroParam() throws Exception {
		int buf[] = new int[100];
		int expected[] = {'J', 'a', 'v', 'a'};
		Project2 p = new Project2(new StringReader("Hello\r\nJava"), 3);
		p.readLine();
		int n;
		int num = 0;
		while((n = p.read()) > 0) {
			buf[num++] = n;
		}
		for(int i = 0;i < expected.length;i++) {
			assertEquals(expected[i], buf[i]);
		}
		p.close();
	}
}
