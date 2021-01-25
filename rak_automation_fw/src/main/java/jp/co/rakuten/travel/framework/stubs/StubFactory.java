package jp.co.rakuten.travel.framework.stubs;

public class StubFactory
{

    public static Stub stub( StubInfo info ) throws InstantiationException, IllegalAccessException
    {
        Stub stub = info.clazz().newInstance();
        return stub;
    }
}
