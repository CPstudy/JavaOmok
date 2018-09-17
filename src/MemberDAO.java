
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {
	
	public static final int SUCCESS = 1;
	public static final int ERROR_ID_EXIST = 2;
	public static final int ERROR = 3;

	public int insertmember(MemberDTO dto) {

		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			
			pstmt = con.prepareStatement("SELECT mem_id FROM member WHERE mem_id = ?");
			pstmt.setString(1, dto.getId());
			
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				DBConnect.close(con, pstmt);
				return ERROR_ID_EXIST;
			}
			
			pstmt.close();
			
			pstmt = con.prepareStatement("INSERT INTO member (mem_id, mem_pw) VALUES (?, ?)");

			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPw());
			int result = pstmt.executeUpdate();
			if (result == 0) {
				DBConnect.close(con, pstmt);
				return ERROR;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		return SUCCESS;
	}

	public boolean login(MemberDTO dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("SELECT MEM_ID FROM MEMBER WHERE (MEM_ID=(?) and MEM_PW=(?))");

			pstmt.setString(1, dto.getId());
			pstmt.setString(2, dto.getPw());

			int result = pstmt.executeUpdate();
			if (result == 0) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		return true;
	}
	
	public static String getRate(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String result = "";
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("SELECT wincount, drawcount, defeatcount FROM MEMBER WHERE mem_id = ?");

			pstmt.setString(1, id);

			ResultSet set = pstmt.executeQuery();
			
			if(set.next()) {
				result = set.getString("wincount") + "½Â " + set.getString("defeatcount") + "ÆÐ " + set.getString("drawcount") + "¹«";
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		
		return result;
	}
	
	public static int setWin(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("UPDATE member SET wincount = wincount + 1 WHERE mem_id = ?");
			pstmt.setString(1, id);
			
			int result = pstmt.executeUpdate();
			if (result == 0) {
				DBConnect.close(con, pstmt);
				return ERROR;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		return SUCCESS;
	}
	
	public static int setDefeat(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("UPDATE member SET defeatcount = defeatcount + 1 WHERE mem_id = ?");
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();
			if (result == 0) {
				DBConnect.close(con, pstmt);
				return ERROR;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		return SUCCESS;
	}
	
	public static int setDraw(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBConnect.getConnection();
			pstmt = con.prepareStatement("UPDATE member SET drawcount = drawcount + 1 WHERE mem_id = ?");
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();
			if (result == 0) {
				DBConnect.close(con, pstmt);
				return ERROR;
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} finally {
			DBConnect.close(con, pstmt);
		}
		return SUCCESS;
	}

}
