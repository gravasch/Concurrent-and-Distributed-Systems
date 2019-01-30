import java.sql.*;

public class Test {

	public static void main(String[] args) {
		System.out.println("Testing SQLite Base.");
		
		try {
			Class.forName("org.sqlite.JDBC");
			
			String url = "jdbc:sqlite:test.db";
			Connection con = DriverManager.getConnection(url);
			Statement st = con.createStatement();
			
			st.execute("create table student(id integer, name varchar)");
			st.execute("insert into student values (5, 'A')");
			
			String query = "select * from student";
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				System.out.println(rs.getString(1) + rs.getString(2));
			}
			
			rs.close();
			st.close();
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		
		System.out.println("End of testing.");
	}
}
