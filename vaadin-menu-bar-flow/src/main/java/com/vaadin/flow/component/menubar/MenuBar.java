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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.dom.Element;

/**
 * Server-side component for the <code>vaadin-menu-bar</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag("vaadin-menu-bar")
@HtmlImport("frontend://bower_components/vaadin-menu-bar/src/vaadin-menu-bar.html")
@JavaScript("frontend://contextMenuConnector.js")
public class MenuBar extends Component {

    private final List<MenuItem> items = new ArrayList<>();

    /**
     * 
     */
    public MenuBar() {
        container = new Element("div");
        getElement().appendVirtualChild(container);
        addAttachListener(event -> resetContent());
    }

    public MenuItem addItem(String text) {
        MenuItem rootMenuItem = new MenuBarRootItem(this::resetContent);
        rootMenuItem.setText(text);
        items.add(rootMenuItem);
        return rootMenuItem;
    }

    private boolean updateScheduled = false;
    private final Element container;

    private void resetContent() {
        if (updateScheduled) {
            return;
        }
        updateScheduled = true;
        runBeforeClientResponse(ui -> {
            container.removeAllChildren();
            getItems().forEach(this::resetContainers);

            int containerNodeId = createNewContainer(getChildren());
            String appId = ui.getInternals().getAppId();

            ui.getPage().executeJavaScript(
                    "window.Vaadin.Flow.contextMenuConnector.generateItems($0, $1, $2)",
                    getElement(), appId, containerNodeId);

            updateScheduled = false;
        });
    }

    public List<MenuItem> getItems() {
        return items;
    }

    @Override
    public Stream<Component> getChildren() {
        return getItems().stream().map(item -> (Component) item);
    }

    private void resetContainers(MenuItem menuItem) {
        if (!menuItem.isParentItem()) {
            menuItem.getElement().removeProperty("_containerNodeId");
            return;
        }
        SubMenu subMenu = menuItem.getSubMenu();

        int containerNodeId = createNewContainer(subMenu.getChildren());
        menuItem.getElement().setProperty("_containerNodeId", containerNodeId);

        subMenu.getItems().forEach(this::resetContainers);
    }

    private int createNewContainer(Stream<Component> components) {
        Element subContainer = new Element("div");
        container.appendChild(subContainer);

        components
                .forEach(child -> subContainer.appendChild(child.getElement()));
        return subContainer.getNode().getId();
    }

    private void runBeforeClientResponse(Consumer<UI> command) {
        getElement().getNode().runWhenAttached(ui -> ui
                .beforeClientResponse(this, context -> command.accept(ui)));
    }
}
