package org.cetide.hibiscus.infrastructure.persistence.mongo;

import org.cetide.hibiscus.infrastructure.persistence.mongo.entity.DocumentVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentVersionRepository extends MongoRepository<DocumentVersion, String> {

    List<DocumentVersion> findByDocumentIdOrderByVersionDesc(Long documentId);

    DocumentVersion findTopByDocumentIdOrderByVersionDesc(Long documentId);

}