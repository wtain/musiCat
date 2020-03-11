package ru.rz.musiCat.data.repositories;

import org.springframework.stereotype.Repository;

import ru.rz.musiCat.data.entities.Host;
import ru.rz.musiCat.data.entities.RootFolder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface RootRepository extends JpaRepository<RootFolder, Long>/* , JpaSpecificationExecutor */ {
	List<RootFolder> findAllByHost(Host host);
}
