package jp.co.rakuten.travel.framework.property.elementaction.singularity;

/**
 * To be used by FORM elements wherein click action is not guaranteed to work
 *
 */
public interface SubmittableForm extends WebElementAction
{
    boolean submit();
}
