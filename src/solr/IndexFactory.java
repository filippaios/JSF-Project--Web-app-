package solr;

public final class IndexFactory
{
	private static SolrIndex indexInstance = null;
	
	private static final Object lock = new Object();
	
	public static Index getInstance()
	{
		synchronized( lock )
		{
			if( indexInstance == null )
				indexInstance = new SolrIndex();
		}
		return indexInstance;
	}
}
