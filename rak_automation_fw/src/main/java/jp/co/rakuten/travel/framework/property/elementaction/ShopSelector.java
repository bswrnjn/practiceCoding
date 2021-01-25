package jp.co.rakuten.travel.framework.property.elementaction;

import jp.co.rakuten.travel.framework.property.elementaction.singularity.Clickable;

/**
 * Supports a window that has list of shops but not displaying all merchandise
 * <br>This will click the
 *
 */
@FunctionalInterface
public interface ShopSelector
{
    void showAllItems( Clickable element );
}
