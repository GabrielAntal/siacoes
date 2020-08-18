package br.edu.utfpr.dv.siacoes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.dv.siacoes.log.UpdateEvent;
import br.edu.utfpr.dv.siacoes.model.ActivityUnit;

/*A única alteração que encontrei que poderia ser feita seria em transformar as variáveis do tipo Connection, PreparedStatement e ResultSet em globais, pois como todos os métodos utilizam
essa variáveis para a conexão com o banco, assim seria melhor para identificar se as variáveis estivessem fora dos métodos e evitaria repetição. Nesse caso o que poderia implicar bug ou erro
seria a concorrência entre os métodos, porém pelo que verifiquei na utilização desses métodos, creio que dois ou mais métodos não serão utilizados simultaneamente. As demais declarações
de variáveis, a classe estar separada em métodos só de passar o olho verificando seu nome e o código descrito nele, já dá para se ter uma noção do que se trata, onde as bibliotecas
estão sendo usadas de maneira efetiva*/

public class ActivityUnitDAO {
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	public List<ActivityUnit> listAll() throws SQLException{
		/*Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;*/
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.createStatement();
		
			rs = stmt.executeQuery("SELECT * FROM activityunit ORDER BY description");
			
			List<ActivityUnit> list = new ArrayList<ActivityUnit>();
			
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
	
	public ActivityUnit findById(int id) throws SQLException{
		/*Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;*/
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			stmt = conn.prepareStatement("SELECT * FROM activityunit WHERE idActivityUnit=?");
		
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
	
	public int save(int idUser, ActivityUnit unit) throws SQLException{
		boolean insert = (unit.getIdActivityUnit() == 0);
		/*Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;*/
		
		try{
			conn = ConnectionDAO.getInstance().getConnection();
			
			if(insert){
				stmt = conn.prepareStatement("INSERT INTO activityunit(description, fillAmount, amountDescription) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			}else{
				stmt = conn.prepareStatement("UPDATE activityunit SET description=?, fillAmount=?, amountDescription=? WHERE idActivityUnit=?");
			}
			
			stmt.setString(1, unit.getDescription());
			stmt.setInt(2, (unit.isFillAmount() ? 1 : 0));
			stmt.setString(3, unit.getAmountDescription());
			
			if(!insert){
				stmt.setInt(4, unit.getIdActivityUnit());
			}
			
			stmt.execute();
			
			if(insert){
				rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					unit.setIdActivityUnit(rs.getInt(1));
				}
				
				new UpdateEvent(conn).registerInsert(idUser, unit);
			} else {
				new UpdateEvent(conn).registerUpdate(idUser, unit);
			}
			
			return unit.getIdActivityUnit();
		}finally{
			if((rs != null) && !rs.isClosed())
				rs.close();
			if((stmt != null) && !stmt.isClosed())
				stmt.close();
			if((conn != null) && !conn.isClosed())
				conn.close();
		}
	}
	
	private ActivityUnit loadObject(ResultSet rs) throws SQLException{
		ActivityUnit unit = new ActivityUnit();
		
		unit.setIdActivityUnit(rs.getInt("idActivityUnit"));
		unit.setDescription(rs.getString("Description"));
		unit.setFillAmount(rs.getInt("fillAmount") == 1);
		unit.setAmountDescription(rs.getString("amountDescription"));
		
		return unit;
	}

}
