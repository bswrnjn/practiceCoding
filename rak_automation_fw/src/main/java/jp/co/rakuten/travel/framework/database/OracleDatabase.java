package jp.co.rakuten.travel.framework.database;

import java.sql.ResultSet;

import jp.co.rakuten.travel.framework.configuration.Controller;

public interface OracleDatabase extends Controller
{
    ResultSet send( String query );
}
