package davidof.misjuegos.pruebas;


import org.junit.Assert;
import org.junit.Test;

public class TestPruebas {

	@Test
	public void test() {
		Prueba1 p = new Prueba1();
		Assert.assertTrue("OK", p.getPrueba1());
	}
	
	@Test
	public void test2() {
		Prueba1 p = new Prueba1();
		Assert.assertTrue(true);
	}

}
