package org.gonnaup.tutorial.datafactory.repository;

import org.gonnaup.tutorial.common.entity.datafactory.Novel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author gonnaup
 * @version created at 2022/10/13 21:03
 */
@Repository
public interface NovelRepository extends JpaRepository<Novel, Integer> {
}
