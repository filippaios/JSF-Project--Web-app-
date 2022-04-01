package solr;

import org.apache.commons.io.FilenameUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.OfficeParser;
import org.apache.tika.parser.microsoft.OldExcelParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import com.sun.tools.javac.util.Context.Key;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolrIndex implements Index
{
	private String serverUrlString;
	//notice this is a package private constructor. Combined with the factory method, it allows
	//us to create a singleton design pattern for the index. DO NOT change the scope or move other
	//objects into the package to avoid granting them access to the constructor
	public SolrIndex()
	{

		serverUrlString = "http://localhost:8983/solr/disproject";
		System.out.println( "Initialized SolrIndex @ url: " + serverUrlString );
	}	
	
	public void indexFiles( List<IndexFile> files )
	{
		AbstractParser parser = null;
		SolrClient solrClient = new HttpSolrClient.Builder( serverUrlString ).build();
		BodyContentHandler textHandler = null;
		Metadata metadata = new Metadata();
		ParseContext context = new ParseContext();
        for (IndexFile file : files) {
            SolrInputDocument document = new SolrInputDocument();
            try {
                if (textIndexedMimeType(file.getFilename().getFieldValue())) {
                    if (FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("xls") ||
                            FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("xlsx") ||
                            FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("doc") ||
                            FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("docx"))
                        parser = new OOXMLParser();
                    else if (FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("pdf"))
                        parser = new PDFParser();
                    textHandler = new BodyContentHandler(file.getFileSize());
                    file.getFileStream().mark(file.getFileSize());
/*					if( file.getFileStream().markSupported() )
						System.out.println( "Mark is supported" );
					else
						System.out.println( "Mark is not supported" );*/
                    parser.parse(file.getFileStream(), textHandler, metadata, context);
                    document.addField(IndexedFieldName.content.toString(), textHandler.toString());
                    file.getFileStream().close();
                }
            } catch (Exception e) {
                try {
                    if (textIndexedMimeType(file.getFilename().getFieldValue())) {
                        if (FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("doc") ||
                                FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("docx"))
                            parser = new OfficeParser();
                        else if (FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("xls") ||
                                FilenameUtils.getExtension(file.getFilename().getFieldValue()).equalsIgnoreCase("xlsx"))
                            parser = new OldExcelParser();
                        else
                            parser = new AutoDetectParser();
                        file.getFileStream().reset();
                        parser.parse(file.getFileStream(), textHandler, metadata, context);
                        document.addField(IndexedFieldName.content.toString(), textHandler.toString());
                        file.getFileStream().close();
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            
            document.addField(file.getId().getFieldName(), file.getId().getFieldValue().toString());
            document.addField(file.getFilename().getFieldName(), file.getFilename().getFieldValue());
            UpdateResponse response;
            try {
                response = solrClient.add(document);
                response = solrClient.commit();
                if (((Integer) response.getResponseHeader().get("status")) != 0)
                    System.err.println("Something went wrong while attempting to commit changes to the index!");
/*				else //Debug stuff
					System.out.println( "Successfully uploaded the following file to the index: " + file );*/
            } catch (Exception e) {
                System.err.println("Something went wrong while attempting to communicate with the Solr server to add/commit the changes to the index!");
                e.printStackTrace();
            }
        }
	}
	
	public List<IndexFile> query( Map<String,String> searchFieldMap )
	{
		List<IndexFile> matchingIndexFilesList = new ArrayList<>();
		SolrClient solr = new HttpSolrClient.Builder( serverUrlString ).build();
		SolrQuery query = new SolrQuery();
		StringBuilder queryString = new StringBuilder();
		for( Map.Entry<String,String> currentField : searchFieldMap.entrySet() )
			queryString.append(queryString.toString().equals("") ? "" : " AND ").append(currentField.getValue());
		StringBuilder queryFilter = new StringBuilder();
//		for( Map.Entry<String,String> currentField : searchFieldMap.entrySet() )
//		{
//				throw new RuntimeException( "SolrIndex:query(): Invalid search field specified: " + currentField.getKey() );
//		}
		query.set( "q" , queryString.toString());
		query.set( "fq" , queryFilter.toString());
		//Debug stuff
/*		System.out.println( queryString );
		System.out.println( query.toString());
		System.out.println( query.toQueryString() );*/
		QueryResponse response;
		try
		{
			response = solr.query( query );
			SolrDocumentList list = response.getResults();
			//Debug stuff
			//System.out.println( list );
            for (SolrDocument currentDocument : list) {
                IndexFile file = new IndexFile();
                file.setId((String) currentDocument.getFieldValue(IndexedFieldName.id.toString()));
                file.setFilename((String) currentDocument.getFieldValue(IndexedFieldName.name.toString()));
                
                matchingIndexFilesList.add(file);
                //Debug stuff
                System.out.println("Fetched result file:");
                System.out.println(file);
            }
		}
		catch( Exception e )
		{
			System.err.println( "Error while querying index!");
			e.printStackTrace();
		}
		return matchingIndexFilesList;
	}
	
	public void delete2( Map<String,String> deleteFieldMap )
	{
		SolrClient solr = new HttpSolrClient.Builder( serverUrlString ).build();
		SolrQuery query = new SolrQuery();
		StringBuilder queryFilter = new StringBuilder();
//		for( Map.Entry<String,String> currentField : deleteFieldMap.entrySet() )
//		{
//			
//				throw new RuntimeException( "SolrIndex:delete(): Invalid deletion field specified: " + currentField.getKey() );
//		}
		
		System.out.println( queryFilter);
		UpdateResponse response = new UpdateResponse();
		
		queryFilter.append(queryFilter.toString().equals("id:")).append(deleteFieldMap);

		try
		{
			response = solr.deleteByQuery("id:"+deleteFieldMap);
			if( ((Integer )response.getResponseHeader().get( "status" )) != 0 )
				System.err.println( "Something went wrong while attempting to delete from the index!" );
			response = solr.commit();
			if( ((Integer )response.getResponseHeader().get( "status" )) != 0 )
				System.err.println( "Something went wrong while attempting to commit deletion from the index!" );
		}
		catch( Exception e )
		{
			System.err.println( "Error while deleting from index!");
			e.printStackTrace();
		}
	}
		public void delete( String id)
		{
			SolrClient solr = new HttpSolrClient.Builder( serverUrlString ).build();
			SolrQuery query = new SolrQuery();
			StringBuilder queryFilter = new StringBuilder();
//			for( Map.Entry<String,String> currentField : deleteFieldMap.entrySet() )
//			{
//				
//					throw new RuntimeException( "SolrIndex:delete(): Invalid deletion field specified: " + currentField.getKey() );
//			}
			
			System.out.println( queryFilter);
			UpdateResponse response = new UpdateResponse();
			
			//queryFilter.append(queryFilter.toString().equals("id:")).append(deleteFieldMap);

			try
			{
				response = solr.deleteByQuery("id:"+id);
				if( ((Integer )response.getResponseHeader().get( "status" )) != 0 )
					System.err.println( "Something went wrong while attempting to delete from the index!" );
				response = solr.commit();
				if( ((Integer )response.getResponseHeader().get( "status" )) != 0 )
					System.err.println( "Something went wrong while attempting to commit deletion from the index!" );
			}
			catch( Exception e )
			{
				System.err.println( "Error while deleting from index!");
				e.printStackTrace();
			}
	}

	

	
	private boolean textIndexedMimeType( String filename )
	{
		if( FilenameUtils.getExtension( filename ).equalsIgnoreCase( "jpeg" ) || 
				FilenameUtils.getExtension( filename ).equalsIgnoreCase( "jpg" ) || 
				FilenameUtils.getExtension( filename ).equalsIgnoreCase( "flv" ) || 
				FilenameUtils.getExtension( filename ).equalsIgnoreCase( "mp4" ) )
			return false;
		return true;
	}

	@Override
	public void delete(Map<String, String> deleteFieldMap) {
		// TODO Auto-generated method stub
		
	}
}
