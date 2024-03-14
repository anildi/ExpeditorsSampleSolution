package expeditors.backend.commonconfig.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("serial")
public class MyUserDetails extends User {
	private int id;
	private String realName;

	private MyUserDetails(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);

		this.id = id;
	}

	private MyUserDetails(int id, String username, String password, String realName, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);

		this.id = id;
		this.realName = realName;
	}


	public static MyUserDetails of(int id, String username, String password, String realName, List<String>authorities ) {
		List<GrantedAuthority> auths = new ArrayList<>();
		for(String auth : authorities) {
			auths.add(new SimpleGrantedAuthority(auth));
		}
		return new MyUserDetails(id, username, password, realName, auths);
	}

	public static MyUserDetails of(int id, String username, String password, String realName, String ...authorities ) {
		List<String> auths = List.of(authorities);
		return of(id, username, password, realName, auths);
	}

	public int getId() {
		return id;
	}

	public String getRealName() {
		return realName;
	}

	@Override
	public String toString() {
		return "MyUserDetails{" +
				"id=" + id +
				"super=" + super.toString() +
				", realName='" + realName + '\'' +
				'}';
	}
}
