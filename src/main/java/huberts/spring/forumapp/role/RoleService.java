package huberts.spring.forumapp.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleService implements RoleServiceApi {
    private final RoleRepository roleRepository;

    @Override
    public Role addRole(Role role) {
        if (roleRepository.findByCode(role.getCode()) == null) {
            return roleRepository.save(role);
        }
        throw new RuntimeException();
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}
