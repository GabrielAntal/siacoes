package br.edu.utfpr.dv.siacoes.dao;

import java.sql.*;
import java.util.List;

import br.edu.utfpr.dv.siacoes.model.ActivityUnit;
import br.edu.utfpr.dv.siacoes.model.BugReport;
import br.edu.utfpr.dv.siacoes.model.Department;


public abstract class Template {
	
	
	abstract BugReport findByIdBugReport(int id) throws SQLException;
	
	abstract ActivityUnit findByIdActivityUnit(int id) throws SQLException;
	
	abstract Department findByIdDepartment(int id) throws SQLException;
	
	abstract List<Department> listByCampus(int idCampus, boolean onlyActive) throws SQLException;
	
	abstract int saveDepartment(int idUser, Department department) throws SQLException;
	
	abstract int saveActivityUnit(int idUser, ActivityUnit unit) throws SQLException;
	
	abstract int  saveBugReport(BugReport bug) throws SQLException;

	abstract  List<ActivityUnit> listAllActivityUnit() throws SQLException;
	
	abstract  List<Department> listAllDepartment(boolean onlyActive) throws SQLException;
	
	abstract  List<BugReport> listAllBugReport() throws SQLException;
	
}
