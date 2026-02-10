package com.test.FundStack.service;

import com.test.FundStack.repository.CrudRepositoryBase;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> CurdService
 *      - Initial Version.
 */

public abstract class CrudService<E, ID> {

    protected final CrudRepositoryBase<E, ID> repository;

    protected CrudService(CrudRepositoryBase<E, ID> repository) {
        this.repository = repository;
    }

    public E save(E entity) {
        return repository.save(entity);
    }

    public List<E> saveAll(List<E> entity) {
        return repository.saveAll(entity);
    }

    public E findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }
}
