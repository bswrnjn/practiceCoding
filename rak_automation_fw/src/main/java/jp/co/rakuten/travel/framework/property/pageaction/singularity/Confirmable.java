package jp.co.rakuten.travel.framework.property.pageaction.singularity;

@FunctionalInterface
public interface Confirmable extends WebPageAction
{
    boolean confirm();
}
