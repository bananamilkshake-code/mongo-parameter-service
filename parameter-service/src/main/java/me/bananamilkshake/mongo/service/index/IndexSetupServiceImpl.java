package me.bananamilkshake.mongo.service.index;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IndexSetupServiceImpl implements IndexSetupService {

	private final IndexDeclarationAssembler indexDeclarationAssembler;

	@Override
	public void setupIndex(final MongoTemplate mongoTemplate, final String type, final String index) {
		mongoTemplate.getCollection(type).createIndex(indexDeclarationAssembler.assemble(index), null, true);
	}
}
