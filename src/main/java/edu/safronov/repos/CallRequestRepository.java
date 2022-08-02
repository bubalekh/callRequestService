package edu.safronov.repos;

import edu.safronov.domain.CallRequest;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.CrudRepository;

@Order(1)
public interface CallRequestRepository extends CrudRepository<CallRequest, Integer> {
}
