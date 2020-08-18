package br.edu.utfpr.dv.siacoes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.siacoes.model.BugReport;
import br.edu.utfpr.dv.siacoes.model.BugReport.BugStatus;
import br.edu.utfpr.dv.siacoes.model.Module;
import br.edu.utfpr.dv.siacoes.model.User;

/*A única alteração que encontrei que poderia ser feita seria em transformar as variáveis do tipo Connection, PreparedStatement e ResultSet em globais, pois como todos os métodos utilizam
essa variáveis para a conexão com o banco, assim seria melhor para identificar se as variáveis estivessem fora dos métodos e evitaria repetição. Nesse caso o que poderia implicar bug ou erro
seria a concorrência entre os métodos, porém pelo que verifiquei na utilização desses métodos, creio que dois ou mais métodos não serão utilizados simultaneamente. As demais declarações
de variáveis, a classe estar separada em métodos só de passar o olho verificando seu nome e o código descrito nele, já dá para se ter uma noção do que se trata, onde as bibliotecas
estão sendo usadas de maneira efetiva*/

public class BugReportDAO {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null; 
	
	public BugReport findById(int id) throws SQLException{
		/*Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null; */
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.prepareStatement("SELECT bugreport.*, \"user\".name " + 
				"FROM bugreport INNER JOIN \"user\" ON \"user\".idUser=bugreport.idUser " +
				"WHERE idBugReport = ?");
		
			stmt.setInt(1, id);
			
			rs = stmt.executeQuery();
			
			if(rs.next()){
				return this.loadObject(rs);
			}else{
				return null;
			}
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public List<BugReport> listAll() throws SQLException{
		/*Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;*/
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery("SELECT bugreport.*, \"user\".name " +
					"FROM bugreport INNER JOIN \"user\" ON \"user\".idUser=bugreport.idUser " +
					"ORDER BY status, reportdate");
			List<BugReport> list = new ArrayList<BugReport>();
			
			while(rs.next()){
				list.add(this.loadObject(rs));
			}
			
			return list;
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	public int save(BugReport bug) throws SQLException{
		boolean insert = (bug.getIdBugReport() == 0);
		/*Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null; */
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			
			if(insert){
				stmt = conn.prepareStatement("INSERT INTO bugreport(idUser, module, title, description, reportDate, type, status, statusDate, statusDescription) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = conn.prepareStatement("UPDATE bugreport SET idUser=?, module=?, title=?, description=?, reportDate=?, type=?, status=?, statusDate=?, statusDescription=? WHERE idBugReport=?");
			}
			
			stmt.setInt(1, bug.getUser().getIdUser());
			stmt.setInt(2, bug.getModule().getValue());
			stmt.setString(3, bug.getTitle());
			stmt.setString(4, bug.getDescription());
			stmt.setDate(5, new java.sql.Date(bug.getReportDate().getTime()));
			stmt.setInt(6, bug.getType().getValue());
			stmt.setInt(7, bug.getStatus().getValue());
			if(bug.getStatus() == BugStatus.REPORTED){
				stmt.setNull(8, Types.DATE);
			}else{
				stmt.setDate(8, new java.sql.Date(bug.getStatusDate().getTime()));
			}
			stmt.setString(9, bug.getStatusDescription());
			
			if(!insert){
				stmt.setInt(10, bug.getIdBugReport());
			}
			
			stmt.execute();
			
			if(insert){
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					bug.setIdBugReport(rs.getInt(1));
				}
			}
			
			return bug.getIdBugReport();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	private BugReport loadObject(ResultSet rs) throws SQLException{
		BugReport bug = new BugReport();
		
		bug.setIdBugReport(rs.getInt("idBugReport"));
		bug.setUser(new User());
		bug.getUser().setIdUser(rs.getInt("idUser"));
		bug.getUser().setName(rs.getString("name"));
		bug.setModule(Module.SystemModule.valueOf(rs.getInt("module")));
		bug.setTitle(rs.getString("title"));
		bug.setDescription(rs.getString("description"));
		bug.setReportDate(rs.getDate("reportDate"));
		bug.setType(BugReport.BugType.valueOf(rs.getInt("type")));
		bug.setStatus(BugReport.BugStatus.valueOf(rs.getInt("status")));
		bug.setStatusDate(rs.getDate("statusDate"));
		bug.setStatusDescription(rs.getString("statusDescription"));
		
		return bug;
	}

}
