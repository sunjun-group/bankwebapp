package sutdbank;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.sutd.bank.webapp.model.User;
import sg.edu.sutd.bank.webapp.commons.ServiceException;
import sg.edu.sutd.bank.webapp.model.ClientAccount;
import sg.edu.sutd.bank.webapp.model.ClientTransaction;
import sg.edu.sutd.bank.webapp.model.TransactionStatus;
import sg.edu.sutd.bank.webapp.service.AbstractDAOImpl;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAO;
import sg.edu.sutd.bank.webapp.service.ClientAccountDAOImpl;

public class ClientAccDAOTest extends AbstractDAOImpl {
	private ClientAccountDAO cad = new ClientAccountDAOImpl();
	private ClientAccount sample = new ClientAccount();
	private ClientAccount sample2 = new ClientAccount();
	private ClientAccount sample3 = new ClientAccount();
	
	@Before
	public void setUp() throws Exception {
		Connection conn = connectDB();	
		sample.setUser(new User(123));
		sample.setAmount(new BigDecimal(1000));
		sample2.setUser(new User(456));
		sample2.setAmount(new BigDecimal(1000));
		sample3.setUser(new User(1234));
		sample3.setAmount(new BigDecimal(1000));
	}

	@Test
	public void testCreate() throws ServiceException, SQLException {
		// test creation of sample user
		Connection conn = connectDB();
		cad.create(sample);
		
		PreparedStatement ps = prepareStmt(conn, "SELECT * FROM client_account WHERE user_id = 123");
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("user_id"),123);
		assertEquals(rs.getBigDecimal("amount").toString(), "1000.0000");
		
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 123");
		endps.execute();
		
	}

	@Test
	public void testUpdate() throws ServiceException, SQLException {
		Connection conn = connectDB();
		cad.create(sample);
		PreparedStatement ps = prepareStmt(conn, "SELECT * FROM client_account WHERE user_id = 123");
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("user_id"),123);
		assertEquals(rs.getBigDecimal("amount").toString(), "1000.0000");
		
		ClientAccount updater = new ClientAccount();
		updater.setUser(new User(123));
		updater.setAmount(new BigDecimal(10));
		cad.update(updater);
		
		ps = prepareStmt(conn, "SELECT * FROM client_account WHERE user_id = 123");
		rs = ps.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("user_id"),123);
		assertEquals(rs.getBigDecimal("amount").toString(), "10.0000");
		
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 123");
		endps.execute();
		
	}


	@Test
	public void testGetAmountFrom() throws ServiceException, SQLException {
		Connection conn = connectDB();
		cad.create(sample);
		PreparedStatement ps = prepareStmt(conn, "SELECT * FROM client_account WHERE user_id = 123");
		ResultSet rs = ps.executeQuery();
		rs.next();
		assertEquals(rs.getInt("user_id"),123);
		assertEquals(rs.getBigDecimal("amount").toString(), "1000.0000");
		
		assertEquals(cad.getAmountFrom(123).toString(), "1000.0000");
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 123");
		endps.execute();
	}

	@Test
	public void testAccountExists() throws ServiceException, SQLException {
		Connection conn = connectDB();
		cad.create(sample);
		PreparedStatement ps = prepareStmt(conn, "SELECT * FROM client_account WHERE user_id = 123");
		ResultSet rs = ps.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("user_id"),123);
		assertEquals(rs.getBigDecimal("amount").toString(), "1000.0000");
		assertTrue(cad.accountExists(123));
		
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 123");
		endps.execute();
	}
	
	@Test
	public void executeTransaction() throws ServiceException, SQLException {
		Connection conn = connectDB();
		cad.create(sample3);
		cad.create(sample2);
		
		// set in SQL table
		PreparedStatement ps = prepareStmt(conn, "INSERT INTO client_transaction("
				+ "trans_code, amount, user_id, to_account_num) VALUES("
				+ "?,?,?,?)");
		int idx = 1;
		ps.setString(idx++, "toberemoved");
		ps.setBigDecimal(idx++, new BigDecimal(500));
		ps.setInt(idx++, 1234);
		ps.setInt(idx++, 456);
		ps.execute();
		
		PreparedStatement getid = prepareStmt(conn, "SELECT max(id) FROM client_transaction");
		ResultSet rs = getid.executeQuery();
		rs.next();
		
		ClientTransaction ct = new ClientTransaction();
		ct.setId(rs.getInt("max(id)"));
		ct.setStatus(TransactionStatus.APPROVED);
		List<ClientTransaction> transactions = Arrays.asList(ct);
		
		// transfer money across
		cad.executeTransaction(transactions);

		//verify amount
		PreparedStatement smp1 = prepareStmt(conn, "SELECT amount FROM client_account WHERE user_id = 1234");
		ResultSet rs1 = smp1.executeQuery();
		rs1.next();
		assertEquals(rs1.getInt("amount"), 500);
		
		PreparedStatement smp2 = prepareStmt(conn, "SELECT amount FROM client_account WHERE user_id = 456");
		ResultSet rs2 = smp2.executeQuery();
		rs2.next();
		assertEquals(rs2.getInt("amount"), 1500);
		
		// delete 2 accounts
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 1234 or user_id = 456");
		endps.execute();
		endps = prepareStmt(conn, "DELETE FROM client_transaction WHERE user_id=1234");
		endps.execute();
	}
	
	@Test
	public void executeDeclinedTransaction() throws ServiceException, SQLException {
		Connection conn = connectDB();
		cad.create(sample3);
		cad.create(sample2);
		
		// set in SQL table
		PreparedStatement ps = prepareStmt(conn, "INSERT INTO client_transaction("
				+ "trans_code, amount, user_id, to_account_num) VALUES("
				+ "?,?,?,?)");
		int idx = 1;
		ps.setString(idx++, "toberemoved");
		ps.setBigDecimal(idx++, new BigDecimal(500));
		ps.setInt(idx++, 1234);
		ps.setInt(idx++, 456);
		ps.execute();
		
		PreparedStatement getid = prepareStmt(conn, "SELECT max(id) FROM client_transaction");
		ResultSet rs = getid.executeQuery();
		rs.next();
		
		ClientTransaction ct = new ClientTransaction();
		ct.setId(rs.getInt("max(id)"));
		ct.setStatus(TransactionStatus.DECLINED);
		List<ClientTransaction> transactions = Arrays.asList(ct);
		
		// transfer money across
		cad.executeTransaction(transactions);

		//verify amount
		PreparedStatement smp1 = prepareStmt(conn, "SELECT amount FROM client_account WHERE user_id = 1234");
		ResultSet rs1 = smp1.executeQuery();
		rs1.next();
		assertEquals(rs1.getInt("amount"), 1000);
		
		PreparedStatement smp2 = prepareStmt(conn, "SELECT amount FROM client_account WHERE user_id = 456");
		ResultSet rs2 = smp2.executeQuery();
		rs2.next();
		assertEquals(rs2.getInt("amount"), 1000);
		
		// delete 2 accounts
		PreparedStatement endps = prepareStmt(conn, "DELETE FROM client_account WHERE user_id = 1234 or user_id = 456");
		endps.execute();
		endps = prepareStmt(conn, "DELETE FROM client_transaction WHERE user_id=1234");
		endps.execute();
	}

}
