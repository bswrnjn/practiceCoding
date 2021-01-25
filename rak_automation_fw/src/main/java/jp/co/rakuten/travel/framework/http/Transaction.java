package jp.co.rakuten.travel.framework.http;

public interface Transaction
{
    boolean send();

    boolean validateResult();
}
