package jp.co.rakuten.travel.framework.utility;

public class Pair< M, N >
{
    private final M m_m;
    private final N m_n;

    public Pair( M m, N n )
    {
        m_m = m;
        m_n = n;
    }

    public M first()
    {
        return m_m;
    }

    public N second()
    {
        return m_n;
    }
}
