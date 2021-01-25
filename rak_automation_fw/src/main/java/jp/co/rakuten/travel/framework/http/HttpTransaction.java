package jp.co.rakuten.travel.framework.http;

public class HttpTransaction implements Transaction
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
