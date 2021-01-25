package jp.co.rakuten.travel.framework.http;

public class AsyncHttpTransaction implements Transaction
{
    @Override
    public boolean send()
    {
        return false;
    }

    @Override
    public boolean validateResult()
    {
        return false;
    }
}
