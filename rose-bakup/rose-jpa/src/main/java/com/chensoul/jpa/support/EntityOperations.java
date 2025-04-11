package com.chensoul.jpa.support;

import org.springframework.data.repository.CrudRepository;

/**
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class EntityOperations {

	public static <T, ID> EntityUpdater<T, ID> doUpdate(CrudRepository<T, ID> repository) {
		return new EntityUpdater<>(repository);
	}

	public static <T, ID> EntityCreator<T, ID> doCreate(CrudRepository<T, ID> repository) {
		return new EntityCreator(repository);
	}
}
