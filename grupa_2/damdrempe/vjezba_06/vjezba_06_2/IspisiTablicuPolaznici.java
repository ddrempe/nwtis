import java.sql.*;

public class IspisiTablicuPolaznici {

	public static void main(String args[]) {

		String url = args[0];
		Connection con;
		Statement stmt;
		String query = "select maticni_broj, prezime, ime from POLAZNICI";

		try {
			Class.forName(args[1]);

		} catch(java.lang.ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}

		try {
			con = DriverManager.getConnection(url,args[2], args[3]);

			stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(query);

			System.out.println("Popis polaznika:");
			while (rs.next()) {
				String mb = rs.getString("maticni_broj");
				String pr = rs.getString("prezime");
				String im = rs.getString("ime");
				System.out.println(mb + " " + pr + " " + im);
			}

			stmt.close();
			con.close();

		} catch(SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
		}
	}
}

