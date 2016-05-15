/**
 * JDBC를 이용하는 일반적인 작업순서
 * 
 * 1. DB 연결을 위한 Connection을 가져온다
 * 2. SQL을 담은 Statement(또는 PreparedStatement)를 만든다.
 * 3. 만들어진 Statement를 실행한다.
 * 4. 조회의 경우 SQL 쿼리의 실행 결과를 ResultSet으로 받아서 정보를 저장할 오브젝트(여기서는 User)에 옮겨준다.
 * 5. 작업 중에 생성된 Connection, Statement, ResultSet 같은 리소스는 작업을 마친후 반드시 닫아준다.
 * 6. JDBC API가 만들어내는 예외(Exception)를 잡아서 직접 처리하거나, 메소드에 throws를 선언하여 예외가
 *    발생하면 메소드 밖으로 던지게 한다.
 *    일단 예외는 모두 메소드 밖으로 던저버리는 편이 간편하다.
 */
package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import springbook.user.domain.User;

/**
 * 상속을 통한 확장을 위해 UserDao를 추상클래스로 만듬.
 * 이렇게 슈퍼클래스에 기본적인 로직의 흐름(컨넥션 가져오기, SQL생성, 실행, 반환)을 만들고 그 기능의 일부를
 * 추상 메소드나 오버라이딩이 가능한 protect 메소드 등으로 만든뒤 서브클래스에서 이런 메소드를 필요에 맞게
 * 구현해서 사용 하도록 하는 방법을 템플릿 메소스 패턴 이라고 한다.
 * 
 * @author xinu
 *
 */
public abstract class UserDao {

	/**
	 * 
	 * @param user
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void add(User user) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();

		PreparedStatement ps = c.prepareStatement("insert into users(id, name, password) values(?, ?, ?)");
		ps.setString(1, user.getId());
		ps.setString(2, user.getName());
		ps.setString(3, user.getPassword());

		ps.executeUpdate();

		ps.close();
		c.close();

	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public User get(String id) throws ClassNotFoundException, SQLException {
		Connection c = getConnection();

		PreparedStatement ps = c.prepareStatement("select * from users where id = ?");
		ps.setString(1, id);

		ResultSet rs = ps.executeQuery();

		rs.next();

		User user = new User();
		user.setId(rs.getString("id"));
		user.setName(rs.getString("name"));
		user.setPassword(rs.getString("password"));

		rs.close();
		ps.close();
		c.close();

		return user;
	}

	/**
	 * 상속을 받은 클래스들이 구현해야할 getConnection()
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

}
