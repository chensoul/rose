package com.chensoul.rose.core.domain;

/**
 * The interface To dto.
 *
 * @param <D> the type parameter
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 */
public interface ToData<D> {

    /**
     * This method convert model model object to data transfer object.
     *
     * @return the dto object
     */
    D toData();
}
