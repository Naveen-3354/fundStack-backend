package com.test.FundStack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -08-02-2026 <NaveenDhanasekaran> CurdRepository
 * - Initial Version.
 */
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CrudRepositoryBase<E, ID> extends JpaRepository<E, ID> {
}

