package com.test.FundStack.repository;

import com.test.FundStack.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author NaveenDhanasekaran
 * <p>
 * History:
 * -11-02-2026 <NaveenDhanasekaran> CategoryRepo
 * - Initial Version.
 */

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findByParentIsNull();

    List<Category> findByParentId(Long parentId);

    Optional<Category> findByNameAndParentIsNull(String parentName);

    Optional<Category> findByNameAndParent(String childName, Category parent);
}
