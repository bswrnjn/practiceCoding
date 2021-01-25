package jp.co.rakuten.travel.framework.property;

import java.util.Map;

import jp.co.rakuten.travel.framework.parameter.Parameters;

@FunctionalInterface
public interface ServiceOutputParameters< T extends Parameters >
{
    Map< T, String > outputParams();
}
