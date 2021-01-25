package jp.co.rakuten.travel.framework.property.pageaction.singularity;

import java.util.Map;

import jp.co.rakuten.travel.framework.property.FilterOption;

public interface Filterable< T extends FilterOption > extends WebPageAction
{

    boolean filter( Map< T, Object > filters );

    boolean checkFilteredResults( Map< T, Object > filters );
}
