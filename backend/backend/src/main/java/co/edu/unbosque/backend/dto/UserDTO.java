package co.edu.unbosque.backend.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.unbosque.backend.model.User.Role;

public class UserDTO {

	private Long id;
	private String username;

	/** Se recibe en las peticiones pero NUNCA se devuelve en las respuestas. */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private Role role;

	public UserDTO() {
	}

	public UserDTO(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public UserDTO(String username, String password, Role role) {
		this(username, password);
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, username, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		return Objects.equals(id, other.id) && Objects.equals(username, other.username) && role == other.role;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", username=" + username + ", role=" + role + "]";
	}

}
