package com.test.FundStack.controller;


import com.test.FundStack.service.CrudService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -08-02-2026 <NaveenDhanasekaran> CrudController
 *      - Initial Version.
 */

public abstract class CrudController<E, ID> {

    protected final CrudService<E, ID> service;

    protected CrudController(CrudService<E, ID> service) {
        this.service = service;
    }

    @PostMapping
    public E create(@RequestBody E entity) {
        return service.save(entity);
    }

    @PostMapping("/bulk")
    public List<E> createMany(@RequestBody List<E> entity) {
        return service.saveAll(entity);
    }

    @GetMapping("/{id}")
    public E getById(@PathVariable ID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<E> getAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) {
        service.deleteById(id);
    }
}