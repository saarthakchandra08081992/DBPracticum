package helpers;

import models.Tuple;
import java.util.List;

public class AttributeMapper {

	public static Long getColumnActualValue(Tuple t, List<String> tSchema, String columnName){
		int columnIndexInSchema = tSchema.indexOf(columnName);
		if(columnIndexInSchema != -1)
			return (long)t.getValue(columnIndexInSchema);
		else{
			for(int i=0;i<tSchema.size();i++){
				if(columnName.equals((tSchema.get(i)).split("\\.")[1])){
					return (long)t.getValue(i);
				}
			}
		}
		return null;
	}

}
