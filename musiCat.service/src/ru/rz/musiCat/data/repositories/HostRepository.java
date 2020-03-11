package ru.rz.musiCat.data.repositories;

import org.springframework.stereotype.Repository;

import ru.rz.musiCat.data.entities.Host;
import ru.rz.musiCat.helpers.HostHelpers;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface HostRepository extends CrudRepository<Host, Long> {
	Optional<Host> findByNetworkAddress(String networkAddress);
	Optional<Host> findByPhysicalId(String physicalId);
	
	default Host getCurrentHost() {
		Host currentHost = HostHelpers.getCurrentHost();
		Optional<Host> ho = findByPhysicalId(currentHost.getPhysicalId());
		return ho.orElse(currentHost);
	}
	
	default boolean isCurrentHost(Long id) {
		return getCurrentHost().getId() == id;
	}
}
