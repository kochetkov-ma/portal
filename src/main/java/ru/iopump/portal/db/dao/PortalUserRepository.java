package ru.iopump.portal.db.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.iopump.portal.db.entity.PortalUser;

import java.util.List;

@Repository
public interface PortalUserRepository extends CrudRepository<PortalUser, Long> {

    List<PortalUser> findByUsernameLike(String name);

    PortalUser findOneByUsername(String name);
}
