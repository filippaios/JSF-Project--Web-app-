package solr;

import java.io.InputStream;
import java.math.BigDecimal;

public class IndexFile
{
	private InputStream fileStream = null;
	private String illustratedFilename = null;
	private Integer fileSize = null;
	private IndexedField<String> id;
	private IndexedField<String> name;
	private IndexedField<String> content;
	
	public IndexFile()
	{
		id = new IndexedField<>(IndexedFieldName.id, null);
		name = new IndexedField<>(IndexedFieldName.name, null);
		content = new IndexedField<>(IndexedFieldName.content, null);
	}
	
	public InputStream getFileStream() {
		return fileStream;
	}

	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}

	public String getIllustratedFilename(){
		return illustratedFilename;
	}

	public void setIllustratedFilename(String illustratedFilename) {
		this.illustratedFilename = illustratedFilename;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public IndexedField<String> getId() {
		return id;
	}

	public void setId( String id) {
		this.id.setFieldValue( id );
	}


	public IndexedField<String> getFilename() {
		return name;
	}

	public void setFilename( String name) {
		this.name.setFieldValue( name );
	}
	
	public IndexedField<String> get_text_() {
		return content;
	}

	public void set_text_( String _text_) {
		this.content.setFieldValue( _text_ );
	}
	

	public String toString()
	{
		return "[filename: " + name.getFieldValue() + ",filesize:" + fileSize + "]";
	}
}
