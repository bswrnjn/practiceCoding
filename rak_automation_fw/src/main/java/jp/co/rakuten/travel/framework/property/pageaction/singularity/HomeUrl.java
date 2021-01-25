package jp.co.rakuten.travel.framework.property.pageaction.singularity;

/**
 * This interface should be taken under the assumption that initial log in was successful
 *
 */
@FunctionalInterface
public interface HomeUrl extends WebPageAction
{
    boolean gotoHomeUrl();
}
