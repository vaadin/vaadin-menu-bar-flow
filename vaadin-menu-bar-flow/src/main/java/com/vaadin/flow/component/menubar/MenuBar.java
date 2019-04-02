/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.menubar;

import java.util.List;
import java.util.stream.Stream;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.contextmenu.HasMenuItems;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.MenuItemsArrayGenerator;
import com.vaadin.flow.component.contextmenu.MenuManager;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;

/**
 * Server-side component for the <code>vaadin-menu-bar</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vaadin-menu-bar")
@HtmlImport("frontend://bower_components/vaadin-menu-bar/src/vaadin-menu-bar.html")
public class MenuBar extends Component
        implements HasMenuItems, HasSize, HasStyle {

    private MenuManager<MenuBar, MenuItem, SubMenu> menuManager;
    private MenuItemsArrayGenerator<MenuItem> menuItemsArrayGenerator;

    /**
     * Creates an empty menu bar component.
     * <p>
     * Use {@link #addItem(String)} to add content to the menu bar.
     */
    public MenuBar() {
        menuItemsArrayGenerator = new MenuItemsArrayGenerator<>(this);
        menuManager = new MenuManager<>(this, this::resetContent,
                (menu, contentReset) -> new MenuBarRootItem(contentReset),
                MenuItem.class, null);
        addAttachListener(event -> resetContent());
    }

    /**
     * Adds a new item component with the given text content to the root level
     * of this menu bar.
     * <p>
     * The root level items are normally placed inside buttons. When the buttons
     * overflow the menu bar horizontally, the overflowing items are placed into
     * a context menu openable via ellipsis button.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(String text) {
        return menuManager.addItem(text);
    }

    /**
     * Adds a new item component with the given component as its content to the
     * root level of this menu bar.
     * <p>
     * The root level items are normally placed inside buttons. When the buttons
     * overflow the menu bar horizontally, the overflowing items are placed into
     * a context menu openable via ellipsis button.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     * 
     * @param component
     *            the component to add inside new item
     * @return the added {@link MenuItem} component
     */
    public MenuItem addItem(Component component) {
        return menuManager.addItem(component);
    }

    /**
     * Adds a new item component with the given text content and click listener
     * to the root level of this menu bar.
     * <p>
     * The root level items are normally placed inside buttons. When the buttons
     * overflow the menu bar horizontally, the overflowing items are placed into
     * a context menu openable via ellipsis button.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param text
     *            the text content for the new item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     */
    @Override
    public MenuItem addItem(String text,
            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        return menuManager.addItem(text, clickListener);
    }

    /**
     * Adds a new item component with the given component content and click
     * listener to the root level of this menu bar.
     * <p>
     * The root level items are normally placed inside buttons. When the buttons
     * overflow the menu bar horizontally, the overflowing items are placed into
     * a context menu openable via ellipsis button.
     * <p>
     * To add content to the sub menu opened by clicking the root level item,
     * use {@link MenuItem#getSubMenu()}.
     *
     * @param component
     *            the component to add inside the added menu item
     * @param clickListener
     *            the handler for clicking the new item, can be {@code null} to
     *            not add listener
     * @return the added {@link MenuItem} component
     */
    @Override
    public MenuItem addItem(Component component,
            ComponentEventListener<ClickEvent<MenuItem>> clickListener) {
        return menuManager.addItem(component, clickListener);
    }

    /**
     * Gets the {@link MenuItem} components added to the root level of the menu
     * bar.
     * <p>
     * To manage the contents inside the sub menus, use the
     * {@link MenuItem#getSubMenu()}.
     *
     * @return the root level {@link MenuItem} components added to this menu bar
     */
    public List<MenuItem> getItems() {
        return menuManager.getItems();
    }

    /**
     * Removes the given item components from this menu bar.
     *
     * @param items
     *            the item components to remove, not {@code null}
     * @throws IllegalArgumentException
     *             if any of the item components to remove is not a child of
     *             this menu bar
     */
    public void remove(MenuItem... items) {
        menuManager.remove(items);
    }

    /**
     * Removes all item components from this menu bar.
     */
    public void removeAll() {
        menuManager.removeAll();
    }

    /**
     * Gets the child components of this menu bar.
     * <p>
     * The returned components are the same as the ones returned by
     * {@link #getItems()}.
     *
     * @return the child components of this menu bar
     */
    @Override
    public Stream<Component> getChildren() {
        return menuManager.getChildren();
    }

    /**
     * Sets the event which opens the sub menus of the root level buttons.
     * 
     * @param openOnHover
     *            {@code true} to make the sub menus open on hover (mouseover),
     *            {@code false} to make them openable by clicking
     */
    public void setOpenOnHover(boolean openOnHover) {
        getElement().setProperty("openOnHover", openOnHover);
    }

    /**
     * Gets whether the sub menus open by clicking or hovering on the root level
     * buttons.
     * 
     * @return {@code true} if the sub menus open by hovering on the root level
     *         buttons, {@code false} if they open by clicking
     */
    public boolean isOpenOnHover() {
        return getElement().getProperty("openOnHover", false);
    }

    private void resetContent() {
        menuItemsArrayGenerator.generate();
    }
}
