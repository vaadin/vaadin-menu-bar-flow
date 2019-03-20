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
package com.vaadin.flow.component.menubar.tests;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.RootMenuItem;
import com.vaadin.flow.router.Route;

@Route("menu-bar-test")
public class MenuBarTestPage extends Div {

    public MenuBarTestPage() {
        MenuBar menuBar = new MenuBar();
        add(menuBar);

        RootMenuItem foo = menuBar.addItem("foo");
        menuBar.addItem("bar");

        MenuItem baz = foo.getContextMenu().addItem("baz");

        MenuItem headingItem = baz.getSubMenu().addItem(new H1("EZ4ENCE"));
        headingItem.addClickListener(e -> System.out.println("clicked h1"));

        RootMenuItem itemWithIcon = menuBar.addItem("");
        itemWithIcon.add(createIcon(), new Label("asdf"));
        itemWithIcon.addClickListener(e -> System.out.println("click"));
    }

    private Component createIcon() {
        Image image = new Image();
        image.setSrc(
                "https://66.media.tumblr.com/6c91401eb84c46f7b570a8f5ed548166/tumblr_inline_ovao4lxyls1qdmftd_75sq.png");
        return image;
    }
}
