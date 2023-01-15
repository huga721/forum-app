package huberts.spring.forumapp.role;

public interface RoleServiceApi {
    Role addRole(Role role);
    Role findByName(String name);
}
