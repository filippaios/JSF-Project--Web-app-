package solr;

import java.util.List;
import java.util.Map;

public interface Index
{
	void indexFiles(List<IndexFile> files);
	List<IndexFile> query(Map<String, String> searchFieldMap);
	void delete(Map<String, String> deleteFieldMap);
}
