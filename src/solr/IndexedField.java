package solr;

public class IndexedField<T>
{
	private IndexedFieldName fieldName;
	private T fieldValue;
	
	public IndexedField()
	{
	}

	public IndexedField(IndexedFieldName fieldName, T fieldValue) 
	{
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName.toString();
	}

	public T getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(T fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	public String toString()
	{
		return "fieldName:" + fieldName.toString() + ",fieldValue:" + fieldValue.toString();
	}
	
}
