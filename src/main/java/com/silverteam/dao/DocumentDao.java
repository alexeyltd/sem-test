package com.silverteam.dao;

import com.silverteam.model.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentDao extends CrudRepository<Document, Long> {}
