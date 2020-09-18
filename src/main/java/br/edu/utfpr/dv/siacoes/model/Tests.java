package br.edu.utfpr.dv.siacoes.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class Tests {
	
	@Test
	void DepartamentTests() {
		Department dep = new Department();
		
		dep.setFullName("Jack Sparrow");
		
		String capitain = dep.getFullName();
		String compar= "Jack Sparow";
		
		assertEquals(compar, capitain);
		
	}


}
