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


	@Test
	void CampusTests() {
		Campus campus = new Campus();
		
		campus.setAddress("Cornelio-PR");
		
		String camp1 = campus.setAddress();
		String camp2= "Cornelio-PR";
		
		assertEquals(camp2, camp1);
}
