package com.example.locc_development.Controller;


/*
Items will need to have some commands that are always the same in order to control them in a uniform matter.
We do this by implementing the commands through an interface.

This allows the super class Item to have a list of overwritten commands that will be the "base" command-set for every item created.
This also allows for sup-classes of Item to modify the way they do these commands while having the same names.

ex. the useItem command on an armor piece would likely try to equip that item if it is possible.
Whereas useItem on a consumable item would need to check if it can be consumed first,
an arrow should not have or give the same usage feedback as a potion.

At the time of this comment:
we have chosen to only implement the most basic/useful functions of an Item.
Which is for the item to be displayed/shown and for the Item to be used.
 */
public interface ItemAction {

    String showItemInfo();

    String useItem();

}// End
