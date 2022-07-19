package edu.safronov.repos;

import edu.safronov.domain.CallRequest;
import org.springframework.data.repository.CrudRepository;

public interface CallRequestRepository extends CrudRepository<CallRequest, Integer> {
}
