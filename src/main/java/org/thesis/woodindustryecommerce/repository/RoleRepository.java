package org.thesis.woodindustryecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thesis.woodindustryecommerce.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String authority);
}
